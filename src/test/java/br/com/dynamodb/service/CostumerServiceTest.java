package br.com.dynamodb.service;


import br.com.dynamodb.converter.Converter;
import br.com.dynamodb.dto.CostumerDTO;
import br.com.dynamodb.model.Costumer;
import br.com.dynamodb.repository.CostumerRepository;
import br.com.dynamodb.service.impl.CostumerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.com.dynamodb.commom.CostumerConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CostumerServiceTest {

    @InjectMocks
    private CostumerServiceImpl service;

    @Mock
    private CostumerRepository repository;

    public Converter converter = new Converter();


//    @Test
//    public void createCostumer_WithValidData_ReturnsCostumer() {
//        when(repository.findByCompanyDocumentNumber(anyString())).thenReturn(Optional.empty());
//        when(repository.save(converter.toCostumer(COSTUMER_DTO))).thenReturn(COSTUMER_ID);
//
//        //System under test
//        CostumerDTO sut = service.saveCostumer(COSTUMER_DTO);
//
//        assertThat(sut.getCreateDate()).isNotEmpty();
//        assertThat(sut.getCompanyName()).isEqualTo(COSTUMER_DTO.getCompanyName());
//    }


    @Test
    public void createCostumer_WithInvalidData_ThrowsException() {
        when(repository.save(INVALID_COSTUMER)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> service.saveCostumer(INVALID_COSTUMER_DTO)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getCostumer_ByExistingCompanyName_ReturnsCostumer() {

        when(repository.findByCompanyName(anyString())).thenReturn(List.of(COSTUMER_ID));

        List<CostumerDTO> sut = service.findByCompanyName("Empresa Portuguesa LTDA");

        assertThat(sut).isNotEmpty();
        assertThat(sut.getFirst().getCompanyName()).isEqualTo(COSTUMER_ID.getCompanyName());
        assertThat(sut.getFirst().getCompanyDocumentNumber()).isEqualTo(COSTUMER_ID.getCompanyDocumentNumber());
        assertThat(sut.getFirst().getPhoneNumber()).isEqualTo(COSTUMER_ID.getPhoneNumber());
    }

    @Test
    public void getCostumer_ByUnExistingCompanyName_ReturnsEmpty() {
        final String name = "UnExisting name";

        when(repository.findByCompanyName(name)).thenReturn(List.of());

        List<CostumerDTO> sut = service.findByCompanyName(name);

        assertThat(sut).isEmpty();
    }

    @Test
    public void listCostumers_ReturnsAllCostumers() {
        List<Costumer> costumers = new ArrayList<>() {{
            add(COSTUMER_ID);
        }};

        when(repository.findAll()).thenReturn(costumers);

        List<CostumerDTO> sut = service.findAllCostumers();

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(1);
        assertThat(sut.getFirst().getCompanyName()).isEqualTo(COSTUMER_ID.getCompanyName());
        assertThat(sut.getFirst().getCompanyDocumentNumber()).isEqualTo(COSTUMER_ID.getCompanyDocumentNumber());
        assertThat(sut.getFirst().getPhoneNumber()).isEqualTo(COSTUMER_ID.getPhoneNumber());
    }
}
