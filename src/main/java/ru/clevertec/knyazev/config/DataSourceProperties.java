package ru.clevertec.knyazev.config;

import lombok.Builder;

/**
 *
 * Represents datasource connection properties from application property file
 *
 * @param driverClassName driver class name
 * @param jdbcUrl JDBC url
 * @param username username
 * @param password password
 * @param maxPoolSize max pool size
 * @param connectionTimeout connection timeout
 */
@Builder
public record DataSourceProperties (

    String driverClassName,

    String jdbcUrl,

    String username,

    String password,

    Integer maxPoolSize,

    Long connectionTimeout
) {}
