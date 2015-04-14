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

package com.mnubo.platform.android.sdk.api.services.buffer.impl;

import android.util.Log;

import com.mnubo.platform.android.sdk.api.store.MnuboEntity;
import com.mnubo.platform.android.sdk.api.store.impl.MnuboFileDataStore;
import com.mnubo.platform.android.sdk.internal.connect.connection.MnuboConnectionManager;
import com.mnubo.platform.android.sdk.internal.tasks.TaskFactory;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSampleOnPublicSensorTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSamplesTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Log.class,
        TaskFactory.class
})
@SuppressWarnings("unchecked")
public class MnuboSmartObjectFileCachingService {

    private MnuboFileDataStore mockedMnuboDataStore = mock(MnuboFileDataStore.class);
    private MnuboConnectionManager mockedConnectionManager = mock(MnuboConnectionManager.class);

    private MnuboBufferServiceImpl mnuboSmartObjectFileCachingService;
    private AddSamplesTask addSamplesTask = mock(AddSamplesTask.class);
    private AddSampleOnPublicSensorTask addSampleOnPublicSensorTask = mock(AddSampleOnPublicSensorTask.class);

    @Before
    public void setUp() throws Exception {
        mockStatic(Log.class);
        mockStatic(TaskFactory.class);

        mnuboSmartObjectFileCachingService = new MnuboBufferServiceImpl(mockedMnuboDataStore, mockedConnectionManager);


    }

    @Test
    public void testRetryFailedAttempts() throws Exception {
        final MnuboEntity mnuboEntity1 = new MnuboEntity(addSamplesTask);
        final MnuboEntity mnuboEntity2 = new MnuboEntity(addSamplesTask);
        final MnuboEntity mnuboEntity3 = new MnuboEntity(addSampleOnPublicSensorTask);

        final List<MnuboEntity> entities = Arrays.asList(mnuboEntity1, mnuboEntity2, mnuboEntity3);

        when(mockedMnuboDataStore.getEntities("failed")).thenReturn(entities);

        mnuboSmartObjectFileCachingService.retryFailedAttempts();

        verify(addSamplesTask, times(2)).executeSync(eq(mockedConnectionManager), eq(mnuboSmartObjectFileCachingService.getFailedAttemptCallback()));
        verify(addSampleOnPublicSensorTask, times(1)).executeSync(eq(mockedConnectionManager), eq(mnuboSmartObjectFileCachingService.getFailedAttemptCallback()));

        verify(mockedMnuboDataStore).remove(mnuboEntity1);
        verify(mockedMnuboDataStore).remove(mnuboEntity2);
        verify(mockedMnuboDataStore).remove(mnuboEntity3);
    }

    @Test
    public void testGetFailedAttemptsCount() throws Exception {

        when(mockedMnuboDataStore.getEntities("failed")).thenReturn(Arrays.asList(new MnuboEntity(), new MnuboEntity()));

        assertThat(mnuboSmartObjectFileCachingService.getFailedAttemptsCount(), is(equalTo(2)));

    }

    @Test
    public void testGetFailedAttemptsCountEmpty() throws Exception {

        when(mockedMnuboDataStore.getEntities("failed")).thenReturn(Collections.EMPTY_LIST);

        assertThat(mnuboSmartObjectFileCachingService.getFailedAttemptsCount(), is(equalTo(0)));

    }

}