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