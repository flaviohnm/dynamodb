package br.com.dynamodb.controller;

import br.com.dynamodb.dto.CustomerDTO;
import br.com.dynamodb.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    private CustomerController controller;

    @BeforeEach
    void setup() {
        controller = new CustomerController(customerService);
    }

    // ---------------------------------------------------------------
    // createCustomer
    // ---------------------------------------------------------------

    @Test
    void createCustomer_deveRetornarStatusOkComCustomerCriadoPeloService() {
        CustomerDTO entrada = CustomerDTO.builder()
                .companyName("Empresa Teste")
                .companyDocumentNumber("12345678000199")
                .phoneNumber("81999999999")
                .build();

        CustomerDTO retornoDoService = CustomerDTO.builder()
                .companyName("Empresa Teste")
                .companyDocumentNumber("12345678000199")
                .phoneNumber("81999999999")
                .createDate("15/01/2024 10:00:00")
                .active(true)
                .build();

        when(customerService.saveCustomer(entrada)).thenReturn(retornoDoService);

        ResponseEntity<CustomerDTO> resposta = controller.createCustomer(entrada);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).isEqualTo(retornoDoService);
        verify(customerService).saveCustomer(entrada);
    }

    // ---------------------------------------------------------------
    // findCustomerByName
    // ---------------------------------------------------------------

    @Test
    void findCustomerByName_deveRetornarListaRetornadaPeloService() {
        String companyName = "Empresa Teste";
        List<CustomerDTO> lista = List.of(
                CustomerDTO.builder().companyName(companyName).build()
        );

        when(customerService.findByCompanyName(companyName)).thenReturn(lista);

        ResponseEntity<List<CustomerDTO>> resposta = controller.findCustomerByName(companyName);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).isEqualTo(lista);
        verify(customerService).findByCompanyName(companyName);
    }

    @Test
    void findCustomerByName_devePropagarListaVaziaSemAlterarNada() {
        when(customerService.findByCompanyName("Inexistente")).thenReturn(List.of());

        ResponseEntity<List<CustomerDTO>> resposta = controller.findCustomerByName("Inexistente");

        assertThat(resposta.getBody()).isEmpty();
    }

    // ---------------------------------------------------------------
    // findCompanyNameByQuery
    // ---------------------------------------------------------------

    @Test
    void findCompanyNameByQuery_deveRetornarCustomerRetornadoPeloService() {
        String companyName = "Empresa Query";
        CustomerDTO dto = CustomerDTO.builder().companyName(companyName).build();

        when(customerService.findCompanyNameByQuery(companyName)).thenReturn(dto);

        ResponseEntity<CustomerDTO> resposta = controller.findCompanyNameByQuery(companyName);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).isEqualTo(dto);
        verify(customerService).findCompanyNameByQuery(companyName);
    }

    // ---------------------------------------------------------------
    // Customers (listar todos)
    // ---------------------------------------------------------------

    @Test
    void customers_deveRetornarTodosOsCustomersDoService() {
        List<CustomerDTO> todos = List.of(
                CustomerDTO.builder().companyName("Empresa A").build(),
                CustomerDTO.builder().companyName("Empresa B").build()
        );

        when(customerService.findAllCustomers()).thenReturn(todos);

        ResponseEntity<List<CustomerDTO>> resposta = controller.Customers();

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).hasSize(2).isEqualTo(todos);
        verify(customerService).findAllCustomers();
    }

    // ---------------------------------------------------------------
    // updateCustomer
    // ---------------------------------------------------------------

    @Test
    void updateCustomer_deveRetornarCustomerAtualizadoPeloService() {
        CustomerDTO entrada = CustomerDTO.builder()
                .companyName("Nome Novo")
                .companyDocumentNumber("doc-1")
                .build();

        CustomerDTO retornoDoService = CustomerDTO.builder()
                .companyName("Nome Novo")
                .companyDocumentNumber("doc-1")
                .updatedDate("15/01/2024 10:00:00")
                .build();

        when(customerService.updateCustomer(entrada)).thenReturn(retornoDoService);

        ResponseEntity<CustomerDTO> resposta = controller.updateCustomer(entrada);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).isEqualTo(retornoDoService);
        verify(customerService).updateCustomer(entrada);
    }

    // ---------------------------------------------------------------
    // disableCustomer
    // ---------------------------------------------------------------

    @Test
    void disableCustomer_deveRetornarCustomerDesativadoPeloService() {
        String companyDocumentNumber = "12345678000199";
        CustomerDTO retornoDoService = CustomerDTO.builder()
                .companyDocumentNumber(companyDocumentNumber)
                .active(false)
                .build();

        when(customerService.disableCustomer(companyDocumentNumber)).thenReturn(retornoDoService);

        ResponseEntity<CustomerDTO> resposta = controller.disableCustomer(companyDocumentNumber);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody().getActive()).isFalse();
        verify(customerService).disableCustomer(eq(companyDocumentNumber));
    }
}
