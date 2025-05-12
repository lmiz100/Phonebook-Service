package io.github.lmizrahi.phonebook.exception;

import io.github.lmizrahi.phonebook.dto.ErrorResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), exchange);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateKey(DuplicateKeyException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), exchange);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ErrorResponse handleResponseStatus(ResponseStatusException ex, ServerWebExchange exchange) {
        return buildErrorResponse(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getMessage(), exchange);
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, String message, ServerWebExchange exchange) {
        return new ErrorResponse(
                status.value(),
                message,
                exchange.getRequest().getPath().toString()
        );
    }
}
