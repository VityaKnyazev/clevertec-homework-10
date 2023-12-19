package ru.clevertec.knyazev.config;

import lombok.Builder;

@Builder
public record PagingProperties(
        Integer defaultPage,
        Integer defaultPageSize
) {}
