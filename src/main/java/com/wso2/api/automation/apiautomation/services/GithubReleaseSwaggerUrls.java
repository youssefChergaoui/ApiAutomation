package com.wso2.api.automation.apiautomation.services;

import com.wso2.api.automation.apiautomation.models.SwaggerAsset;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GithubReleaseSwaggerUrls {

    private static final String owner = "youssefChergaoui";
    private static final String repo = "ApiAutomation";
    private static final String releaseTag = "wso";
    private static final String accessToken = "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";
    public static List<SwaggerAsset> getSwaggerUrls() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/repos/" + owner + "/" + repo + "/releases/tags/" + releaseTag)
                .header("Authorization", "Bearer " + accessToken)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(responseBody);

        JSONObject releaseJson = new JSONObject(responseBody);
        JSONArray assetsJsonArray = releaseJson.getJSONArray("assets");
        List<SwaggerAsset> swaggerAssets = new ArrayList<>();
        for (int i = 0; i < assetsJsonArray.length(); i++) {
            JSONObject assetJson = assetsJsonArray.getJSONObject(i);
            String assetName = assetJson.getString("name");
            String assetUrl = assetJson.getString("browser_download_url");
            if (assetName.startsWith("swagger-")) {
                SwaggerAsset swaggerAsset = new SwaggerAsset(assetName, assetUrl);
                swaggerAssets.add(swaggerAsset);
            }
        }
        return swaggerAssets;
    }

    /*public static List<String> getSwaggerUrls() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/repos/" + owner + "/" + repo + "/releases/tags/" + releaseTag)
                .header("Authorization", "Bearer " + accessToken)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        JSONObject releaseJson = new JSONObject(responseBody);
        JSONArray assetsJsonArray = releaseJson.getJSONArray("assets");
        List<String> swaggerUrls = new ArrayList<String>();
        for (int i = 0; i < assetsJsonArray.length(); i++) {
            JSONObject assetJson = assetsJsonArray.getJSONObject(i);
            String assetName = assetJson.getString("name");
            String assetUrl = assetJson.getString("browser_download_url");
            if (assetName.startsWith("swagger-")) {
                swaggerUrls.add(assetUrl);
            }
        }return swaggerUrls;
    }*/
}
