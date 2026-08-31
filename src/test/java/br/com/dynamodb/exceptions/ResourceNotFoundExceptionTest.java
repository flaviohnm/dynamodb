package br.com.dynamodb.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceNotFoundExceptionTest {

    @Test
    void devePropagarAMensagemParaARuntimeException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("cliente não encontrado");

        assertThat(exception.getMessage()).isEqualTo("cliente não encontrado");
    }

    @Test
    void deveSerUmaRuntimeException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("qualquer mensagem");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    void deveTerAAnotacaoResponseStatusComHttpStatusNotFound() {
        ResponseStatus responseStatus = ResourceNotFoundException.class.getAnnotation(ResponseStatus.class);

        assertThat(responseStatus).isNotNull();
        assertThat(responseStatus.value()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
