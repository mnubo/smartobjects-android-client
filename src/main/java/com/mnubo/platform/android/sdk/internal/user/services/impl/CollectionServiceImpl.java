package com.mnubo.platform.android.sdk.internal.user.services.impl;

import com.mnubo.platform.android.sdk.internal.connect.query.PlatformPath;
import com.mnubo.platform.android.sdk.internal.connect.query.PlatformQuery;
import com.mnubo.platform.android.sdk.internal.services.AbstractMnuboService;
import com.mnubo.platform.android.sdk.internal.user.services.CollectionService;
import com.mnubo.platform.android.sdk.models.collections.Collection;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;

import org.apache.commons.lang3.Validate;
import org.springframework.web.client.RestTemplate;

public class CollectionServiceImpl extends AbstractMnuboService implements CollectionService {

    private RestTemplate restTemplate;

    public CollectionServiceImpl(String platformBaseUrl, RestTemplate restTemplate) {
        super(platformBaseUrl, PlatformPath.collections);
        this.restTemplate = restTemplate;
    }

    @Override
    public void delete(SdkId collectionId) {
        Validate.notNull(collectionId, "The collectionId is required.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{collectionId}", collectionId.getId());
        query.idType(collectionId.getIdType());

        restTemplate.delete(query.buildUrl());
    }

    @Override
    public Collection create(Collection collection) {
        Validate.notNull(collection, "The collection to createUser shouldn't be null is required.");

        PlatformQuery query = this.getQuery();
        query.setBody(collection);

        return restTemplate.postForObject(query.buildUrl(), query.getBody(), Collection.class);
    }

    @Override
    public Collection findOne(SdkId collectionId) {
        Validate.notNull(collectionId, "The collectionId is required.");

        PlatformQuery query = this.getQuery();
        query.setUri("/{collectionId}", collectionId.getId());
        query.idType(collectionId.getIdType());

        return restTemplate.getForObject(query.buildUrl(), Collection.class);
    }

    @Override
    public SmartObjects listAllObjects(SdkId collectionId, int resultSizeLimit) {
        Validate.notNull(collectionId, "The collectionId is required.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{collectionId}/objects", collectionId.getId());
        query.idType(collectionId.getIdType());
        query.limit(resultSizeLimit);

        return restTemplate.getForObject(query.buildUrl(), SmartObjects.class);
    }

    @Override
    public SmartObjects listAllObjects(SdkId collectionId) {
        return this.listAllObjects(collectionId, 0);
    }

    @Override
    public void addObject(SdkId collectionId, SdkId objectId) {
        Validate.notNull(collectionId, "The collectionId is required.");
        Validate.notNull(objectId, "The objectId is required.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{collectionId}/objects/{objectId}", collectionId.getId(), objectId.getId());
        query.idType(collectionId.getIdType());

        restTemplate.put(query.buildUrl(), null);
    }

    @Override
    public void removeObject(SdkId collectionId, SdkId objectId) {
        Validate.notNull(collectionId, "The collectionId is required.");
        Validate.notNull(objectId, "The objectId is required.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{collectionId}/objects/{objectId}", collectionId.getId(), objectId.getId());
        query.idType(collectionId.getIdType());

        restTemplate.delete(query.buildUrl());
    }

}
