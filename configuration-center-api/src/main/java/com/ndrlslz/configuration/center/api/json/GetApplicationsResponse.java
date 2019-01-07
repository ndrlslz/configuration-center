package com.ndrlslz.configuration.center.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ndrlslz.configuration.center.api.model.Application;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetApplicationsResponse {
    @JsonProperty("data")
    @ApiModelProperty(value = "Applications Information", required = true, position = 1)
    private List<Data<Application>> data;

    private List<Link> links;

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
