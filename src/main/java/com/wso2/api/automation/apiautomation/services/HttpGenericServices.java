package com.wso2.api.automation.apiautomation.services;

import com.wso2.api.automation.apiautomation.utils.AuthHttpUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;


import static com.wso2.api.automation.apiautomation.utils.FileUtils.convertFileToString;

public class HttpGenericServices {

    /**
     * Get Swagger Content.
     *
     * @param swaggerUrl swagger url.
     * @return swagger content.
     */
    public static String getSwaggerContentFromUrll(String swaggerUrl) {

        Request request = new Request.Builder()
                .url(swaggerUrl)
                .build();
        try {
            Response response = AuthHttpUtils.getOkHttpClient().newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getSwaggerContentFromUrl(String swaggerUrl) throws IOException {
        String swaggerContent = null;
        try {
            URL url = new URL(swaggerUrl);
            InputStream is = url.openStream();
            swaggerContent = IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return swaggerContent;
    }

    public static String getSwaggerContentFromLocalFile(String pathFile) throws IOException {
        File swaggerFile = new File(pathFile);
        String swaggerFileContent = "";
        try {
            swaggerFileContent = convertFileToString(swaggerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return swaggerFileContent;
    }


    }
