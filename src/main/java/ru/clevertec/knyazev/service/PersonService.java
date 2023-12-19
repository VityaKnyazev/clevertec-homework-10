package ru.clevertec.knyazev.service;

import ru.clevertec.knyazev.data.PersonDTO;
import ru.clevertec.knyazev.service.exception.ServiceException;

import java.util.List;
import java.util.UUID;

/**
 * Represents service that provided operations with person like mapping, validation etc.
 * Person service is working with DAO layer realization to manage data in datasource
 */
public interface PersonService {

    /**
     * Get person DTO by person id
     *
     * @param personId person id
     * @return Person data transfer object
     * @throws ServiceException when person not found
     */
    PersonDTO get(UUID personId) throws ServiceException;

    /**
     * Get all Person data transfer objects
     *
     * @return all person DTOs or empty list
     */
    List<PersonDTO> getAll();

    /**
     * Validate and map person DTO and call to save person to datasource
     * Return saved person with person id like DTO
     *
     * @param personDTO person DTO
     * @return saved person with person id like DTO
     * @throws ServiceException when validation failed or product dto is null
     */
    PersonDTO add(PersonDTO personDTO) throws ServiceException;

    /**
     * Validate and map person DTO and call to update person to datasource
     *
     * @param personDTO person DTO
     * @throws ServiceException when validation failed or product dto is null
     */
    void update(PersonDTO personDTO) throws ServiceException;

    /**
     * Call to delete method to remove person by its id from datasource
     *
     * @param personId person id for removing
     * @throws ServiceException when personId is null
     */
    void remove(UUID personId) throws ServiceException;
}
