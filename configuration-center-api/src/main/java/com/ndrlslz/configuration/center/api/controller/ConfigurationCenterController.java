package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.api.json.Data;
import com.ndrlslz.configuration.center.api.json.GetApplicationsResponse;
import com.ndrlslz.configuration.center.api.json.Link;
import com.ndrlslz.configuration.center.api.json.Metadata;
import com.ndrlslz.configuration.center.api.model.Application;
import com.ndrlslz.configuration.center.api.model.Type;
import com.ndrlslz.configuration.center.api.service.ConfigurationCenterService;
import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
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

        String self = ServletUriComponentsBuilder.fromCurrentRequest().build().toString();

        Page<String> applicationsPage = configurationCenterService.getApplications(pageable);

        List<String> applications = applicationsPage.getContent();

        GetApplicationsResponse getApplicationsResponse = new GetApplicationsResponse();

        List<Data> dataList = applications.stream()
                .map((Function<String, Data>) app -> {
                    Data<Application> applicationData = new Data<>();
                    applicationData.setType(Type.APPLICATION);
                    Application application = new Application(app);

                    HashMap<String, String> map = new HashMap<>();
                    map.put("environments", "/applications/" + app + "/environments");

                    applicationData.setRelationships(map);
                    applicationData.setAttributes(application);
                    return applicationData;
                }).collect(toList());

        getApplicationsResponse.setData(dataList);


        Pagination pagination = applicationsPage.getPagination();

        ArrayList<Link> links = new ArrayList<>();
        Link link = new Link();
        link.setRef("self");
        link.setHref(self + "?size=" + pagination.getSize() + "&page=" + pagination.getNumber());
        links.add(link);
        getApplicationsResponse.setLinks(links);


        Metadata metadata = new Metadata();
        metadata.setNumber(pagination.getNumber());
        metadata.setSize(pagination.getSize());
        metadata.setTotalElements(applicationsPage.getTotalElements());
        metadata.setTotalPages(applicationsPage.getTotalPages());

        getApplicationsResponse.setMetadata(metadata);


        return getApplicationsResponse;
    }
}
