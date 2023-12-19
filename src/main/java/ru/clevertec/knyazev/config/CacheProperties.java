package ru.clevertec.knyazev.config;

import lombok.Builder;

/**
 *
 * Represents cache properties from application property file
 *
 * @param algorithm cache algorithm type
 * @param size cache size
 */
@Builder
public record CacheProperties (
    String algorithm,

    Integer size
) {}
