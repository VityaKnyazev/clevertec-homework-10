package ru.clevertec.knyazev.pagination.impl;

import ru.clevertec.knyazev.config.PagingProperties;
import ru.clevertec.knyazev.pagination.Paging;

public class PagingImpl implements Paging {

    private Integer page;
    private Integer pageElements;

    public PagingImpl(Integer page,
                      Integer pageSize,
                      PagingProperties pagingProperties) {
        this.page = (page != null && page > 0)
                ? page
                : pagingProperties.defaultPage();
        this.pageElements = (pageSize != null && pageSize > 0)
                ? pageSize
                : pagingProperties.defaultPageSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPage() {
        return page;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getLimit() {
        return pageElements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getOffset() {
        return page * pageElements - pageElements;
    }
}
