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

package com.mnubo.sdk_android_test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mnubo.android.Mnubo;
import com.mnubo.android.api.AuthenticationProblemCallback;
import com.mnubo.android.config.MnuboSDKConfig;
import com.mnubo.android.exceptions.MnuboException;
import com.mnubo.android.models.Event;
import com.mnubo.android.models.Owner;
import com.mnubo.android.models.SmartObject;
import com.mnubo.android.utils.ValidationUtils;
import com.mnubo.sdk_android_test.ResitutionTestHelper.SearchResultColumn;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static com.mnubo.sdk_android_test.EventuallyAssert.eventually;
import static com.mnubo.sdk_android_test.ResitutionTestHelper.SearchResultColumn.datetime;
import static com.mnubo.sdk_android_test.ResitutionTestHelper.SearchResultColumn.text;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class EndtoEndTesting {
    /*
        This is required because any failure in @BeforeClass goes in the void.
        The result of this is a build failure saying the test suite is empty...

        the boolean is used to mimic a @BeforeClass
     */
    private static boolean initialized = false;
    /*
        Data model details
     */
    private final static String eventType = "event_type1";
    private final static String objectType = "object_type1";

    private final static String USERNAME = "username";
    private final static String DEVICE_ID = "x_device_id";
    private final static String REG_DATE = "x_registration_date";
    private final static String TIMESTAMP = "x_timestamp";
    private final static String OBJECT_TYPE = "x_object_type";
    private final static String EVENT_ID = "event_id";

    private final static String OWNER_TEXT_ATTR = "owner_text_attribute";
    private final static String OBJECT_TEXT_ATTR = "object_text_attribute";
    private final static String TIMESERIES_TEXT_ATTR = "ts_text_attribute";


    private final DateTime timestamp = LocalDateTime.now().toDateTime(DateTimeZone.UTC).toDateTimeISO();

    private static final String url = "https://rest.sandbox.mnubo.com";

    private static MnuboTestHelper mnuboTestHelper;

    private static final String username = "username-" + UUID.randomUUID().toString();
    private static final String password = "password-" + UUID.randomUUID().toString();
    private static final String deviceId = "device-" + UUID.randomUUID().toString();

    @Before
    public void setUp() throws Exception {
        if(!initialized) {
            final String consumerKey = getInstrumentation().getTargetContext().getResources().getString(R.string.consumer_key);
            final String consumerSecret = getInstrumentation().getTargetContext().getResources().getString(R.string.consumer_secret);
            ValidationUtils.notNullOrEmpty(consumerKey, "THE CONSUMER KEY IS NOT AVAILABLE.");
            ValidationUtils.notNullOrEmpty(consumerSecret, "THE CONSUMER SECRET IS NOT AVAILABLE.");

            mnuboTestHelper = new MnuboTestHelper(url, consumerKey, consumerSecret);
            mnuboTestHelper.createOwner(username, password);
            mnuboTestHelper.createObject(deviceId, objectType);
            mnuboTestHelper.claim(deviceId, username);

            Mnubo.init(
                    InstrumentationRegistry.getTargetContext(),
                    MnuboSDKConfig.withUrlAndKey(url, consumerKey),
                    new AuthenticationProblemCallback() {
                        @Override
                        public void onError() {
                            fail("login failed");
                        }
                    });

            assertTrue(Mnubo.logIn(username, password));
            initialized = true;
        }
    }

    @AfterClass
    public static void afterAll() {
        Mnubo.logOut();
    }

    @Test
    public void testSendEvents() throws Exception {
        assertTrue("Test was not initialized.", initialized);

        final UUID eventId1 = UUID.randomUUID();
        final UUID eventId2 = UUID.randomUUID();

        final String value1 = "value-" + eventId1.toString();
        final String value2 = "value-" + eventId2.toString();

        final List<Event> events = new ArrayList<Event>() {{
            add(Event.builder().eventType(eventType).timeserie(TIMESTAMP, timestamp).timeserie(EVENT_ID, eventId1.toString()).timeserie(TIMESERIES_TEXT_ATTR, value1).build());
            add(Event.builder().eventType(eventType).timeserie(TIMESTAMP, timestamp).timeserie(EVENT_ID, eventId2.toString()).timeserie(TIMESERIES_TEXT_ATTR, value2).build());
        }};
        Mnubo.getApi().getEventOperations().sendEvents(deviceId, events);
    }

    @Test
    public void testUpdateObject() throws Exception {
        assertTrue("Test was not initialized.", initialized);

        final String newValue = UUID.randomUUID().toString();
        SmartObject objectBody = SmartObject.builder()
                .attribute(OBJECT_TYPE, objectType)
                .attribute(OBJECT_TEXT_ATTR, newValue)
                .attribute(REG_DATE, timestamp)
                .build();
        Mnubo.getApi().getSmartObjectOperations().update(deviceId, objectBody);
    }

    @Test
    public void testCreateOwnersObject() throws Exception {
        assertTrue("Test was not initialized.", initialized);

        final String newValue = UUID.randomUUID().toString();
        SmartObject objectBody = SmartObject.builder()
                .attribute(OBJECT_TEXT_ATTR, newValue)
                .attribute(REG_DATE, timestamp)
                .build();
        Mnubo.getApi().getOwnerOperations().createObject(newValue, objectType, objectBody);

        Mnubo.getApi().getOwnerOperations().deleteObject(newValue);
    }

    @Test
    public void testUpdateOwner() throws Exception {
        assertTrue("Test was not initialized.", initialized);

        final String newValue = UUID.randomUUID().toString();
        Owner ownerBody = Owner.builder()
                .attribute(OWNER_TEXT_ATTR, newValue)
                .attribute(REG_DATE, timestamp)
                .build();
        Mnubo.getApi().getOwnerOperations().update(ownerBody);
    }

    @Test
    public void testCreateOwner() throws Exception {
        assertTrue("Test was not initialized.", initialized);

        final String newValue = UUID.randomUUID().toString();
        Owner ownerBody = Owner.builder()
                .attribute(OWNER_TEXT_ATTR, newValue)
                .attribute(REG_DATE, timestamp)
                .build();
        Mnubo.getApi().getOwnerOperations().create(newValue, ownerBody);

        Mnubo.getApi().getOwnerOperations().delete();
    }

    @Test
    public void testAttemptToSendEventsOnAnObjectNotBelongToOwner() throws Exception {
        assertTrue("Test was not initialized.", initialized);

        final List<Event> events = new ArrayList<Event>() {{
            add(Event.builder().eventType(eventType).build());
            add(Event.builder().eventType(eventType).build());
        }};
        ThrowableAssert assertable = ThrowableAssert.assertThrown(new ThrowableAssert.Thrower() {
            @Override
            public void throwing() throws Exception {
                Mnubo.getApi().getEventOperations().sendEvents("unknown", events);
            }
        });
        assertable.assertClass(MnuboException.class);
        assertable.assertMessage("The response code [403] was not in the 2xx family. The error message was: Access Denied");
    }

    @Test
    public void testAttemptToUpdateAnObjectNotBelongToTheOwner() throws Exception {
        assertTrue("Test was not initialized.", initialized);

        final SmartObject objectBody = SmartObject.builder()
                .attribute(OBJECT_TYPE, objectType)
                .attribute(REG_DATE, timestamp)
                .attribute(TIMESTAMP, timestamp)
                .build();

        //Update object
        ThrowableAssert assertable = ThrowableAssert.assertThrown(new ThrowableAssert.Thrower() {
            @Override
            public void throwing() throws Exception {
                Mnubo.getApi().getSmartObjectOperations().update("unknown_device", objectBody);
            }
        });
        assertable.assertClass(MnuboException.class);
        assertable.assertMessage("The response code [403] was not in the 2xx family. The error message was: Access Denied");
    }
}
