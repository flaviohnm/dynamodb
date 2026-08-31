package br.com.dynamodb.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomizedResponseEntityExceptionHandlerTest {

    @Mock
    private WebRequest webRequest;

    private final CustomizedResponseEntityExceptionHandler handler =
            new CustomizedResponseEntityExceptionHandler();

    // ---------------------------------------------------------------
    // handleAllExceptions
    // ---------------------------------------------------------------

    @Test
    void handleAllExceptions_deveRetornarStatus500ComDetalhesDaExcecao() {
        when(webRequest.getDescription(false)).thenReturn("uri=/v1/customer");
        Exception ex = new RuntimeException("erro inesperado");

        ResponseEntity<ExceptionResponse> resposta = handler.handleAllExceptions(ex, webRequest);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(resposta.getBody().getMessage()).isEqualTo("erro inesperado");
        assertThat(resposta.getBody().getDetails()).isEqualTo("uri=/v1/customer");
        assertThat(resposta.getBody().getTimestamp()).isNotNull();
    }

    // ---------------------------------------------------------------
    // handleNotFoundExceptions
    // ---------------------------------------------------------------

    @Test
    void handleNotFoundExceptions_deveRetornarStatus404ComDetalhesDaExcecao() {
        when(webRequest.getDescription(false)).thenReturn("uri=/v1/customer/123");
        ResourceNotFoundException ex = new ResourceNotFoundException("cliente não encontrado");

        ResponseEntity<ExceptionResponse> resposta = handler.handleNotFoundExceptions(ex, webRequest);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(resposta.getBody().getMessage()).isEqualTo("cliente não encontrado");
        assertThat(resposta.getBody().getDetails()).isEqualTo("uri=/v1/customer/123");
        assertThat(resposta.getBody().getTimestamp()).isNotNull();
    }

    // ---------------------------------------------------------------
    // handleUnprocessableEntityExceptions
    // ---------------------------------------------------------------

    @Test
    void handleUnprocessableEntityExceptions_deveRetornarStatus422ComDetalhesDaExcecao() {
        when(webRequest.getDescription(false)).thenReturn("uri=/v1/customer");
        UnprocessableEntityException ex = new UnprocessableEntityException("dados inválidos");

        ResponseEntity<ExceptionResponse> resposta =
                handler.handleUnprocessableEntityExceptions(ex, webRequest);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT);
        assertThat(resposta.getBody().getMessage()).isEqualTo("dados inválidos");
        assertThat(resposta.getBody().getDetails()).isEqualTo("uri=/v1/customer");
        assertThat(resposta.getBody().getTimestamp()).isNotNull();
    }
}
