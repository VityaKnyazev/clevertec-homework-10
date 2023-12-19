package ru.clevertec.knyazev.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.clevertec.knyazev.dao.PersonDAO;
import ru.clevertec.knyazev.dao.exception.DAOException;
import ru.clevertec.knyazev.entity.Person;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class PersonDAOImpl implements PersonDAO {
    private static final String FIND_BY_ID = "SELECT id, name, surname, email, citizenship, age FROM person WHERE id=?";
    private static final String FIND_ALL = "SELECT id, name, surname, email, citizenship, age FROM person";
    private static final String SAVE = "INSERT INTO person(name, surname, email, citizenship, age) VALUES(?,?,?,?,?)";
    private static final String UPDATE = """
            UPDATE person SET name = ?, surname = ?, email = ?, citizenship = ?, age = ? WHERE id = ?
            """;
    private static final String DELETE = "DELETE FROM person WHERE id = ?";

    private static final String SEARCHING_ERROR = "Error when searching product(s) {}";
    private static final String ID_ERROR = "Person id error: %s";
    private static final String ID_SAVING_ERROR = "id must be null for saving";
    private static final String ID_UPDATING_ERROR = "id must be not null for updating";

    private JdbcTemplate jdbcTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Person> findById(UUID id) {

        Optional<Person> personWrap = Optional.empty();

        try {
            personWrap = Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID,
                    new BeanPropertyRowMapper<>(Person.class),
                    id));
        } catch (DataAccessException e) {
            log.error(SEARCHING_ERROR, e.getMessage(), e);
        }

        return personWrap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Person> findAll() {

        List<Person> persons = new ArrayList<>();

        try {
            persons = jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Person.class));
        } catch (DataAccessException e) {
            log.error(SEARCHING_ERROR, e.getMessage(), e);
        }

        return persons;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Person save(Person person) {

        if (person.getId() != null) {
            throw new DAOException(String.format(ID_ERROR, ID_SAVING_ERROR));
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(con -> {
                PreparedStatement preparedStatement = con.prepareStatement(SAVE, new String[]{"id"});
                preparedStatement.setString(1, person.getName());
                preparedStatement.setString(2, person.getSurname());
                preparedStatement.setString(3, person.getEmail());
                preparedStatement.setString(4, person.getCitizenship());
                preparedStatement.setInt(5, person.getAge());
                return preparedStatement;
            }, keyHolder);
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }

        person.setId(keyHolder.getKeyAs(UUID.class));

        return person;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Person update(Person person) {

        if (person.getId() == null) {
            throw new DAOException(String.format(ID_ERROR, ID_UPDATING_ERROR));
        }

        try {
            jdbcTemplate.update(UPDATE,
                    person.getName(),
                    person.getSurname(),
                    person.getEmail(),
                    person.getCitizenship(),
                    person.getAge(),
                    person.getId());
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }

        return person;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID personId) {

        try {
            findById(personId).ifPresent(p -> jdbcTemplate.update(DELETE, personId));
        } catch (DataAccessException e) {
            throw new DAOException(e);
        }
    }
}
