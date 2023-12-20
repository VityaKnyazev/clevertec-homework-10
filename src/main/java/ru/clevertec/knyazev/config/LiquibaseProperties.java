package ru.clevertec.knyazev.config;

import lombok.Builder;

/**
 *
 * Represents liquibase properties from application property file
 *
 * @param changelogFile liquibase changelog file path
 */
@Builder
public record LiquibaseProperties(
        String changelogFile
) {}
