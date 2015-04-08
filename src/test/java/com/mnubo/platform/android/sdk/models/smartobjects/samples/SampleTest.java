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

package com.mnubo.platform.android.sdk.models.smartobjects.samples;

import android.location.Location;
import android.os.Parcel;

import com.mnubo.platform.android.sdk.mocks.MockParcel;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SampleTest {

    @Test
    public void testWriteToParcel() throws Exception {
        Sample sample = new Sample();

        Parcel parcel = MockParcel.obtain();
        sample.writeToParcel(parcel, 0);

        Sample sampleFromParcel = Sample.CREATOR.createFromParcel(parcel);

        assertEquals(sample, sampleFromParcel);
    }

    @Test
    public void testWriteToParcelWithData() throws Exception {
        Sample sample = new Sample();
        sample.setSensorName("name");
        final List<Double> coordinates = new ArrayList<>();
        coordinates.add(1d);
        sample.setCoordinates(coordinates);
        sample.setTimestamp("timestamp");
        final HashMap<String, Object> values = new HashMap<>();
        values.put("ket", "value");
        sample.setValue(values);

        Parcel parcel = MockParcel.obtain();
        sample.writeToParcel(parcel, 0);

        Sample sampleFromParcel = Sample.CREATOR.createFromParcel(parcel);

        assertEquals(sample, sampleFromParcel);
    }

    @Test
    public void testSetLocation() throws Exception {
        Sample sample = new Sample();
        final Location location = mock(Location.class);
        final double latitude = 1.0d;
        when(location.getLatitude()).thenReturn(latitude);
        final double longitude = 2.0d;
        when(location.getLongitude()).thenReturn(longitude);

        sample.setLocation(location);

        assertThat(sample.getCoordinates().size(), is(equalTo(2)));

        assertThat(sample.getLatitude(), is(equalTo(latitude)));
        assertThat(sample.getLongitude(), is(equalTo(longitude)));

    }

    @Test
    public void testSetLocationWithAltidude() throws Exception {
        Sample sample = new Sample();
        final Location location = mock(Location.class);
        final double latitude = 1.0d;
        when(location.getLatitude()).thenReturn(latitude);
        final double longitude = 2.0d;
        when(location.getLongitude()).thenReturn(longitude);
        final double altitude = 3.0d;
        when(location.getAltitude()).thenReturn(altitude);

        sample.setLocation(location);

        assertThat(sample.getCoordinates().size(), is(equalTo(3)));

        assertThat(sample.getLatitude(), is(equalTo(latitude)));
        assertThat(sample.getLongitude(), is(equalTo(longitude)));
        assertThat(sample.getAltitude(), is(equalTo(altitude)));
    }
}