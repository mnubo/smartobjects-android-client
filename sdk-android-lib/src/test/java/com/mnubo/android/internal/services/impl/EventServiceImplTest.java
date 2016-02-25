package com.mnubo.android.internal.services.impl;

import com.mnubo.android.internal.connect.MnuboConnectionManager;
import com.mnubo.android.internal.connect.interceptor.AccessTokenAuthenticationInterceptor;
import com.mnubo.android.internal.services.EventService;
import com.mnubo.android.models.Event;
import com.mnubo.android.utils.JsonUtils;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventServiceImplTest {

    private EventService eventService;
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

        eventService = new EventServiceImpl(mnuboConnectionManager, server.url("/rest"));
    }

    @Test
    public void testSendEvents() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200));

        Event event = Event.builder()
                .eventType("type")
                .timestamp(DateTime.now())
                .build();

        List<Event> events = Collections.singletonList(event);
        eventService.sendEvents("deviceId", events);

        String expectedPayload = JsonUtils.toJson(events);
        RecordedRequest request = server.takeRequest();
        assertThat(request.getHeader("Authorization"), is(equalTo("Bearer " + "token")));
        assertThat(request.getPath(), is(equalTo("/rest/objects/deviceId/events")));
        assertThat(request.getMethod(), is(equalTo("POST")));
        assertThat(request.getBody().readUtf8(), is(equalTo(expectedPayload)));
    }

    @Test
    public void testSendEventsEmptyList() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200));

        List<Event> events = Collections.emptyList();
        eventService.sendEvents("deviceId", events);

        String expectedPayload = JsonUtils.toJson(events);
        RecordedRequest request = server.takeRequest();
        assertThat(request.getHeader("Authorization"), is(equalTo("Bearer " + "token")));
        assertThat(request.getPath(), is(equalTo("/rest/objects/deviceId/events")));
        assertThat(request.getMethod(), is(equalTo("POST")));
        assertThat(request.getBody().readUtf8(), is(equalTo(expectedPayload)));
    }
}