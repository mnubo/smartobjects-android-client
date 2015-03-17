package com.mnubo.platform.android.sdk.internal.user.services.impl;

import com.mnubo.platform.android.sdk.internal.AbstractServicesTest;
import com.mnubo.platform.android.sdk.internal.user.services.CollectionService;
import com.mnubo.platform.android.sdk.models.collections.Collection;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;

import org.junit.Before;
import org.junit.Test;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


public class CollectionServiceImplTest extends AbstractServicesTest {
    private CollectionService collectionService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        collectionService = mnuboUserApi.collectionService();
    }

    @Test
    public void testDelete() throws Exception {
        mockUserServiceServer.expect(requestTo(expectedUrl("/collections/collectionId?id_type=objectid")))
                .andExpect(method(DELETE))
                .andRespond(withNoContent());

        collectionService.delete(SdkId.valueOf("collectionId"));
        mockUserServiceServer.verify();
    }

    @Test
    public void testCreate() throws Exception {
        Collection collectionToCreate = new Collection();
        collectionToCreate.setOwner("owner");

        mockUserServiceServer.expect(requestTo(expectedUrl("/collections")))
                .andExpect(method(POST))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(toJson(collectionToCreate)))
                .andRespond(withNoContent());

        collectionService.create(collectionToCreate);
        mockUserServiceServer.verify();
    }

    @Test
    public void testFindOne() throws Exception {
        Collection expectedcollection = new Collection();
        expectedcollection.setOwner("owner");

        mockUserServiceServer.expect(requestTo(expectedUrl("/collections/collectionId?id_type=objectid")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedcollection), APPLICATION_JSON_UTF8));

        Collection collection = collectionService.findOne(SdkId.valueOf("collectionId"));
        mockUserServiceServer.verify();
    }

    

    @Test
    public void testListAllObjects() throws Exception {
        SmartObjects expectedSmartObjects = new SmartObjects();

        mockUserServiceServer.expect(requestTo(expectedUrl("/collections/collectionId/objects?id_type=objectid")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedSmartObjects), APPLICATION_JSON_UTF8));

        SmartObjects SmartObjects = collectionService.listAllObjects(SdkId.valueOf("collectionId"));
        mockUserServiceServer.verify();
    }

    @Test
    public void testListAllObjectsWithLimit() throws Exception {
        SmartObjects expectedSmartObjects = new SmartObjects();

        mockUserServiceServer.expect(requestTo(expectedUrl("/collections/collectionId/objects?id_type=objectid&limit=10")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedSmartObjects), APPLICATION_JSON_UTF8));

        SmartObjects SmartObjects = collectionService.listAllObjects(SdkId.valueOf("collectionId"), 10);
        mockUserServiceServer.verify();
    }

    @Test
    public void testAddObject() throws Exception {
        mockUserServiceServer.expect(requestTo(expectedUrl("/collections/collectionId/objects/objectId?id_type=objectid")))
                .andExpect(method(PUT))
                .andExpect(userAuthMatch())
                .andRespond(withNoContent());

        collectionService.addObject(SdkId.valueOf("collectionId"), SdkId.valueOf("objectId"));
        mockUserServiceServer.verify();
    }

    @Test
    public void testRemoveObject() throws Exception {
        mockUserServiceServer.expect(requestTo(expectedUrl("/collections/collectionId/objects/objectId?id_type=objectid")))
                .andExpect(method(DELETE))
                .andExpect(userAuthMatch())
                .andRespond(withNoContent());

        collectionService.removeObject(SdkId.valueOf("collectionId"), SdkId.valueOf("objectId"));
        mockUserServiceServer.verify();
    }
}