package br.com.dynamodb.exceptions;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class ExceptionResponseTest {

    @Test
    void deveArmazenarTimestampMessageEDetailsPassadosNoConstrutor() {
        Date timestamp = new Date();

        ExceptionResponse response = new ExceptionResponse(
                timestamp,
                "cliente não encontrado",
                "uri=/v1/customer"
        );

        assertThat(response.getTimestamp()).isEqualTo(timestamp);
        assertThat(response.getMessage()).isEqualTo("cliente não encontrado");
        assertThat(response.getDetails()).isEqualTo("uri=/v1/customer");
    }

    @Test
    void deveAceitarMessageNula() {
        ExceptionResponse response = new ExceptionResponse(new Date(), null, "uri=/v1/customer");

        assertThat(response.getMessage()).isNull();
    }

    @Test
    void deveImplementarSerializable() {
        ExceptionResponse response = new ExceptionResponse(new Date(), "msg", "details");

        assertThat(response).isInstanceOf(Serializable.class);
    }
}