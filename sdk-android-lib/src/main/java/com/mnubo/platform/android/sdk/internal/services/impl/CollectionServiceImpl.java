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

import com.mnubo.platform.android.sdk.internal.connect.query.PlatformPath;
import com.mnubo.platform.android.sdk.internal.connect.query.PlatformQuery;
import com.mnubo.platform.android.sdk.internal.services.CollectionService;
import com.mnubo.platform.android.sdk.models.collections.Collection;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;

import org.apache.commons.lang3.Validate;
import org.springframework.web.client.RestTemplate;

public class CollectionServiceImpl extends AbstractMnuboService implements CollectionService {

    public CollectionServiceImpl(String platformBaseUrl, RestTemplate restTemplate) {
        super(platformBaseUrl, PlatformPath.collections, restTemplate);
    }

    @Override
    public void delete(SdkId collectionId) {
        Validate.notNull(collectionId, "The collectionId is required.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{collectionId}", collectionId.getId());
        query.idType(collectionId.getIdType());

        getRestTemplate().delete(query.buildUrl());
    }

    @Override
    public Collection create(Collection collection) {
        Validate.notNull(collection, "The collection to createUserAsync shouldn't be null is required.");

        PlatformQuery query = this.getQuery();
        query.setBody(collection);

        return getRestTemplate().postForObject(query.buildUrl(), query.getBody(), Collection.class);
    }

    @Override
    public Collection findOne(SdkId collectionId) {
        Validate.notNull(collectionId, "The collectionId is required.");

        PlatformQuery query = this.getQuery();
        query.setUri("/{collectionId}", collectionId.getId());
        query.idType(collectionId.getIdType());

        return getRestTemplate().getForObject(query.buildUrl(), Collection.class);
    }

    @Override
    public SmartObjects listAllObjects(SdkId collectionId, int resultSizeLimit) {
        Validate.notNull(collectionId, "The collectionId is required.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{collectionId}/objects", collectionId.getId());
        query.idType(collectionId.getIdType());
        query.limit(resultSizeLimit);

        return getRestTemplate().getForObject(query.buildUrl(), SmartObjects.class);
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

        getRestTemplate().put(query.buildUrl(), null);
    }

    @Override
    public void removeObject(SdkId collectionId, SdkId objectId) {
        Validate.notNull(collectionId, "The collectionId is required.");
        Validate.notNull(objectId, "The objectId is required.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{collectionId}/objects/{objectId}", collectionId.getId(), objectId.getId());
        query.idType(collectionId.getIdType());

        getRestTemplate().delete(query.buildUrl());
    }

}
