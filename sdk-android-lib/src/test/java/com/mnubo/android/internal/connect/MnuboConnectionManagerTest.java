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

package com.mnubo.android.internal.connect;

import android.util.Log;

import com.mnubo.android.api.AuthenticationProblemCallback;
import com.mnubo.android.config.MnuboSDKConfig;
import com.mnubo.android.internal.connect.interceptor.AccessTokenAuthenticationInterceptor;
import com.mnubo.android.utils.TimeUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Log.class,
        TimeUtils.class,
})
@PowerMockIgnore("javax.net.*")
public class MnuboConnectionManagerTest {

    long currentTimeSeconds = 500L;
    long expiresIn = 1000L;
    long expireTime = currentTimeSeconds + expiresIn;

    private final String tokenPayload = format("{\"access_token\": \"new_access_token\",  \"token_type\": \"Bearer\",  \"refresh_token\": \"new_refresh_token\",  \"expires_in\": %s}", expiresIn);

    private MnuboSDKConfig dummyCfg;

    private MnuboConnectionManager testee;

    private ConnectionStore connectionStore;
    private OkHttpClient httpClient;
    private MockWebServer server;
    private AuthenticationProblemCallback authenticationProblemCallback;

    @Before
    public void setUp() throws Exception {
        mockStatic(Log.class);
        mockStatic(TimeUtils.class);
        PowerMockito.when(TimeUtils.getCurrentTimeSeconds()).thenReturn(currentTimeSeconds);

        httpClient = new OkHttpClient();
        server = new MockWebServer();
        server.start();

        dummyCfg = MnuboSDKConfig.builder()
                .key("key")
                .ingestionUrl(server.url("/rest"))
                .oauthUrl(server.url("/oauth/token"))
                .build();

        authenticationProblemCallback = mock(AuthenticationProblemCallback.class);
        connectionStore = mock(ConnectionStore.class);
    }

    @Test
    public void testRefreshIfRequired_whenConnected() throws Exception {
        initConnected(expireTime);

        testee.refreshIfRequired();
        MnuboConnection mnuboConnection = testee.getCurrentConnection();

        assertThat(mnuboConnection, is(notNullValue()));
        assertThat(mnuboConnection.getUsername(), is(equalTo("username")));
        assertThat(mnuboConnection.getToken().getAccessToken(), is(equalTo("access_token")));
        assertThat(mnuboConnection.getToken().getRefreshToken(), is(equalTo("refresh_token")));
        assertThat(mnuboConnection.getToken().getExpireTime(), is(equalTo(expireTime)));
    }

    @Test
    public void testRefreshIfRequired_whenConnectedButExpired() throws Exception {
        initConnected(0L);
        server.enqueue(new MockResponse().setResponseCode(200).setBody(tokenPayload));

        testee.refreshIfRequired();
        MnuboConnection mnuboConnection = testee.getCurrentConnection();

        assertThat(mnuboConnection, is(notNullValue()));
        assertThat(mnuboConnection.getUsername(), is(equalTo("username")));
        assertThat(mnuboConnection.getToken().getAccessToken(), equalTo("new_access_token"));
        assertThat(mnuboConnection.getToken().getRefreshToken(), equalTo("new_refresh_token"));
        assertThat(mnuboConnection.getToken().getExpireTime(), equalTo(currentTimeSeconds + expiresIn));

        RecordedRequest request = server.takeRequest();
        assertThat(request.getPath(), equalTo("/oauth/token"));
        assertThat(request.getMethod(), equalTo("POST"));
        assertThat(request.getBody().readUtf8(), equalTo("refresh_token=refresh_token&grant_type=refresh_token&client_id=key"));
    }

    @Test
    public void testRefreshIfRequired_whenConnectedButExpiredAndRefreshingFails() throws Exception {
        initConnected(0L);
        server.enqueue(new MockResponse().setResponseCode(400));

        testee.refreshIfRequired();
        MnuboConnection mnuboConnection = testee.getCurrentConnection();

        assertThat(mnuboConnection, is(notNullValue()));
        assertThat(mnuboConnection.getUsername(), is(equalTo("username")));
        assertThat(mnuboConnection.getToken().getAccessToken(), equalTo("access_token"));
        assertThat(mnuboConnection.getToken().getRefreshToken(), equalTo("refresh_token"));
        assertThat(mnuboConnection.getToken().getExpireTime(), equalTo(0L));

        RecordedRequest request = server.takeRequest();
        assertThat(request.getPath(), equalTo("/oauth/token"));
        assertThat(request.getMethod(), equalTo("POST"));
        assertThat(request.getBody().readUtf8(), equalTo("refresh_token=refresh_token&grant_type=refresh_token&client_id=key"));
    }

    @Test
    public void testRefreshIfRequired_whenNotConnected() throws Exception {
        initNotConnected();

        testee.refreshIfRequired();
        MnuboConnection mnuboConnection = testee.getCurrentConnection();

        assertThat(mnuboConnection, is(nullValue()));
        verify(authenticationProblemCallback, times(1)).onError();
        verify(connectionStore, times(1)).clear();
    }

    @Test
    public void testGetUsername_whenConnected() throws Exception {
        initConnected(expireTime);

        assertThat(testee.getUsername(), equalTo("username"));
    }

    @Test
    public void testGetUsername_whenNotConnected() throws Exception {
        initNotConnected();

        assertThat(testee.getUsername(), is(nullValue()));
        verify(authenticationProblemCallback, times(1)).onError();
        verify(connectionStore, times(1)).clear();
    }

    @Test
    public void testLogOut_whenConnected() throws Exception {
        initConnected(expireTime);

        testee.logOut();
        verify(connectionStore, times(1)).clear();
    }

    @Test
    public void testLogOut_whenNotConnected() throws Exception {
        initNotConnected();

        testee.logOut();
        verify(connectionStore, times(1)).clear();
    }

    @Test
    public void testLogIn_whenConnected() throws Exception {
        initConnected(expireTime);
        server.enqueue(new MockResponse().setResponseCode(200).setBody(tokenPayload));

        boolean success = testee.logIn("username", "password");
        verify(connectionStore, times(1)).clear();

        assertThat(success, equalTo(true));

        RecordedRequest request = server.takeRequest();
        assertThat(request.getPath(), equalTo("/oauth/token"));
        assertThat(request.getMethod(), equalTo("POST"));
        assertThat(request.getBody().readUtf8(), equalTo("client_id=key&username=username&password=password&grant_type=password"));

    }

    @Test
    public void testLogIn_whenNotConnected() throws Exception {
        initNotConnected();
        server.enqueue(new MockResponse().setResponseCode(200).setBody(tokenPayload));

        boolean success = testee.logIn("username", "password");

        assertThat(success, equalTo(true));

        RecordedRequest request = server.takeRequest();
        assertThat(request.getPath(), equalTo("/oauth/token"));
        assertThat(request.getMethod(), equalTo("POST"));
        assertThat(request.getBody().readUtf8(), equalTo("client_id=key&username=username&password=password&grant_type=password"));
    }

    @Test
    public void testLogIn_fetchTokenFails() throws Exception {
        initNotConnected();
        server.enqueue(new MockResponse().setResponseCode(400));

        boolean success = testee.logIn("username", "password");

        assertThat(success, equalTo(false));

        RecordedRequest request = server.takeRequest();
        assertThat(request.getPath(), equalTo("/oauth/token"));
        assertThat(request.getMethod(), equalTo("POST"));
        assertThat(request.getBody().readUtf8(), equalTo("client_id=key&username=username&password=password&grant_type=password"));
    }

    @Test
    public void testIsLoggedIn_whenConnected() throws Exception {
        initConnected(expireTime);
        boolean loggedIn = testee.isLoggedIn();
        assertThat(loggedIn, equalTo(true));
    }

    @Test
    public void testIsLoggedIn_whenNotConnected() throws Exception {
        initNotConnected();
        boolean loggedIn = testee.isLoggedIn();
        assertThat(loggedIn, equalTo(false));
    }

    @Test
    public void testgetUserAuthClient() throws Exception {
        initConnected(expireTime);
        OkHttpClient userAuthenticatedHttpClient = testee.getUserAuthenticatedHttpClient();
        assertThat(userAuthenticatedHttpClient, is(notNullValue()));
        assertThat(userAuthenticatedHttpClient.interceptors().size(), equalTo(1));
        AccessTokenAuthenticationInterceptor interceptor = (AccessTokenAuthenticationInterceptor)userAuthenticatedHttpClient.interceptors().get(0);
        assertThat(interceptor.getAccessToken(), equalTo("access_token"));

    }

    @Test
    public void testgetUserAuthClient_whenNotConnected() throws Exception {
        initConnected(expireTime);
        testee.getUserAuthenticatedHttpClient();

    }

    private void initConnected(long expireTime) {
        when(connectionStore.hasConnection()).thenReturn(true);
        when(connectionStore.getConnection()).thenReturn(
                new MnuboConnection("username", new MnuboConnection.Token("access_token", "refresh_token", expireTime)));
        testee = new MnuboConnectionManager(dummyCfg, connectionStore, httpClient, authenticationProblemCallback);

    }

    private void initNotConnected() {
        when(connectionStore.hasConnection()).thenReturn(false);
        testee = new MnuboConnectionManager(dummyCfg, connectionStore, httpClient, authenticationProblemCallback);
    }
}