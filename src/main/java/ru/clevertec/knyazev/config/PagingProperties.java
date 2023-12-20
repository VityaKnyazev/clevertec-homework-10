package ru.clevertec.knyazev.config;

import lombok.Builder;

/**
 * @param defaultPage default page
 * @param defaultPageSize default quantity elements on page
 */
@Builder
public record PagingProperties(
        Integer defaultPage,
        Integer defaultPageSize
) {}
