package com.wso2.api.automation.apiautomation.models;


import com.wso2.api.automation.apiautomation.utils.Constants;

import static com.wso2.api.automation.apiautomation.utils.Constants.THROTTLING_POLICIES;

public class AdditionalProperties {

    private String name;
    private String context;
    private String version;
    private String[] policies = THROTTLING_POLICIES;
    private EndpointConfig endpointConfig;
    private String swaggerDefinition;



    public AdditionalProperties(String name, String context, String version) {
        this.name = name;
        this.context = context;
        this.version = version;
    }
    public AdditionalProperties(){}
    public void setName(String name) {
        this.name = name;
    }
    public void setContext(String context) {
        this.context = context;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public void setPolicies(String[] policies) { this.policies = policies; }
    public void setEndpointConfig(EndpointConfig endpointConfig){ this.endpointConfig=endpointConfig; }
    public void setSwaggerDefinition(String swaggerDefinition){ this.swaggerDefinition=swaggerDefinition; }
    public String getName() {
        return name;
    }
    public String getContext() {
        return context;
    }
    public String getVersion() {
        return version;
    }
    public EndpointConfig getEndpointConfig(){ return endpointConfig; }
    public String[] getPolicies() {
        return policies;
    }



}
