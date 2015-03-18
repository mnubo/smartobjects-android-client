/*
 * Copyright (c) 2015 Mnubo. Released under MIT License.
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy
 *     of this software and associated documentation files (the "Software"), to deal
 *     in the Software without restriction, including without limitation the rights
 *     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *     copies of the Software, and to permit persons to whom the Software is
 *     furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in
 *     all copies or substantial portions of the Software.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *     THE SOFTWARE.
 */
package com.mnubo.platform.android.sdk.internal;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnubo.platform.android.sdk.BuildConstants;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApiImpl;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApiImpl;

import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.RequestMatcher;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Log.class,
        PlainSocketFactory.class,
        SSLSocketFactory.class,
        HttpComponentsClientHttpRequestFactory.class,
        ConnManagerParams.class,
        AbstractHttpClient.class
})
public abstract class AbstractServicesTest {

    private final ObjectMapper mapper = new ObjectMapper();
    protected static final MediaType APPLICATION_JSON_UTF8 = MediaType.valueOf("application/json;charset=UTF-8");

    private final PlainSocketFactory mockedPlainSocketFactory = mock(PlainSocketFactory.class);
    private final SSLSocketFactory mockedSSLSocketFactory = mock(SSLSocketFactory.class);
    private final SchemeRegistry mockedSchemeRegistry = mock(SchemeRegistry.class);
    private final DefaultHttpClient mockedHttpClient = mock(DefaultHttpClient.class);
    private final HttpParams mockedHttpParams = mock(HttpParams.class);

    private final String USER_ACCESS_TOKEN = "user_token";
    private final String CLIENT_ACCESS_TOKEN = "client_token";
    private final String PLATFORM_BASE_URL = "http://test.com";

    protected MockRestServiceServer mockUserServiceServer;
    protected MockRestServiceServer mockClientServiceServer;
    protected MnuboUserApiImpl mnuboUserApi;
    protected MnuboClientApiImpl mnuboClientApi;

    @Before
    public void setUp() throws Exception {
        mockStatic(Log.class);
        mockStatic(PlainSocketFactory.class);
        mockStatic(SchemeRegistry.class);
        mockStatic(SSLSocketFactory.class);
        mockStatic(ConnManagerParams.class);

        whenNew(SchemeRegistry.class).withAnyArguments().thenReturn(mockedSchemeRegistry);
        whenNew(DefaultHttpClient.class).withAnyArguments().thenReturn(mockedHttpClient);
        when(PlainSocketFactory.getSocketFactory()).thenReturn(mockedPlainSocketFactory);
        when(SSLSocketFactory.getSocketFactory()).thenReturn(mockedSSLSocketFactory);
        when(mockedHttpClient.getParams()).thenReturn(mockedHttpParams);

        mnuboUserApi = new MnuboUserApiImpl(USER_ACCESS_TOKEN, PLATFORM_BASE_URL);
        mnuboClientApi = new MnuboClientApiImpl(CLIENT_ACCESS_TOKEN, PLATFORM_BASE_URL);

        mockUserServiceServer = MockRestServiceServer.createServer(mnuboUserApi.getRestTemplate());
        mockClientServiceServer = MockRestServiceServer.createServer(mnuboClientApi.getRestTemplate());

    }

    protected String toJson(Object response) throws Exception {
        return mapper.writeValueAsString(response);
    }

    protected RequestMatcher userAuthMatch() {
        return header("Authorization", bearerTokenHeader(USER_ACCESS_TOKEN));
    }

    protected RequestMatcher clientAuthMatch() {
        return header("Authorization", bearerTokenHeader(CLIENT_ACCESS_TOKEN));
    }

    private String bearerTokenHeader(final String token) {
        return String.format("Bearer %s", token);
    }

    protected String expectedUrl(final String uri) {
        return String.format("%s%s%s", PLATFORM_BASE_URL, BuildConstants.PATH, uri);
    }
}
