package br.com.dynamodb.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class UnprocessableEntityExceptionTest {

    @Test
    void devePropagarAMensagemParaARuntimeException() {
        UnprocessableEntityException exception = new UnprocessableEntityException("dados inválidos");

        assertThat(exception.getMessage()).isEqualTo("dados inválidos");
    }

    @Test
    void deveSerUmaRuntimeException() {
        UnprocessableEntityException exception = new UnprocessableEntityException("qualquer mensagem");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    void deveTerAAnotacaoResponseStatusComHttpStatusUnprocessableEntity() {
        ResponseStatus responseStatus = UnprocessableEntityException.class.getAnnotation(ResponseStatus.class);

        assertThat(responseStatus).isNotNull();
        assertThat(responseStatus.value()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
