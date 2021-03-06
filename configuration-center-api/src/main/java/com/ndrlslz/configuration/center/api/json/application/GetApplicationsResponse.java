package com.ndrlslz.configuration.center.api.json.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Link;
import com.ndrlslz.configuration.center.api.json.common.Metadata;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"data", "links", "metadata"})
public class GetApplicationsResponse {
    @JsonProperty("data")
    @ApiModelProperty(value = "Applications Information", required = true, position = 1)
    private List<Data<Application>> data;

    @JsonProperty("links")
    @ApiModelProperty(value = "Links Information", required = true, position = 2)
    private List<Link> links;

    @JsonProperty("metadata")
    @ApiModelProperty(value = "Pagination Information", required = true, position = 3)
    private Metadata metadata;

    public List<Data<Application>> getData() {
        return data;
    }

    public void setData(List<Data<Application>> data) {
        this.data = data;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
