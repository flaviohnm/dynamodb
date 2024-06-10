package br.com.dynamodb.service;

import br.com.dynamodb.converter.Converter;
import br.com.dynamodb.dto.CustomerDTO;
import br.com.dynamodb.model.Customer;
import br.com.dynamodb.repository.CustomerRepository;
import br.com.dynamodb.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.dynamodb.commom.CustomerConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerServiceImpl service;

    @Mock
    private CustomerRepository repository;

    public Converter converter;

    @BeforeEach
    public void setup() {
        converter = new Converter();
    }

    @Test
    public void createCustomer_WithValidData_ReturnsCustomer() {

        given(repository.findByCompanyDocumentNumber(anyString())).willReturn(Optional.empty());
        given(repository.save(any(Customer.class))).willReturn(CREATED_CUSTOMER_ID);

        //System under test
        CustomerDTO sut = service.saveCustomer(CUSTOMER_DTO);

        assertNotNull(sut);
        assertThat(sut.getCreateDate()).isNotEmpty();
        assertThat(sut.getCompanyName()).isEqualTo(CREATED_CUSTOMER_ID.getCompanyName());
        assertThat(sut.getCompanyDocumentNumber()).isEqualTo(CREATED_CUSTOMER_ID.getCompanyDocumentNumber());
        assertThat(sut.getPhoneNumber()).isEqualTo(CREATED_CUSTOMER_ID.getPhoneNumber());
        assertThat(sut.getActive()).isEqualTo(CREATED_CUSTOMER_ID.getActive());
        assertThat(sut.getExpirationDate()).isEqualTo(converter.toStringDate(CREATED_CUSTOMER_ID.getExpirationDate()));
        assertTrue(sut.getUpdatedDate().isEmpty());

    }

    @Test
    public void createCustomer_WithInvalidData_ThrowsException() {

        given(repository.findByCompanyDocumentNumber(anyString())).willReturn(Optional.of(CUSTOMER_ID));

        Exception exception = assertThrows(RuntimeException.class, () -> service.saveCustomer(CUSTOMER_DTO));

        String expectedMessage = "There is already a customer with this document number";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void getCustomer_ByExistingCompanyName_ReturnsCustomer() {

        CUSTOMER_ID.setId("c630b6d5-8650-4bcb-89a2-61e0500fcb95");

        given(repository.findByCompanyName(anyString())).willReturn(List.of(CUSTOMER_ID));

        List<CustomerDTO> sut = service.findByCompanyName("Empresa Portuguesa LTDA");

        assertNotNull(sut);
        assertThat(sut).isNotEmpty();
        assertThat(sut.getFirst().getCompanyName()).isEqualTo(CUSTOMER_ID.getCompanyName());
        assertThat(sut.getFirst().getCompanyDocumentNumber()).isEqualTo(CUSTOMER_ID.getCompanyDocumentNumber());
        assertThat(sut.getFirst().getPhoneNumber()).isEqualTo(CUSTOMER_ID.getPhoneNumber());
        assertThat(sut.getFirst().getActive()).isEqualTo(CUSTOMER_ID.getActive());
        assertThat(sut.getFirst().getExpirationDate()).isEqualTo(converter.toStringDate(CUSTOMER_ID.getExpirationDate()));
        assertFalse(sut.getFirst().getUpdatedDate().isEmpty());

    }

    @Test
    public void getCustomer_ByUnExistingCompanyName_ReturnsEmpty() {
        final String name = "UnExisting name";

        given(repository.findByCompanyName(name)).willReturn(List.of());

        List<CustomerDTO> sut = service.findByCompanyName(name);

        assertThat(sut).isEmpty();
    }

    @Test
    public void listCustomers_ReturnsAllCustomers() {

        given(repository.findAll()).willReturn(CUSTOMERS);

        List<CustomerDTO> sut = service.findAllCustomers();

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(3);

        assertNotNull(sut.getFirst());
        assertThat(sut.getFirst().getCompanyName()).isEqualTo(AMERICANA.getCompanyName());
        assertThat(sut.getFirst().getCompanyDocumentNumber()).isEqualTo(AMERICANA.getCompanyDocumentNumber());
        assertThat(sut.getFirst().getPhoneNumber()).isEqualTo(AMERICANA.getPhoneNumber());
        assertThat(sut.getFirst().getExpirationDate()).isEqualTo(converter.toStringDate(AMERICANA.getExpirationDate()));

        assertNotNull(sut.get(1));
        assertThat(sut.get(1).getCompanyName()).isEqualTo(CHINESA.getCompanyName());
        assertThat(sut.get(1).getCompanyDocumentNumber()).isEqualTo(CHINESA.getCompanyDocumentNumber());
        assertThat(sut.get(1).getPhoneNumber()).isEqualTo(CHINESA.getPhoneNumber());
        assertThat(sut.get(1).getExpirationDate()).isEqualTo(converter.toStringDate(CHINESA.getExpirationDate()));


        assertNotNull(sut.getLast());
        assertThat(sut.getLast().getCompanyName()).isEqualTo(BRASILEIRA.getCompanyName());
        assertThat(sut.getLast().getCompanyDocumentNumber()).isEqualTo(BRASILEIRA.getCompanyDocumentNumber());
        assertThat(sut.getLast().getPhoneNumber()).isEqualTo(BRASILEIRA.getPhoneNumber());
        assertThat(sut.getLast().getExpirationDate()).isEqualTo(converter.toStringDate(BRASILEIRA.getExpirationDate()));
    }

    @Test
    public void listPlanets_ReturnsNoCustomers() {
        given(repository.findAll()).willReturn(Collections.emptyList());

        List<CustomerDTO> sut = service.findAllCustomers();

        assertThat(sut).isEmpty();
    }

    @Test
    public void disableCustomer_ByExistingCompanyName_ReturnsCustomer() {

        given(repository.findByCompanyDocumentNumber(anyString())).willReturn(Optional.of(CUSTOMER_ID));
        given(repository.save(any(Customer.class))).willReturn(DISABLE_CUSTOMER_ID);

        //System under test
        CustomerDTO sut = service.disableCustomer(CUSTOMER_ID.getCompanyDocumentNumber());

        assertNotNull(sut);
        assertThat(sut.getCompanyDocumentNumber()).isEqualTo(DISABLE_CUSTOMER_ID.getCompanyDocumentNumber());
        assertThat(sut.getCompanyName()).isEqualTo(DISABLE_CUSTOMER_ID.getCompanyName());
        assertThat(sut.getPhoneNumber()).isEqualTo(DISABLE_CUSTOMER_ID.getPhoneNumber());
        assertFalse(sut.getActive());
        assertFalse(sut.getUpdatedDate().isEmpty());
        assertThat(sut.getUpdatedDate()).isEqualTo(converter.toStringLocalDateTime(DISABLE_CUSTOMER_ID.getUpdatedDate()));

    }

    @Test
    public void disableCustomer_ByUnExistingCompanyDocumentNumber_ReturnsEmpty() {

        given(repository.findByCompanyDocumentNumber(anyString())).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.disableCustomer(CUSTOMER_DTO.getCompanyDocumentNumber()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void disableCustomer_ByUnExistingCompanyDocumentNumber_ReturnsExceptionMessage() {

        Exception exception = assertThrows(RuntimeException.class, () -> service.disableCustomer(null));

        String expectedMessage = "There is no customer with this document number";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void updateCustomer_ByExistingCompanyName_ReturnsUpdatedCustomer() {

        given(repository.findByCompanyDocumentNumber(anyString())).willReturn(Optional.of(CUSTOMER_ID));
        given(repository.save(any(Customer.class))).willReturn(AMERICANA);

        CustomerDTO sut = service.updateCustomer(CUSTOMER_DTO);

        assertNotNull(sut);
        assertThat(sut.getCompanyName()).isEqualTo(AMERICANA.getCompanyName());
        assertThat(sut.getPhoneNumber()).isEqualTo(AMERICANA.getPhoneNumber());
        assertThat(sut.getActive()).isEqualTo(AMERICANA.getActive());

    }

    @Test
    public void updateCustomer_ByUnExistingCompanyDocumentNumber_ReturnsExceptionMessage() {

        Exception exception = assertThrows(RuntimeException.class, () -> service.updateCustomer(CUSTOMER_DTO));

        String expectedMessage = "There is no customer with this document number";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
