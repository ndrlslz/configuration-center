package com.ndrlslz.configuration.center.api.validation;

import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.property.Property;
import com.ndrlslz.configuration.center.api.json.property.PropertyRequest;
import org.springframework.stereotype.Service;

import static com.ndrlslz.configuration.center.api.json.common.Type.PROPERTY;

@Service
public class PropertyDataValidator implements Validator<PropertyRequest> {
    @Override
    public void validate(PropertyRequest request) {
        Data<Property> data = request.getData();

        checkNotNull(data, "data cannot be null");
        checkNotNull(data.getAttributes(), "attribute cannot be null");
        checkNotNull(data.getAttributes().getValue(), "property value is mandatory");
        checkState(data.getType() == PROPERTY, "type not match, expect property, but receive " + data.getType());
    }

    public void validateUpdateRequest(PropertyRequest request) {
        validate(request);

        checkNotNull(request.getData().getAttributes().getVersion(), "version cannot be null");
    }
}
