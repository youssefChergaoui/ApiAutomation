package com.wso2.api.automation.apiautomation.services;



import com.wso2.api.automation.apiautomation.utils.Constants;
import com.wso2.api.automation.apiautomation.utils.AuthHttpUtils;
import com.wso2.api.automation.apiautomation.utils.PropertyReader;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

import static com.wso2.api.automation.apiautomation.utils.Constants.*;


public class OAuth2TokenServices {


   /* @Value("${token.url}")
    private  String tokenUrl;
    @Value("${scopes}")
    private  String scopes;
    @Value("${client.secret}")
    private  String clientSecret;
    @Value("${client.id}")
    private  String clientId;*/

    private static final String clientId = PropertyReader.getProperty("client.id");
    private static final String clientSecret = PropertyReader.getProperty("client.secret");
    private static final String tokenUrl = PropertyReader.getProperty("apim.base.url")+PropertyReader.getProperty("token.url");
    private static final String scopes = PropertyReader.getProperty("scopes");



    /**
     * generate access Token.
     *
     * @param username username.
     * @param password password.
     * @return access token.
     */
    public static String oauth2PasswordCredentials(String username, String password) throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.GRANT_TYPE, Constants.PASSWORD_CREDENTIALS)
                .add(USER_NAME, username)
                .add(PASSWORD, password)
                .add(SCOPE, scopes)
                .build();
        String credentials = Credentials.basic(clientId, clientSecret);
        Request request = new Request.Builder()
                .url(tokenUrl)
                .addHeader(AUTHORIZATION, credentials)
                .post(requestBody)
                .build();

        Response response = null;
        String body = null;
        try {

            response = AuthHttpUtils.getUnsafeOkHttpClientWithProxy().newCall(request).execute();
            body = response.body().string();
            JSONObject jsonBody = new JSONObject(body);
            String accesToken = jsonBody.getString(ACCES_TOKEN);
            System.out.println("Access token : "+accesToken);
            return accesToken;
        } catch (Exception e) {
           System.out.println("Failed to generate token. Answer :" + response.code() + " - " + body);
            return null;
        }
    }

    public static String oauth2PasswordCredentials(String grantType, String username, String password) throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add(GRANT_TYPE, grantType)
                .add(USER_NAME, username)
                .add(PASSWORD, password)
                .add(SCOPE, scopes)
                .build();

        String credentials = Credentials.basic(clientId, clientSecret);

        Request request = new Request.Builder()
                .url(tokenUrl)
                .addHeader(AUTHORIZATION, credentials)
                .post(requestBody)
                .build();

        try (Response response = AuthHttpUtils.getUnsafeOkHttpClientWithProxy().newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            ResponseBody responseBody = response.body();
            if (responseBody == null) throw new IOException("Response body is null");

            String responseString = responseBody.string();
            JSONObject jsonResponse = new JSONObject(responseString);
            return jsonResponse.getString("access_token");
        }
    }
}

