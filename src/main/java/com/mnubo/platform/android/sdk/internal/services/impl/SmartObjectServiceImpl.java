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
import com.mnubo.platform.android.sdk.internal.services.AbstractMnuboService;
import com.mnubo.platform.android.sdk.internal.services.SmartObjectService;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.common.ValueType;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;
import com.mnubo.platform.android.sdk.models.users.Users;

import org.apache.commons.lang3.Validate;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class SmartObjectServiceImpl extends AbstractMnuboService implements SmartObjectService {


    public SmartObjectServiceImpl(String platformBaseUrl, RestTemplate restTemplate) {
        super(platformBaseUrl, PlatformPath.objects, restTemplate);
    }

    @Override
    public void delete(SdkId objectId) {
        Validate.notNull(objectId, "The objectId is required.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{objectId}", objectId.getId());
        query.idType(objectId.getIdType());

        getRestTemplate().delete(query.buildUrl());
    }

    @Override
    public SmartObject create(SmartObject object, boolean updateIfExists) {
        Validate.notNull(object, "The object to update shouldn't be null.");

        PlatformQuery query = this.getQuery();

        query.updateIfExists(updateIfExists);
        query.setBody(object);

        return getRestTemplate().postForObject(query.buildUrl(), query.getBody(), SmartObject.class);
    }

    @Override
    public void update(SdkId objectId, SmartObject object) {
        Validate.notNull(object, "The object to update shouldn't be null.");
        Validate.notNull(objectId, "The objectId to update shouldn't be null.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{objectId}", objectId.getId());
        query.idType(objectId.getIdType());
        query.setBody(object);

        getRestTemplate().put(query.buildUrl(), query.getBody());
    }

    @Override
    public SmartObject findOne(SdkId objectId) {

        return this.findOne(objectId, null);
    }

    @Override
    public SmartObject findOne(SdkId objectId, List<String> attributes) {
        Validate.notNull(objectId, "The object to update shouldn't be null.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{objectId}", objectId.getId());
        query.idType(objectId.getIdType());
        query.queryParams("attributes", attributes);

        return getRestTemplate().getForObject(query.buildUrl(), SmartObject.class);
    }

    @Override
    public Samples searchSamples(SdkId objectId, String sensorName) {
        return this.searchSamples(objectId, sensorName, null, null, null, 0);
    }

    @Override
    public Samples searchSamples(SdkId objectId, String sensorName, ValueType value) {
        return this.searchSamples(objectId, sensorName, value, null, null, 0);
    }

    @Override
    public Samples searchSamples(SdkId objectId, String sensorName, ValueType value, String from, String to) {
        return this.searchSamples(objectId, sensorName, value, from, to, 0);
    }

    @Override
    public Samples searchSamples(SdkId objectId, String sensorName, ValueType value, String from, String to, int resultSizeLimit) {
        Validate.notNull(objectId, "The objectId to update shouldn't be null.");
        Validate.notBlank(sensorName, "The sensor name shouldn't be null or empty.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{objectId}/sensors/{sensorsName}/samples", objectId.getId(), sensorName);

        query.idType(objectId.getIdType());
        query.value(value);
        query.timeRange(from, to);
        query.limit(resultSizeLimit);

        return getRestTemplate().getForObject(query.buildUrl(), Samples.class);
    }

    @Override
    public void addSamples(SdkId objectId, Samples samples) {
        Validate.notNull(objectId, "The objectId to update shouldn't be null.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{objectId}/samples", objectId.getId());
        query.idType(objectId.getIdType());
        query.setBody(samples);

        getRestTemplate().postForLocation(query.buildUrl(), query.getBody());
    }

    @Override
    public void addSampleOnPublicSensor(SdkId objectId, String sensorName, Sample sample) {
        Validate.notNull(objectId, "The objectId to update shouldn't be null.");
        Validate.notBlank(sensorName, "The objectId to update shouldn't be null.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{objectId}/sensors/{sensorName}/sample", objectId.getId(), sensorName);
        query.idType(objectId.getIdType());
        query.setBody(sample);

        getRestTemplate().postForLocation(query.buildUrl(), query.getBody());
    }

    @Override
    public Users listOwnersHistory(SdkId objectId) {
        return this.listOwnersHistory(objectId, false);
    }

    @Override
    public Users listOwnersHistory(SdkId objectId, boolean details) {
        Validate.notNull(objectId, "The objectId to update shouldn't be null.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{objectId}/owners_history", objectId.getId());
        query.idType(objectId.getIdType());
        query.details(details);

        return getRestTemplate().getForObject(query.buildUrl(), Users.class);
    }
}
