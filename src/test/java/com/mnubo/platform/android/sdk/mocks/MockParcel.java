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

package com.mnubo.platform.android.sdk.mocks;

import android.os.Parcel;
import android.os.Parcelable;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockParcel {

    public static Parcel obtain() {
        return new MockParcel().getMockedParcel();
    }

    private Parcel mockedParcel;
    private List<Object> objects;
    private int position;

    public Parcel getMockedParcel() {
        return mockedParcel;
    }

    public MockParcel() {
        mockedParcel = mock(Parcel.class);
        objects = new ArrayList<>();
        setupMock();
    }

    private void setupMock() {
        setupWrites();
        setupReads();
    }

    private void setupWrites() {
        Answer<Void> writeValueAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object parameter = invocation.getArguments()[0];
                objects.add(parameter);
                return null;
            }
        };
        doAnswer(writeValueAnswer).when(mockedParcel).writeLong(anyLong());
        doAnswer(writeValueAnswer).when(mockedParcel).writeParcelable(any(Parcelable.class), anyInt());
        doAnswer(writeValueAnswer).when(mockedParcel).writeString(anyString());
        doAnswer(writeValueAnswer).when(mockedParcel).writeMap(any(Map.class));
        doAnswer(writeValueAnswer).when(mockedParcel).writeDoubleArray(any(double[].class));
        doAnswer(writeValueAnswer).when(mockedParcel).writeList(anyList());
        doAnswer(writeValueAnswer).when(mockedParcel).writeInt(anyInt());
    }

    private void setupReads() {
        when(mockedParcel.readInt()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                final Integer value = (Integer) objects.get(position);
                position++;
                return value;
            }
        });
        when(mockedParcel.readLong()).thenAnswer(new Answer<Long>() {
            @Override
            public Long answer(InvocationOnMock invocation) throws Throwable {
                final Long value = (Long) objects.get(position);
                position++;
                return value;
            }
        });
        when(mockedParcel.readString()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                final String value = (String) objects.get(position);
                position++;
                return value;
            }
        });
        when(mockedParcel.readHashMap(any(ClassLoader.class))).thenAnswer(new Answer<Map<String, Object>>() {
            @Override
            @SuppressWarnings("unchecked")
            public Map<String, Object> answer(InvocationOnMock invocation) throws Throwable {
                final Map value = (Map) objects.get(position);
                position++;
                return value;
            }
        });
        when(mockedParcel.readParcelable(any(ClassLoader.class))).thenAnswer(new Answer<Parcelable>() {
            @Override
            public Parcelable answer(InvocationOnMock invocation) throws Throwable {
                final Parcelable value = (Parcelable) objects.get(position);
                position++;
                return value;
            }
        });
        when(mockedParcel.createDoubleArray()).thenAnswer(new Answer<double[]>() {
            @Override
            public double[] answer(InvocationOnMock invocation) throws Throwable {
                final double[] value = (double[]) objects.get(position);
                position++;
                return value;
            }
        });
        when(mockedParcel.readArrayList(any(ClassLoader.class))).thenAnswer(new Answer<ArrayList>() {
            @Override
            public ArrayList answer(InvocationOnMock invocation) throws Throwable {
                final ArrayList value = (ArrayList) objects.get(position);
                position++;
                return value;
            }
        });

    }
} 