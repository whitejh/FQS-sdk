package com.f4.fqs.springSdk.utils;

import com.f4.fqs.springSdk.exception.FQSException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.f4.fqs.springSdk.constants.FQSConstants.*;

public class FQSHttpClient {

    private final HttpClient client;

    public FQSHttpClient() {
        this.client = HttpClient.newHttpClient();
    }

    public HttpResponse<String> sendHttpRequest(String url, String method, String requestBody, String secretKey) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header(SECRET_KEY, secretKey);

            switch (method.toUpperCase()) {
                case POST:
                    requestBuilder
                            .header(CONTENT_TYPE, APPLICATION_JSON)
                            .POST(HttpRequest.BodyPublishers.ofString(requestBody != null ? requestBody : "{}"));
                    break;

                case GET:
                    requestBuilder.GET();
                    break;

                default:
                    throw new FQSException("Unsupported HTTP method: " + method);
            }

            HttpRequest request = requestBuilder.build();
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new FQSException("HTTP request failed: " + e.getMessage(), e);
        }
    }
}