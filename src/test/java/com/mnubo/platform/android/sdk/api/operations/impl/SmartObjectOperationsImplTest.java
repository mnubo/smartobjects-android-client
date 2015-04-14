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
import com.mnubo.platform.android.sdk.api.services.cache.impl.MnuboSmartObjectFileCachingServiceImpl;
import com.mnubo.platform.android.sdk.internal.services.SmartObjectService;
import com.mnubo.platform.android.sdk.internal.tasks.MnuboResponse;
import com.mnubo.platform.android.sdk.internal.tasks.TaskFactory;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSampleOnPublicSensorTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSamplesTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.CreateObjectTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.DeleteObjectTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.FindObjectTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.SearchSamplesTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.UpdateObjectTask;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;

import org.junit.Before;
import org.junit.Test;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static com.mnubo.platform.android.sdk.models.common.IdType.deviceid;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@SuppressWarnings("unchecked")
public class SmartObjectOperationsImplTest extends AbstractOperationsTest {

    private final SmartObjectService mockedSmartObjectService = mock(SmartObjectService.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<SmartObject> mockedSmartObjectCallback = mock(CompletionCallBack.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<Samples> mockedSamplesCallback = mock(CompletionCallBack.class);


    private final MnuboSmartObjectFileCachingServiceImpl mockedCacheService = mock(MnuboSmartObjectFileCachingServiceImpl.class);


    private SmartObjectOperationsImpl smartObjectOperations;


    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        when(mockedMnuboSDKApi.objectService()).thenReturn(mockedSmartObjectService);

        smartObjectOperations = new SmartObjectOperationsImpl(mockedConnectionManager, null, false);
        smartObjectOperations.setMnuboSmartObjectFileCachingService(mockedCacheService);
    }

    @Test
    public void testSyncFindObject() throws Exception {
        final SdkId objectId = SdkId.build("object-id", deviceid);

        final SmartObject expectedResult = new SmartObject();
        final FindObjectTask mockedTask = mock(FindObjectTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(expectedResult, null));
        when(TaskFactory.newFindObjectTask(eq(mockedRefreshableConnection), eq(objectId))).thenReturn(mockedTask);

        final SmartObject result = smartObjectOperations.findObject(objectId).getResult();

        assertEquals(expectedResult, result);
        verify(mockedTask, only()).executeSync();
    }

    @Test
    public void testAsyncFindObject() throws Exception {
        final SdkId objectId = SdkId.build("object-id", deviceid);

        final FindObjectTask mockedTask = mock(FindObjectTask.class);
        when(TaskFactory.newFindObjectTask(eq(mockedRefreshableConnection), eq(objectId))).thenReturn(mockedTask);

        smartObjectOperations.findObjectAsync(objectId, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));
    }

    @Test
    public void testAsyncFindObjectWithCallback() throws Exception {
        final SdkId objectId = SdkId.build("object-id", deviceid);

        final FindObjectTask mockedTask = mock(FindObjectTask.class);
        when(TaskFactory.newFindObjectTask(eq(mockedRefreshableConnection), eq(objectId))).thenReturn(mockedTask);

        smartObjectOperations.findObjectAsync(objectId, mockedSmartObjectCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSmartObjectCallback));
    }

    @Test
    public void testSyncUpdate() throws Exception {
        final SdkId objectId = SdkId.build("object-id", deviceid);
        final SmartObject updatedObject = new SmartObject();

        final UpdateObjectTask mockedTask = mock(UpdateObjectTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newUpdateObjectTask(eq(mockedRefreshableConnection), eq(objectId), eq(updatedObject))).thenReturn(mockedTask);

        final Boolean result = smartObjectOperations.update(objectId, updatedObject).getResult();

        assertEquals(true, result);
        verify(mockedTask, only()).executeSync();

    }

    @Test
    public void testAsyncUpdate() throws Exception {
        final SdkId objectId = SdkId.build("object-id", deviceid);
        final SmartObject updatedObject = new SmartObject();

        final UpdateObjectTask mockedTask = mock(UpdateObjectTask.class);
        when(TaskFactory.newUpdateObjectTask(eq(mockedRefreshableConnection), eq(objectId), eq(updatedObject))).thenReturn(mockedTask);

        smartObjectOperations.updateAsync(objectId, updatedObject, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));

    }

    @Test
    public void testAsyncUpdateWithCallback() throws Exception {
        final SdkId objectId = SdkId.build("object-id", deviceid);
        final SmartObject updatedObject = new SmartObject();

        final UpdateObjectTask mockedTask = mock(UpdateObjectTask.class);
        when(TaskFactory.newUpdateObjectTask(eq(mockedRefreshableConnection), eq(objectId), eq(updatedObject))).thenReturn(mockedTask);

        smartObjectOperations.updateAsync(objectId, updatedObject, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSuccessCallback));
    }

    @Test
    public void testSyncSearchSamples() throws Exception {

        final SdkId objectId = SdkId.build("object-id", deviceid);
        final String sensorName = "sensorName";

        final Samples expectedResult = new Samples();
        final SearchSamplesTask mockedTask = mock(SearchSamplesTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(expectedResult, null));
        when(TaskFactory.newSearchSamplesTask(eq(mockedRefreshableConnection), eq(objectId), eq(sensorName))).thenReturn(mockedTask);


        Samples result = smartObjectOperations.searchSamples(objectId, sensorName).getResult();

        assertEquals(expectedResult, result);
        verify(mockedTask, only()).executeSync();
    }

    @Test
    public void testAsyncSearchSamples() throws Exception {

        final SdkId objectId = SdkId.build("object-id", deviceid);
        final String sensorName = "sensorName";

        final SearchSamplesTask mockedTask = mock(SearchSamplesTask.class);
        when(TaskFactory.newSearchSamplesTask(eq(mockedRefreshableConnection), eq(objectId), eq(sensorName))).thenReturn(mockedTask);

        smartObjectOperations.searchSamplesAsync(objectId, sensorName, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));
    }

    @Test
    public void testAsyncSearchSamplesWithCallback() throws Exception {

        final SdkId objectId = SdkId.build("object-id", deviceid);
        final String sensorName = "sensorName";

        final SearchSamplesTask mockedTask = mock(SearchSamplesTask.class);
        when(TaskFactory.newSearchSamplesTask(eq(mockedRefreshableConnection), eq(objectId), eq(sensorName))).thenReturn(mockedTask);

        smartObjectOperations.searchSamplesAsync(objectId, sensorName, mockedSamplesCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSamplesCallback));


    }

    @Test
    public void testSyncAddSamples() throws Exception {
        final SdkId objectId = SdkId.build("object-id", deviceid);
        final Samples samples = new Samples();

        final AddSamplesTask mockedTask = mock(AddSamplesTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newAddSamplesTask(eq(mockedRefreshableConnection), eq(objectId), eq(samples))).thenReturn(mockedTask);


        Boolean result = smartObjectOperations.addSamples(objectId, samples).getResult();

        assertEquals(true, result);
        verify(mockedTask, only()).executeSync();
    }

    @Test
    public void testAsyncAddSamples() throws Exception {

        final SdkId objectId = SdkId.build("object-id", deviceid);
        final Samples samples = new Samples();

        final AddSamplesTask mockedTask = mock(AddSamplesTask.class);
        when(TaskFactory.newAddSamplesTask(eq(mockedRefreshableConnection), eq(objectId), eq(samples))).thenReturn(mockedTask);

        smartObjectOperations.addSamplesAsync(objectId, samples, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));

    }

    @Test
    public void testAsyncAddSamplesWithCallback() throws Exception {

        final SdkId objectId = SdkId.build("object-id", deviceid);
        final Samples samples = new Samples();

        final AddSamplesTask mockedTask = mock(AddSamplesTask.class);
        when(TaskFactory.newAddSamplesTask(eq(mockedRefreshableConnection), eq(objectId), eq(samples))).thenReturn(mockedTask);

        smartObjectOperations.addSamplesAsync(objectId, samples, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSuccessCallback));


    }

    @Test
    public void testSyncAddSample() throws Exception {
        final SdkId objectId = SdkId.build("object-id", deviceid);
        final Samples expectedSamples = new Samples();
        final Sample singleSample = new Sample();
        expectedSamples.addSample(singleSample);

        final AddSamplesTask mockedTask = mock(AddSamplesTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newAddSamplesTask(eq(mockedRefreshableConnection), eq(objectId), eq(expectedSamples))).thenReturn(mockedTask);


        Boolean result = smartObjectOperations.addSample(objectId, singleSample).getResult();

        assertEquals(true, result);
        verify(mockedTask, only()).executeSync();
    }

    @Test
    public void testAsyncAddSample() throws Exception {

        final SdkId objectId = SdkId.build("object-id", deviceid);
        final Samples expectedSamples = new Samples();
        final Sample singleSample = new Sample();
        expectedSamples.addSample(singleSample);

        final AddSamplesTask mockedTask = mock(AddSamplesTask.class);
        when(TaskFactory.newAddSamplesTask(eq(mockedRefreshableConnection), eq(objectId), eq(expectedSamples))).thenReturn(mockedTask);

        smartObjectOperations.addSampleAsync(objectId, singleSample, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));

    }

    @Test
    public void testAsyncAddSampleWithCallback() throws Exception {

        final SdkId objectId = SdkId.build("object-id", deviceid);
        final Sample singleSample = new Sample();
        final Samples expectedSamples = new Samples();
        expectedSamples.addSample(singleSample);

        final AddSamplesTask mockedTask = mock(AddSamplesTask.class);
        when(TaskFactory.newAddSamplesTask(eq(mockedRefreshableConnection), eq(objectId), eq(expectedSamples))).thenReturn(mockedTask);

        smartObjectOperations.addSampleAsync(objectId, singleSample, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSuccessCallback));


    }

    @Test
    public void testSyncAddSampleOnPublicSensor() throws Exception {
        final SdkId objectId = SdkId.build("object-id", deviceid);
        final Sample sample = new Sample();
        final String sensorName = "sensorName";

        final AddSampleOnPublicSensorTask mockedTask = mock(AddSampleOnPublicSensorTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newAddSamplesOnPublicSensorTask(eq(mockedRefreshableConnection), eq(objectId), eq(sensorName), eq(sample))).thenReturn(mockedTask);

        Boolean result = smartObjectOperations.addSampleOnPublicSensor(objectId, sensorName, sample).getResult();

        assertEquals(true, result);
        verify(mockedTask, only()).executeSync();
    }

    @Test
    public void testAsyncAddSampleOnPublicSensor() throws Exception {
        final SdkId objectId = SdkId.build("object-id", deviceid);
        final String sensorName = "sensorName";
        final Sample sample = new Sample();

        final AddSampleOnPublicSensorTask mockedTask = mock(AddSampleOnPublicSensorTask.class);
        when(TaskFactory.newAddSamplesOnPublicSensorTask(eq(mockedRefreshableConnection), eq(objectId), eq(sensorName), eq(sample))).thenReturn(mockedTask);

        smartObjectOperations.addSampleOnPublicSensorAsync(objectId, sensorName, sample, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));
    }

    @Test
    public void testAsyncAddSampleOnPublicSensorWithCallback() throws Exception {
        final SdkId objectId = SdkId.build("object-id", deviceid);
        final String sensorName = "sensorName";
        final Sample sample = new Sample();

        final AddSampleOnPublicSensorTask mockedTask = mock(AddSampleOnPublicSensorTask.class);
        when(TaskFactory.newAddSamplesOnPublicSensorTask(eq(mockedRefreshableConnection), eq(objectId), eq(sensorName), eq(sample))).thenReturn(mockedTask);

        smartObjectOperations.addSampleOnPublicSensorAsync(objectId, sensorName, sample, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSuccessCallback));

    }

    @Test
    public void testSyncCreateObject() throws Exception {
        final SmartObject object = new SmartObject();


        final CreateObjectTask mockedTask = mock(CreateObjectTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newCreateObjectTask(eq(mockedRefreshableConnection), eq(object), eq(true))).thenReturn(mockedTask);


        Boolean result = smartObjectOperations.createObject(object, true).getResult();

        assertEquals(true, result);
        verify(mockedTask, only()).executeSync();

    }

    @Test
    public void testAsyncCreateObject() throws Exception {
        final SmartObject object = new SmartObject();

        final CreateObjectTask mockedTask = mock(CreateObjectTask.class);
        when(TaskFactory.newCreateObjectTask(eq(mockedRefreshableConnection), eq(object), eq(true))).thenReturn(mockedTask);

        smartObjectOperations.createObjectAsync(object, true, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));

    }

    @Test
    public void testAsyncCreateObjectWithCallback() throws Exception {

        final SmartObject object = new SmartObject();

        final CreateObjectTask mockedTask = mock(CreateObjectTask.class);
        when(TaskFactory.newCreateObjectTask(eq(mockedRefreshableConnection), eq(object), eq(true))).thenReturn(mockedTask);

        smartObjectOperations.createObjectAsync(object, true, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSuccessCallback));

    }

    @Test
    public void testSyncDeleteObject() throws Exception {
        final SdkId id = SdkId.build("id", deviceid);


        final DeleteObjectTask mockedTask = mock(DeleteObjectTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newDeleteObjectTask(eq(mockedRefreshableConnection), eq(id))).thenReturn(mockedTask);


        Boolean result = smartObjectOperations.deleteObject(id).getResult();

        assertEquals(true, result);
        verify(mockedTask, only()).executeSync();

    }

    @Test
    public void testAsyncDeleteObject() throws Exception {
        final SdkId id = SdkId.build("id", deviceid);

        final DeleteObjectTask mockedTask = mock(DeleteObjectTask.class);
        when(TaskFactory.newDeleteObjectTask(eq(mockedRefreshableConnection), eq(id))).thenReturn(mockedTask);


        smartObjectOperations.deleteObjectAsync(id, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));

    }

    @Test
    public void testAsyncDeleteObjectWithCallback() throws Exception {
        final SdkId id = SdkId.build("id", deviceid);

        final DeleteObjectTask mockedTask = mock(DeleteObjectTask.class);
        when(TaskFactory.newDeleteObjectTask(eq(mockedRefreshableConnection), eq(id))).thenReturn(mockedTask);


        smartObjectOperations.deleteObjectAsync(id, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSuccessCallback));

    }
}