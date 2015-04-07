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

import com.mnubo.platform.android.sdk.mocks.MockParcel;
import com.mnubo.platform.android.sdk.models.collections.Collection;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SmartObjectsTest {

    @Test
    public void testWriteToParcel() throws Exception {
        SmartObjects smartObjects = new SmartObjects();

        Parcel parcel = MockParcel.obtain();
        smartObjects.writeToParcel(parcel, 0);

        SmartObjects smartObjectsFromParcel = SmartObjects.CREATOR.createFromParcel(parcel);

        assertEquals(smartObjects, smartObjectsFromParcel);
    }

    @Test
    public void testWriteToParcelWithData() throws Exception {
        SmartObject smartObject = new SmartObject("deviceId", "owner", "objectModelName");
        smartObject.setAttribute("test", "test");
        Location location = mock(Location.class);
        smartObject.setRegistrationLocationWithLocation(location);
        smartObject.getCollections().add(new Collection());

        SmartObjects smartObjects = new SmartObjects();
        smartObjects.addObject(smartObject);

        Parcel parcel = MockParcel.obtain();
        smartObjects.writeToParcel(parcel, 0);

        SmartObjects smartObjectsFromParcel = SmartObjects.CREATOR.createFromParcel(parcel);

        assertEquals(smartObjects, smartObjectsFromParcel);
    }
}