package com.wso2.api.automation.apiautomation.models;

public class SwaggerAsset {
    private String name;
    private String downloadUrl;

    public SwaggerAsset(String name, String downloadUrl) {
        this.name = name;
        this.downloadUrl = downloadUrl;
    }

    public String getName() {
        return name;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
