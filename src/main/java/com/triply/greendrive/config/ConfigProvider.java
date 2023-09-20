package com.triply.greendrive.config;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ConfigProvider {


    private final Environment env;


    public long getJwtExpiration() {
        return this.env.getRequiredProperty("app.jwtExpirationMs", Long.class);
    }

    public String getJwtKey() {
        return this.env.getRequiredProperty("app.jwtSecret");
    }

    public Integer getReplaceableSuggestion() {
        return this.env.getRequiredProperty("app.replaceable.suggestion", Integer.class);
    }

    public Long getChargingRequiredThreshold() {
        return this.env.getRequiredProperty("app.charging.required.threshold", Long.class);
    }
}
