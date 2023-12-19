package ru.clevertec.knyazev.data;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

/**
 * Represents Person data transfer object
 *
 * @param id          person dto id
 * @param name        person dto name
 * @param surname     person dto surname
 * @param email       person dto email
 * @param citizenship person dto citizenship
 * @param age         person dto age
 */
@Builder
public record PersonDTO(
        /*
         * id may be null or not
         */
        UUID id,

        /*
         * name must be not null from 3 to 10 symbols
         */
        @NotNull(message = "Person name must be not null")
        @Size(min = 3, max = 10, message = "Person name must contains from 3 to 10 symbols")
        String name,

        /*
         * surname must be not null from 3 to 20 symbols
         */
        @NotNull(message = "Person surname must be not null")
        @Size(min = 3, max = 20, message = "Person surname must contains from 3 to 20 symbols")
        String surname,

        /*
         * email must be validated of pattern and not null, must be from 5 to 45 symbols
         */
        @NotNull(message = "Person email must be not null")
        @Pattern(regexp = "^[\\w-_]+@[a-z]+\\.[a-z]{2,3}$", message = "Invalid person email")
        @Size(min = 5, max = 45, message = "Person email must contains from 5 to 45 symbols")
        String email,

        /*
         *  citizenship must be not null from 3 to 25 symbols
         */
        @NotNull(message = "Person citizenship must be not null")
        @Size(min = 3, max = 25, message = "Person citizenship must contains from 3 to 10 symbols")
        String citizenship,

        /*
         * Age must be not null and positive from 0 to 150
         */
        @NotNull(message = "Person age must be not null")
        @Positive(message = "Person age must be positive number")
        @Max(value = 150L, message = "Person age must be to 150 years")
        Integer age) {

    public String toXML() {
        return "<person>" + System.lineSeparator()
                + "<id>" + id() + "</id>" + System.lineSeparator()
                + "<name>" + name() + "</name>" + System.lineSeparator()
                + "<surname>" + surname() + "</surname>" + System.lineSeparator()
                + "<email>" + email() + "</email>" + System.lineSeparator()
                + "<citizenship>" + citizenship() + "</citizenship>" + System.lineSeparator()
                + "<age>" + age() + "</age>" + System.lineSeparator()
                + "</person>";
    }
}
