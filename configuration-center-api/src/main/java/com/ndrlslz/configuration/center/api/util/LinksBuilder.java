package com.ndrlslz.configuration.center.api.util;

import com.ndrlslz.configuration.center.api.json.Link;
import com.ndrlslz.configuration.center.core.model.Page;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class LinksBuilder {
    private static final String REL_SELF = "self";
    private static final String REL_FIRST = "first";
    private static final String REL_PREVIOUS = "prev";
    private static final String REL_NEXT = "next";
    private static final String REL_LAST = "last";
    private static final String QUERY_PARAM_SIZE = "size";
    private static final String QUERY_PARAM_PAGE = "page";
    private Page page;
    private ServletUriComponentsBuilder servletUriComponentsBuilder;
    private List<Link> links;

    public LinksBuilder() {
        this.servletUriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
        this.links = new ArrayList<>();
    }

    public LinksBuilder withPage(Page page) {
        this.page = page;
        return this;
    }

    public List<Link> build() {
        int totalPages = page.getTotalPages();
        int number = page.getNumber();

        this
                .generateLink(REL_SELF, () -> true, number)
                .generateLink(REL_FIRST, () -> true, 0)
                .generateLink(REL_PREVIOUS, () -> number > 0 && number <= totalPages, number - 1)
                .generateLink(REL_NEXT, () -> number < totalPages - 1, number + 1)
                .generateLink(REL_LAST, () -> true, totalPages - 1);

        return links.stream()
                .filter(link -> nonNull(link.getHref()))
                .collect(toList());
    }

    private LinksBuilder generateLink(String ref, BooleanSupplier supplier, int pageNumber) {
        if (supplier.getAsBoolean()) {
            String href = servletUriComponentsBuilder
                    .replaceQueryParam(QUERY_PARAM_SIZE, page.getSize())
                    .replaceQueryParam(QUERY_PARAM_PAGE, pageNumber)
                    .build()
                    .toString();

            Link link = new Link();
            link.setRef(ref);
            link.setHref(href);
            links.add(link);
        }

        return this;
    }
}
