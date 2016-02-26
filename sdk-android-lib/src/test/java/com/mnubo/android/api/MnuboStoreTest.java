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

package com.mnubo.android.api;

import android.util.Log;

import com.mnubo.android.Mnubo;
import com.mnubo.android.api.MnuboStore.ReadEventsCallback;
import com.mnubo.android.api.MnuboStore.StoredEvents;
import com.mnubo.android.internal.writer.FileStore;
import com.mnubo.android.models.Event;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.cglib.core.CollectionUtils;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Log.class,
})
public class MnuboStoreTest {

    private MnuboStore testee;

    private FileStore store;
    private File file;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Log.class);
        store = mock(FileStore.class);
        file = mock(File.class);
        testee = new MnuboStore(file, 50, store);
    }

    @Test
    public void testWriteEvents_emptyList() throws Exception {
        List<Event> eventList = Collections.emptyList();
        boolean success = testee.writeEvents("deviceId", eventList);
    }

    @Test
    public void testWriteEvents() throws Exception {
        ArgumentCaptor<Serializable> toWrite = ArgumentCaptor.forClass(Serializable.class);
        List<Event> eventList = Collections.singletonList(Event.builder().eventType("et").build());
        when(store.write(eq("events"), eq(file), eq(50), toWrite.capture())).thenReturn(true);

        boolean success = testee.writeEvents("deviceId", eventList);

        verify(store, times(1)).write(eq("events"), eq(file), eq(50), any(Serializable.class));
        assertThat(success, equalTo(true));
        StoredEvents written = (StoredEvents) toWrite.getValue();
        assertThat(written.getDeviceId(), equalTo("deviceId"));
        assertThat(written.getEvents(), equalTo(eventList));

    }

    @Test
    public void testWriteEvents_cantWrite() throws Exception {
        ArgumentCaptor<Serializable> toWrite = ArgumentCaptor.forClass(Serializable.class);
        List<Event> eventList = Collections.singletonList(Event.builder().eventType("et").build());
        when(store.write(eq("events"), eq(file), eq(50), toWrite.capture())).thenReturn(false);

        boolean success = testee.writeEvents("deviceId", eventList);

        verify(store, times(1)).write(eq("events"), eq(file), eq(50), any(Serializable.class));
        assertThat(success, equalTo(false));
        StoredEvents written = (StoredEvents) toWrite.getValue();
        assertThat(written.getDeviceId(), equalTo("deviceId"));
        assertThat(written.getEvents(), equalTo(eventList));
    }

    @Test
    public void testReadEvents() throws Exception {
        File eventFolder = mock(File.class);
        File savedEvents = mock(File.class);
        ReadEventsCallback callback = mock(ReadEventsCallback.class);
        List<Event> eventList = Collections.singletonList(Event.builder().eventType("et").build());
        StoredEvents writtenEvents = new StoredEvents("deviceId", new ArrayList<>(eventList));

        when(eventFolder.listFiles()).thenReturn(new File[]{savedEvents});
        when(store.prepareFolder(eq("events"), eq(file), eq(50))).thenReturn(eventFolder);
        when(store.read(eq(savedEvents))).thenReturn(writtenEvents);

        testee.readEvents(callback);

        verify(callback, times(1)).process("deviceId", eventList);
    }

    @Test
    public void testReadEvents_errorPreparingFolder() throws Exception {
        ReadEventsCallback callback = mock(ReadEventsCallback.class);
        when(file.getPath()).thenReturn("/path");

        when(store.prepareFolder(eq("events"), eq(file), eq(50))).thenThrow(new IOException("oops"));

        testee.readEvents(callback);

        verifyZeroInteractions(callback);
    }

    @Test
    public void testReadEvents_multipleFiles() throws Exception {
        File eventFolder = mock(File.class);
        File savedEvents = mock(File.class);
        File savedEvents2 = mock(File.class);
        ReadEventsCallback callback = mock(ReadEventsCallback.class);
        List<Event> eventList = Collections.singletonList(Event.builder().eventType("et").build());
        List<Event> eventList2 = Collections.singletonList(Event.builder().eventType("et2").build());
        StoredEvents writtenEvents = new StoredEvents("deviceId", new ArrayList<>(eventList));
        StoredEvents writtenEvents2 = new StoredEvents("deviceId2", new ArrayList<>(eventList2));

        when(eventFolder.listFiles()).thenReturn(new File[]{savedEvents, savedEvents2});
        when(store.prepareFolder(eq("events"), eq(file), eq(50))).thenReturn(eventFolder);
        when(store.read(eq(savedEvents))).thenReturn(writtenEvents);
        when(store.read(eq(savedEvents2))).thenReturn(writtenEvents2);

        testee.readEvents(callback);

        verify(callback, times(1)).process("deviceId", eventList);
        verify(callback, times(1)).process("deviceId2", eventList2);
    }

    @Test
    public void testReadEvents_folderIsEmpty() throws Exception {
        File eventFolder = mock(File.class);
        ReadEventsCallback callback = mock(ReadEventsCallback.class);
        when(eventFolder.listFiles()).thenReturn(new File[]{});
        when(store.prepareFolder(eq("events"), eq(file), eq(50))).thenReturn(eventFolder);

        testee.readEvents(callback);
        verifyZeroInteractions(callback);
    }

    @Test
    public void testReadEvents_readReturnsNull() throws Exception {
        File eventFolder = mock(File.class);
        File savedEvents = mock(File.class);
        ReadEventsCallback callback = mock(ReadEventsCallback.class);

        when(eventFolder.listFiles()).thenReturn(new File[]{savedEvents});
        when(store.prepareFolder(eq("events"), eq(file), eq(50))).thenReturn(eventFolder);
        when(store.read(eq(savedEvents))).thenReturn(null);

        testee.readEvents(callback);

        verify(callback, times(1)).error(eq(savedEvents));
    }

    @Test
    public void testGetRootDir() throws Exception {
        File rootDir = testee.getRootDir();
        assertThat(rootDir, equalTo(file));

        File newRootDir = new File("/new");
        testee.setRootDir(newRootDir);
        File actualNewRootDir = testee.getRootDir();
        assertThat(actualNewRootDir, equalTo(newRootDir));
    }

    @Test
    public void testGetSizeLimit() throws Exception {
        int sizeLimit = testee.getSizeLimit();
        assertThat(sizeLimit, equalTo(50));

        testee.setSizeLimit(75);
        int newSizeLimit = testee.getSizeLimit();
        assertThat(newSizeLimit, equalTo(75));
    }
}