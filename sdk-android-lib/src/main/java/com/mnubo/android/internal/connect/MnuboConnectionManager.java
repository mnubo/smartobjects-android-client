/*
 * Copyright (c) 2017 Mnubo. Released under MIT License.
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

package com.mnubo.android.internal.connect;

import android.content.Context;
import android.util.Log;

import com.mnubo.android.api.AuthenticationProblemCallback;
import com.mnubo.android.config.MnuboSDKConfig;
import com.mnubo.android.config.SupportedIsp;
import com.mnubo.android.exceptions.MnuboException;
import com.mnubo.android.exceptions.MnuboNotLoggedInException;
import com.mnubo.android.internal.connect.interceptor.AccessTokenAuthenticationInterceptor;

import lombok.NonNull;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.mnubo.android.utils.HttpUtils.executeForBodyAsObject;
import static com.mnubo.android.utils.HttpUtils.newClient;

public class MnuboConnectionManager {

    private static final String TAG = MnuboConnectionManager.class.getName();

    private final MnuboSDKConfig config;
    private final ConnectionStore connectionStore;
    private final OkHttpClient okHttpClient;
    private final AuthenticationProblemCallback authenticationProblemCallback;

    private MnuboConnection currentConnection = null;

    MnuboConnectionManager(@NonNull MnuboSDKConfig config,
                           @NonNull ConnectionStore connectionStore,
                           @NonNull OkHttpClient httpClient,
                           @NonNull AuthenticationProblemCallback authenticationProblemCallback) {
        this.config = config;
        this.okHttpClient = httpClient;
        this.authenticationProblemCallback = authenticationProblemCallback;

        this.connectionStore = connectionStore;

        if (this.connectionStore.hasConnection()) {
            currentConnection = this.connectionStore.getConnection();
        }
    }

    public static MnuboConnectionManager newMnuboConnectionManager(MnuboSDKConfig config, Context context, AuthenticationProblemCallback authenticationProblemCallback) {
        return new MnuboConnectionManager(config, new ConnectionStore(context), new OkHttpClient(), authenticationProblemCallback);
    }

    void refreshIfRequired() {
        if (currentConnection == null) {
            Log.d(TAG, "User is not logged in.");
            authenticationProblemCallback.onError();
            logOut();
        } else if (currentConnection.getToken().hasExpired()) {
            Log.d(TAG, "Refreshing user token...");
            final MnuboConnection refreshedConnection = refreshConnection(currentConnection);
            if (refreshedConnection != null) {
                Log.d(TAG, "User token was successfully refreshed.");

                connectionStore.saveConnection(refreshedConnection);

                currentConnection = refreshedConnection;
            } else {
                Log.e(TAG, "Unable to refresh token");
            }
        }

    }

    public String getUsername() {
        if (currentConnection == null) {
            logOut();
            authenticationProblemCallback.onError();
            return null;
        }
        return currentConnection.getUsername();
    }

    public void logOut() {
        String REMOVE_PERSISTED_USER_CONNECTION = "Removing all user connections.";
        Log.d(TAG, REMOVE_PERSISTED_USER_CONNECTION);
        connectionStore.clear();

        currentConnection = null;
    }

    public boolean logIn(String username, String password) {
        this.logOut();
        this.currentConnection = this.logWithOwnerPassword(username, password);
        return currentConnection != null;
    }

    public boolean logIn(String username, String token, SupportedIsp isp) {
        this.logOut();
        this.currentConnection = this.logWithIspToken(username, token, isp);
        return currentConnection != null;
    }

    public boolean isLoggedIn() {
        return currentConnection != null;
    }

    public OkHttpClient getUserAuthenticatedHttpClient() throws MnuboException {
        refreshIfRequired();

        if (currentConnection == null) {
            logOut();
            authenticationProblemCallback.onError();
            throw new MnuboNotLoggedInException();
        }

        return newClient(new AccessTokenAuthenticationInterceptor(currentConnection.getToken().getAccessToken()));

    }

    MnuboConnection logWithOwnerPassword(@NonNull String username, @NonNull String password) {

        RequestBody body = new FormBody.Builder()
                .add("client_id", config.getKey())
                .add("username", username)
                .add("password", password)
                .add("grant_type", "password")
                .build();

        Request request = new Request.Builder()
                .url(config.getOauthUrl())
                .post(body)
                .build();

        try {
            MnuboConnection.Token token = executeForBodyAsObject(okHttpClient, request, MnuboConnection.Token.class);
            return new MnuboConnection(username, token);
        } catch (MnuboException e) {
            Log.d(TAG, "A error occurred while authenticating.", e);
            return null;
        }
    }

    MnuboConnection logWithIspToken(@NonNull String username, @NonNull String token, @NonNull SupportedIsp isp) {

        RequestBody body = new FormBody.Builder()
                .add("client_id", config.getKey())
                .add("isp_token", token)
                .add("isp", isp.name().toLowerCase())
                .add("grant_type", "isp_token")
                .build();

        Request request = new Request.Builder()
                .url(config.getOauthUrl())
                .post(body)
                .build();

        try {
            MnuboConnection.Token mnuboToken = executeForBodyAsObject(okHttpClient, request, MnuboConnection.Token.class);
            return new MnuboConnection(username, mnuboToken);
        } catch (MnuboException e) {
            Log.d(TAG, "A error occurred while authenticating.", e);
            return null;
        }
    }

    MnuboConnection refreshConnection(@NonNull MnuboConnection connection) {
        RequestBody body = new FormBody.Builder()
                .add("refresh_token", connection.getToken().getRefreshToken())
                .add("grant_type", "refresh_token")
                .add("client_id", config.getKey())
                .build();

        Request request = new Request.Builder()
                .url(config.getOauthUrl())
                .post(body)
                .build();

        try {
            MnuboConnection.Token token = executeForBodyAsObject(okHttpClient, request, MnuboConnection.Token.class);
            return new MnuboConnection(connection.getUsername(), token);
        } catch (MnuboException e) {
            Log.d(TAG, "A error occurred while refreshing the token.", e);
            return null;
        }
    }

    MnuboConnection getCurrentConnection(){
        return currentConnection;
    }
}
