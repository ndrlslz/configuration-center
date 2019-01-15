package com.ndrlslz.configuration.center.api.service;

import com.ndrlslz.configuration.center.api.json.property.*;
import org.springframework.data.domain.Pageable;

public interface PropertyService {
    GetPropertiesResponse getProperties(String application, String environment, Pageable pageable);

    CreatePropertyResponse createProperty(String application, String environment, CreatePropertyRequest request);

    UpdatePropertyResponse updateProperty(String application, String environment, String property, UpdatePropertyRequest request);

    void deleteProperty(String application, String environment, String property);
}
