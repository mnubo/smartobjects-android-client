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

package com.mnubo.platform.android.sdk.api.operations.impl;

import com.mnubo.platform.android.sdk.api.operations.SmartObjectOperations;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.impl.TaskWithRefreshImpl;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;

import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.Mnubo.ConnectionOperations;
import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;

public class SmartObjectOperationsImpl extends AbstractMnuboOperations implements SmartObjectOperations {

    private final static String OPERATION_TAG = SmartObjectOperationsImpl.class.getName();

    public SmartObjectOperationsImpl(ConnectionOperations connectionOperations,
                                     Connection<MnuboClientApi> clientConnection,
                                     Connection<MnuboUserApi> userConnection) {
        super(connectionOperations, clientConnection, userConnection);
    }

    @Override
    public void findObject(final SdkId objectId, final CompletionCallBack<SmartObject> completionCallBack) {
        execute(new TaskWithRefreshImpl<>(new MnuboOperation<SmartObject>() {
            @Override
            public SmartObject executeMnuboCall() {
                return getUserApi().objectService().findOne(objectId);
            }
        }, getUserConnectionRefresher()), completionCallBack);
    }

    @Override
    public void update(final SdkId objectId, final SmartObject object, final CompletionCallBack<Boolean> completionCallBack) {
        execute(new TaskWithRefreshImpl<>(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                getUserApi().objectService().update(objectId, object);
                return true;
            }
        }, getUserConnectionRefresher()), completionCallBack);
    }

    @Override
    public void searchSamples(final SdkId objectId, final String sensorName, final CompletionCallBack<Samples> completionCallBack) {
        execute(new TaskWithRefreshImpl<>(new MnuboOperation<Samples>() {
            @Override
            public Samples executeMnuboCall() {
                return getUserApi().objectService().searchSamples(objectId, sensorName);
            }
        }, getUserConnectionRefresher()), completionCallBack);
    }

    @Override
    public void addSamples(final SdkId objectId, final Samples samples, final CompletionCallBack<Boolean> completionCallBack) {
        execute(new TaskWithRefreshImpl<>(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                getUserApi().objectService().addSamples(objectId, samples);
                return true;
            }
        }, getUserConnectionRefresher()), completionCallBack);
    }

    @Override
    public void addSampleOnPublicSensor(final SdkId objectId, final String sensorName, final Sample sample, final CompletionCallBack<Boolean> completionCallBack) {
        execute(new TaskWithRefreshImpl<>(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                getUserApi().objectService().addSampleOnPublicSensor(objectId, sensorName, sample);
                return true;
            }
        }, getUserConnectionRefresher()), completionCallBack);
    }

    @Override
    public void createObject(final SmartObject smartObject, final Boolean updateIfExists, CompletionCallBack<Boolean> completionCallBack) {
        execute(new TaskWithRefreshImpl<>(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                getUserApi().objectService().create(smartObject, updateIfExists);
                return true;
            }
        }, getUserConnectionRefresher()), completionCallBack);
    }

    @Override
    public String getOperationTag() {
        return OPERATION_TAG;
    }
}
