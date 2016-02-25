/*
 * Copyright (c) 2015 Mnubo. Released under MIT License.
 *
 * 	Permission is hereby granted, free of charge, to any person obtaining a copy
 * 	of this software and associated documentation files (the "Software"), to deal
 * 	in the Software without restriction, including without limitation the rights
 * 	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * 	copies of the Software, and to permit persons to whom the Software is
 * 	furnished to do so, subject to the following conditions:
 *
 * 	The above copyright notice and this permission notice shall be included in
 * 	all copies or substantial portions of the Software.
 *
 * 	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * 	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * 	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * 	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * 	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * 	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * 	THE SOFTWARE.
 */

package com.mnubo.android.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Preconditions;
import com.mnubo.android.exceptions.MnuboException;
import com.mnubo.android.exceptions.MnuboNetworkException;

import java.io.IOException;

import lombok.NonNull;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.google.common.base.Strings.isNullOrEmpty;

public class HttpUtils {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static OkHttpClient newClient(Interceptor... interceptors) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        for (Interceptor interceptor : interceptors) {
            Preconditions.checkArgument(interceptor != null, "interceptors should not be null");
            client.addInterceptor(interceptor);
        }
        return client.build();
    }

    public static RequestBody buildBody(Object object) throws MnuboException {
        try {
            return RequestBody.create(JSON, JsonUtils.toJson(object));
        } catch (JsonProcessingException e) {
            throw new MnuboException("Error building the JSON.", e);
        }
    }

    public static HttpUrl addPathVariables(@NonNull HttpUrl url, @NonNull String... variables) {
        HttpUrl.Builder builder = url.newBuilder();
        for (String path : variables) {
            Preconditions.checkArgument(!isNullOrEmpty(path), "path variable should not be null or empty");
            builder.addPathSegment(path);
        }
        return builder.build();
    }

    public static Response execute(OkHttpClient client, Request request) throws MnuboException {
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new MnuboNetworkException("An IOException occurred while sending your request.", e);
        }
    }

    public static Response executeAndThrowOnFailure(OkHttpClient client, Request request) throws MnuboException {
        Response response = execute(client, request);
        boolean successful = response.isSuccessful();
        if (!successful) {
            int responseCode = response.code();
            String errorMsg = tryToParseError(response);
            throw new MnuboException(String.format("The response code [%s] was not in the 2xx family. The error message was: %s", responseCode, errorMsg));
        }
        return response;
    }

    public static String executeForBodyAsString(OkHttpClient client, Request request) throws MnuboException {
        Response response = executeAndThrowOnFailure(client, request);
        try {
            return response.body().string();
        } catch (IOException e) {
            throw new MnuboException("Impossible to read response.", e);
        }
    }

    public static <T> T executeForBodyAsObject(OkHttpClient client, Request request, Class<T> clazz) throws MnuboException {
        String payload = executeForBodyAsString(client, request);
        try {
            return JsonUtils.fromJson(payload, clazz);
        } catch (IOException e) {
            throw new MnuboException("Impossible to parse JSON.", e);
        }
    }

    private static String tryToParseError(Response response) {
        try {
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
    }

}
