package com.ndrlslz.configuration.center.core.model;

import java.util.List;

public class Page<T> {
    private List<T> content;
    private Pagination pagination;
    private long totalElements;
    private long totalPages;

    public Page(List<T> content, Pagination pagination, long totalElements, long totalPages) {
        this.content = content;
        this.pagination = pagination;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public long getTotalPages() {
        return totalPages;
    }
}
