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
                .username(username)
                .password(password)
                .registrationDate(now)
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