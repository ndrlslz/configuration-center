package com.ndrlslz.configuration.center.api.json.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Link {
    @JsonProperty("ref")
    @ApiModelProperty(value = "Link Relationship", required = true, position = 1)
    private String ref;

    @JsonProperty("href")
    @ApiModelProperty(value = "Link Href", required = true, position = 2)
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
