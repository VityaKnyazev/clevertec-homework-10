package ru.clevertec.knyazev.dao.impl;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.clevertec.knyazev.dao.PersonDAO;
import ru.clevertec.knyazev.entity.Person;
import ru.clevertec.knyazev.entity.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ServiceDAOImplTest {

    @Mock
    PersonDAO personDAOImplMock;

    @Mock
    JdbcTemplate jdbcTemplateMock;

    @InjectMocks
    ServiceDAOImpl serviceDAOImpl;


    @Test
    public void checkFindByPersonIdShouldReturnServices() {

        Person expectedPerson = Person.builder()
                .id(new UUID(24L, 18L))
                .name("Misha")
                .surname("Ivanov")
                .email("misha@mail.ru")
                .citizenship("Russia")
                .age(34)
                .build();

        List<Service> expectedServices = new ArrayList<>() {{
            add(Service.builder()
                    .id(UUID.randomUUID())
                    .name("Справка о составе семьи")
                    .description("Справка гражданину")
                    .price(new BigDecimal("74.00"))
                    .build());
            add(Service.builder()
                    .id(UUID.randomUUID())
                    .name("Справка")
                    .description("Справка о количестве детей")
                    .price(new BigDecimal("37.00"))
                    .build());
        }};

        Mockito.when(personDAOImplMock.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(expectedPerson));

        Mockito.when(jdbcTemplateMock.query(Mockito.anyString(),
                        Mockito.any(RowMapper.class),
                        Mockito.any(UUID.class)))
                .thenReturn(expectedServices);

        UUID inputPersonId = new UUID(24L, 18L);
        List<Service> actualServices = serviceDAOImpl.findByPersonId(inputPersonId);

        assertThat(actualServices).isNotEmpty()
                .hasSize(2)
                .has(new Condition<>(services -> services
                        .stream()
                        .allMatch(s -> s.getPerson().equals(expectedPerson)), "check person"));
    }

    @Test
    public void checkFindByPersonIdShouldReturnEmptyList() {

        Optional<Person> expectedPersonWrap = Optional.empty();

        Mockito.when(personDAOImplMock.findById(Mockito.any(UUID.class)))
                .thenReturn(expectedPersonWrap);

        UUID inputUUID = new UUID(16L, 188L);
        List<Service> actualServices = serviceDAOImpl.findByPersonId(inputUUID);

        assertThat(actualServices).isEmpty();
    }

    @Test
    public void checkFindByPersonIdShouldReturnEmptyListWhenDataAccessException() {

        Person expectedPerson = Person.builder()
                .id(new UUID(16L, 188L))
                .name("Masha")
                .surname("Ivanova")
                .email("masha@mail.ru")
                .citizenship("Russia")
                .age(31)
                .build();

        Mockito.when(personDAOImplMock.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(expectedPerson));

        Mockito.when(jdbcTemplateMock.query(Mockito.anyString(),
                        Mockito.any(RowMapper.class),
                        Mockito.any(UUID.class)))
                .thenThrow(new DataAccessException("empty") {
                });

        UUID inputUUID = new UUID(16L, 188L);
        List<Service> actualServices = serviceDAOImpl.findByPersonId(inputUUID);

        assertThat(actualServices).isEmpty();
    }
}
