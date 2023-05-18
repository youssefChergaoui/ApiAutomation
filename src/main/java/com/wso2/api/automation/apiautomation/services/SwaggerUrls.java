package com.wso2.api.automation.apiautomation.services;

import com.wso2.api.automation.apiautomation.models.SwaggerAsset;
import com.wso2.api.automation.apiautomation.utils.AuthHttpUtils;
import com.wso2.api.automation.apiautomation.utils.PropertyReader;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.wso2.api.automation.apiautomation.utils.Constants.*;

public class SwaggerUrls {

    private static final String OWNER = PropertyReader.getProperty("owner");
    private static final String REPO = PropertyReader.getProperty("repo");
    private static final String RELEASE_TAG = PropertyReader.getProperty("release.tag");
    private static final String ACCESS_TOKEN = PropertyReader.getProperty("access.token");
    private static final String URL_REPOS_GIT = PropertyReader.getProperty("url.repos.git");
    private static final String SWAGGERS_FILE_NAME = PropertyReader.getProperty("file.swaggers.name");

    /**
     * Get the content of the swaggersUrls file which contains the urls of the swaggers.
     *
     * @param
     * @return List of swagger urls.
     */
    public static List<String> getSwaggerUrlsFromFile() throws IOException {

        OkHttpClient client = AuthHttpUtils.getOkHttpClient();

        Request request = new Request.Builder()
                .url(URL_REPOS_GIT + OWNER + "/" + REPO + "/contents/" + SWAGGERS_FILE_NAME)
                .header(AUTHORIZATION, BEARER + SPACE_SEPARATOR + ACCESS_TOKEN)
                .build();
        Response response = null;
        String responseBody = null;
        try {
            response = client.newCall(request).execute();
            responseBody = response.body().string();
            if (response.isSuccessful()){
                System.out.println("Swagger url was geted successfully From File");
            }

        }catch (Exception e){
            System.err.println("Error when getting swagger urls from file "+e.getMessage()+"Responce body : "+responseBody);

        }

        JSONObject jsonObject = new JSONObject(responseBody);
        String encodedContent = jsonObject.getString("content");
        // Décode le contenu encodé en base64
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary(encodedContent);
        // Convertit les bytes décodés en chaîne de caractères
        String content = new String(decodedBytes);
        // Divise la chaîne de caractères en lignes et les stocke dans une liste
        List<String> urlList = Arrays.asList(content.split("\n"));
        return urlList;
    }

    /**
     * Get the swagger urls share on the release.
     *
     * @param
     * @return List of swagger urls.
     */
    public static List<SwaggerAsset> getSwaggerUrlsFromRelease() throws IOException {
        OkHttpClient client = AuthHttpUtils.getOkHttpClient();

        Request request = new Request.Builder()
                .url(URL_REPOS_GIT + OWNER + "/" + REPO + "/releases/tags/" + RELEASE_TAG)
                .header(AUTHORIZATION, BEARER + SPACE_SEPARATOR + ACCESS_TOKEN)
                .build();
        Response response = null;
        String responseBody = null;

        try{
            response = client.newCall(request).execute();
            responseBody = response.body().string();
            if (response.isSuccessful()){
                System.out.println("Swagger url was geted successfully From Release");
            }

        }catch (Exception e){
            System.err.println("Error when getting swagger urls from release "+e.getMessage()+"Responce body : "+responseBody);
        }

        JSONObject releaseJson = new JSONObject(responseBody);
        JSONArray assetsJsonArray = releaseJson.getJSONArray(ASSETS);
        List<SwaggerAsset> swaggerAssets = new ArrayList<>();
        for (int i = 0; i < assetsJsonArray.length(); i++) {
            JSONObject assetJson = assetsJsonArray.getJSONObject(i);
            String assetName = assetJson.getString(NAME);
            String assetUrl = assetJson.getString(BROWSER_DOWLOAD_URL);
            if (assetName.startsWith(SWAGGER_)) {
                SwaggerAsset swaggerAsset = new SwaggerAsset(assetName, assetUrl);
                swaggerAssets.add(swaggerAsset);
            }
        }
        return swaggerAssets;
    }
    /**
     * Add swagger ti release.
     *
     * @param swaggerContent
     * @return void.
     */
    public static void addSwaggerToRelease(String swaggerContent) throws IOException {

        String swaggerName = "swagger-okk2.txt";//FileUtils.find(swaggerContent,"info.title");
        String releaseUrl = URL_REPOS_GIT + OWNER + "/" + REPO + "/releases/tags/" + RELEASE_TAG;

        // Créer la requête pour récupérer les informations de la release
        Request request = new Request.Builder()
                .url(releaseUrl)
                .header(AUTHORIZATION, BEARER + SPACE_SEPARATOR + ACCESS_TOKEN)
                .build();

        // Envoyer la requête pour récupérer les informations de la release
        OkHttpClient client = AuthHttpUtils.getOkHttpClient();
        Response response = null;
        String responseBody = null;
        try{
            response = client.newCall(request).execute();
            responseBody = response.body().string();

        }catch (Exception e){
            System.err.println("Error when sending request to retrieve release information "+e.getMessage()+"Responce body : "+responseBody);
        }

        JSONObject releaseJson = new JSONObject(responseBody);

        // Récupérer l'ID de la release
        int releaseIdint = releaseJson.getInt("id");
        String releaseId = String.valueOf(releaseIdint);

        // Créer la requête pour ajouter un fichier à la release

        request = new Request.Builder()
                .url("https://uploads.github.com/repos/" + OWNER + "/" + REPO + "/releases/" + releaseId + "/assets?name=" + swaggerName)
                .header(AUTHORIZATION, BEARER + SPACE_SEPARATOR + ACCESS_TOKEN)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .header(ACCEPT, "application/vnd.github.v3+json")
                .post(RequestBody.create(MediaType.parse(APPLICATION_JSON), "{\"url\":\"" + swaggerContent + "\"}"))
                .build();

        // Envoyer la requête pour ajouter un fichier à la release

        try{
            response = client.newCall(request).execute();
            responseBody=response.body().string();
            if (response.isSuccessful()){
                System.out.println("Swagger was successfully added to releas");
            }

        }catch (Exception e){
            System.err.println("Error when sending request to add file to the release "+e.getMessage()+"Responce body : "+responseBody);
        }

    }
    ////////////////////::
    public static List<String> getModifiedOrAddedLines(String fileName, String branchName) throws IOException {
        OkHttpClient client = AuthHttpUtils.getOkHttpClient();

        // Get the commit SHA for the latest commit on the branch
        Request request = new Request.Builder()
                .url(URL_REPOS_GIT + OWNER + "/" + REPO + "/branches/" + branchName)
                .header(AUTHORIZATION, BEARER + SPACE_SEPARATOR + ACCESS_TOKEN)
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        JSONObject jsonObject = new JSONObject(responseBody);
        String latestCommitSha = jsonObject.getJSONObject("commit").getString("sha");

        // Get the contents of the file at the latest commit
        request = new Request.Builder()
                .url(URL_REPOS_GIT + OWNER + "/" + REPO + "/contents/" + fileName + "?ref=" + latestCommitSha)
                .header(AUTHORIZATION, BEARER + SPACE_SEPARATOR + ACCESS_TOKEN)
                .build();
        response = client.newCall(request).execute();
        responseBody = response.body().string();
        jsonObject = new JSONObject(responseBody);

        // Decode the content of the file from base64
        String encodedContent = jsonObject.getString("content");
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary(encodedContent);
        String content = new String(decodedBytes);


        return null;
    }










}
