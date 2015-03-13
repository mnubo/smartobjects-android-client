package com.mnubo.platform.android.sdk.internal.user.services.impl;

import com.mnubo.platform.android.sdk.internal.AbstractServicesTest;
import com.mnubo.platform.android.sdk.internal.user.services.SmartObjectService;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.common.ValueType;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;
import com.mnubo.platform.android.sdk.models.users.Users;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


public class ObjectServiceImplTest extends AbstractServicesTest {
    private SmartObjectService smartObjectService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        smartObjectService = mnuboUserApi.objectService();
    }

    @Test
    public void createOrUpdateSmartObjectTest() throws Exception {

        final SmartObject objectToCreate = new SmartObject();
        objectToCreate.setOwner("owner");
        objectToCreate.setDeviceId("deviceid");
        objectToCreate.setObjectId(new UUID(48, 16));

        mockUserServiceServer.expect(requestTo(expectedUrl("/objects?update_if_exists=true")))
                .andExpect(method(POST))
                .andExpect(content().string(toJson(objectToCreate)))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andRespond(withSuccess(toJson(objectToCreate), APPLICATION_JSON_UTF8));
        smartObjectService.create(objectToCreate, true);
        mockUserServiceServer.verify();
    }

    @Test
    public void updateSmartObjectTest() throws Exception {

        final SmartObject objectToUpdate = new SmartObject();
        objectToUpdate.setOwner("owner");
        objectToUpdate.setDeviceId("deviceid");
        objectToUpdate.setObjectId(new UUID(48, 16));

        final SdkId objecSdkId = SdkId.withUuid(objectToUpdate.getObjectId());

        mockUserServiceServer.expect(requestTo(expectedUrl("/objects/" + objecSdkId.getId() + "?id_type=" + objecSdkId.getIdType())))
                .andExpect(method(PUT))
                .andExpect(content().string(toJson(objectToUpdate)))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andRespond(withSuccess());
        smartObjectService.update(objecSdkId, objectToUpdate);
        mockUserServiceServer.verify();
    }

    @Test
    public void deleteObjectTest() throws Exception {

        mockUserServiceServer.expect(requestTo(expectedUrl("/objects/objectid?id_type=objectid")))
                .andExpect(method(DELETE))
                .andRespond(withNoContent());
        smartObjectService.delete(SdkId.valueOf("objectid"));
        mockUserServiceServer.verify();
    }

    @Test
    public void getObjectTest() throws Exception {
        final SmartObject expectedObject = new SmartObject();
        expectedObject.setOwner("owner");
        expectedObject.setDeviceId("deviceid");
        expectedObject.setObjectId(new UUID(48, 16));

        mockUserServiceServer.expect(requestTo(expectedUrl("/objects/objectid?id_type=objectid")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedObject), APPLICATION_JSON_UTF8));

        SmartObject SmartObject = smartObjectService.findOne(SdkId.valueOf("objectid"));
        mockUserServiceServer.verify();
    }

    @Test
    public void getObjectWithAttributesTest() throws Exception {
        final SmartObject expectedObject = new SmartObject();
        expectedObject.setOwner("owner");
        expectedObject.setDeviceId("deviceid");
        expectedObject.setObjectId(new UUID(48, 16));

        mockUserServiceServer.expect(requestTo(expectedUrl("/objects/objectid?id_type=objectid&attributes=attributes1&attributes=attributes2")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedObject), APPLICATION_JSON_UTF8));

        List<String> attributes = Arrays.asList("attributes1", "attributes2");
        SmartObject SmartObject = smartObjectService.findOne(SdkId.valueOf("objectid"), attributes);
        mockUserServiceServer.verify();
    }

    @Test
    public void getObjectOwnerHistory() throws Exception {
        final Users expectedUsers = new Users();

        mockUserServiceServer.expect(requestTo(expectedUrl("/objects/objectid/owners_history?id_type=objectid&details=false")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedUsers), APPLICATION_JSON_UTF8));

        Users users = smartObjectService.listOwnersHistory(SdkId.valueOf("objectid"));
        mockUserServiceServer.verify();
    }

    @Test
    public void getObjectOwnerHistoryWithDetail() throws Exception {
        final Users expectedUsers = new Users();

        mockUserServiceServer.expect(requestTo(expectedUrl("/objects/objectid/owners_history?id_type=objectid&details=true")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedUsers), APPLICATION_JSON_UTF8));

        Users users = smartObjectService.listOwnersHistory(SdkId.valueOf("objectid"), true);
        mockUserServiceServer.verify();
    }

    @Test
    public void getObjectSamples() throws Exception {
        final Samples expectedSamples = new Samples();

        mockUserServiceServer.expect(requestTo(expectedUrl("/objects/objectid/sensors/sensorName/samples?id_type=objectid")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedSamples), APPLICATION_JSON_UTF8));

        Samples samples = smartObjectService.searchSamples(SdkId.valueOf("objectid"), "sensorName");
        mockUserServiceServer.verify();
    }

    @Test
    public void getObjectSamplesWithValueType() throws Exception {
        final Samples expectedSamples = new Samples();

        mockUserServiceServer.expect(requestTo(expectedUrl("/objects/objectid/sensors/sensorName/samples?id_type=objectid&value=last")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedSamples), APPLICATION_JSON_UTF8));

        Samples samples = smartObjectService.searchSamples(SdkId.valueOf("objectid"), "sensorName", ValueType.last);
        mockUserServiceServer.verify();
    }

    @Test
    public void getObjectSamplesWithValueTypeWithTimeRange() throws Exception {
        final Samples expectedSamples = new Samples();

        mockUserServiceServer.expect(requestTo(expectedUrl("/objects/objectid/sensors/sensorName/samples?id_type=objectid&value=last&from=from&to=to")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedSamples), APPLICATION_JSON_UTF8));

        Samples samples = smartObjectService.searchSamples(SdkId.valueOf("objectid"), "sensorName", ValueType.last, "from", "to");
        mockUserServiceServer.verify();
    }

    @Test
    public void getObjectSamplesWithValueTypeWithTimeRangeAndLimit() throws Exception {
        final Samples expectedSamples = new Samples();

        mockUserServiceServer.expect(requestTo(expectedUrl("/objects/objectid/sensors/sensorName/samples?id_type=objectid&value=last&from=from&to=to&limit=10")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedSamples), APPLICATION_JSON_UTF8));

        Samples samples = smartObjectService.searchSamples(SdkId.valueOf("objectid"), "sensorName", ValueType.last, "from", "to", 10);
        mockUserServiceServer.verify();
    }

    @Test
    public void addSamplesForObject() throws Exception {

        final Samples samplesToCreate = new Samples();

        Sample sample = new Sample();
        sample.setSensorName("sample1");
        sample.setTimestamp("now");

        samplesToCreate.addSample(sample);

        mockUserServiceServer.expect(requestTo(expectedUrl("/objects/objectid/samples?id_type=objectid")))
                .andExpect(method(POST))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(toJson(samplesToCreate)))
                .andRespond(withNoContent());
        smartObjectService.addSamples(SdkId.valueOf("objectid"), samplesToCreate);
        mockUserServiceServer.verify();
    }

}
