package com.wso2.api.automation.apiautomation.models;

public class EndpointConfig {
    private String endpoint_type;
    private Endpoints production_endpoints;
    private Endpoints sandbox_endpoints;

    public EndpointConfig(String endpoint_type, Endpoints production_endpoints, Endpoints sandbox_endpoints) {
        this.endpoint_type = endpoint_type;
        this.production_endpoints = production_endpoints;
        this.sandbox_endpoints = sandbox_endpoints;
    }

    public String getEndpoint_type() {
        return endpoint_type;
    }

    public void setEndpoint_type(String endpoint_type) {
        this.endpoint_type = endpoint_type;
    }

    public Endpoints getProduction_endpoints() {
        return production_endpoints;
    }

    public void setProduction_endpoints(Endpoints production_endpoints) {
        this.production_endpoints = production_endpoints;
    }

    public Endpoints getSandbox_endpoints() {
        return sandbox_endpoints;
    }

    public void setSandbox_endpoints(Endpoints sandbox_endpoints) {
        this.sandbox_endpoints = sandbox_endpoints;
    }
}
