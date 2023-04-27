package com.wso2.api.automation.apiautomation.models;

public class UpdateApiRequest {
    private final String name;
    private final String description;
    private final String endpointConfig;

    public UpdateApiRequest(String name, String description, String endpointConfig) {
        this.name = name;
        this.description = description;
        this.endpointConfig = endpointConfig;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEndpointConfig() {
        return endpointConfig;
    }
}
