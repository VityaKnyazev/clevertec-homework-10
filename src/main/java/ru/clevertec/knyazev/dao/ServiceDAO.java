package ru.clevertec.knyazev.dao;

import ru.clevertec.knyazev.entity.Service;

import java.util.List;
import java.util.UUID;

/**
 * Represents data access object for Service entity
 */
public interface ServiceDAO {
    /**
     *
     * Find Services by person id
     *
     * @param personId person id for searching services that person used
     * @return all services that person used or empty list - if person id
     *         not found or person didn't use any service.
     */
    List<Service> findByPersonId(UUID personId);
}
