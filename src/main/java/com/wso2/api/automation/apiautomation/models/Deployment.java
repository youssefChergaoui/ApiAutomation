package com.wso2.api.automation.apiautomation.models;

public class Deployment {
    private String name;
    private String vhost;
    private boolean displayOnDevportal = true;

    public Deployment(String name, String vhost) {
        this.name = name;
        this.vhost = vhost;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public boolean isDisplayOnDevportal() {
        return displayOnDevportal;
    }

    public void setDisplayOnDevportal(boolean displayOnDevportal) {
        this.displayOnDevportal = displayOnDevportal;
    }
}
