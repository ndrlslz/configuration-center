package com.ndrlslz.configuration.center.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Link {
    public static final String REL_SELF = "self";
    public static final String REL_FIRST = "first";
    public static final String REL_PREVIOUS = "prev";
    public static final String REL_NEXT = "next";
    public static final String REL_LAST = "last";
    private String ref;
    private String href;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
