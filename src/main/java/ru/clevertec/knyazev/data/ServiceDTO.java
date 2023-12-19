package ru.clevertec.knyazev.data;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * Represents Service of person data transfer object
 *
 * @param id service dto id
 * @param personId service dto person id
 * @param localDateTime service dto local date time
 * @param personName service dto person name
 * @param personSurname service dto person surname
 * @param serviceName service dto name
 * @param description service dto description
 * @param price service dto price
 */
@Builder
public record ServiceDTO(
        UUID id,

        UUID personId,

        LocalDateTime localDateTime,

        String personName,

        String personSurname,

        String serviceName,

        String description,

        BigDecimal price
) {

}
