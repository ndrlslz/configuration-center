package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.api.json.Data;
import com.ndrlslz.configuration.center.api.json.GetApplicationsResponse;
import com.ndrlslz.configuration.center.api.model.Application;
import com.ndrlslz.configuration.center.api.model.Type;
import com.ndrlslz.configuration.center.api.service.ConfigurationCenterService;
import com.ndrlslz.configuration.center.core.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@RestController
public class ConfigurationCenterController {
    private final ConfigurationCenterService configurationCenterService;

    @Autowired
    public ConfigurationCenterController(ConfigurationCenterService configurationCenterService) {
        this.configurationCenterService = configurationCenterService;
    }

    @GetMapping(path = "applications")
    public GetApplicationsResponse getApplications(@PageableDefault Pageable pageable) {
        Pagination pagination = new Pagination.Builder()
                .withSize(pageable.getPageSize())
                .withNumber(pageable.getPageNumber())
                .build();

        List<String> applications = configurationCenterService.getApplications(pagination).getContent();

        GetApplicationsResponse getApplicationsResponse = new GetApplicationsResponse();

        List<Data> dataList = applications.stream()
                .map((Function<String, Data>) app -> {
                    Data<Application> data = new Data<>();
                    data.setType(Type.APPLICATION);
                    data.setAttributes(new Application(app));
                    return data;
                }).collect(toList());

        getApplicationsResponse.setData(dataList);
        return getApplicationsResponse;
    }
}
