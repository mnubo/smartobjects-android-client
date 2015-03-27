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

package com.mnubo.platform.android.sdk.api.store.impl;

import android.text.TextUtils;
import android.util.Log;

import com.mnubo.platform.android.sdk.api.store.MnuboEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;

import static com.mnubo.platform.android.sdk.api.store.MnuboEntity.EntityType.ADD_SAMPLES;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        TextUtils.class,
        Log.class,
        MnuboFileStore.class,
        ObjectInputStream.class
})
public class MnuboFileStoreTest {

    private File rootDir = mock(File.class);
    private File mockedFile = mock(File.class);


    private FileOutputStream mockedFos = mock(FileOutputStream.class);
    private FileInputStream mockedFis = mock(FileInputStream.class);

    private static final String QUEUE_NAME = "folder";

    private final MnuboFileStore stringStore = new MnuboFileStore(rootDir);

    @Before
    public void setUp() throws Exception {
        mockStatic(TextUtils.class);
        mockStatic(Log.class);

        when(TextUtils.equals(any(CharSequence.class), any(CharSequence.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CharSequence a = (CharSequence) invocation.getArguments()[0];
                CharSequence b = (CharSequence) invocation.getArguments()[1];
                if (a == b) return true;
                int length;
                if (a != null && b != null && (length = a.length()) == b.length()) {
                    if (a instanceof String && b instanceof String) {
                        return a.equals(b);
                    } else {
                        for (int i = 0; i < length; i++) {
                            if (a.charAt(i) != b.charAt(i)) return false;
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        when(TextUtils.isEmpty(any(CharSequence.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CharSequence str = (CharSequence) invocation.getArguments()[0];
                return str == null || str.length() == 0;
            }
        });

        whenNew(File.class).withAnyArguments().thenReturn(mockedFile);
        whenNew(FileOutputStream.class).withAnyArguments().thenReturn(mockedFos);
        whenNew(FileInputStream.class).withAnyArguments().thenReturn(mockedFis);

        when(mockedFile.getAbsolutePath()).thenReturn("/tmp");
    }

    @Test
    public void testPut() throws Exception {
        when(mockedFile.exists()).thenReturn(false);
        when(mockedFile.mkdirs()).thenReturn(true);
        when(mockedFile.createNewFile()).thenReturn(true);

        String id = "id";
        String idType = "idType";
        String data = "data";

        final Boolean success = stringStore.put(QUEUE_NAME, new MnuboEntity(ADD_SAMPLES, id, null, "data"));
        assertThat(success, is(equalTo(true)));
    }

    @Test
    public void testPutMkDirsFailed() throws Exception {
        when(mockedFile.exists()).thenReturn(false);
        when(mockedFile.mkdirs()).thenReturn(false);


        String id = "id";
        String idType = "idType";
        String data = "data";

        final Boolean success = stringStore.put(QUEUE_NAME, new MnuboEntity(ADD_SAMPLES, id, null, "data"));
        assertThat(success, is(equalTo(false)));
    }

    @Test
    public void testPutCreateFileFails() throws Exception {
        when(mockedFile.exists()).thenReturn(false);
        when(mockedFile.mkdirs()).thenReturn(true);
        when(mockedFile.createNewFile()).thenReturn(false);

        String id = "id";
        String idType = "idType";
        String data = "data";

        final Boolean success = stringStore.put(QUEUE_NAME, new MnuboEntity(ADD_SAMPLES, id, null, "data"));
        assertThat(success, is(equalTo(false)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullQueueName() throws Exception {
        String id = "id";
        String idType = "idType";
        String data = "data";

        stringStore.put(null, new MnuboEntity(ADD_SAMPLES, id, null, "data"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyQueueName() throws Exception {
        String id = "id";
        String idType = "idType";
        String data = "data";

        stringStore.put("", new MnuboEntity(ADD_SAMPLES, id, null, "data"));
    }

    @Test
    public void testRemove() throws Exception {
        when(mockedFile.delete()).thenReturn(true);

        MnuboFileEntity entity = new MnuboFileEntity(new MnuboEntity(ADD_SAMPLES, "id", null, "data"), "location");
        final boolean success = stringStore.remove(entity);
        assertThat(success, is(equalTo(true)));
    }

    @Test
    public void testRemoveDeleteNotWorking() throws Exception {
        when(mockedFile.delete()).thenReturn(false);

        MnuboFileEntity entity = new MnuboFileEntity(new MnuboEntity(ADD_SAMPLES, "id", null, "data"), "location");
        final boolean success = stringStore.remove(entity);
        assertThat(success, is(equalTo(false)));
    }

    @Test
    public void testRemoveInvalidType() throws Exception {

        MnuboEntity entity = new DummyMnuboEntity("id", "value");
        final boolean success = stringStore.remove(entity);
        assertThat(success, is(equalTo(false)));
    }

    @Test
    public void testGetQueue() throws Exception {
        when(mockedFile.exists()).thenReturn(false);
        when(mockedFile.mkdirs()).thenReturn(true);
        when(mockedFile.createNewFile()).thenReturn(true);

        File mockedFile1 = mock(File.class);
        File mockedFile2 = mock(File.class);
        File[] fileList = new File[]{mockedFile1, mockedFile2};
        when(mockedFile.listFiles()).thenReturn(fileList);

        //unable to mock objectinputstream
        //List<MnuboEntity> entityList = stringStore.getEntities("folder");


    }

    private class DummyMnuboEntity extends MnuboEntity {
        private DummyMnuboEntity(String id, String value) {
            super(ADD_SAMPLES, id, null, value);
        }
    }
}