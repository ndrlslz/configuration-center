package com.ndrlslz.configuration.center.core.util;

import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

public class PaginationHelper {
    public static <T> Page<T> pagination(List<T> content, Pagination pagination) {
        checkNotNull(pagination, "pagination cannot be null");
        checkNotNull(content, "content cannot be null");

        int pageNumber = pagination.getNumber();
        int pageSize = pagination.getSize();
        int totalElements = content.size();
        int totalPages = (totalElements / pageSize) + (totalElements % pageSize == 0 ? 0 : 1);

        return new Page.Builder<T>()
                .withContent(content.stream()
                        .limit(pageNumber * pageSize + pageSize)
                        .skip(pageNumber * pageSize)
                        .collect(toList()))
                .withSize(pageSize)
                .withNumber(pageNumber)
                .withTotalElements(totalElements)
                .withTotalPages(totalPages)
                .build();
    }
}
