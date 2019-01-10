package com.ndrlslz.configuration.center.api.json.environment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ndrlslz.configuration.center.api.json.common.Data;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateEnvironmentRequest implements EnvironmentRequest {
    @JsonProperty("data")
    @ApiModelProperty(value = "Environments Information", required = true, position = 1)
    private Data<Environment> data;

    public Data<Environment> getData() {
        return data;
    }

    public void setData(Data<Environment> data) {
        this.data = data;
    }
}
