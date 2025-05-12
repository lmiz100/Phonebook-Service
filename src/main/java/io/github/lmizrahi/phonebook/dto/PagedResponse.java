package io.github.lmizrahi.phonebook.dto;

import java.util.List;

public record PagedResponse<T>(List<T> content, long totalElements, int page, int size) {
}
