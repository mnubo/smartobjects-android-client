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
import com.mnubo.platform.android.sdk.api.services.buffer.MnuboBufferService;
import com.mnubo.platform.android.sdk.internal.connect.connection.MnuboConnectionManager;
import com.mnubo.platform.android.sdk.internal.services.impl.SampleOrderResult;
import com.mnubo.platform.android.sdk.internal.tasks.MnuboResponse;
import com.mnubo.platform.android.sdk.internal.tasks.Task;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static com.mnubo.platform.android.sdk.internal.tasks.TaskFactory.newAddSamplesTask;
import static com.mnubo.platform.android.sdk.internal.tasks.TaskFactory.newCreateObjectTask;
import static com.mnubo.platform.android.sdk.internal.tasks.TaskFactory.newDeleteObjectTask;
import static com.mnubo.platform.android.sdk.internal.tasks.TaskFactory.newFindObjectTask;
import static com.mnubo.platform.android.sdk.internal.tasks.TaskFactory.newSearchSamplesTask;
import static com.mnubo.platform.android.sdk.internal.tasks.TaskFactory.newUpdateObjectTask;

public class SmartObjectOperationsImpl extends AbstractMnuboOperations implements SmartObjectOperations {

    public SmartObjectOperationsImpl(MnuboConnectionManager connectionManager,
                                     MnuboBufferService mnuboBufferService) {
        super(connectionManager, mnuboBufferService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<SmartObject> findObject(SdkId id) {
        final Task<SmartObject> task = newFindObjectTask(id);
        return task.executeSync(mnuboConnectionManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findObjectAsync(final SdkId id, final CompletionCallBack<SmartObject> completionCallBack) {
        final Task<SmartObject> task = newFindObjectTask(id);
        task.executeAsync(mnuboConnectionManager, completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<Boolean> update(SdkId id, SmartObject object) {
        final Task<Boolean> task = newUpdateObjectTask(id, object);
        return task.executeSync(mnuboConnectionManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAsync(final SdkId id, final SmartObject object, final CompletionCallBack<Boolean> completionCallBack) {
        final Task<Boolean> task = newUpdateObjectTask(id, object);
        task.executeAsync(mnuboConnectionManager, completionCallBack);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<Samples> searchSamples(SdkId id, String sensorName) {
        final Task<Samples> task = newSearchSamplesTask(id, sensorName);
        return task.executeSync(mnuboConnectionManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void searchSamplesAsync(final SdkId id, final String sensorName, final CompletionCallBack<Samples> completionCallBack) {
        final Task<Samples> task = newSearchSamplesTask(id, sensorName);
        task.executeAsync(mnuboConnectionManager, completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<Samples> searchSamples(SdkId id, String sensorName, String from, String to,
                                                int limit, SampleOrderResult order) {
        final Task<Samples> task = newSearchSamplesTask(id, sensorName, from, to, limit, order);
        return task.executeSync(mnuboConnectionManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void searchSamplesAsync(final SdkId id, final String sensorName, String from, String to,
                                   int limit, SampleOrderResult order,
                                   final CompletionCallBack<Samples> completionCallBack) {
        final Task<Samples> task = newSearchSamplesTask(id, sensorName, from, to, limit, order);
        task.executeAsync(mnuboConnectionManager, completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<Boolean> addSamples(SdkId id, Samples samples) {
        final Task<Boolean> task = newAddSamplesTask(id, samples);
        final MnuboResponse<Boolean> result;
        if (isBufferServiceEnabled()) {
            result = task.executeSync(mnuboConnectionManager, getMnuboBufferService().getFailedAttemptCallback());
        } else {
            result = task.executeSync(mnuboConnectionManager);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSamplesAsync(final SdkId id, final Samples samples, final CompletionCallBack<Boolean> completionCallBack) {
        final Task<Boolean> task = newAddSamplesTask(id, samples);
        if (isBufferServiceEnabled()) {
            task.executeAsync(mnuboConnectionManager, completionCallBack, getMnuboBufferService().getFailedAttemptCallback());
        } else {
            task.executeAsync(mnuboConnectionManager, completionCallBack);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<Boolean> addSample(SdkId id, Sample sample) {
        final Samples samples = new Samples();
        samples.addSample(sample);
        return addSamples(id, samples);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSampleAsync(final SdkId id, final Sample sample, final CompletionCallBack<Boolean> completionCallBack) {
        final Samples samples = new Samples();
        samples.addSample(sample);
        addSamplesAsync(id, samples, completionCallBack);
    }

    @Override
    public MnuboResponse<Boolean> createObject(SmartObject smartObject, Boolean updateIfExists) {
        final Task<Boolean> task = newCreateObjectTask(smartObject, updateIfExists);
        return task.executeSync(mnuboConnectionManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createObjectAsync(SmartObject smartObject, Boolean updateIfExists, CompletionCallBack<Boolean> completionCallBack) {
        final Task<Boolean> task = newCreateObjectTask(smartObject, updateIfExists);
        task.executeAsync(mnuboConnectionManager, completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<Boolean> deleteObject(SdkId id) {
        final Task<Boolean> task = newDeleteObjectTask(id);
        return task.executeSync(mnuboConnectionManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteObjectAsync(SdkId id, CompletionCallBack<Boolean> completionCallBack) {
        final Task<Boolean> task = newDeleteObjectTask(id);
        task.executeAsync(mnuboConnectionManager, completionCallBack);
    }
}
