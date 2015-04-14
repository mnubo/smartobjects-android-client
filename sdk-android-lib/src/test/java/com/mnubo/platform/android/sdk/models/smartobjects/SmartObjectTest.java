/*
 * Copyright (c) 2015 Mnubo. Released under MIT License.
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy
 *     of this software and associated documentation files (the "Software"), to deal
 *     in the Software without restriction, including without limitation the rights
 *     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *     copies of the Software, and to permit persons to whom the Software is
 *     furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in
 *     all copies or substantial portions of the Software.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *     THE SOFTWARE.
 */

package com.mnubo.platform.android.sdk.models.smartobjects;

import android.location.Location;
import android.os.Parcel;
import android.text.TextUtils;

import com.mnubo.platform.android.sdk.mocks.MockParcel;
import com.mnubo.platform.android.sdk.models.collections.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        TextUtils.class,
})
public class SmartObjectTest {

    private final Location mockedLocation = mock(Location.class);

    @Before
    public void setUp() throws Exception {
        mockStatic(TextUtils.class);
        when(TextUtils.isEmpty(any(CharSequence.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CharSequence a = (CharSequence) invocation.getArguments()[0];
                return a == null || a.length() == 0;
            }
        });

        when(mockedLocation.toString()).thenReturn("location");

    }

    @Test
    public void testWriteToParcel() throws Exception {
        SmartObject smartObject = new SmartObject();

        Parcel parcel = MockParcel.obtain();
        smartObject.writeToParcel(parcel, 0);

        SmartObject smartObjectFromParcel = SmartObject.CREATOR.createFromParcel(parcel);

        assertEquals(smartObject, smartObjectFromParcel);
    }

    @Test
    public void testWriteToParcelWithData() throws Exception {
        SmartObject smartObject = new SmartObject("deviceId", "owner", "objectModelName");
        smartObject.setAttribute("test", "test");
        Location location = mock(Location.class);
        smartObject.setRegistrationLocationWithLocation(location);
        smartObject.getCollections().add(new Collection());

        Parcel parcel = MockParcel.obtain();
        smartObject.writeToParcel(parcel, 0);

        SmartObject smartObjectFromParcel = SmartObject.CREATOR.createFromParcel(parcel);

        assertEquals(smartObject, smartObjectFromParcel);
    }
}