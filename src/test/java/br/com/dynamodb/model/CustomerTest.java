package br.com.dynamodb.model;

import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerTest {

    // ---------------------------------------------------------------
    // NoArgsConstructor + setters/getters individuais
    // ---------------------------------------------------------------

    @Test
    void deveCriarComConstrutorVazioESetarTodosOsCamposIndividualmente() {
        Customer customer = new Customer();

        customer.setId("id-1");
        customer.setCompanyName("Empresa Teste");
        customer.setCompanyDocumentNumber("12345678000199");
        customer.setPhoneNumber("81999999999");
        customer.setCreateDate("2024-01-15T10:30:45");
        customer.setUpdatedDate("2024-02-15T10:30:45");
        customer.setExpirationDate(1713186000L);
        customer.setActive(true);

        assertThat(customer.getId()).isEqualTo("id-1");
        assertThat(customer.getCompanyName()).isEqualTo("Empresa Teste");
        assertThat(customer.getCompanyDocumentNumber()).isEqualTo("12345678000199");
        assertThat(customer.getPhoneNumber()).isEqualTo("81999999999");
        assertThat(customer.getCreateDate()).isEqualTo("2024-01-15T10:30:45");
        assertThat(customer.getUpdatedDate()).isEqualTo("2024-02-15T10:30:45");
        assertThat(customer.getExpirationDate()).isEqualTo(1713186000L);
        assertThat(customer.getActive()).isTrue();
    }

    @Test
    void deveAceitarValoresNulosNosCamposOpcionais() {
        Customer customer = new Customer();

        customer.setId("id-2");
        customer.setUpdatedDate(null);
        customer.setExpirationDate(null);
        customer.setActive(null);

        assertThat(customer.getUpdatedDate()).isNull();
        assertThat(customer.getExpirationDate()).isNull();
        assertThat(customer.getActive()).isNull();
    }

    // ---------------------------------------------------------------
    // AllArgsConstructor
    // ---------------------------------------------------------------

    @Test
    void deveCriarComConstrutorComTodosOsArgumentos() {
        Customer customer = new Customer(
                "id-3",
                "Empresa AllArgs",
                "doc-allargs",
                "8100000000",
                "2024-01-01T00:00:00",
                "2024-01-02T00:00:00",
                1713186000L,
                false
        );

        assertThat(customer.getId()).isEqualTo("id-3");
        assertThat(customer.getCompanyName()).isEqualTo("Empresa AllArgs");
        assertThat(customer.getCompanyDocumentNumber()).isEqualTo("doc-allargs");
        assertThat(customer.getPhoneNumber()).isEqualTo("8100000000");
        assertThat(customer.getCreateDate()).isEqualTo("2024-01-01T00:00:00");
        assertThat(customer.getUpdatedDate()).isEqualTo("2024-01-02T00:00:00");
        assertThat(customer.getExpirationDate()).isEqualTo(1713186000L);
        assertThat(customer.getActive()).isFalse();
    }

    // ---------------------------------------------------------------
    // Builder
    // ---------------------------------------------------------------

    @Test
    void deveCriarComBuilderPreenchendoTodosOsCampos() {
        Customer customer = Customer.builder()
                .id("id-4")
                .companyName("Empresa Builder")
                .companyDocumentNumber("doc-builder")
                .phoneNumber("8199999999")
                .createDate("2024-03-01T00:00:00")
                .updatedDate("2024-03-02T00:00:00")
                .expirationDate(1720000000L)
                .active(true)
                .build();

        assertThat(customer.getId()).isEqualTo("id-4");
        assertThat(customer.getCompanyName()).isEqualTo("Empresa Builder");
        assertThat(customer.getCompanyDocumentNumber()).isEqualTo("doc-builder");
        assertThat(customer.getPhoneNumber()).isEqualTo("8199999999");
        assertThat(customer.getCreateDate()).isEqualTo("2024-03-01T00:00:00");
        assertThat(customer.getUpdatedDate()).isEqualTo("2024-03-02T00:00:00");
        assertThat(customer.getExpirationDate()).isEqualTo(1720000000L);
        assertThat(customer.getActive()).isTrue();
    }

    @Test
    void builderDeveAceitarCamposOpcionaisNaoInformados() {
        Customer customer = Customer.builder()
                .id("id-5")
                .companyName("Empresa Sem Update")
                .build();

        assertThat(customer.getId()).isEqualTo("id-5");
        assertThat(customer.getUpdatedDate()).isNull();
        assertThat(customer.getExpirationDate()).isNull();
        assertThat(customer.getActive()).isNull();
    }

    // ---------------------------------------------------------------
    // Serializable
    // ---------------------------------------------------------------

    @Test
    void deveImplementarSerializable() {
        Customer customer = new Customer();

        assertThat(customer).isInstanceOf(Serializable.class);
    }
}