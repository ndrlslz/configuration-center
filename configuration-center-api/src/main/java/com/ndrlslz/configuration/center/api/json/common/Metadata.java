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
    private int size;

    @JsonProperty("number")
    @ApiModelProperty(value = "Page Number", position = 2)
    private int number;

    @JsonProperty("totalElements")
    @ApiModelProperty(value = "Total Page Size", position = 3)
    private int totalElements;

    @JsonProperty("totalPages")
    @ApiModelProperty(value = "Total Page Numbers", position = 4)
    private int totalPages;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
