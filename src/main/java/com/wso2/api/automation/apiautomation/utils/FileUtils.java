package com.wso2.api.automation.apiautomation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wso2.api.automation.apiautomation.models.Revision;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertFileToString(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        return new String(bytes, StandardCharsets.UTF_8);
    }

    // 		"basePath": "/v2"==>context
    //
    public static String findValueInSwaggerInfo(String name, String swaggerContent) {
        String value = null;
        try {
            // Ouverture du fichier Swagger
            // Utilisation de la bibliothèque Gson pour parser le fichier JSON
            JsonObject swagger = new Gson().fromJson(swaggerContent, JsonObject.class);
            // Récupération des valeurs
            value = swagger.get("info").getAsJsonObject().get(name).getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
    public static String findServerUrl(String swaggerContent) {
        String serverUrl = null;
        try {
            // Ouverture du fichier Swagger
            // Utilisation de la bibliothèque Gson pour parser le fichier JSON
            JsonObject swagger = new Gson().fromJson(swaggerContent, JsonObject.class);
            // Récupération de l'URL du premier serveur
            serverUrl = swagger.getAsJsonArray("info")
                    .get(0).getAsJsonObject()
                    .get("title").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverUrl;
    }


    public static String extractValueFromSwagger(String swaggerContent, String valueName) {
        String regex = "\"" + valueName + "\":\\s*\"([^\"]*)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(swaggerContent);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
    public static <T> T jsonToObject(String json, Class<T> clazz) throws IOException {
        return new Gson().fromJson(json, clazz);
    }
    public static String marshalJavaObjecttoJSON(Object object){
        Gson gson = new GsonBuilder().create();
        String body = gson.toJson(object);
        return body;
    }
    public static String find(String swaggerContent, String jsonPath) {
        try {
            // Parsing du fichier Swagger
            JsonObject swagger = new Gson().fromJson(swaggerContent, JsonObject.class);

            // Récupération de l'élément JSON correspondant au chemin spécifié
            String[] segments = jsonPath.split("\\.");
            JsonElement result = swagger;
            for (String segment : segments) {
                if (segment.contains("[")) {
                    String key = segment.substring(0, segment.indexOf("["));
                    int index = Integer.parseInt(segment.substring(segment.indexOf("[") + 1, segment.indexOf("]")));
                    result = result.getAsJsonObject().get(key).getAsJsonArray().get(index);
                } else {
                    result = result.getAsJsonObject().get(segment);
                }
            }
            return result.getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
