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

package com.mnubo.platform.android.sdk.api.services.cache.impl;

import android.util.Log;

import com.mnubo.platform.android.sdk.api.store.MnuboEntity;
import com.mnubo.platform.android.sdk.api.store.impl.MnuboFileStore;
import com.mnubo.platform.android.sdk.internal.services.SmartObjectService;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;
import com.mnubo.platform.android.sdk.models.common.IdType;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mnubo.platform.android.sdk.api.store.MnuboEntity.EntityType;
import static com.mnubo.platform.android.sdk.internal.tasks.Task.ApiFetcher;
import static com.mnubo.platform.android.sdk.internal.tasks.impl.TaskWithRefreshImpl.ConnectionRefresher;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Log.class
})
@SuppressWarnings("unchecked")
public class MnuboSmartObjectFileCachingService {

    private MnuboFileStore mockedStore = mock(MnuboFileStore.class);
    private File mockedFile = mock(File.class);
    private ApiFetcher mockedApiFetcher = mock(ApiFetcher.class);
    private MnuboUserApi mockedUserApi = mock(MnuboUserApi.class);
    private SmartObjectService smartObjectService = mock(SmartObjectService.class);
    private ConnectionRefresher mockedConnectionRefresher = mock(ConnectionRefresher.class);

    private MnuboSmartObjectFileCachingServiceImpl mnuboSmartObjectFileCachingService;

    @Before
    public void setUp() throws Exception {
        mockStatic(Log.class);

        when(mockedUserApi.objectService()).thenReturn(smartObjectService);
        when(mockedApiFetcher.getMnuboUserApi()).thenReturn(mockedUserApi);


        mnuboSmartObjectFileCachingService = new MnuboSmartObjectFileCachingServiceImpl(mockedFile, mockedConnectionRefresher, mockedApiFetcher);
        mnuboSmartObjectFileCachingService.setMnuboStore(mockedStore);

    }

    @Test
    public void testRetryFailedAttempts() throws Exception {
        Map<String, Object> idData1 = new HashMap<>();
        final SdkId id1 = SdkId.build("id", IdType.objectid);
        idData1.put("id", id1);

        Map<String, Object> idData2 = new HashMap<>();
        final SdkId id2 = SdkId.build("id2", IdType.objectid);
        idData2.put("id", id2);

        final Samples submittedSamples = new Samples();

        final MnuboEntity mnuboEntity1 = new MnuboEntity(EntityType.ADD_SAMPLES, id1.getId(), idData1, submittedSamples);
        final MnuboEntity mnuboEntity2 = new MnuboEntity(EntityType.ADD_SAMPLES, id2.getId(), idData2, submittedSamples);

        final List<MnuboEntity> entities = Arrays.asList(mnuboEntity1, mnuboEntity2);

        when(mockedStore.getEntities("failed")).thenReturn(entities);

        mnuboSmartObjectFileCachingService.retryFailedAttempts();

        verify(smartObjectService).addSamples(eq(id1), eq(submittedSamples));
        verify(smartObjectService).addSamples(eq(id2), eq(submittedSamples));

        verify(mockedStore).remove(mnuboEntity1);
        verify(mockedStore).remove(mnuboEntity2);


    }

    @Test
    public void testGetFailedAttemptsCount() throws Exception {

        when(mockedStore.getEntities("failed")).thenReturn(Arrays.asList(new MnuboEntity(), new MnuboEntity()));

        assertThat(mnuboSmartObjectFileCachingService.getFailedAttemptsCount(), is(equalTo(2)));

    }

    @Test
    public void testGetFailedAttemptsCountEmpty() throws Exception {

        when(mockedStore.getEntities("failed")).thenReturn(Collections.EMPTY_LIST);

        assertThat(mnuboSmartObjectFileCachingService.getFailedAttemptsCount(), is(equalTo(0)));

    }

}