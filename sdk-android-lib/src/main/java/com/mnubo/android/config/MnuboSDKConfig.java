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