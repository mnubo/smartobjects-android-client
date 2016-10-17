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

package com.mnubo.android.internal.services.impl;

import com.mnubo.android.internal.connect.MnuboConnectionManager;
import com.mnubo.android.internal.connect.interceptor.AccessTokenAuthenticationInterceptor;
import com.mnubo.android.internal.services.OwnerService;
import com.mnubo.android.models.Owner;
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
import static org.joda.time.DateTimeZone.UTC;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by davidfrancoeur on 2015-12-07.
 */
public class OwnerServiceImplTest {


    private OwnerService ownerService;
    private MockWebServer server;

    private MnuboConnectionManager mnuboConnectionManager;

    OkHttpClient okHttpClient =
            new OkHttpClient.Builder()
                    .addInterceptor(new AccessTokenAuthenticationInterceptor("token"))
                    .build();

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();

        mnuboConnectionManager = mock(MnuboConnectionManager.class);
        when(mnuboConnectionManager.getUserAuthenticatedHttpClient()).thenReturn(okHttpClient);

        ownerService = new OwnerServiceImpl(mnuboConnectionManager, server.url("/rest"));
    }

    @Test
    public void testUpdate() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200));

        String username = "username";
        String password = "password";
        DateTime now = DateTime.now(UTC);

        Owner owner = Owner.builder()
                .attribute("x_registration_date", now)
                .attribute("x_registration_latitude", 123.213d)
                .attribute("x_registration_longitude", 45.321d)
                .build();

        ownerService.update(username, owner);

        String expectedPayload = JsonUtils.toJson(owner);
        RecordedRequest request = server.takeRequest();
        assertThat(request.getHeader("Authorization"), is(equalTo("Bearer " + "token")));
        assertThat(request.getPath(), is(equalTo(String.format("/rest/owners/%s", username))));
        assertThat(request.getMethod(), is(equalTo("PUT")));
        assertThat(request.getBody().readUtf8(), is(equalTo(expectedPayload)));
    }
}