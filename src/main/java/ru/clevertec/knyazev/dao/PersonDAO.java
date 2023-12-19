package ru.clevertec.knyazev.dao;

import ru.clevertec.knyazev.dao.exception.DAOException;
import ru.clevertec.knyazev.entity.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * Represents data access object for Person entity
 *
 */
public interface PersonDAO {

    /**
     *
     * Find person by id
     *
     * @param id person id for searching
     * @return optional person or optional empty if person not found
     */
    Optional<Person> findById(UUID id);

    /**
     *
     * Find all persons
     *
     * @return all persons
     */
    List<Person> findAll();

    /**
     *
     * save person
     *
     * @param person for saving
     * @return saved person
     * @throws DAOException when error saving
     */
    Person save(Person person) throws DAOException;

    /**
     *
     * update person
     *
     * @param person for updating
     * @return updated person
     * @throws DAOException when error updating
     */
    Person update(Person person) throws DAOException;

    /**
     *
     * delete person
     *
     * @param personId person id
     * @throws DAOException when deleting constraint
     */
    void delete(UUID personId) throws DAOException;
}
