package com.ndrlslz.configuration.center.api.json.property;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ndrlslz.configuration.center.api.json.common.Data;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePropertyRequest implements PropertyRequest {
    @JsonProperty("data")
    @ApiModelProperty(value = "Property Information", required = true, position = 1)
    private Data<Property> data;

    public Data<Property> getData() {
        return data;
    }

    public void setData(Data<Property> data) {
        this.data = data;
    }
}
