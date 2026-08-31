package br.com.dynamodb.repository;

import br.com.dynamodb.model.Customer;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DynamoDbRepositoryTest {

    @Mock
    private DynamoDbTemplate dynamoDbTemplate;

    private DynamoDbRepository repository;

    @BeforeEach
    void setup() {
        repository = new DynamoDbRepository();
        repository.dynamoDbTemplate = dynamoDbTemplate;
    }

    private static PageIterable<Customer> pageIterableOf(List<Page<Customer>> pages) {
        return PageIterable.create(pages::iterator);
    }

    private static Page<Customer> pageOf(Customer... customers) {
        return Page.builder(Customer.class)
                .items(List.of(customers))
                .build();
    }

    // ---------------------------------------------------------------
    // findByCompanyDocumentNumber
    // ---------------------------------------------------------------

    @Test
    void findByCompanyDocumentNumber_deveRetornarItensDaPrimeiraPagina() {
        Customer customer = Customer.builder()
                .id("id-1")
                .companyDocumentNumber("12345678000199")
                .build();

        when(dynamoDbTemplate.scan(any(ScanEnhancedRequest.class), eq(Customer.class)))
                .thenReturn(pageIterableOf(List.of(pageOf(customer))));

        List<Customer> resultado = repository.findByCompanyDocumentNumber("12345678000199");

        assertThat(resultado).containsExactly(customer);
    }

    @Test
    void findByCompanyDocumentNumber_deveMontarFilterExpressionComODocumentoInformado() {
        when(dynamoDbTemplate.scan(any(ScanEnhancedRequest.class), eq(Customer.class)))
                .thenReturn(pageIterableOf(List.of(pageOf())));

        ArgumentCaptor<ScanEnhancedRequest> requestCaptor = ArgumentCaptor.forClass(ScanEnhancedRequest.class);

        repository.findByCompanyDocumentNumber("doc-123");

        verify(dynamoDbTemplate).scan(requestCaptor.capture(), eq(Customer.class));
        ScanEnhancedRequest request = requestCaptor.getValue();

        assertThat(request.filterExpression().expression())
                .isEqualTo("company_document_number = :company_document_number");
        assertThat(request.filterExpression().expressionValues())
                .containsEntry(":company_document_number",
                        software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromS("doc-123"));
    }

    @Test
    void findByCompanyDocumentNumber_semPaginasRetornadas_deveLancarExcecao() {
        when(dynamoDbTemplate.scan(any(ScanEnhancedRequest.class), eq(Customer.class)))
                .thenReturn(pageIterableOf(List.of()));

        assertThatThrownBy(() -> repository.findByCompanyDocumentNumber("inexistente"))
                .isInstanceOf(NoSuchElementException.class);
    }

    // ---------------------------------------------------------------
    // findByCompanyName
    // ---------------------------------------------------------------

    @Test
    void findByCompanyName_deveRetornarItensDeTodasAsPaginas() {
        Customer customer1 = Customer.builder().id("id-1").companyName("Empresa A").build();
        Customer customer2 = Customer.builder().id("id-2").companyName("Empresa A").build();

        when(dynamoDbTemplate.scan(any(ScanEnhancedRequest.class), eq(Customer.class)))
                .thenReturn(pageIterableOf(List.of(pageOf(customer1), pageOf(customer2))));

        List<Customer> resultado = repository.findByCompanyName("Empresa A");

        assertThat(resultado).containsExactly(customer1, customer2);
    }

    @Test
    void findByCompanyName_semResultados_deveRetornarListaVazia() {
        when(dynamoDbTemplate.scan(any(ScanEnhancedRequest.class), eq(Customer.class)))
                .thenReturn(pageIterableOf(List.of()));

        List<Customer> resultado = repository.findByCompanyName("Inexistente");

        assertThat(resultado).isEmpty();
    }

    @Test
    void findByCompanyName_deveMontarFilterExpressionComONomeInformado() {
        when(dynamoDbTemplate.scan(any(ScanEnhancedRequest.class), eq(Customer.class)))
                .thenReturn(pageIterableOf(List.of()));

        ArgumentCaptor<ScanEnhancedRequest> requestCaptor = ArgumentCaptor.forClass(ScanEnhancedRequest.class);

        repository.findByCompanyName("Empresa X");

        verify(dynamoDbTemplate).scan(requestCaptor.capture(), eq(Customer.class));
        ScanEnhancedRequest request = requestCaptor.getValue();

        assertThat(request.filterExpression().expression())
                .isEqualTo("company_name = :company_name");
        assertThat(request.filterExpression().expressionValues())
                .containsEntry(":company_name",
                        software.amazon.awssdk.services.dynamodb.model.AttributeValue.fromS("Empresa X"));
    }

    // ---------------------------------------------------------------
    // findCompanyNameByQuery
    // ---------------------------------------------------------------

    @Test
    void findCompanyNameByQuery_deveRetornarPrimeiroItemQuandoExiste() {
        Customer customer = Customer.builder().id("id-3").companyName("Empresa Query").build();

        when(dynamoDbTemplate.query(any(QueryEnhancedRequest.class), eq(Customer.class), eq("xCompanyName")))
                .thenReturn(pageIterableOf(List.of(pageOf(customer))));

        Optional<Customer> resultado = repository.findCompanyNameByQuery("Empresa Query");

        assertThat(resultado).contains(customer);
    }

    @Test
    void findCompanyNameByQuery_semResultados_deveRetornarOptionalVazio() {
        when(dynamoDbTemplate.query(any(QueryEnhancedRequest.class), eq(Customer.class), eq("xCompanyName")))
                .thenReturn(pageIterableOf(List.of()));

        Optional<Customer> resultado = repository.findCompanyNameByQuery("Inexistente");

        assertThat(resultado).isEmpty();
    }

    @Test
    void findCompanyNameByQuery_deveConsultarOIndiceXCompanyName() {
        when(dynamoDbTemplate.query(any(QueryEnhancedRequest.class), eq(Customer.class), eq("xCompanyName")))
                .thenReturn(pageIterableOf(List.of()));

        repository.findCompanyNameByQuery("Empresa Y");

        verify(dynamoDbTemplate).query(any(QueryEnhancedRequest.class), eq(Customer.class), eq("xCompanyName"));
    }

    // ---------------------------------------------------------------
    // findAllCustomers
    // ---------------------------------------------------------------

    @Test
    void findAllCustomers_deveRetornarItensDeTodasAsPaginas() {
        Customer customer1 = Customer.builder().id("id-1").build();
        Customer customer2 = Customer.builder().id("id-2").build();

        when(dynamoDbTemplate.scanAll(Customer.class))
                .thenReturn(pageIterableOf(List.of(pageOf(customer1), pageOf(customer2))));

        List<Customer> resultado = repository.findAllCustomers();

        assertThat(resultado).containsExactly(customer1, customer2);
    }

    @Test
    void findAllCustomers_semClientes_deveRetornarListaVazia() {
        when(dynamoDbTemplate.scanAll(Customer.class))
                .thenReturn(pageIterableOf(List.of()));

        List<Customer> resultado = repository.findAllCustomers();

        assertThat(resultado).isEmpty();
    }
}
