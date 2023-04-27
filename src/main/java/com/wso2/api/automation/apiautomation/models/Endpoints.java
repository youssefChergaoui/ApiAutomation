package com.wso2.api.automation.apiautomation.models;

public class Endpoints {
    private String url;

    public Endpoints(String url) {
        this.url=url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
