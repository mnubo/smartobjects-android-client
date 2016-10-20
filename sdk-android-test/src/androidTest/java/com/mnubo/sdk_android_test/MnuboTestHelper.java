/*
 * Copyright (c) 2016 Mnubo. Released under MIT License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.mnubo.sdk_android_test;

import android.util.Base64;

import com.mnubo.android.internal.connect.interceptor.AccessTokenAuthenticationInterceptor;
import com.mnubo.android.utils.HttpUtils;
import com.mnubo.android.utils.JsonUtils;
import com.mnubo.sdk_android_test.ResitutionTestHelper.SearchResult;
import com.mnubo.sdk_android_test.ResitutionTestHelper.SearchResultColumn;

import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.mnubo.android.utils.HttpUtils.executeAndThrowOnFailure;
import static com.mnubo.android.utils.HttpUtils.executeForBodyAsObject;
import static com.mnubo.android.utils.HttpUtils.newClient;

public class MnuboTestHelper {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final static OkHttpClient tokenClient = newClient();

    @Getter
    private final OkHttpClient authorizedClient;
    private final String url;

    public MnuboTestHelper(@NonNull String url, @NonNull String key, @NonNull String secret) {
        final String token = getToken(url, key, secret);

        this.url = url;
        this.authorizedClient = newClient(new AccessTokenAuthenticationInterceptor(token));
    }

    @SneakyThrows
    private static String getToken(String url, String key, String secret) {
        final String keySecret = key + ":" + secret;
        final String basicAuth = Base64.encodeToString(keySecret.getBytes("UTF-8"), Base64.NO_WRAP);

        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("scope", "ALL")
                .build();

        Request request = new Request.Builder()
                .addHeader("Authorization", "Basic " + basicAuth)
                .url(HttpUtils.addPathVariables(baseUrl(url), "oauth", "token"))
                .post(body)
                .build();

        Map<String, Object> token = executeForBodyAsObject(tokenClient, request, JsonUtils.MAP_TYPE_REF);
        return token.get("access_token").toString();
    }

    @SneakyThrows
    public void createOwner(@NonNull String username, @NonNull String password) {
        final String payload = String.format("{\"username\": \"%s\", \"x_password\": \"%s\"}", username, password);
        final Request request = new Request.Builder()
                .url(HttpUtils.addPathVariables(baseUrl(url), "api", "v3", "owners"))
                .post(jsonBodyFromString(payload))
                .build();
        executeAndThrowOnFailure(authorizedClient, request);
    }

    @SneakyThrows
    public void createObject(@NonNull String deviceId, @NonNull String objectType) {
        final String payload = String.format("{\"x_device_id\": \"%s\", \"x_object_type\": \"%s\"}", deviceId, objectType);
        final Request request = new Request.Builder()
                .url(HttpUtils.addPathVariables(baseUrl(url), "api", "v3", "objects"))
                .post(jsonBodyFromString(payload))
                .build();
        executeAndThrowOnFailure(authorizedClient, request);
    }

    @SneakyThrows
    public void claim(@NonNull String deviceId, @NonNull String username) {
        final Request request = new Request.Builder()
                .url(HttpUtils.addPathVariables(baseUrl(url), "api", "v3", "owners", username, "objects", deviceId, "claim"))
                .post(emptyBody())
                .build();
        executeAndThrowOnFailure(authorizedClient, request);
    }
    @SneakyThrows
    public Map<SearchResultColumn, Object> searchOwner(@NonNull String username, @NonNull String... columnNames)  {
        final Request request = new Request.Builder()
                .url(HttpUtils.addPathVariables(baseUrl(url), "api", "v3", "search", "all"))
                .post(jsonBodyFromString(ownerQuery(username, columnNames)))
                .build();
        return executeForBodyAsObject(authorizedClient, request, SearchResult.class).firstResult();
    }

    @SneakyThrows
    public Map<SearchResultColumn, Object> searchObject(@NonNull String deviceId, @NonNull String... columnNames) {
        final Request request = new Request.Builder()
                .url(HttpUtils.addPathVariables(baseUrl(url), "api", "v3", "search", "all"))
                .post(jsonBodyFromString(objectQuery(deviceId, columnNames)))
                .build();
        return executeForBodyAsObject(authorizedClient, request, SearchResult.class).firstResult();
    }

    @SneakyThrows
    public Map<SearchResultColumn, Object> searchEvent(@NonNull UUID eventId, @NonNull String... columnNames) {
        final Request request = new Request.Builder()
                .url(HttpUtils.addPathVariables(baseUrl(url), "api", "v3", "search", "all"))
                .post(jsonBodyFromString(eventByIdQuery(eventId, columnNames)))
                .build();
        return executeForBodyAsObject(authorizedClient, request, SearchResult.class).firstResult();
    }

    private static HttpUrl baseUrl(String url) {
        return HttpUrl.parse(url);
    }

    private RequestBody jsonBodyFromString(String payload) {
        return RequestBody.create(JSON, payload);
    }

    private RequestBody emptyBody() {
        return RequestBody.create(null, new byte[0]);
    }

    private static String eventByIdQuery(UUID eventId, String... columnNames) {
        final String select = buildSelectClause(columnNames);
        return String.format(
                "{\"from\":\"event\",\"select\":%s,\"where\":{\"event_id\":{\"eq\":\"%s\"}}}",
                select,
                eventId
        );
    }

    private static String ownerQuery(String username, String... columnNames) {
        final String select = buildSelectClause(columnNames);
        return String.format(
                "{\"from\":\"owner\",\"select\":%s,\"where\":{\"username\":{\"eq\":\"%s\"}}}",
                select,
                username
        );
    }

    private static String objectQuery(String deviceId, String... columnNames) {
        final String select = buildSelectClause(columnNames);
        return String.format(
                "{\"from\":\"object\",\"select\":%s,\"where\":{\"x_device_id\":{\"eq\":\"%s\"}}}",
                select,
                deviceId
        );
    }

    private static String buildSelectClause(String... columnNames) {

        final StringBuilder select = new StringBuilder("[");
        for (int i = 0; i < columnNames.length; i++) {
            String columnName = columnNames[i];

            select.append(String.format("{\"value\":\"%s\"}", columnName));
            if(i != columnNames.length - 1) {
                select.append(",");
            }
        }
        select.append("]");
        return select.toString();
    }
}
