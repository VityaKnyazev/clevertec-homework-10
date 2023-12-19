package ru.clevertec.knyazev.pagination;

/**
 *
 * Contract for dealing with paging quires
 *
 */
public interface Paging {
    /**
     *
     * Get current page
     *
     * @return current page
     */
    Integer getPage();

    /**
     *
     * Get records quantity for current page
     *
     * @return records quantity for current page
     */
    Integer getLimit();

    /**
     *
     * Get offset for skipping to start fetching records
     *
     * @return offset for skipping to start fetching records
     */
    Integer getOffset();
}
