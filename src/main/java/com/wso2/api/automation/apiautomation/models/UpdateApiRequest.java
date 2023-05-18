package com.wso2.api.automation.apiautomation.models;

public class UpdateApiRequest {
    private final String endpointConfig;

    public UpdateApiRequest(String name, String description, String endpointConfig) {
        this.endpointConfig = endpointConfig;
    }


    public String getEndpointConfig() {
        return endpointConfig;
    }
}
