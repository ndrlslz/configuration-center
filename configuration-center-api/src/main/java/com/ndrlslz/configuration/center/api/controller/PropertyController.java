package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.api.json.property.*;
import com.ndrlslz.configuration.center.api.service.PropertyService;
import com.ndrlslz.configuration.center.api.validation.PropertyDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class PropertyController {
    private PropertyService propertyService;
    private PropertyDataValidator validator;

    @Autowired
    public PropertyController(PropertyService propertyService, PropertyDataValidator propertyDataValidator) {
        this.propertyService = propertyService;
        this.validator = propertyDataValidator;
    }

    @GetMapping("/applications/{application}/environments/{environment}/properties")
    public GetPropertiesResponse getProperties(@PathVariable String application,
                                               @PathVariable String environment,
                                               @PageableDefault Pageable pageable) {
        return propertyService.getProperties(application, environment, pageable);
    }

    @PostMapping("/applications/{application}/environments/{environment}/properties")
    public CreatePropertyResponse createProperty(@PathVariable String application,
                                                 @PathVariable String environment,
                                                 @RequestBody CreatePropertyRequest createPropertyRequest) {
        validator.validate(createPropertyRequest);

        return propertyService.createProperty(application, environment, createPropertyRequest);
    }

    @PutMapping("/applications/{application}/environments/{environment}/properties/{property}")
    public UpdatePropertyResponse updateProperty(@PathVariable String application,
                                                 @PathVariable String environment,
                                                 @PathVariable String property,
                                                 @RequestBody UpdatePropertyRequest updatePropertyRequest) {
        validator.validateUpdateOrDelete(updatePropertyRequest);

        return propertyService.updateProperty(application, environment, property, updatePropertyRequest);
    }
}
