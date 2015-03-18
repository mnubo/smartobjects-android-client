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
package com.mnubo.platform.android.sdk.internal.user.services.mocked;

import com.mnubo.platform.android.sdk.internal.MockedAbstractServiceTest;
import com.mnubo.platform.android.sdk.internal.user.services.SmartObjectService;
import com.mnubo.platform.android.sdk.internal.user.services.impl.SmartObjectServiceImpl;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.common.ValueType;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;
import com.mnubo.platform.android.sdk.models.users.Users;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class SmartObjectsOperationImplTestMocked extends MockedAbstractServiceTest {

    private SmartObjectService smartObjectService;


    @Before
    public void setUp() throws Exception {
        super.setUp();
        smartObjectService = new SmartObjectServiceImpl(PLATFORM_BASE_URL, mockedRestTemplate);
    }

    @Test
    public void createOrUpdateUserObjectTest() throws Exception {

        String calledUrl = expectedUrl("/objects?update_if_exists=true");
        when(mockedRestTemplate.postForObject(calledUrl, expectedSmartObject, SmartObject.class)).thenReturn(expectedSmartObject);

        smartObjectService.create(expectedSmartObject, true);
        verify(mockedRestTemplate, only()).postForObject(calledUrl, expectedSmartObject, SmartObject.class);


    }

    @Test
    public void updateUserObjectTest() throws Exception {

        final SdkId objecSdkId = SdkId.withUuid(expectedSmartObject.getObjectId());
        String calledUrl = expectedUrl("/objects/" + objecSdkId.getId() + "?id_type=" + objecSdkId.getIdType());

        smartObjectService.update(objecSdkId, expectedSmartObject);

        verify(mockedRestTemplate, only()).put(calledUrl, expectedSmartObject);

    }

    @Test
    public void deleteObjectTest() throws Exception {

        String calledUrl = expectedUrl("/objects/objectid?id_type=objectid");

        smartObjectService.delete(SdkId.valueOf("objectid"));

        verify(mockedRestTemplate, only()).delete(calledUrl);
    }

    @Test
    public void getObjectTest() throws Exception {

        String calledUrl = expectedUrl("/objects/objectid?id_type=objectid");
        when(mockedRestTemplate.getForObject(calledUrl, SmartObject.class)).thenReturn(expectedSmartObject);

        SmartObject smartObject = smartObjectService.findOne(SdkId.valueOf("objectid"));

        verify(mockedRestTemplate, only()).getForObject(calledUrl, SmartObject.class);
    }

    @Test
    public void getObjectWithAttributesTest() throws Exception {

        String calledUrl = expectedUrl("/objects/objectid?id_type=objectid&attributes=attributes1&attributes=attributes2");
        when(mockedRestTemplate.getForObject(calledUrl, SmartObject.class)).thenReturn(expectedSmartObject);

        List<String> attributes = Arrays.asList("attributes1", "attributes2");
        SmartObject smartObject = smartObjectService.findOne(SdkId.valueOf("objectid"), attributes);

        verify(mockedRestTemplate, only()).getForObject(calledUrl, SmartObject.class);
    }

    @Test
    public void getObjectOwnerHistory() throws Exception {


        String calledUrl = expectedUrl("/objects/objectid/owners_history?id_type=objectid&details=false");
        when(mockedRestTemplate.getForObject(calledUrl, Users.class)).thenReturn(expectedUsers);

        Users users = smartObjectService.listOwnersHistory(SdkId.valueOf("objectid"));
        verify(mockedRestTemplate, only()).getForObject(calledUrl, Users.class);
    }

    @Test
    public void getObjectOwnerHistoryWithDetail() throws Exception {
        String calledUrl = expectedUrl("/objects/objectid/owners_history?id_type=objectid&details=true");
        when(mockedRestTemplate.getForObject(calledUrl, Users.class)).thenReturn(expectedUsers);

        Users users = smartObjectService.listOwnersHistory(SdkId.valueOf("objectid"), true);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, Users.class);
    }

    @Test
    public void getObjectSamples() throws Exception {
        final Samples expectedSamples = new Samples();

        String calledUrl = expectedUrl("/objects/objectid/sensors/sensorName/samples?id_type=objectid");
        when(mockedRestTemplate.getForObject(calledUrl, Samples.class)).thenReturn(expectedSamples);

        Samples samples = smartObjectService.searchSamples(SdkId.valueOf("objectid"), "sensorName");
        verify(mockedRestTemplate, only()).getForObject(calledUrl, Samples.class);
    }

    @Test
    public void getObjectSamplesWithValueType() throws Exception {

        String calledUrl = expectedUrl("/objects/objectid/sensors/sensorName/samples?id_type=objectid&value=last");
        when(mockedRestTemplate.getForObject(calledUrl, Samples.class)).thenReturn(expectedSamples);

        Samples samples = smartObjectService.searchSamples(SdkId.valueOf("objectid"), "sensorName", ValueType.last);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, Samples.class);
    }

    @Test
    public void getObjectSamplesWithValueTypeWithTimeRange() throws Exception {

        String calledUrl = expectedUrl("/objects/objectid/sensors/sensorName/samples?id_type=objectid&value=last&from=from&to=to");
        when(mockedRestTemplate.getForObject(calledUrl, Samples.class)).thenReturn(expectedSamples);

        Samples samples = smartObjectService.searchSamples(SdkId.valueOf("objectid"), "sensorName", ValueType.last, "from", "to");
        verify(mockedRestTemplate, only()).getForObject(calledUrl, Samples.class);
    }

    @Test
    public void getObjectSamplesWithValueTypeWithTimeRangeAndLimit() throws Exception {

        String calledUrl = expectedUrl("/objects/objectid/sensors/sensorName/samples?id_type=objectid&value=last&from=from&to=to&limit=10");

        when(mockedRestTemplate.getForObject(calledUrl, Samples.class)).thenReturn(expectedSamples);

        Samples samples = smartObjectService.searchSamples(SdkId.valueOf("objectid"), "sensorName", ValueType.last, "from", "to", 10);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, Samples.class);
    }

    @Test
    public void addSamplesForObject() throws Exception {

        String calledUrl = expectedUrl("/objects/objectid/samples?id_type=objectid");

        smartObjectService.addSamples(SdkId.valueOf("objectid"), expectedSamples);
        verify(mockedRestTemplate, only()).postForLocation(calledUrl, expectedSamples);

    }

    @Test
    public void addSampleForObjectPublicSensor() throws Exception {

        String calledUrl = expectedUrl("/objects/objectid/sensors/sensorName/sample?id_type=objectid");

        smartObjectService.addSampleOnPublicSensor(SdkId.valueOf("objectid"), "sensorName", expectedSample);
        verify(mockedRestTemplate, only()).postForLocation(calledUrl, expectedSample);
    }

}
