package com.mnubo.platform.android.sdk.api.operations.impl;

import com.mnubo.platform.android.sdk.api.operations.AbstractOperationsTest;
import com.mnubo.platform.android.sdk.internal.user.services.SmartObjectService;
import com.mnubo.platform.android.sdk.models.common.IdType;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;

import org.junit.Before;
import org.junit.Test;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SmartObjectOperationsImplTest extends AbstractOperationsTest {

    private final SmartObjectOperationsImpl smartObjectOperations = new SmartObjectOperationsImpl(mockedConnectionOperations, mockedClientApiConnection, mockedUserApiConnection);
    private final SmartObjectService mockedSmartObjectService = mock(SmartObjectService.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<SmartObject> mockedSmartObjectCallback = mock(CompletionCallBack.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<Samples> mockedSamplesCallback = mock(CompletionCallBack.class);


    @SuppressWarnings("unchecked")
    private final CompletionCallBack<Boolean> mockedSuccessCallback = mock(CompletionCallBack.class);

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        smartObjectOperations.setAsyncTaskFactory(mockedAsyncTaskFactory);

        when(mockedUserApiConnection.getApi()).thenReturn(mockedUserApi);
        when(mockedUserApi.objectService()).thenReturn(mockedSmartObjectService);
    }

    @Test
    public void testFindObject() throws Exception {

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);

        smartObjectOperations.findObject(objectId, null);

        verify(mockedUserApiConnection, only()).getApi();
        verify(mockedUserApi, only()).objectService();
        verify(mockedSmartObjectService, only()).findOne(objectId);

    }

    @Test
    public void testFindObjectWithCallback() throws Exception {

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);

        smartObjectOperations.findObject(objectId, mockedSmartObjectCallback);

        verify(mockedAsyncTask, only()).execute();

    }

    @Test
    public void testUpdate() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final SmartObject updatedObject = new SmartObject();

        smartObjectOperations.update(objectId, updatedObject, null);

        verify(mockedUserApiConnection, only()).getApi();
        verify(mockedUserApi, only()).objectService();
        verify(mockedSmartObjectService, only()).update(objectId, updatedObject);
    }

    @Test
    public void testUpdateWithCallback() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final SmartObject updatedObject = new SmartObject();

        smartObjectOperations.update(objectId, updatedObject, mockedSuccessCallback);

        verify(mockedAsyncTask, only()).execute();
    }

    @Test
    public void testSearchSamples() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final String sensorName = "sensorName";

        smartObjectOperations.searchSamples(objectId, sensorName, null);

        verify(mockedUserApiConnection, only()).getApi();
        verify(mockedUserApi, only()).objectService();
        verify(mockedSmartObjectService, only()).searchSamples(objectId, sensorName);
    }

    @Test
    public void testSearchSamplesWithCallback() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final String sensorName = "sensorName";

        smartObjectOperations.searchSamples(objectId, sensorName, mockedSamplesCallback);

        verify(mockedAsyncTask, only()).execute();
    }

    @Test
    public void testAddSamples() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final Samples samples = new Samples();

        smartObjectOperations.addSamples(objectId, samples, null);

        verify(mockedUserApiConnection, only()).getApi();
        verify(mockedUserApi, only()).objectService();
        verify(mockedSmartObjectService, only()).addSamples(objectId, samples);

    }

    @Test
    public void testAddSamplesWithCallback() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final Samples samples = new Samples();

        smartObjectOperations.addSamples(objectId, samples, mockedSuccessCallback);

        verify(mockedAsyncTask, only()).execute();

    }

    @Test
    public void testAddSampleOnPublicSensor() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final String sensorName = "sensorName";
        final Sample sample = new Sample();

        smartObjectOperations.addSampleOnPublicSensor(objectId, sensorName, sample, null);

        verify(mockedUserApiConnection, only()).getApi();
        verify(mockedUserApi, only()).objectService();
        verify(mockedSmartObjectService, only()).addSampleOnPublicSensor(objectId, sensorName, sample);

    }

    @Test
    public void testAddSampleOnPublicSensorWithCallback() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final String sensorName = "sensorName";
        final Sample sample = new Sample();

        smartObjectOperations.addSampleOnPublicSensor(objectId, sensorName, sample, mockedSuccessCallback);

        verify(mockedAsyncTask, only()).execute();

    }

    @Test
    public void testCreateObject() throws Exception {
        final SmartObject object = new SmartObject();

        smartObjectOperations.createObject(object, true, null);

        verify(mockedUserApiConnection, only()).getApi();
        verify(mockedUserApi, only()).objectService();
        verify(mockedSmartObjectService, only()).create(object, true);
    }

    @Test
    public void testCreateObjectWithCallback() throws Exception {
        final SmartObject object = new SmartObject();

        smartObjectOperations.createObject(object, true, mockedSuccessCallback);

        verify(mockedAsyncTask, only()).execute();
    }
}