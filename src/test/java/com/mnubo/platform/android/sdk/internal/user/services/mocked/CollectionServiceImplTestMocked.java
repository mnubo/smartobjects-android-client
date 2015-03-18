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
import com.mnubo.platform.android.sdk.internal.user.services.CollectionService;
import com.mnubo.platform.android.sdk.internal.user.services.impl.CollectionServiceImpl;
import com.mnubo.platform.android.sdk.models.collections.Collection;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class CollectionServiceImplTestMocked extends MockedAbstractServiceTest {

    private CollectionService collectionService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        collectionService = new CollectionServiceImpl(PLATFORM_BASE_URL, mockedRestTemplate);
    }

    @Test
    public void testDelete() throws Exception {
        String calledUrl = expectedUrl("/collections/collectionId?id_type=objectid");

        collectionService.delete(SdkId.valueOf("collectionId"));

        verify(mockedRestTemplate, only()).delete(calledUrl);
    }


    @Test
    public void testCreate() throws Exception {
        String calledUrl = expectedUrl("/collections");
        when(mockedRestTemplate.postForObject(calledUrl, expectedCollection, Collection.class)).thenReturn(expectedCollection);

        collectionService.create(expectedCollection);
        verify(mockedRestTemplate, only()).postForObject(calledUrl, expectedCollection, Collection.class);
    }

    @Test
    public void testFindOne() throws Exception {

        String calledUrl = expectedUrl("/collections/collectionId?id_type=objectid");
        when(mockedRestTemplate.getForObject(calledUrl, Collection.class)).thenReturn(expectedCollection);

        Collection collection = collectionService.findOne(SdkId.valueOf("collectionId"));
        verify(mockedRestTemplate, only()).getForObject(calledUrl, Collection.class);
    }

    @Test
    public void testListAllObjects() throws Exception {
        String calledUrl = expectedUrl("/collections/collectionId/objects?id_type=objectid");
        when(mockedRestTemplate.getForObject(calledUrl, SmartObjects.class)).thenReturn(expectedSmartObjects);

        SmartObjects smartObjects = collectionService.listAllObjects(SdkId.valueOf("collectionId"));
        verify(mockedRestTemplate, only()).getForObject(calledUrl, SmartObjects.class);
    }

    @Test
    public void testListAllObjectsWithLimit() throws Exception {

        String calledUrl = expectedUrl("/collections/collectionId/objects?id_type=objectid&limit=10");
        when(mockedRestTemplate.getForObject(calledUrl, SmartObjects.class)).thenReturn(expectedSmartObjects);

        SmartObjects smartObjects = collectionService.listAllObjects(SdkId.valueOf("collectionId"), 10);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, SmartObjects.class);
    }

    @Test
    public void testAddObject() throws Exception {
        String calledUrl = expectedUrl("/collections/collectionId/objects/objectId?id_type=objectid");

        collectionService.addObject(SdkId.valueOf("collectionId"), SdkId.valueOf("objectId"));

        verify(mockedRestTemplate, only()).put(calledUrl, null);
    }

    @Test
    public void testRemoveObject() throws Exception {
        String calledUrl = expectedUrl("/collections/collectionId/objects/objectId?id_type=objectid");

        collectionService.removeObject(SdkId.valueOf("collectionId"), SdkId.valueOf("objectId"));

        verify(mockedRestTemplate, only()).delete(calledUrl);
    }
}