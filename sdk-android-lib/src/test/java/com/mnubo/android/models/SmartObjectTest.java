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
 * Created by davidfrancoeur on 2015-12-07.
 */
public class SmartObjectTest {

    @Test(expected = IllegalArgumentException.class)
    public void testBuildEmpty() throws Exception {
        SmartObject.builder().build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildNoObjectType() throws Exception {
        SmartObject.builder()
                .build();
    }

    @Test
    public void testBuild() throws Exception {
        SmartObject smartObject = SmartObject.builder()
                .objectType("type")
                .build();
        assertThat(smartObject, is(notNullValue()));
        assertThat(smartObject.getObjectType(), is(equalTo("type")));
        assertThat(smartObject.getAttributes(), is(notNullValue()));
    }

    @Test
    public void testSerialize() throws Exception {
        String type = "type";
        DateTime now = DateTime.now(UTC);
        String testValue = "test";
        SmartObject smartObject = SmartObject.builder()
                .objectType(type)
                .registrationDate(now)
                .attribute("test", testValue)
                .build();
        String payload = JsonUtils.toJson(smartObject);
        assertThat(payload, is(notNullValue()));
        String expectedPayload = String.format("{\"x_object_type\":\"%s\",\"x_registration_date\":\"%s\",\"test\":\"%s\"}",
                type, now, testValue);
        assertThat(payload, equalTo(expectedPayload));
    }
}