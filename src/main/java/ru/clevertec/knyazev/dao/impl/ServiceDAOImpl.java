package ru.clevertec.knyazev.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.clevertec.knyazev.dao.PersonDAO;
import ru.clevertec.knyazev.dao.ServiceDAO;
import ru.clevertec.knyazev.entity.Person;
import ru.clevertec.knyazev.entity.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class ServiceDAOImpl implements ServiceDAO {
    private static final String FIND_BY_PERSON_ID = "SELECT id, name, description, price FROM service WHERE person_id = ?";

    private static final String SEARCHING_ERROR = "Error when searching product(s) {}";

    private PersonDAO personDAOImpl;
    private JdbcTemplate jdbcTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Service> findByPersonId(UUID personId) {
        List<Service> services = new ArrayList<>();

        Optional<Person> personWrap = personDAOImpl.findById(personId);

        if (personWrap.isPresent()) {
            Person person = personWrap.get();

            try {
                services = jdbcTemplate.query(FIND_BY_PERSON_ID,
                        new BeanPropertyRowMapper<>(Service.class),
                        personId);
                services.forEach(service -> service.setPerson(person));
            } catch (DataAccessException e) {
                log.error(SEARCHING_ERROR, e.getMessage(), e);
            }

        }

        return services;
    }
}
