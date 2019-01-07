package com.ndrlslz.configuration.center.core.util;

import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PaginationHelperTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowNullPointExceptionGivenContentIsNull() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("content cannot be null");

        Pagination pagination = new Pagination.Builder()
                .withSize(10)
                .withNumber(0).build();
        PaginationHelper.pagination(null, pagination);
    }

    @Test
    public void shouldPagination() {
        List<Integer> data = IntStream.range(0, 100).boxed().collect(toList());

        Pagination pagination = new Pagination.Builder()
                .withSize(10)
                .withNumber(0).build();

        Page<Integer> firstPageData = PaginationHelper.pagination(data, pagination);

        assertThat(firstPageData.getContent().size(), is(10));
        assertThat(firstPageData.getTotalElements(), is(100));
        assertThat(firstPageData.getTotalPages(), is(10));
        assertThat(firstPageData.getSize(), is(10));
        assertThat(firstPageData.getNumber(), is(0));
    }
}