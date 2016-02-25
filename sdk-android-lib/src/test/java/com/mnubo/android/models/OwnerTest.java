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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.joda.time.DateTimeZone.UTC;
import static org.junit.Assert.assertThat;

/**
 * Created by davidfrancoeur on 2015-12-07.
 */
public class OwnerTest {
    @Test(expected = IllegalArgumentException.class)
    public void testBuildNoUsername() throws Exception {
        Owner.builder().build();
    }

    @Test
    public void testBuild() throws Exception {
        Owner smartObject = Owner.builder()
                .username("username")
                .build();
        assertThat(smartObject, is(notNullValue()));
        assertThat(smartObject.getUsername(), is(equalTo("username")));
        assertThat(smartObject.getAttributes(), is(notNullValue()));
    }

    @Test
    public void testSerialize() throws Exception {
        DateTime now = DateTime.now(UTC);
        String testValue = "test";
        String username = "username";
        String password = "password";
        Owner smartObject = Owner.builder()
                .username(username)
                .password(password)
                .registrationDate(now)
                .attribute("test", testValue)
                .build();
        String payload = JsonUtils.toJson(smartObject);
        assertThat(payload, is(notNullValue()));
        String expectedPayload = String.format("{\"username\":\"%s\",\"x_password\":\"%s\",\"x_registration_date\":\"%s\",\"test\":\"%s\"}",
                username, password, now, testValue);
        assertThat(payload, equalTo(expectedPayload));
    }

    @Test
    public void testNotIncludingEmptyFields() throws Exception {
        String testValue = "test";
        String username = "username";
        Owner smartObject = Owner.builder()
                .username(username)
                .attribute("test", testValue)
                .build();
        String payload = JsonUtils.toJson(smartObject);
        assertThat(payload, is(notNullValue()));
        String expectedPayload = String.format("{\"username\":\"username\",\"test\":\"%s\"}", testValue);
        assertThat(payload, equalTo(expectedPayload));
    }
}