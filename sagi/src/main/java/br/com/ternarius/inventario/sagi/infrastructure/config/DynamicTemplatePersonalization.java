package br.com.ternarius.inventario.sagi.infrastructure.config;

import com.sendgrid.Personalization;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

public class DynamicTemplatePersonalization extends Personalization {

    @JsonProperty(value = "dynamic_template_data")
    private Map<String, Object> dynamicTemplateData;

    public Map<String, Object> getDynamicTemplateData() {
        if (isNull(dynamicTemplateData)) {
            return Collections.<String, Object>emptyMap();
        }

        return dynamicTemplateData;
    }

    public void addDynamicTemplateData(String key, Object value) {
        if (isNull(dynamicTemplateData)) {
            dynamicTemplateData = new HashMap<>();
            dynamicTemplateData.put(key, value);

            return;
        }

        dynamicTemplateData.put(key, value);
    }
}
