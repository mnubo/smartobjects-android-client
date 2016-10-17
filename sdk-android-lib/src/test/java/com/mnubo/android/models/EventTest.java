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
        String type = "type";
        DateTime now = DateTime.now(UTC);
        String testValue = "test";
        Event event = Event.builder()
                .eventType(type)
                .timeserie("x_timestamp", now)
                .timeserie("test", testValue)
                .build();
        String payload = JsonUtils.toJson(event);
        assertThat(payload, is(notNullValue()));
        String expectedPayload = String.format("{\"x_event_type\":\"%s\",\"x_timestamp\":\"%s\",\"test\":\"%s\"}",
                type, now, testValue);
        assertThat(payload, equalTo(expectedPayload));
    }
}