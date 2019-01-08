package com.ndrlslz.configuration.center.api.util;

import com.ndrlslz.configuration.center.api.json.common.Metadata;
import com.ndrlslz.configuration.center.core.model.Page;

public class MetadataBuilder {
    private Page page;

    public MetadataBuilder withPage(Page page) {
        this.page = page;
        return this;
    }

    public Metadata build() {
        Metadata metadata = new Metadata();
        metadata.setSize(page.getSize());
        metadata.setNumber(page.getNumber());
        metadata.setTotalPages(page.getTotalPages());
        metadata.setTotalElements(page.getTotalElements());

        return metadata;
    }
}
