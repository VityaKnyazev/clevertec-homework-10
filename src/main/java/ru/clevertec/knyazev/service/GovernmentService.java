package ru.clevertec.knyazev.service;

import ru.clevertec.knyazev.service.exception.ServiceException;

import java.util.UUID;

/**
 * Represents service that provide services to people
 */
public interface GovernmentService {

    /**
     *
     * Get absolute path to saved pdf check that contains
     * information about all bought person services
     *
     * @param personId person id
     * @return absolute path to saved pdf check that contains
     *         information about all person services
     * @throws ServiceException when invalid person id or person never used
     *                          services
     */
    String getAbsolutePathByPersonId(UUID personId) throws ServiceException;
}
