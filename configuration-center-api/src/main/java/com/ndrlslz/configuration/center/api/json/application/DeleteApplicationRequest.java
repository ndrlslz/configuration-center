package com.ndrlslz.configuration.center.api.json.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ndrlslz.configuration.center.api.json.common.Data;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteApplicationRequest implements ApplicationRequest {
    @JsonProperty("data")
    @ApiModelProperty(value = "Applications Information", required = true, position = 1)
    private Data<Application> data;

    @Override
    public Data<Application> getData() {
        return data;
    }

    public void setData(Data<Application> data) {
        this.data = data;
    }
}
