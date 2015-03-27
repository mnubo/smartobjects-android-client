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

package com.mnubo.platform.android.sdk.api.operations.impl;

import com.mnubo.platform.android.sdk.api.operations.AbstractOperationsTest;
import com.mnubo.platform.android.sdk.internal.tasks.AsyncTaskFactory;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSampleOnPublicSensorTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSamplesTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.CreateObjectTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.FindObjectTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.SearchSamplesTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.UpdateObjectTask;
import com.mnubo.platform.android.sdk.api.services.cache.impl.MnuboSmartObjectFileCachingServiceImpl;
import com.mnubo.platform.android.sdk.internal.services.SmartObjectService;
import com.mnubo.platform.android.sdk.models.common.IdType;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;

import org.junit.Before;
import org.junit.Test;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static com.mnubo.platform.android.sdk.api.services.cache.MnuboFileCachingService.FailedAttemptCallback;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@SuppressWarnings("unchecked")
public class SmartObjectOperationsImplTest extends AbstractOperationsTest {

    private final SmartObjectService mockedSmartObjectService = mock(SmartObjectService.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<SmartObject> mockedSmartObjectCallback = mock(CompletionCallBack.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<Samples> mockedSamplesCallback = mock(CompletionCallBack.class);


    @SuppressWarnings("unchecked")
    private final CompletionCallBack<Boolean> mockedSuccessCallback = mock(CompletionCallBack.class);

    private final MnuboSmartObjectFileCachingServiceImpl mockedCacheService = mock(MnuboSmartObjectFileCachingServiceImpl.class);


    private SmartObjectOperationsImpl smartObjectOperations;



    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        when(mockedUserApiConnection.getApi()).thenReturn(mockedUserApi);
        when(mockedUserApi.objectService()).thenReturn(mockedSmartObjectService);

        smartObjectOperations = new SmartObjectOperationsImpl(mockedConnectionOperations, mockedClientApiConnection,
                mockedUserApiConnection, null);
        smartObjectOperations.setMnuboSmartObjectFileCachingService(mockedCacheService);
    }

    @Test
    public void testFindObject() throws Exception {
        when(AsyncTaskFactory.create(any(FindObjectTask.class), isNull(CompletionCallBack.class), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);

        smartObjectOperations.findObject(objectId, null);


    }

    @Test
    public void testFindObjectWithCallback() throws Exception {
        when(AsyncTaskFactory.create(any(FindObjectTask.class), eq(mockedSmartObjectCallback), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);

        smartObjectOperations.findObject(objectId, mockedSmartObjectCallback);


    }

    @Test
    public void testUpdate() throws Exception {
        when(AsyncTaskFactory.create(any(UpdateObjectTask.class), isNull(CompletionCallBack.class), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final SmartObject updatedObject = new SmartObject();

        smartObjectOperations.update(objectId, updatedObject, null);


    }

    @Test
    public void testUpdateWithCallback() throws Exception {
        when(AsyncTaskFactory.create(any(UpdateObjectTask.class), eq(mockedSuccessCallback), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final SmartObject updatedObject = new SmartObject();

        smartObjectOperations.update(objectId, updatedObject, mockedSuccessCallback);

    }

    @Test
    public void testSearchSamples() throws Exception {
        when(AsyncTaskFactory.create(any(SearchSamplesTask.class), isNull(CompletionCallBack.class), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final String sensorName = "sensorName";

        smartObjectOperations.searchSamples(objectId, sensorName, null);


    }

    @Test
    public void testSearchSamplesWithCallback() throws Exception {
        when(AsyncTaskFactory.create(any(SearchSamplesTask.class), eq(mockedSamplesCallback), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final String sensorName = "sensorName";

        smartObjectOperations.searchSamples(objectId, sensorName, mockedSamplesCallback);

    }

    @Test
    public void testAddSamples() throws Exception {
        when(AsyncTaskFactory.create(any(AddSamplesTask.class), isNull(CompletionCallBack.class), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final Samples samples = new Samples();

        smartObjectOperations.addSamples(objectId, samples, null);


    }

    @Test
    public void testAddSamplesWithCallback() throws Exception {
        when(AsyncTaskFactory.create(any(AddSamplesTask.class), eq(mockedSuccessCallback), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final Samples samples = new Samples();

        smartObjectOperations.addSamples(objectId, samples, mockedSuccessCallback);


    }

    @Test
    public void testAddSampleOnPublicSensor() throws Exception {
        when(AsyncTaskFactory.create(any(AddSampleOnPublicSensorTask.class), isNull(CompletionCallBack.class), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final String sensorName = "sensorName";
        final Sample sample = new Sample();

        smartObjectOperations.addSampleOnPublicSensor(objectId, sensorName, sample, null);


    }

    @Test
    public void testAddSampleOnPublicSensorWithCallback() throws Exception {
        when(AsyncTaskFactory.create(any(AddSampleOnPublicSensorTask.class), eq(mockedSuccessCallback), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final String sensorName = "sensorName";
        final Sample sample = new Sample();

        smartObjectOperations.addSampleOnPublicSensor(objectId, sensorName, sample, mockedSuccessCallback);


    }

    @Test
    public void testCreateObject() throws Exception {

        when(AsyncTaskFactory.create(any(CreateObjectTask.class), isNull(CompletionCallBack.class), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);
        final SmartObject object = new SmartObject();

        smartObjectOperations.createObject(object, true, null);

    }

    @Test
    public void testCreateObjectWithCallback() throws Exception {
        when(AsyncTaskFactory.create(any(CreateObjectTask.class), eq(mockedSuccessCallback), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final SmartObject object = new SmartObject();

        smartObjectOperations.createObject(object, true, mockedSuccessCallback);

    }
}