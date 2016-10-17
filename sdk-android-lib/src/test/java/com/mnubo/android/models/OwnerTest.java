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

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.joda.time.DateTimeZone.UTC;
import static org.junit.Assert.assertThat;


public class OwnerTest {
    @Test
    public void testBuild() throws Exception {
        final DateTime now = DateTime.now();
        Owner smartObject = Owner.builder()
                .attribute("x_registration_date", now)
                .attribute("x_registration_latitude", 123.213d)
                .attribute("x_registration_longitude", 45.321d)
                .build();
        assertThat(smartObject, is(notNullValue()));
        Map<String, Object> expected = new HashMap<String, Object>(){{
            put("x_registration_date", now);
            put("x_registration_latitude", 123.213d);
            put("x_registration_longitude", 45.321d);
        }};
        assertThat(smartObject.getAttributes(), is(equalTo(expected)));
    }

    @Test
    public void testSerialize() throws Exception {
        DateTime now = DateTime.now(UTC);
        String testValue = "test";
        double latitude = 123.213d;
        double longitude = 45.321d;
        Owner smartObject = Owner.builder()
                .attribute("x_registration_date", now)
                .attribute("x_registration_latitude", latitude)
                .attribute("x_registration_longitude", longitude)
                .attribute("test", testValue)
                .build();
        String payload = JsonUtils.toJson(smartObject);
        assertThat(payload, is(notNullValue()));
        String expectedPayload = String.format("{\"x_registration_date\":\"%s\",\"x_registration_latitude\":%s,\"x_registration_longitude\":%s,\"test\":\"%s\"}",
                now, latitude, longitude, testValue);
        assertThat(payload, equalTo(expectedPayload));
    }
}