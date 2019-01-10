package com.ndrlslz.configuration.center.api.util;

import com.ndrlslz.configuration.center.api.json.common.Link;
import com.ndrlslz.configuration.center.core.model.Page;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LinksBuilderTest {

    @Before
    public void setUp() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @Test
    public void shouldGenerateThreeLinks() {
        Page<String> page = new Page.Builder<String>()
                .withContent(Arrays.asList("1", "2", "3", "4", "5"))
                .withSize(10)
                .withNumber(0)
                .withTotalElements(5)
                .withTotalPages(1)
                .build();

        List<Link> links = new LinksBuilder().withPage(page).build();

        Map<String, String> map = links.stream().collect(toMap(Link::getRef, Link::getHref));
        assertThat(map.size(), is(3));
        assertThat(map.get("self"), is("http://localhost?size=10&page=0"));
        assertThat(map.get("first"), is("http://localhost?size=10&page=0"));
        assertThat(map.get("last"), is("http://localhost?size=10&page=0"));
    }

    @Test
    public void shouldGenerateFiveLinks() {
        Page<String> page = new Page.Builder<String>()
                .withContent(Arrays.asList("1", "2", "3", "4", "5"))
                .withSize(2)
                .withNumber(1)
                .withTotalElements(5)
                .withTotalPages(3)
                .build();

        List<Link> links = new LinksBuilder().withPage(page).build();

        Map<String, String> map = links.stream().collect(toMap(Link::getRef, Link::getHref));
        assertThat(map.size(), is(5));
        assertThat(map.get("self"), is("http://localhost?size=2&page=1"));
        assertThat(map.get("first"), is("http://localhost?size=2&page=0"));
        assertThat(map.get("last"), is("http://localhost?size=2&page=2"));
        assertThat(map.get("prev"), is("http://localhost?size=2&page=0"));
        assertThat(map.get("next"), is("http://localhost?size=2&page=2"));
    }

    @Test
    public void shouldGenerateFourLinksExceptPrevious() {
        Page<String> page = new Page.Builder<String>()
                .withContent(Arrays.asList("1", "2", "3", "4", "5"))
                .withSize(2)
                .withNumber(0)
                .withTotalElements(5)
                .withTotalPages(3)
                .build();

        List<Link> links = new LinksBuilder().withPage(page).build();

        Map<String, String> map = links.stream().collect(toMap(Link::getRef, Link::getHref));
        assertThat(map.size(), is(4));
        assertThat(map.get("self"), is("http://localhost?size=2&page=0"));
        assertThat(map.get("first"), is("http://localhost?size=2&page=0"));
        assertThat(map.get("last"), is("http://localhost?size=2&page=2"));
        assertThat(map.get("next"), is("http://localhost?size=2&page=1"));
    }

    @Test
    public void shouldGenerateFourLinksExceptNext() {
        Page<String> page = new Page.Builder<String>()
                .withContent(Arrays.asList("1", "2", "3", "4", "5"))
                .withSize(2)
                .withNumber(2)
                .withTotalElements(5)
                .withTotalPages(3)
                .build();

        List<Link> links = new LinksBuilder().withPage(page).build();

        Map<String, String> map = links.stream().collect(toMap(Link::getRef, Link::getHref));
        assertThat(map.size(), is(4));
        assertThat(map.get("self"), is("http://localhost?size=2&page=2"));
        assertThat(map.get("first"), is("http://localhost?size=2&page=0"));
        assertThat(map.get("last"), is("http://localhost?size=2&page=2"));
        assertThat(map.get("prev"), is("http://localhost?size=2&page=1"));
    }

    @Test
    public void shouldNotGeneratePreviousAndNextLinkGivenPageNumberIsLargerThanTotalPages() {
        Page<String> page = new Page.Builder<String>()
                .withContent(Arrays.asList("1", "2", "3", "4", "5"))
                .withSize(2)
                .withNumber(10)
                .withTotalElements(5)
                .withTotalPages(3)
                .build();

        List<Link> links = new LinksBuilder().withPage(page).build();

        Map<String, String> map = links.stream().collect(toMap(Link::getRef, Link::getHref));
        assertThat(map.size(), is(3));
        assertThat(map.get("self"), is("http://localhost?size=2&page=10"));
        assertThat(map.get("first"), is("http://localhost?size=2&page=0"));
        assertThat(map.get("last"), is("http://localhost?size=2&page=2"));
    }

    @Test
    public void shouldNotGeneratePreviousAndNextLinkGivenPageNumberIsNegative() {
        Page<String> page = new Page.Builder<String>()
                .withContent(Arrays.asList("1", "2", "3", "4", "5"))
                .withSize(2)
                .withNumber(-10)
                .withTotalElements(5)
                .withTotalPages(3)
                .build();

        List<Link> links = new LinksBuilder().withPage(page).build();

        Map<String, String> map = links.stream().collect(toMap(Link::getRef, Link::getHref));
        assertThat(map.size(), is(3));
        assertThat(map.get("self"), is("http://localhost?size=2&page=-10"));
        assertThat(map.get("first"), is("http://localhost?size=2&page=0"));
        assertThat(map.get("last"), is("http://localhost?size=2&page=2"));
    }

    @Test
    public void shouldNotGeneratePreviousAndNextLinkGivenEmptyContent() {
        Page<String> page = new Page.Builder<String>()
                .withContent(new ArrayList<>())
                .withSize(10)
                .withNumber(0)
                .withTotalElements(0)
                .withTotalPages(0)
                .build();

        List<Link> links = new LinksBuilder().withPage(page).build();

        Map<String, String> map = links.stream().collect(toMap(Link::getRef, Link::getHref));
        assertThat(map.size(), is(3));
        assertThat(map.get("self"), is("http://localhost?size=10&page=0"));
        assertThat(map.get("first"), is("http://localhost?size=10&page=0"));
        assertThat(map.get("last"), is("http://localhost?size=10&page=0"));
    }
}