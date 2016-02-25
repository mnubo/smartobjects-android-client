package com.mnubo.android.models;

import com.mnubo.android.utils.JsonUtils;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.joda.time.DateTimeZone.UTC;
import static org.junit.Assert.assertThat;

/**
 * Created by davidfrancoeur on 2015-12-06.
 */
public class EventTest {

    @Test(expected = IllegalArgumentException.class)
    public void testBuildNoEventType() throws Exception {
        Event.builder().build();
    }

    @Test
    public void testBuild() throws Exception {
        Event event = Event.builder()
                .eventType("type")
                .build();
        assertThat(event, is(notNullValue()));
        assertThat(event.getEventType(), is(equalTo("type")));
        assertThat(event.getTimeseries(), is(notNullValue()));
    }

    @Test
    public void testSerialize() throws Exception {
        UUID eventId = UUID.randomUUID();
        String type = "type";
        DateTime now = DateTime.now(UTC);
        String testValue = "test";
        Event event = Event.builder()
                .eventType(type)
                .timestamp(now)
                .timeserie("test", testValue)
                .build();
        String payload = JsonUtils.toJson(event);
        assertThat(payload, is(notNullValue()));
        String expectedPayload = String.format("{\"x_event_type\":\"%s\",\"x_timestamp\":\"%s\",\"test\":\"%s\"}",
                type, now, testValue);
        assertThat(payload, equalTo(expectedPayload));
    }
}