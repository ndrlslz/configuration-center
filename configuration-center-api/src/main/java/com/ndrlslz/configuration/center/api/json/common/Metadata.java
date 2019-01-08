package com.ndrlslz.configuration.center.api.json.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {
    @JsonProperty("size")
    @ApiModelProperty(value = "Page Size", position = 1)
    private long size;

    @JsonProperty("number")
    @ApiModelProperty(value = "Page Number", position = 2)
    private long number;

    @JsonProperty("totalElements")
    @ApiModelProperty(value = "Total Page Size", position = 3)
    private long totalElements;

    @JsonProperty("totalPages")
    @ApiModelProperty(value = "Total Page Numbers", position = 4)
    private long totalPages;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }
}
