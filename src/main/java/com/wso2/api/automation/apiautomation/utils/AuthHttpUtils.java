package com.wso2.api.automation.apiautomation.utils;

import okhttp3.*;
import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

import static com.wso2.api.automation.apiautomation.utils.Constants.*;

public class AuthHttpUtils {

    private static final String PROXY_USERNAME = PropertyReader.getProperty("http.proxy.username");
    private static final String PROXY_PASSWORD = PropertyReader.getProperty("http.proxy.password");


    private static Authenticator proxyAuthenticator = new Authenticator() {
        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            String credential = Credentials.basic(PROXY_USERNAME, PROXY_PASSWORD);
            return response.request().newBuilder()
                    .header(PROXY_AUTHORIZATION_HEADER, credential)
                    .build();
        }
    };

    public static OkHttpClient getOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();

        return okHttpClient;
    }
    public static OkHttpClient getUnsafeOkHttpClientWithProxy() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                        }
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                        }
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {

                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance(SSL_CONTEXT);


            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);

            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    // Allow connections to bypass proxy
                    return true;
                }
            });

            /*builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT)))
                    .proxyAuthenticator(proxyAuthenticator);*/

            OkHttpClient okHttpClient = builder.build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
