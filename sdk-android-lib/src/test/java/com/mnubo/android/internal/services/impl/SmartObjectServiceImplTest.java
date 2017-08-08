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

package com.mnubo.android.internal.services.impl;

import com.mnubo.android.internal.connect.MnuboConnectionManager;
import com.mnubo.android.internal.connect.interceptor.AccessTokenAuthenticationInterceptor;
import com.mnubo.android.internal.services.SmartObjectService;
import com.mnubo.android.models.SmartObject;
import com.mnubo.android.utils.JsonUtils;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SmartObjectServiceImplTest {

    private SmartObjectService smartObjectService;
    private MockWebServer server;

    private OkHttpClient okHttpClient =
            new OkHttpClient.Builder()
                    .addInterceptor(new AccessTokenAuthenticationInterceptor("token"))
                    .build();

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        MnuboConnectionManager mnuboConnectionManager = mock(MnuboConnectionManager.class);
        when(mnuboConnectionManager.getUserAuthenticatedHttpClient()).thenReturn(okHttpClient);

        smartObjectService = new SmartObjectServiceImpl(mnuboConnectionManager, server.url("/rest"));
    }

    @Test
    public void testUpdate() throws Exception {

        server.enqueue(new MockResponse().setResponseCode(200));

        String deviceId = "deviceId";
        SmartObject smartObject = SmartObject.builder()
                .attribute("x_registration_date", DateTime.now())
                .attribute("x_registration_latitude", 123.123d)
                .attribute("x_registration_longitude", 45.12d)
                .build();

        smartObjectService.update(deviceId, smartObject);

        String expectedPayload = JsonUtils.toJson(smartObject);
        RecordedRequest request = server.takeRequest();
        assertThat(request.getHeader("Authorization"), is(equalTo("Bearer " + "token")));
        assertThat(request.getPath(), is(equalTo(String.format("/rest/objects/%s", deviceId))));
        assertThat(request.getMethod(), is(equalTo("PUT")));
        assertThat(request.getBody().readUtf8(), is(equalTo(expectedPayload)));

    }
}