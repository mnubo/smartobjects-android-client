package com.mnubo.platform.android.sdk.internal.user.services.impl;

import com.mnubo.platform.android.sdk.internal.AbstractServicesTest;
import com.mnubo.platform.android.sdk.internal.user.services.CollectionService;
import com.mnubo.platform.android.sdk.models.collections.Collection;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class CollectionServiceImplServicesTest extends AbstractServicesTest {

    private CollectionService collectionService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        collectionService = new CollectionServiceImpl(PLATFORM_BASE_URL, mockedRestTemplate);
    }

    @Test
    public void testDelete() throws Exception {
        String calledUrl = buildPath("/collections/collectionId?id_type=objectid");

        collectionService.delete(SdkId.valueOf("collectionId"));

        verify(mockedRestTemplate, only()).delete(calledUrl);
    }


    @Test
    public void testCreate() throws Exception {
        String calledUrl = buildPath("/collections");
        when(mockedRestTemplate.postForObject(calledUrl, expectedCollection, Collection.class)).thenReturn(expectedCollection);

        collectionService.create(expectedCollection);
        verify(mockedRestTemplate, only()).postForObject(calledUrl, expectedCollection, Collection.class);
    }

    @Test
    public void testFindOne() throws Exception {

        String calledUrl = buildPath("/collections/collectionId?id_type=objectid");
        when(mockedRestTemplate.getForObject(calledUrl, Collection.class)).thenReturn(expectedCollection);

        Collection collection = collectionService.findOne(SdkId.valueOf("collectionId"));
        verify(mockedRestTemplate, only()).getForObject(calledUrl, Collection.class);
    }

    @Test
    public void testListAllObjects() throws Exception {
        String calledUrl = buildPath("/collections/collectionId/objects?id_type=objectid");
        when(mockedRestTemplate.getForObject(calledUrl, SmartObjects.class)).thenReturn(expectedSmartObjects);

        SmartObjects smartObjects = collectionService.listAllObjects(SdkId.valueOf("collectionId"));
        verify(mockedRestTemplate, only()).getForObject(calledUrl, SmartObjects.class);
    }

    @Test
    public void testListAllObjectsWithLimit() throws Exception {

        String calledUrl = buildPath("/collections/collectionId/objects?id_type=objectid&limit=10");
        when(mockedRestTemplate.getForObject(calledUrl, SmartObjects.class)).thenReturn(expectedSmartObjects);

        SmartObjects smartObjects = collectionService.listAllObjects(SdkId.valueOf("collectionId"), 10);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, SmartObjects.class);
    }

    @Test
    public void testAddObject() throws Exception {
        String calledUrl = buildPath("/collections/collectionId/objects/objectId?id_type=objectid");

        collectionService.addObject(SdkId.valueOf("collectionId"), SdkId.valueOf("objectId"));

        verify(mockedRestTemplate, only()).put(calledUrl, null);
    }

    @Test
    public void testRemoveObject() throws Exception {
        String calledUrl = buildPath("/collections/collectionId/objects/objectId?id_type=objectid");

        collectionService.removeObject(SdkId.valueOf("collectionId"), SdkId.valueOf("objectId"));

        verify(mockedRestTemplate, only()).delete(calledUrl);
    }
}