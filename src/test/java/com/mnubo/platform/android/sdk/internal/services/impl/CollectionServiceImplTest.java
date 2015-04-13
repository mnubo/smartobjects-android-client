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

package com.mnubo.platform.android.sdk.internal.services.impl;

import com.mnubo.platform.android.sdk.internal.api.MnuboSDKApiImpl;
import com.mnubo.platform.android.sdk.internal.services.CollectionService;
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
        this.mnuboUserApi = new MnuboSDKApiImpl(USER_ACCESS_TOKEN, PLATFORM_BASE_URL);
        setUpMockServer();

        collectionService = mnuboUserApi.collectionService();
    }

    @Test
    public void testDelete() throws Exception {
        mockServiceServer.expect(requestTo(expectedUrl("/collections/collectionId?id_type=objectid")))
                .andExpect(method(DELETE))
                .andRespond(withNoContent());

        collectionService.delete(SdkId.valueOf("collectionId"));
        mockServiceServer.verify();
    }

    @Test
    public void testCreate() throws Exception {
        Collection collectionToCreate = new Collection();
        collectionToCreate.setOwner("owner");

        mockServiceServer.expect(requestTo(expectedUrl("/collections")))
                .andExpect(method(POST))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(toJson(collectionToCreate)))
                .andRespond(withNoContent());

        collectionService.create(collectionToCreate);
        mockServiceServer.verify();
    }

    @Test
    public void testFindOne() throws Exception {
        Collection expectedCollection = new Collection();
        expectedCollection.setOwner("owner");

        mockServiceServer.expect(requestTo(expectedUrl("/collections/collectionId?id_type=objectid")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedCollection), APPLICATION_JSON_UTF8));

        Collection collection = collectionService.findOne(SdkId.valueOf("collectionId"));
        mockServiceServer.verify();
    }


    @Test
    public void testListAllObjects() throws Exception {
        SmartObjects expectedSmartObjects = new SmartObjects();

        mockServiceServer.expect(requestTo(expectedUrl("/collections/collectionId/objects?id_type=objectid")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedSmartObjects), APPLICATION_JSON_UTF8));

        SmartObjects SmartObjects = collectionService.listAllObjects(SdkId.valueOf("collectionId"));
        mockServiceServer.verify();
    }

    @Test
    public void testListAllObjectsWithLimit() throws Exception {
        SmartObjects expectedSmartObjects = new SmartObjects();

        mockServiceServer.expect(requestTo(expectedUrl("/collections/collectionId/objects?id_type=objectid&limit=10")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedSmartObjects), APPLICATION_JSON_UTF8));

        SmartObjects SmartObjects = collectionService.listAllObjects(SdkId.valueOf("collectionId"), 10);
        mockServiceServer.verify();
    }

    @Test
    public void testAddObject() throws Exception {
        mockServiceServer.expect(requestTo(expectedUrl("/collections/collectionId/objects/objectId?id_type=objectid")))
                .andExpect(method(PUT))
                .andExpect(userAuthMatch())
                .andRespond(withNoContent());

        collectionService.addObject(SdkId.valueOf("collectionId"), SdkId.valueOf("objectId"));
        mockServiceServer.verify();
    }

    @Test
    public void testRemoveObject() throws Exception {
        mockServiceServer.expect(requestTo(expectedUrl("/collections/collectionId/objects/objectId?id_type=objectid")))
                .andExpect(method(DELETE))
                .andExpect(userAuthMatch())
                .andRespond(withNoContent());

        collectionService.removeObject(SdkId.valueOf("collectionId"), SdkId.valueOf("objectId"));
        mockServiceServer.verify();
    }
}