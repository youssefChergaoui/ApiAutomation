package com.wso2.api.automation.apiautomation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private static final String PROPERTIES_FILE = "application.properties";
    private static Properties properties;

    static {
        try (InputStream inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file " + PROPERTIES_FILE, e);
        }
    }

    public static String getProperty(String name) {
        return properties.getProperty(name);
    }
}
