package com.wso2.api.automation.apiautomation.services;


import com.google.gson.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wso2.api.automation.apiautomation.models.*;
import com.wso2.api.automation.apiautomation.utils.AuthHttpUtils;
import com.wso2.api.automation.apiautomation.utils.FileUtils;
import com.wso2.api.automation.apiautomation.utils.PropertyReader;
import okhttp3.*;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.wso2.api.automation.apiautomation.utils.Constants.*;


public class PublisherServices {
    private static final String API_PUBLISHER_ENDPOINT = PropertyReader.getProperty("apim.base.url")+PropertyReader.getProperty("import.api.endpoint");
    private static final String PUBLISHER_ENDPOINT = PropertyReader.getProperty("apim.base.url")+PropertyReader.getProperty("publisher.endpoint");

    /**
     * Import Swagger File in wso2 publisher.
     *
     * @param swaggerContent swagger content.
     * @param accessToken access token.
     * @return id of api whose swagger was imported.
     */
    public static String importSwaggerContent(String swaggerContent, String accessToken)  {
        System.out.println(API_PUBLISHER_ENDPOINT);
        AdditionalProperties additionalProperties = new AdditionalProperties();
        String name=null;
        String version=null;
        String endPoints=null;

            name = "Drive";//FileUtils.find(swaggerFileContent,"info.title");
            version = "1.0.0";//FileUtils.find(swaggerContent,"info.version");
            endPoints = "https://localhost:9443/am/v1/api/";//FileUtils.find(swaggerFileContent,"servers[0].url");
        additionalProperties.setName(name);
        additionalProperties.setContext("Apis");
        additionalProperties.setVersion(version);

        Endpoints production_endpoints = new Endpoints(endPoints);
        Endpoints sandbox_endpoints = new Endpoints(endPoints);
        EndpointConfig endpointConfig = new EndpointConfig(
                HTTP_SCHEME,
                production_endpoints,
                sandbox_endpoints);
        additionalProperties.setEndpointConfig(endpointConfig);
        String json = new Gson().toJson(additionalProperties);
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(INLINE_API_DEFINITION, swaggerContent)
                .addFormDataPart(ADDITIONAL_PROPERTIES, json)
                .build();
        Request request = new Request.Builder()
                .url(API_PUBLISHER_ENDPOINT)
                .post(body)
                .addHeader(AUTHORIZATION, BEARER + SPACE_SEPARATOR + accessToken)
                .build();
        Response response = null;
        String bodyResponse = null;
        ImportedApi importedApi = null;
        try {
            response = AuthHttpUtils.getUnsafeOkHttpClientWithProxy().newCall(request).execute();
            bodyResponse = response.body().string();
            if (response.isSuccessful()) {
                System.out.println("successfully import || Response Body : " + bodyResponse);
            } else {
                throw new IOException("Failed to import Swagger definition : " + response.code() + " || " + bodyResponse);
            }
            importedApi =new Gson().fromJson(bodyResponse,ImportedApi.class);
            return importedApi.getId();

        } catch (Exception e) {
           System.out.println("Failed to import Swagger definition. Error : " + response.code() + " - " + bodyResponse);
            return null;
        }
    }
    /**
     * Import Swagger File in wso2 publisher.
     *
     * @param swaggerFileContent swagger content.
     * @param accessToken access token.
     * @return id of api whose swagger was imported.
     */
    public static String importSwaggerFile(File swaggerFileContent, String backendEndpoint, String accessToken) {
        AdditionalProperties additionalProperties = new AdditionalProperties(
                "ApiName",
                "api",
                DEFAULT_API_VERSION
        );

        //TODO: Ask joel about: titre version context backendPoint
        Endpoints production_endpoints = new Endpoints(backendEndpoint);
        Endpoints sandbox_endpoints = new Endpoints(backendEndpoint);
        EndpointConfig endpointConfig = new EndpointConfig(
                HTTP_SCHEME,
                production_endpoints,
                sandbox_endpoints);
        additionalProperties.setEndpointConfig(endpointConfig);
        String json = new Gson().toJson(additionalProperties);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), swaggerFileContent);
        RequestBody propertiesBody = RequestBody.create(MediaType.parse("application/json"), json);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(FILE_API_DEFINITION, swaggerFileContent.getName(), fileBody)
                .addFormDataPart(ADDITIONAL_PROPERTIES, null, propertiesBody)
                .build();
        Request request = new Request.Builder()
                .url(API_PUBLISHER_ENDPOINT)
                .post(requestBody)
                .addHeader(AUTHORIZATION, BEARER + SPACE_SEPARATOR + accessToken)
                .build();

        Response response = null;
        String bodyResponse = null;
        ImportedApi importedApi = null;
        try {
            response = AuthHttpUtils.getUnsafeOkHttpClientWithProxy().newCall(request).execute();
            bodyResponse = response.body().string();
            importedApi =new Gson().fromJson(bodyResponse,ImportedApi.class);
        } catch (Exception e) {
            System.out.println("Exception importSwaggerFile : " + response.code() + " - " + bodyResponse);
            e.printStackTrace();
            return null;
        }
        return importedApi.getId();
    }
    public static String importSwaggerFile1(File swaggerFileContent, String backendEndpoint, String accessToken) {
        AdditionalProperties additionalProperties = new AdditionalProperties();

        String swaggerContent = null;
        String name=null;
        String version=null;
        String endPoints=null;
        try {
            swaggerContent=FileUtils.convertFileToString(swaggerFileContent);
            name = FileUtils.find(swaggerContent,"info.title");
            version = FileUtils.find(swaggerContent,"info.version");
            endPoints = FileUtils.find(swaggerContent,"servers[0].url");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        additionalProperties.setName(name);
        additionalProperties.setContext("Api");
        additionalProperties.setVersion(version);

        Endpoints production_endpoints = new Endpoints(endPoints);
        Endpoints sandbox_endpoints = new Endpoints(endPoints);
        EndpointConfig endpointConfig = new EndpointConfig(
                HTTP_SCHEME,
                production_endpoints,
                sandbox_endpoints);
        additionalProperties.setEndpointConfig(endpointConfig);
        String json = new Gson().toJson(additionalProperties);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), swaggerFileContent);
        RequestBody propertiesBody = RequestBody.create(MediaType.parse("application/json"), json);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(FILE_API_DEFINITION, swaggerFileContent.getName(), fileBody)
                .addFormDataPart(ADDITIONAL_PROPERTIES, null, propertiesBody)
                .build();
        Request request = new Request.Builder()
                .url(API_PUBLISHER_ENDPOINT)
                .post(requestBody)
                .addHeader(AUTHORIZATION, BEARER + SPACE_SEPARATOR + accessToken)
                .build();

        Response response = null;
        String bodyResponse = null;
        ImportedApi importedApi = null;
        try {
            response = AuthHttpUtils.getUnsafeOkHttpClientWithProxy().newCall(request).execute();
            bodyResponse = response.body().string();
            importedApi =new Gson().fromJson(bodyResponse,ImportedApi.class);
        } catch (Exception e) {
            System.out.println("Exception importSwaggerFile : " + response.code() + " - " + bodyResponse);
            e.printStackTrace();
            return null;
        }
        return importedApi.getId();
    }
    /**
     * Print revisions.
     *
     * @param apiId API id.
     * @param accessToken Le access token.
     * @return print list of revisions.
     */
    public static List<Revision> getRevisionsOfApi(String apiId, String accessToken){
        List<Revision> revisions = new ArrayList<>();
        String url = PUBLISHER_ENDPOINT+"/" + apiId + "/revisions";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader(AUTHORIZATION, BEARER + SPACE_SEPARATOR + accessToken)
                .build();
        Response response = null;
        String bodyResponse = null;
        JsonArray revisionArray = null;
        try {
            response = AuthHttpUtils.getUnsafeOkHttpClientWithProxy().newCall(request).execute();
            bodyResponse = response.body().string();
            JsonObject responseJson = JsonParser.parseString(bodyResponse).getAsJsonObject();
            revisionArray = responseJson.getAsJsonArray("list");
        }catch (Exception e) {
        System.out.println("Exception getRevisionsOfApi : " + response.code() + " - " + bodyResponse);
        e.printStackTrace();
        return null;
    }
        for (JsonElement element : revisionArray) {
            JsonObject revisionObject = element.getAsJsonObject();
            Revision revision = new Revision();
            revision.setId(revisionObject.get("id").getAsString());
            revision.setDescription(revisionObject.get("description").getAsString());
            revisions.add(revision);
        }
        return revisions;
    }
    /**
     * Test if the list of revisions is >= to 5 (max of revision can be created 5) ==> remove a revision.
     *
     * @param apiId API id.
     * @param revisionId revision id.
     * @param accessToken access token.
     * @return void.
     */
    public static boolean deleteRevision(String apiId, String revisionId, String accessToken) throws Exception {
        String url = PUBLISHER_ENDPOINT + "/" + apiId + "/revisions/" + revisionId;
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader(AUTHORIZATION, BEARER + SPACE_SEPARATOR + accessToken)
                .build();
        Response response = null;

        try {
            response = AuthHttpUtils.getUnsafeOkHttpClientWithProxy().newCall(request).execute();
            String requestBody=request.body().toString();
            if (response.isSuccessful()) {
                System.out.println("The revision was successfully deleted : "+ response.code() + " "+ requestBody);
                return true;
            } else {
                throw new IOException("Failed to delete revision. Error : " + response.code() + " "+ requestBody);
            }
        } catch (Exception e) {
            System.out.println("Failed to delete revision. Error : " + e.getMessage());
            return false;
        }
    }
    /**
     * Create API Revision.
     *
     * @param apiId API id.
     * @param accessToken access token.
     * @return id revision created.
     */
    public static String createAPIRevision(String apiId, String accessToken) {
        String url = PUBLISHER_ENDPOINT + "/" + apiId + "/revisions";
        String body = "{ \"description\": \" automated new revision \" }";
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody=RequestBody.create(body, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader(AUTHORIZATION, BEARER + SPACE_SEPARATOR + accessToken)
                .build();
        String responseBody=null;
        try {
            Response response = AuthHttpUtils.getUnsafeOkHttpClientWithProxy().newCall(request).execute();
            responseBody = response.body().string();
            Revision revision=null;
            if (response.isSuccessful()) {
                System.out.println("New revision created successfully || Response Body : "+responseBody);
                revision =FileUtils.jsonToObject(responseBody,Revision.class);
                return revision.getId();

            } else {
                throw new IOException("Failed to create new revision. Response code : " + response.code() +" "+ responseBody);
            }
        } catch (IOException e) {
            System.out.println("Failed to create new revision. Error message : " + e.getMessage());
        }
        return responseBody;
    }
    /**
     * Deploy API Revision.
     *
     * @param apiId API id.
     * @param revisionId revision id.
     * @param accessToken access token
     * @return void.
     */
    public static void deployAPIRevision(String apiId, String revisionId, String accessToken) {
        String url = PUBLISHER_ENDPOINT + "/" + apiId + "/deploy-revision?revisionId="+revisionId;
        System.out.println(url);
        String body = FileUtils.marshalJavaObjecttoJSON(Arrays.asList(new Deployment("Default","localhost")));
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(body, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader(AUTHORIZATION, BEARER + SPACE_SEPARATOR + accessToken)
                .build();
        String responseBody=null;
        try {
            Response response = AuthHttpUtils.getUnsafeOkHttpClientWithProxy().newCall(request).execute();
            responseBody = response.body().string();

            if (response.isSuccessful()) {

                System.out.println("Revision deployed successfully || Response Body : "+responseBody);
            } else {
                throw new IOException("Failed to deploy revision. Response code : " + response.code() +" / "+ responseBody);
            }
        } catch (IOException e) {
            System.out.println("Failed to deploy revision. Error message : " + e.getMessage()+" "+ responseBody);
        }
    }
    /**
     * Change API status to publich.
     *
     * @param apiId API id.
     * @param accessToken access token.
     * @return void.
     */
    public static void publishAPI(String apiId, String accessToken) {
        // URL api change lifecycle
        String apiUrl = PUBLISHER_ENDPOINT + "/change-lifecycle?apiId=" + apiId + "&action=Publish";

        String body = "";
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(body, mediaType);
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(requestBody)
                .addHeader(AUTHORIZATION, BEARER + SPACE_SEPARATOR + accessToken)
                .build();
        //print request
        System.out.println("Request : "+request);

        String responseBody=null;
        Response response=null;
        try {
            response = AuthHttpUtils.getUnsafeOkHttpClientWithProxy().newCall(request).execute();
            responseBody = response.body().string();
            if (response.isSuccessful()) {
                System.out.println("API Status changed successfully || Response Body : " + responseBody);
            } else {
                throw new IOException("Failed to change API Status : " + response.code() + " || " + responseBody);
            }
        } catch (IOException e) {
            System.out.println("Failed to change API Status. Error message : " + e.getMessage());
        }
    }
    /**
     * update Api.
     *
     * @param apiId API id.
     * @param accessToken access token.
     * @param updateRequest .
     * @return void.
     */
    public static void updateApi(String apiId, String accessToken, UpdateApiRequest updateRequest) {
        String apiUrl = PUBLISHER_ENDPOINT + apiId;

        JSONObject requestBodyJson = new JSONObject()
                .put("endpointConfig", updateRequest.getEndpointConfig());

        MediaType mediaType = MediaType.parse("application/json");

        Request request = new Request.Builder()
                .url(apiUrl)
                .put(RequestBody.create(requestBodyJson.toString(), mediaType))
                .addHeader(AUTHORIZATION, BEARER + SPACE_SEPARATOR + accessToken)
                .build();

        // Sending the request and retrieving the response
        String responseBody = null;
        Response response = null;
        try {
            response = AuthHttpUtils.getUnsafeOkHttpClientWithProxy().newCall(request).execute();
            responseBody = response.body().string();
            if (response.isSuccessful()) {
                System.out.println("API updated successfully || Response Body : " + responseBody);
            } else {
                throw new IOException("Failed to update API : " + response.code() + " || " + responseBody);
            }
        } catch (IOException e) {
            System.out.println("Failed to update API. Error message : " + e.getMessage());
        }
    }

    ///////////////////////test/////////////////////


    public static boolean updateSwaggerApi(String apiId, String swaggerContent, String accessToken) {
        String url = API_PUBLISHER_ENDPOINT + "/apis/" + apiId + "/swagger";
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(INLINE_API_DEFINITION, swaggerContent)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader(AUTHORIZATION, BEARER + SPACE_SEPARATOR + accessToken)
                .build();
        try {
            Response response = AuthHttpUtils.getUnsafeOkHttpClientWithProxy().newCall(request).execute();
            String bodyResponse = response.body().string();
            if (response.isSuccessful()) {
                System.out.println("Successfully updated API swagger || Response Body : " + bodyResponse);
                return true;
            } else {
                throw new IOException("Failed to update API swagger : " + response.code() + " || " + bodyResponse);
            }
        } catch (Exception e) {
            System.out.println("Failed to update API swagger. Error : " + e.getMessage());
            return false;
        }
    }

    //////test//////////////////











}
