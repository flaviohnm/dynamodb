package br.com.dynamodb.service;

import br.com.dynamodb.dto.CustomerDTO;
import br.com.dynamodb.exceptions.ResourceNotFoundException;
import br.com.dynamodb.exceptions.UnprocessableEntityException;
import br.com.dynamodb.mapper.Mapper;
import br.com.dynamodb.model.Customer;
import br.com.dynamodb.repository.DynamoDbRepository;
import br.com.dynamodb.service.impl.CustomerServiceImpl;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    CustomerServiceImpl service;

    @Mock
    DynamoDbRepository repository;

    @Mock
    DynamoDbTemplate dynamoDbTemplate;


    public Mapper mapper;

    private static final String CUSTOMER_IS_ALREADY = "There is already a customer with this document number";
    private static final String CUSTOMER_IS_NOT_EXISTS = "There is not customer with this document number";

    // Valores literais para substituir o uso indevido de anyString() como argumento real.
    private static final String EXISTING_COMPANY_NAME = "Empresa Portuguesa LTDA";
    private static final String UNKNOWN_COMPANY_NAME = "Empresa Inexistente LTDA";
    private static final String UNKNOWN_DOCUMENT_NUMBER = "00000000000199";


    @BeforeEach
    public void setup() {
        mapper = new Mapper();
    }

    @Test
    public void createCustomer_WithValidData_ReturnsCustomer() {

        given(repository.findByCompanyDocumentNumber(anyString())).willReturn(List.of());
        given(dynamoDbTemplate.save(any(Customer.class))).willReturn(CREATED_CUSTOMER_ID);

        //System under test
        CustomerDTO sut = service.saveCustomer(CUSTOMER_DTO);

        assertNotNull(sut);
        assertThat(sut.getCreateDate()).isNotEmpty();
        assertThat(sut.getCompanyName()).isEqualTo(CREATED_CUSTOMER_ID.getCompanyName());
        assertThat(sut.getCompanyDocumentNumber()).isEqualTo(CREATED_CUSTOMER_ID.getCompanyDocumentNumber());
        assertThat(sut.getPhoneNumber()).isEqualTo(CREATED_CUSTOMER_ID.getPhoneNumber());
        assertThat(sut.getActive()).isEqualTo(CREATED_CUSTOMER_ID.getActive());
        assertThat(sut.getExpirationDate()).isEqualTo(mapper.toStringDate(CREATED_CUSTOMER_ID.getExpirationDate()));
        assertNull(sut.getUpdatedDate());

        // Verifica o objeto realmente enviado ao DynamoDB, não só o retorno mockado.
        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(dynamoDbTemplate, times(1)).save(captor.capture());
        Customer persisted = captor.getValue();
        assertThat(persisted.getCompanyName()).isEqualTo(CUSTOMER_DTO.getCompanyName());
        assertThat(persisted.getCompanyDocumentNumber()).isEqualTo(CUSTOMER_DTO.getCompanyDocumentNumber());
        assertThat(persisted.getPhoneNumber()).isEqualTo(CUSTOMER_DTO.getPhoneNumber());
        assertThat(persisted.getActive()).isTrue();

    }

    @Test
    public void createCustomer_WithInvalidData_ThrowsException() {

        given(repository.findByCompanyDocumentNumber(anyString())).willReturn(List.of(CUSTOMER_ID));

        Exception exception = assertThrows(UnprocessableEntityException.class, () -> service.saveCustomer(CUSTOMER_DTO));

        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, CUSTOMER_IS_ALREADY);

        // Garante que nada foi persistido quando a validação falha.
        verify(dynamoDbTemplate, never()).save(any(Customer.class));

    }

    @Test
    public void getCustomer_ByExistingCompanyName_ReturnsCustomer() {

        given(repository.findByCompanyName(anyString())).willReturn(List.of(CUSTOMER_ID));

        List<CustomerDTO> sut = service.findByCompanyName(EXISTING_COMPANY_NAME);

        assertNotNull(sut);
        assertThat(sut).isNotEmpty();
        assertThat(sut.getFirst().getCompanyName()).isEqualTo(CUSTOMER_ID.getCompanyName());
        assertThat(sut.getFirst().getCompanyDocumentNumber()).isEqualTo(CUSTOMER_ID.getCompanyDocumentNumber());
        assertThat(sut.getFirst().getPhoneNumber()).isEqualTo(CUSTOMER_ID.getPhoneNumber());
        assertThat(sut.getFirst().getActive()).isEqualTo(CUSTOMER_ID.getActive());
        assertThat(sut.getFirst().getExpirationDate()).isEqualTo(mapper.toStringDate(CUSTOMER_ID.getExpirationDate()));
        assertFalse(sut.getFirst().getUpdatedDate().isEmpty());
        assertThat(sut.getFirst().getUpdatedDate()).isEqualTo(mapper.toStringLocalDateTime(CUSTOMER_ID.getUpdatedDate()));

        // Verifica que o repositório foi chamado exatamente com o nome informado, uma única vez.
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        verify(repository, times(1)).findByCompanyName(nameCaptor.capture());
        assertThat(nameCaptor.getValue()).isEqualTo(EXISTING_COMPANY_NAME);

    }

    @Test
    public void getCustomer_ByQuery_ExistingCompanyName_ReturnsCustomer() {

        given(repository.findCompanyNameByQuery(anyString())).willReturn(Optional.of(CUSTOMER_ID));

        CustomerDTO sut = service.findCompanyNameByQuery(EXISTING_COMPANY_NAME);

        assertNotNull(sut);
        assertThat(sut.getCompanyName()).isEqualTo(CUSTOMER_ID.getCompanyName());
        assertThat(sut.getCompanyDocumentNumber()).isEqualTo(CUSTOMER_ID.getCompanyDocumentNumber());
        assertThat(sut.getPhoneNumber()).isEqualTo(CUSTOMER_ID.getPhoneNumber());
        assertThat(sut.getActive()).isEqualTo(CUSTOMER_ID.getActive());
        assertThat(sut.getExpirationDate()).isEqualTo(mapper.toStringDate(CUSTOMER_ID.getExpirationDate()));
        assertFalse(sut.getUpdatedDate().isEmpty());
        assertThat(sut.getUpdatedDate()).isEqualTo(mapper.toStringLocalDateTime(CUSTOMER_ID.getUpdatedDate()));

        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        verify(repository, times(1)).findCompanyNameByQuery(nameCaptor.capture());
        assertThat(nameCaptor.getValue()).isEqualTo(EXISTING_COMPANY_NAME);

    }

    @Test
    public void getCustomer_ByQuery_UnExistingCompanyName_ReturnsEmpty() {

        given(repository.findCompanyNameByQuery(anyString())).willReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.findCompanyNameByQuery(UNKNOWN_COMPANY_NAME));

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(CUSTOMER_IS_NOT_EXISTS));

    }


    @Test
    public void getCustomer_ByUnExistingCompanyName_ReturnsEmpty() {

        given(repository.findByCompanyName(anyString())).willReturn(List.of());

        List<CustomerDTO> sut = service.findByCompanyName(UNKNOWN_COMPANY_NAME);

        assertThat(sut).isEmpty();
    }

    @Test
    public void listCustomers_ReturnsAllCustomers() {

        given(repository.findAllCustomers()).willReturn(CUSTOMERS);

        List<CustomerDTO> sut = service.findAllCustomers();

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(4);

        assertNotNull(sut.getFirst());
        assertThat(sut.getFirst().getCompanyName()).isEqualTo(AMERICANA.getCompanyName());
        assertThat(sut.getFirst().getCompanyDocumentNumber()).isEqualTo(AMERICANA.getCompanyDocumentNumber());
        assertThat(sut.getFirst().getPhoneNumber()).isEqualTo(AMERICANA.getPhoneNumber());
        assertThat(sut.getFirst().getExpirationDate()).isEqualTo(mapper.toStringDate(AMERICANA.getExpirationDate()));
        assertFalse(sut.getFirst().getUpdatedDate().isEmpty());
        assertThat(sut.getFirst().getUpdatedDate()).isEqualTo(mapper.toStringLocalDateTime(AMERICANA.getUpdatedDate()));

        assertNotNull(sut.get(1));
        assertThat(sut.get(1).getCompanyName()).isEqualTo(CHINESA.getCompanyName());
        assertThat(sut.get(1).getCompanyDocumentNumber()).isEqualTo(CHINESA.getCompanyDocumentNumber());
        assertThat(sut.get(1).getPhoneNumber()).isEqualTo(CHINESA.getPhoneNumber());
        assertThat(sut.get(1).getExpirationDate()).isEqualTo(mapper.toStringDate(CHINESA.getExpirationDate()));
        assertThat(sut.get(1).getUpdatedDate()).isEqualTo(mapper.toStringLocalDateTime(CHINESA.getUpdatedDate()));

        assertNotNull(sut.get(2));
        assertThat(sut.get(2).getCompanyName()).isEqualTo(CANADENSE.getCompanyName());
        assertThat(sut.get(2).getCompanyDocumentNumber()).isEqualTo(CANADENSE.getCompanyDocumentNumber());
        assertThat(sut.get(2).getPhoneNumber()).isEqualTo(CANADENSE.getPhoneNumber());
        assertThat(sut.get(2).getExpirationDate()).isEqualTo(mapper.toStringDate(CANADENSE.getExpirationDate()));
        assertNull(sut.get(2).getUpdatedDate());


        assertNotNull(sut.getLast());
        assertThat(sut.getLast().getCompanyName()).isEqualTo(BRASILEIRA.getCompanyName());
        assertThat(sut.getLast().getCompanyDocumentNumber()).isEqualTo(BRASILEIRA.getCompanyDocumentNumber());
        assertThat(sut.getLast().getPhoneNumber()).isEqualTo(BRASILEIRA.getPhoneNumber());
        assertThat(sut.getLast().getExpirationDate()).isEqualTo(mapper.toStringDate(BRASILEIRA.getExpirationDate()));
        assertThat(sut.getLast().getUpdatedDate()).isEqualTo(mapper.toStringLocalDateTime(BRASILEIRA.getUpdatedDate()));

        verify(repository, times(1)).findAllCustomers();
    }

    @Test
    public void listCustomers_ReturnsNoCustomers() {
        given(repository.findAllCustomers()).willReturn(Collections.emptyList());

        List<CustomerDTO> sut = service.findAllCustomers();

        assertThat(sut).isEmpty();
    }

    @Test
    public void disableCustomer_ByExistingCompanyName_ReturnsCustomer() {

        given(repository.findByCompanyDocumentNumber(anyString())).willReturn(List.of(CUSTOMER_ID));
        given(dynamoDbTemplate.update(any(Customer.class))).willReturn(DISABLE_CUSTOMER_ID);

        //System under test
        CustomerDTO sut = service.disableCustomer(CUSTOMER_ID.getCompanyDocumentNumber());

        assertNotNull(sut);
        assertThat(sut.getCompanyDocumentNumber()).isEqualTo(DISABLE_CUSTOMER_ID.getCompanyDocumentNumber());
        assertThat(sut.getCompanyName()).isEqualTo(DISABLE_CUSTOMER_ID.getCompanyName());
        assertThat(sut.getPhoneNumber()).isEqualTo(DISABLE_CUSTOMER_ID.getPhoneNumber());
        assertFalse(sut.getActive());
        assertFalse(sut.getUpdatedDate().isEmpty());
        assertThat(sut.getUpdatedDate()).isEqualTo(mapper.toStringLocalDateTime(DISABLE_CUSTOMER_ID.getUpdatedDate()));

        // Confirma que o Customer enviado ao update já foi marcado como inativo antes de persistir.
        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(dynamoDbTemplate, times(1)).update(captor.capture());
        assertFalse(captor.getValue().getActive());

    }

    @Test
    public void disableCustomer_ByUnExistingCompanyDocumentNumber_ReturnsEmpty() {

        given(repository.findByCompanyDocumentNumber(anyString())).willReturn(List.of());

        assertThatThrownBy(() -> service.disableCustomer(CUSTOMER_DTO.getCompanyDocumentNumber()))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(dynamoDbTemplate, never()).update(any(Customer.class));
    }

    @Test
    public void disableCustomer_ByUnExistingCompanyDocumentNumber_ReturnsExceptionMessage() {

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.disableCustomer(UNKNOWN_DOCUMENT_NUMBER));

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(CUSTOMER_IS_NOT_EXISTS));

        verify(dynamoDbTemplate, never()).update(any(Customer.class));
    }

    @Test
    public void updateCustomer_ByExistingCompanyName_ReturnsUpdatedCustomer() {

        given(repository.findByCompanyDocumentNumber(anyString())).willReturn(List.of(CUSTOMER_ID));
        given(dynamoDbTemplate.update(any(Customer.class))).willReturn(AMERICANA);

        CustomerDTO sut = service.updateCustomer(CUSTOMER_DTO);

        assertNotNull(sut);
        assertThat(sut.getCompanyName()).isEqualTo(AMERICANA.getCompanyName());
        assertThat(sut.getCompanyDocumentNumber()).isEqualTo(AMERICANA.getCompanyDocumentNumber());
        assertThat(sut.getPhoneNumber()).isEqualTo(AMERICANA.getPhoneNumber());
        assertThat(sut.getActive()).isEqualTo(AMERICANA.getActive());
        assertThat(sut.getExpirationDate()).isEqualTo(mapper.toStringDate(AMERICANA.getExpirationDate()));
        assertThat(sut.getUpdatedDate()).isEqualTo(mapper.toStringLocalDateTime(AMERICANA.getUpdatedDate()));

        verify(dynamoDbTemplate, times(1)).update(any(Customer.class));

    }

    @Test
    public void updateCustomer_ByUnExistingCompanyDocumentNumber_ReturnsExceptionMessage() {

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.updateCustomer(CUSTOMER_DTO));

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(CUSTOMER_IS_NOT_EXISTS));

        verify(dynamoDbTemplate, never()).update(any(Customer.class));
    }

}