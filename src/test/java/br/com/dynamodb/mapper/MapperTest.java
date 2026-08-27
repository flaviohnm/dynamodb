package br.com.dynamodb.mapper;

import br.com.dynamodb.dto.CustomerDTO;
import br.com.dynamodb.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MapperTest {

    private Mapper mapper;

    @BeforeEach
    void setup() {
        mapper = new Mapper();
    }

    // ---------------------------------------------------------------
    // toEpocDate
    // ---------------------------------------------------------------

    @Test
    void toEpocDate_deveSomarTresMesesEConverterParaEpochComOffsetCorreto() {
        // 2024-01-15T10:00:00 + 3 meses = 2024-04-15T10:00:00 (-03:00)
        // == 2024-04-15T13:00:00Z == epoch 1713186000
        // Valor calculado manualmente (dia epoch 19828 * 86400 + 13h em segundos),
        // não recalculado com as mesmas constantes de produção.
        Long resultado = mapper.toEpocDate("2024-01-15T10:00:00");

        assertThat(resultado).isEqualTo(1713186000L);
    }

    @Test
    void toEpocDate_deveTratarViradaDeAnoAoSomarMeses() {
        // 2023-11-15T08:00:00 + 3 meses = 2024-02-15T08:00:00 (-03:00)
        // == 2024-02-15T11:00:00Z == epoch 1707994800
        // Cobre mutantes que quebram a soma de meses na virada de ano.
        Long resultado = mapper.toEpocDate("2023-11-15T08:00:00");

        assertThat(resultado).isEqualTo(1707994800L);
    }

    // ---------------------------------------------------------------
    // toStringDate
    // ---------------------------------------------------------------

    @Test
    void toStringDate_deveFormatarEpochNoFusoDeRecife() {
        // epoch 1713186000 == 2024-04-15T13:00:00Z == 2024-04-15T10:00:00 em America/Recife
        String resultado = mapper.toStringDate(1713186000L);

        assertThat(resultado).isEqualTo("15/04/2024 10:00:00");
    }

    @Test
    void toStringDate_deveTratarViradaDeDiaEAnoAoConverterFuso() {
        // epoch 0 == 1970-01-01T00:00:00Z == 1969-12-31T21:00:00 em America/Recife (-03:00)
        // 21h em relogio de 12h (padrao "hh") = 09
        String resultado = mapper.toStringDate(0L);

        assertThat(resultado).isEqualTo("31/12/1969 09:00:00");
    }

    // ---------------------------------------------------------------
    // toStringLocalDateTime
    // ---------------------------------------------------------------

    @Test
    void toStringLocalDateTime_deveFormatarDataESHora() {
        String resultado = mapper.toStringLocalDateTime("2024-01-15T10:30:45");

        assertThat(resultado).isEqualTo("15/01/2024 10:30:45");
    }

    @Test
    void toStringLocalDateTime_deveTratarMeiaNoiteNoPadraoDeRelogio12h() {
        // hora-do-dia 00 -> "hh" (clock-hour-of-am-pm) formata como 12
        String resultado = mapper.toStringLocalDateTime("2024-06-01T00:00:00");

        assertThat(resultado).isEqualTo("01/06/2024 12:00:00");
    }

    // ---------------------------------------------------------------
    // toCreateCustomer
    // ---------------------------------------------------------------

    @Test
    void toCreateCustomer_deveMontarCustomerAPartirDoDTO() {
        CustomerDTO dto = CustomerDTO.builder()
                .companyName("Empresa Teste LTDA")
                .companyDocumentNumber("12345678000199")
                .phoneNumber("81999999999")
                .build();

        Customer resultado = mapper.toCreateCustomer(dto);

        assertThat(resultado.getId())
                .matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        assertThat(resultado.getCompanyName()).isEqualTo(dto.getCompanyName());
        assertThat(resultado.getCompanyDocumentNumber()).isEqualTo(dto.getCompanyDocumentNumber());
        assertThat(resultado.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());
        assertThat(resultado.getActive()).isTrue();
        assertThat(resultado.getUpdatedDate()).isNull();

        // createDate deve ser um LocalDateTime.now().toString() valido (formato ISO)
        assertDoesNotThrow(() -> LocalDateTime.parse(resultado.getCreateDate()));
        assertThat(LocalDateTime.parse(resultado.getCreateDate()))
                .isCloseTo(LocalDateTime.now(), new org.assertj.core.data.TemporalUnitWithinOffset(5, ChronoUnit.SECONDS));

        // expirationDate deve ser exatamente toEpocDate(createDate) -- garante que os dois campos
        // usam a MESMA data como base (kill de mutante que chamasse LocalDateTime.now() de novo
        // para calcular a expiracao, gerando datas ligeiramente diferentes entre si).
        assertThat(resultado.getExpirationDate()).isEqualTo(mapper.toEpocDate(resultado.getCreateDate()));
    }

    // ---------------------------------------------------------------
    // toCustomerDTO
    // ---------------------------------------------------------------

    @Test
    void toCustomerDTO_comUpdatedDateNulo_naoDeveFormatarUpdatedDate() {
        Customer customer = Customer.builder()
                .id("id-1")
                .companyName("Empresa A")
                .companyDocumentNumber("doc-a")
                .phoneNumber("8100000000")
                .createDate("2024-01-15T10:30:45")
                .expirationDate(1713186000L)
                .updatedDate(null)
                .active(true)
                .build();

        CustomerDTO resultado = mapper.toCustomerDTO(customer);

        assertThat(resultado.getCompanyName()).isEqualTo("Empresa A");
        assertThat(resultado.getCompanyDocumentNumber()).isEqualTo("doc-a");
        assertThat(resultado.getPhoneNumber()).isEqualTo("8100000000");
        assertThat(resultado.getActive()).isTrue();
        assertThat(resultado.getCreateDate()).isEqualTo("15/01/2024 10:30:45");
        assertThat(resultado.getExpirationDate()).isEqualTo("15/04/2024 10:00:00");
        assertThat(resultado.getUpdatedDate()).isNull();
    }

    @Test
    void toCustomerDTO_comUpdatedDatePreenchido_deveFormatarUpdatedDate() {
        Customer customer = Customer.builder()
                .id("id-2")
                .companyName("Empresa B")
                .companyDocumentNumber("doc-b")
                .phoneNumber("8100000001")
                .createDate("2024-01-15T10:30:45")
                .expirationDate(1713186000L)
                .updatedDate("2024-01-15T22:15:00")
                .active(false)
                .build();

        CustomerDTO resultado = mapper.toCustomerDTO(customer);

        assertThat(resultado.getActive()).isFalse();
        assertThat(resultado.getUpdatedDate()).isEqualTo("15/01/2024 10:15:00");
    }

    // ---------------------------------------------------------------
    // optionalToCustomer
    // ---------------------------------------------------------------

    @Test
    void optionalToCustomer_devePreencherTodosOsCamposAPartirDoOptional() {
        Customer original = Customer.builder()
                .id("id-3")
                .companyName("Empresa C")
                .companyDocumentNumber("doc-c")
                .phoneNumber("8100000002")
                .createDate("2024-01-01T00:00:00")
                .expirationDate(1713186000L)
                .updatedDate("2024-01-02T00:00:00")
                .active(true)
                .build();

        Customer resultado = mapper.optionalToCustomer(Optional.of(original));

        assertThat(resultado).usingRecursiveComparison().isEqualTo(original);
    }

    @Test
    void optionalToCustomer_comOptionalVazio_deveLancarExcecao() {
        assertThatThrownBy(() -> mapper.optionalToCustomer(Optional.empty()))
                .isInstanceOf(NoSuchElementException.class);
    }

    // ---------------------------------------------------------------
    // optionalToUpdateCustomer
    // ---------------------------------------------------------------

    @Test
    void optionalToUpdateCustomer_devePreservarDadosOriginaisEAplicarDadosDoDTO() {
        Customer original = Customer.builder()
                .id("id-4")
                .companyName("Nome Antigo")
                .companyDocumentNumber("doc-original")
                .phoneNumber("8100000003")
                .createDate("2024-01-01T00:00:00")
                .expirationDate(1713186000L)
                .updatedDate("2023-01-01T00:00:00")
                .active(false)
                .build();

        CustomerDTO dto = CustomerDTO.builder()
                .companyName("Nome Novo")
                .companyDocumentNumber("doc-que-deve-ser-ignorado")
                .phoneNumber("8199999999")
                .build();

        Customer resultado = mapper.optionalToUpdateCustomer(original, dto);

        // Campos que vem do customer original (nao do DTO)
        assertThat(resultado.getId()).isEqualTo(original.getId());
        assertThat(resultado.getCompanyDocumentNumber()).isEqualTo(original.getCompanyDocumentNumber());
        assertThat(resultado.getCreateDate()).isEqualTo(original.getCreateDate());
        assertThat(resultado.getExpirationDate()).isEqualTo(original.getExpirationDate());

        // Campos que vem do DTO (sobrescrevem o customer original)
        assertThat(resultado.getCompanyName()).isEqualTo(dto.getCompanyName());
        assertThat(resultado.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());

        // Sempre reativado e com updatedDate renovado
        assertThat(resultado.getActive()).isTrue();
        assertThat(resultado.getUpdatedDate()).isNotEqualTo(original.getUpdatedDate());
        assertDoesNotThrow(() -> LocalDateTime.parse(resultado.getUpdatedDate()));
        assertThat(LocalDateTime.parse(resultado.getUpdatedDate()))
                .isCloseTo(LocalDateTime.now(), new org.assertj.core.data.TemporalUnitWithinOffset(5, ChronoUnit.SECONDS));
    }

    // ---------------------------------------------------------------
    // optionalToDisableCustomer
    // ---------------------------------------------------------------

    @Test
    void optionalToDisableCustomer_devePreservarDadosEDesativar() {
        Customer original = Customer.builder()
                .id("id-5")
                .companyName("Empresa D")
                .companyDocumentNumber("doc-d")
                .phoneNumber("8100000004")
                .createDate("2024-01-01T00:00:00")
                .expirationDate(1713186000L)
                .updatedDate("2023-01-01T00:00:00")
                .active(true)
                .build();

        Customer resultado = mapper.optionalToDisableCustomer(original);

        assertThat(resultado.getId()).isEqualTo(original.getId());
        assertThat(resultado.getCompanyName()).isEqualTo(original.getCompanyName());
        assertThat(resultado.getCompanyDocumentNumber()).isEqualTo(original.getCompanyDocumentNumber());
        assertThat(resultado.getPhoneNumber()).isEqualTo(original.getPhoneNumber());
        assertThat(resultado.getCreateDate()).isEqualTo(original.getCreateDate());
        assertThat(resultado.getExpirationDate()).isEqualTo(original.getExpirationDate());

        assertThat(resultado.getActive()).isFalse();
        assertThat(resultado.getUpdatedDate()).isNotEqualTo(original.getUpdatedDate());
        assertDoesNotThrow(() -> LocalDateTime.parse(resultado.getUpdatedDate()));
        assertThat(LocalDateTime.parse(resultado.getUpdatedDate()))
                .isCloseTo(LocalDateTime.now(), new org.assertj.core.data.TemporalUnitWithinOffset(5, ChronoUnit.SECONDS));
    }

    // ---------------------------------------------------------------
    // toCustomerDTOList
    // ---------------------------------------------------------------

    @Test
    void toCustomerDTOList_comListaVazia_deveRetornarListaVazia() {
        List<CustomerDTO> resultado = mapper.toCustomerDTOList(List.of());

        assertThat(resultado).isEmpty();
    }

    @Test
    void toCustomerDTOList_deveMapearECManterAOrdemDaLista() {
        Customer customer1 = Customer.builder()
                .id("id-6")
                .companyName("Primeira Empresa")
                .companyDocumentNumber("doc-1")
                .phoneNumber("8100000005")
                .createDate("2024-01-01T00:00:00")
                .expirationDate(1713186000L)
                .active(true)
                .build();

        Customer customer2 = Customer.builder()
                .id("id-7")
                .companyName("Segunda Empresa")
                .companyDocumentNumber("doc-2")
                .phoneNumber("8100000006")
                .createDate("2024-01-02T00:00:00")
                .expirationDate(1713186000L)
                .active(false)
                .build();

        List<CustomerDTO> resultado = mapper.toCustomerDTOList(List.of(customer1, customer2));

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getCompanyName()).isEqualTo("Primeira Empresa");
        assertThat(resultado.get(0).getCompanyDocumentNumber()).isEqualTo("doc-1");
        assertThat(resultado.get(1).getCompanyName()).isEqualTo("Segunda Empresa");
        assertThat(resultado.get(1).getCompanyDocumentNumber()).isEqualTo("doc-2");
    }
}
