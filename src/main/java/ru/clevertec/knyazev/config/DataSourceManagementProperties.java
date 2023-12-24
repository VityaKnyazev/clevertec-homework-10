package ru.clevertec.knyazev.config;

import lombok.Builder;

/**
 *
 * Represents datasource management properties from application property file
 *
 * @param initOnStartup indicator for initializing database with tables and data
 *                      when application starting
 */
@Builder
public record DataSourceManagementProperties(
    Boolean initOnStartup
) {}
