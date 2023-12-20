package ru.clevertec.knyazev.datasource.managing;


import ru.clevertec.knyazev.datasource.managing.exception.DatabaseManagerException;

/**
 *
 * Represents manager for managing database data (set start-up data and so on)
 *
 */
public interface DatabaseManager {

    /**
     *
     * Set start-up data to database
     *
     * @throws DatabaseManagerException when error on connecting to database resource or
     *         error when loading data
     */

    void loadData() throws DatabaseManagerException;
}
