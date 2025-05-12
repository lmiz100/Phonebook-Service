package io.github.lmizrahi.phonebook.dto;

public record ErrorResponse(int statusCode, String message,String requestPath) {
}
