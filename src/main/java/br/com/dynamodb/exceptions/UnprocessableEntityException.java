package br.com.dynamodb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnprocessableEntityException(String ex) {
        super(ex);
    }
}