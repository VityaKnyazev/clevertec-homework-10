package ru.clevertec.knyazev.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import ru.clevertec.knyazev.dao.exception.DAOException;
import ru.clevertec.knyazev.entity.Person;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class PersonDAOImplTest {

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Mock
    JdbcTemplate jdbcTemplateMock;

    @InjectMocks
    PersonDAOImpl personDAOImpl;

    @Test
    public void checkFindByIdShouldReturnOptionalPerson() {
        Person expectedPerson = Person.builder()
                .id(new UUID(24L, 18L))
                .name("Misha")
                .surname("Ivanov")
                .email("misha@mail.ru")
                .citizenship("Russia")
                .age(34)
                .build();

        Mockito.when(jdbcTemplateMock.queryForObject(Mockito.anyString(), Mockito.any(RowMapper.class), Mockito.any()))
                .thenReturn(expectedPerson);

        UUID inputUUID = new UUID(24L, 18L);
        Optional<Person> actualPerson = personDAOImpl.findById(inputUUID);

        assertThat(actualPerson).isNotNull()
                .isNotEmpty()
                .containsSame(expectedPerson);
    }

    @Test
    public void checkFindByIdShouldReturnOptionalEmpty() {

        Mockito.when(jdbcTemplateMock.queryForObject(Mockito.anyString(), Mockito.any(RowMapper.class), Mockito.any(UUID.class)))
                .thenThrow(new DataAccessException("Id not exists") {
                });

        UUID inputUUID = new UUID(15L, 12L);
        Optional<Person> actualPerson = personDAOImpl.findById(inputUUID);

        assertThat(actualPerson).isEmpty();
    }

    @Test
    public void checkFindAllShouldReturnPersons() {

        Person expectedPerson1 = Person.builder()
                .id(new UUID(24L, 18L))
                .name("Misha")
                .surname("Ivanov")
                .email("misha@mail.ru")
                .citizenship("Russia")
                .age(34)
                .build();
        Person expectedPerson2 = Person.builder()
                .id(new UUID(14L, 128L))
                .name("Masha")
                .surname("Ivanova")
                .email("masha@mail.ru")
                .citizenship("France")
                .age(25)
                .build();

        List<Person> expectedPersons = List.of(expectedPerson1, expectedPerson2);

        Mockito.when(jdbcTemplateMock.query(Mockito.anyString(), Mockito.any(RowMapper.class)))
                .thenReturn(expectedPersons);

        List<Person> actualPersons = personDAOImpl.findAll();

        assertThat(actualPersons).isNotEmpty()
                .isNotNull()
                .containsExactly(expectedPerson1, expectedPerson2);
    }

    @Test
    public void checkFindAllShouldReturnEmptyList() {

        Mockito.when(jdbcTemplateMock.query(Mockito.anyString(), Mockito.any(RowMapper.class)))
                .thenThrow(new DataAccessException("empty") {
                });

        List<Person> actualPersons = personDAOImpl.findAll();

        assertThat(actualPersons).isEmpty();
    }

    @Test
    public void checkSaveShouldReturnSavedPerson() {

        UUID expectedSavedUUID = UUID.randomUUID();

        Mockito.when(jdbcTemplateMock.update(Mockito.any(PreparedStatementCreator.class),
                        Mockito.any(KeyHolder.class)))
                .thenAnswer(invocation -> {
                    KeyHolder keyHolder = invocation.getArgument(1);
                    keyHolder.getKeyList()
                            .add(new HashMap<>() {{
                                put("id", expectedSavedUUID);
                            }});
                    return 1;
                });

        Person inputPerson = Person.builder()
                .name("Misha")
                .surname("Ivanov")
                .email("misha@mail.ru")
                .citizenship("Russia")
                .age(34)
                .build();

        Person actualPerson = personDAOImpl.save(inputPerson);

        assertThat(actualPerson).isNotNull()
                .has(new Condition<>(p -> p.getId().equals(expectedSavedUUID),
                        "saved UUID"));

    }

    @Test
    public void checkSaveShouldThrowDAOException() {

        Mockito.when(jdbcTemplateMock.update(Mockito.any(PreparedStatementCreator.class),
                Mockito.any(KeyHolder.class))).thenThrow(new DataAccessException("Saving constraint") {});

        Person inputPerson = Person.builder()
                .name("Misha")
                .surname("Ivanov")
                .email("misha@mail.ru")
                .citizenship("Russia")
                .age(34)
                .build();

        assertThatExceptionOfType(DAOException.class)
                .isThrownBy(() -> personDAOImpl.save(inputPerson));
    }

    @Test
    public void checkSaveShouldThrowDAOExceptionWhenPersonIdIsNotNull() {

        Person inputPerson = Person.builder()
                .id(UUID.randomUUID())
                .name("Misha")
                .surname("Ivanov")
                .email("misha@mail.ru")
                .citizenship("Russia")
                .age(34)
                .build();

        assertThatExceptionOfType(DAOException.class)
                .isThrownBy(() -> personDAOImpl.save(inputPerson));
    }

    @Test
    public void checkUpdateShouldReturnUpdatedPerson() {

        Mockito.when(jdbcTemplateMock.update(Mockito.anyString(), Mockito.any(Object[].class)))
                .thenReturn(1);

        Person inputPerson = Person.builder()
                .id(UUID.randomUUID())
                .name("Misha")
                .surname("Ivanov")
                .email("misha@mail.ru")
                .citizenship("Russia")
                .age(34)
                .build();

        Person actualPerson = personDAOImpl.update(inputPerson);

        assertThat(actualPerson).isEqualTo(inputPerson);
    }

    @Test
    public void checkUpdateShouldThrowDAOException() {

        Mockito.when(jdbcTemplateMock.update(Mockito.anyString(), Mockito.any(Object[].class)))
                .thenThrow(new DataAccessException("Updating constraint") {});

        Person inputPerson = Person.builder()
                .id(UUID.randomUUID())
                .name("Mimi")
                .surname("Kot")
                .email("mimisha@mail.ru")
                .citizenship("Poland")
                .age(28)
                .build();

        assertThatExceptionOfType(DAOException.class)
                .isThrownBy(() -> personDAOImpl.update(inputPerson));
    }

    @Test
    public void checkUpdateShouldThrowDAOExceptionWhenPersonIdIsNull() {

        Person inputPerson = Person.builder()
                .name("Mimi")
                .surname("Kot")
                .email("mimisha@mail.ru")
                .citizenship("Poland")
                .age(28)
                .build();

        assertThatExceptionOfType(DAOException.class)
                .isThrownBy(() -> personDAOImpl.update(inputPerson));
    }

    @Test
    public void checkDeleteShouldDeletePerson() {

        Person expectedDeletingPerson = Person.builder()
                .id(new UUID(24L, 18L))
                .name("Misha")
                .surname("Ivanov")
                .email("misha@mail.ru")
                .citizenship("Russia")
                .age(34)
                .build();

        Mockito.when(jdbcTemplateMock.queryForObject(Mockito.anyString(), Mockito.any(RowMapper.class), Mockito.any()))
                .thenReturn(expectedDeletingPerson);

        Mockito.when(jdbcTemplateMock.update(Mockito.anyString(), Mockito.any(UUID.class)))
                .thenReturn(1);

        UUID inputPersonDeletingId = new UUID(24L, 18L);
        personDAOImpl.delete(inputPersonDeletingId);


        Mockito.verify(jdbcTemplateMock).update(Mockito.anyString(), uuidArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue())
                .isEqualTo(expectedDeletingPerson.getId())
                .isEqualTo(inputPersonDeletingId);
    }

    @Test
    public void checkDeleteShouldThrowDAOException() {

        Person expectedDeletingPerson = Person.builder()
                .id(new UUID(15L, 118L))
                .name("Misha")
                .surname("Ivanov")
                .email("misha@mail.ru")
                .citizenship("Russia")
                .age(26)
                .build();

        Mockito.when(jdbcTemplateMock.queryForObject(Mockito.anyString(), Mockito.any(RowMapper.class), Mockito.any()))
                .thenReturn(expectedDeletingPerson);

        Mockito.when(jdbcTemplateMock.update(Mockito.anyString(), Mockito.any(UUID.class)))
                .thenThrow(new DataAccessException("Deleting constraint") {});

        UUID deletingPersonUUID = new UUID(15L, 118L);

        assertThatExceptionOfType(DAOException.class)
                .isThrownBy(() -> personDAOImpl.delete(deletingPersonUUID));
    }
}
