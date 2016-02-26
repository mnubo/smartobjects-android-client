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

package com.mnubo.android.config;

import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.Properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.HttpUrl;

@Value
@Builder
public class MnuboSDKConfig {
    private final static String TAG = MnuboSDKConfig.class.getName();

    public static String CONFIG_KEY = "mnubo.key";

    public static String CONFIG_URL = "mnubo.url";
    public static String CONFIG_INGESTION_URL = "mnubo.url.ingestion";
    public static String CONFIG_RESTITUTION_URL = "mnubo.url.restitution";
    public static String CONFIG_OAUTH_URL = "mnubo.url.oauth";

    private final static String defaultIngestionPath = "/api/v3";
    private final static String defaultOAuthPath = "/oauth/token";
    private final static String defaultRestitutionPath = "/search";

    @NonNull
    private final String key;
    @NonNull
    private final HttpUrl oauthUrl;
    @NonNull
    private final HttpUrl ingestionUrl;
    @NonNull
    private final HttpUrl restitutionUrl;

    MnuboSDKConfig(String key, HttpUrl oauthUrl, HttpUrl ingestionUrl, HttpUrl restitutionUrl) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "consumerKey must not be empty");

        this.key = key;
        this.oauthUrl = oauthUrl;
        this.ingestionUrl = ingestionUrl;
        this.restitutionUrl = restitutionUrl;
    }

    public static MnuboSDKConfig withProperties(@NonNull Properties properties) {
        String key = properties.getProperty(CONFIG_KEY);

        final String defaultUrl = properties.getProperty(CONFIG_URL);
        Log.d(TAG, String.format("Mnubo default url is: %s", defaultUrl));

        final String oauthUrl = properties.getProperty(CONFIG_OAUTH_URL, defaultUrl + defaultOAuthPath);
        final String ingestionUrl = properties.getProperty(CONFIG_INGESTION_URL, defaultUrl + defaultIngestionPath);
        final String restitutionUrl = properties.getProperty(CONFIG_RESTITUTION_URL, defaultUrl + defaultRestitutionPath);

        return new MnuboSDKConfig(key, parseIfValid(oauthUrl), parseIfValid(ingestionUrl), parseIfValid(restitutionUrl));
    }

    public static MnuboSDKConfig withUrlAndKey(String hostname,
                                               String key) {
        return new MnuboSDKConfig(key,
                parseIfValid(hostname + defaultOAuthPath),
                parseIfValid(hostname + defaultIngestionPath),
                parseIfValid(hostname + defaultRestitutionPath));
    }

    private static HttpUrl parseIfValid(@NonNull String url) {
        HttpUrl parsedUrl = HttpUrl.parse(url);
        if (parsedUrl == null){
            throw new IllegalArgumentException(String.format("Invalid url %s", url));
        }
        return parsedUrl;
    }
}