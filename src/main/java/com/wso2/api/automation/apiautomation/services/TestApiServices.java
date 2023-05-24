package com.wso2.api.automation.apiautomation.services;

import com.wso2.api.automation.apiautomation.utils.AuthHttpUtils;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.wso2.api.automation.apiautomation.utils.Constants.*;

public class TestApiServices {
    public static void testApisWithParamInSwagger(String swaggerUrl, String environmentUrl, String accessToken) throws IOException {
        List<String> results = new ArrayList<>();

        // Charger le contenu Swagger à partir de l'URL
        String swaggerContent = HttpGenericServices.getSwaggerContentFromUrl(swaggerUrl);

        // Convertir le contenu Swagger en objet JSON
        JSONObject swaggerJson = new JSONObject(swaggerContent);

        // Récupérer les chemins des API
        JSONObject paths = swaggerJson.getJSONObject("paths");
        for (String path : paths.keySet()) {
            JSONObject methods = paths.getJSONObject(path);

            // Vérifier chaque méthode
            for (String method : methods.keySet()) {
                if (method.equalsIgnoreCase("get")) {
                    // Construire l'URL de l'environnement spécifique
                    String apiUrl = environmentUrl + path;

                    // Vérifier si des paramètres sont requis pour cette méthode
                    JSONArray parameters = methods.getJSONObject(method).getJSONArray("parameters");
                    if (parameters.length() > 0) {
                        // Construire les paramètres de l'URL
                        StringBuilder paramsBuilder = new StringBuilder();
                        for (int i = 0; i < parameters.length(); i++) {
                            JSONObject parameter = parameters.getJSONObject(i);
                            String paramName = parameter.getString("name");
                            // Ajouter les paramètres requis dans l'URL, vous pouvez utiliser des valeurs par défaut ou les récupérer dynamiquement
                            //if (paramType.equalsIgnoreCase("string")) k
                            paramsBuilder.append(paramName).append("=").append("valeur_du_parametre").append("&");
                        }
                        // Supprimer le dernier "&" dans la chaîne de paramètres
                        String params = paramsBuilder.toString().replaceAll("&$", "");

                        // Ajouter les paramètres à l'URL de l'API
                        apiUrl = apiUrl + "?" + params;
                    }

                    // Appeler la méthode GET avec les paramètres si nécessaire
                    String response = sendGetRequest(apiUrl, accessToken);
                }
            }
        }
    }

    public static void testApisInSwagger(String swaggerUrl, String environmentUrl, String accessToken) throws IOException {
        List<String> results = new ArrayList<>();

        // Charger le contenu Swagger à partir de l'URL
        String swaggerContent = HttpGenericServices.getSwaggerContentFromUrl(swaggerUrl);

        // Convertir le contenu Swagger en objet JSON
        JSONObject swaggerJson = new JSONObject(swaggerContent);

        // Récupérer les chemins des API
        JSONObject paths = swaggerJson.getJSONObject("paths");
        for (String path : paths.keySet()) {
            JSONObject methods = paths.getJSONObject(path);

            // Vérifier chaque méthode
            for (String method : methods.keySet()) {
                if (method.equalsIgnoreCase("get")) {
                        // Construire l'URL de l'environnement spécifique
                    String apiUrl = environmentUrl + path;
                    System.out.println("url : "+apiUrl);
                    // Appeler la méthode GET
                    String response = sendGetRequest(apiUrl, accessToken);
                }
            }
        }

    }


    public static String sendGetRequest(String url, String accessToken) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader(AUTHORIZATION, BEARER + SPACE_SEPARATOR + accessToken)
                .build();

        Response response = null;
        String responseBody = null;
        try {
            response = AuthHttpUtils.getUnsafeOkHttpClientWithProxy().newCall(request).execute();
            responseBody = response.body().string();
            if (response.isSuccessful()) {
                return responseBody;
            } else {
                throw new IOException("Failed to send GET request. Response code: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
