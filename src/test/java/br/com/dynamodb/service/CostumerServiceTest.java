package br.com.dynamodb.service;


import br.com.dynamodb.converter.Converter;
import br.com.dynamodb.dto.CostumerDTO;
import br.com.dynamodb.model.Costumer;
import br.com.dynamodb.repository.CostumerRepository;
import br.com.dynamodb.service.impl.CostumerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.dynamodb.commom.CostumerConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CostumerServiceTest {

    @InjectMocks
    private CostumerServiceImpl service;

    @Mock
    private CostumerRepository repository;

    public Converter converter;

    @BeforeEach
    public void setup() {
        converter = new Converter();
    }

    @Test
    public void createCostumer_WithValidData_ReturnsCostumer() {

        given(repository.findByCompanyDocumentNumber(anyString())).willReturn(Optional.empty());
        given(repository.save(any(Costumer.class))).willReturn(COSTUMER_ID);

        //System under test
        CostumerDTO sut = service.saveCostumer(COSTUMER_DTO);

        assertThat(sut.getCreateDate()).isNotEmpty();
        assertThat(sut.getCompanyName()).isEqualTo(COSTUMER_ID.getCompanyName());
        assertThat(sut.getCompanyDocumentNumber()).isEqualTo(COSTUMER_ID.getCompanyDocumentNumber());
        assertThat(sut.getPhoneNumber()).isEqualTo(COSTUMER_ID.getPhoneNumber());
        assertThat(sut.getActive()).isEqualTo(COSTUMER_ID.getActive());
        assertThat(sut.getExpirationDate()).isEqualTo(converter.toStringDate(COSTUMER_ID.getExpirationDate()));

    }

    @Test
    public void createCostumer_WithInvalidData_ThrowsException() {

        given(repository.findByCompanyDocumentNumber(anyString())).willReturn(Optional.of(COSTUMER_ID));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.saveCostumer(COSTUMER_DTO);
        });

        String expectedMessage = "There is already a customer with this document number";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void getCostumer_ByExistingCompanyName_ReturnsCostumer() {

        COSTUMER_ID.setId("c630b6d5-8650-4bcb-89a2-61e0500fcb95");

        given(repository.findByCompanyName(anyString())).willReturn(List.of(COSTUMER_ID));

        List<CostumerDTO> sut = service.findByCompanyName("Empresa Portuguesa LTDA");

        assertThat(sut).isNotEmpty();
        assertThat(sut.getFirst().getCompanyName()).isEqualTo(COSTUMER_ID.getCompanyName());
        assertThat(sut.getFirst().getCompanyDocumentNumber()).isEqualTo(COSTUMER_ID.getCompanyDocumentNumber());
        assertThat(sut.getFirst().getPhoneNumber()).isEqualTo(COSTUMER_ID.getPhoneNumber());
        assertThat(sut.getFirst().getActive()).isEqualTo(COSTUMER_ID.getActive());
        assertThat(sut.getFirst().getExpirationDate()).isEqualTo(converter.toStringDate(COSTUMER_ID.getExpirationDate()));

    }

    @Test
    public void getCostumer_ByUnExistingCompanyName_ReturnsEmpty() {
        final String name = "UnExisting name";

        given(repository.findByCompanyName(name)).willReturn(List.of());

        List<CostumerDTO> sut = service.findByCompanyName(name);

        assertThat(sut).isEmpty();
    }

    @Test
    public void listCostumers_ReturnsAllCostumers() {

        given(repository.findAll()).willReturn(COSTUMERS);

        List<CostumerDTO> sut = service.findAllCostumers();

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(3);

        assertThat(sut.getFirst().getCompanyName()).isEqualTo(AMERICANA.getCompanyName());
        assertThat(sut.getFirst().getCompanyDocumentNumber()).isEqualTo(AMERICANA.getCompanyDocumentNumber());
        assertThat(sut.getFirst().getPhoneNumber()).isEqualTo(AMERICANA.getPhoneNumber());
        assertThat(sut.getFirst().getExpirationDate()).isEqualTo(converter.toStringDate(AMERICANA.getExpirationDate()));

        assertThat(sut.get(1).getCompanyName()).isEqualTo(CHINESA.getCompanyName());
        assertThat(sut.get(1).getCompanyDocumentNumber()).isEqualTo(CHINESA.getCompanyDocumentNumber());
        assertThat(sut.get(1).getPhoneNumber()).isEqualTo(CHINESA.getPhoneNumber());
        assertThat(sut.get(1).getExpirationDate()).isEqualTo(converter.toStringDate(CHINESA.getExpirationDate()));


        assertThat(sut.getLast().getCompanyName()).isEqualTo(BRASILEIRA.getCompanyName());
        assertThat(sut.getLast().getCompanyDocumentNumber()).isEqualTo(BRASILEIRA.getCompanyDocumentNumber());
        assertThat(sut.getLast().getPhoneNumber()).isEqualTo(BRASILEIRA.getPhoneNumber());
        assertThat(sut.getLast().getExpirationDate()).isEqualTo(converter.toStringDate(BRASILEIRA.getExpirationDate()));
    }

    @Test
    public void listPlanets_ReturnsNoCostumers() {
        given(repository.findAll()).willReturn(Collections.emptyList());

        List<CostumerDTO> sut = service.findAllCostumers();

        assertThat(sut).isEmpty();
    }

    @Test
    public void disableCostumer_ByExistingCompanyName_ReturnsCostumer() {

        given(repository.findByCompanyDocumentNumber(anyString())).willReturn(Optional.of(COSTUMER_ID));
        given(repository.save(any(Costumer.class))).willReturn(COSTUMER_ID);

        //System under test
        CostumerDTO sut = service.disableCostumer(COSTUMER_ID.getCompanyDocumentNumber());

        assertThat(sut.getActive()).isEqualTo(false);

    }

    @Test
    public void disableCostumer_ByUnExistingCompanyDocumentNumber_ReturnsEmpty() {

        given(repository.findByCompanyDocumentNumber(anyString())).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.disableCostumer(COSTUMER_DTO.getCompanyDocumentNumber()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void disableCostumer_ByUnExistingCompanyDocumentNumber_ReturnsExceptionMessage() {

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.disableCostumer(null);
        });

        String expectedMessage = "There is no customer with this document number";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void updateCostumer_ByExistingCompanyName_ReturnsUpdatedCostumer() {

        given(repository.findByCompanyDocumentNumber(anyString())).willReturn(Optional.of(COSTUMER_ID));
        given(repository.save(any(Costumer.class))).willReturn(AMERICANA);

        CostumerDTO sut = service.updateCostumer(COSTUMER_DTO);

        assertThat(sut.getCompanyName()).isEqualTo(AMERICANA.getCompanyName());
        assertThat(sut.getPhoneNumber()).isEqualTo(AMERICANA.getPhoneNumber());

    }

    @Test
    public void updateCostumer_ByUnExistingCompanyDocumentNumber_ReturnsExceptionMessage() {

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.updateCostumer(COSTUMER_DTO);
        });

        String expectedMessage = "There is no customer with this document number";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
