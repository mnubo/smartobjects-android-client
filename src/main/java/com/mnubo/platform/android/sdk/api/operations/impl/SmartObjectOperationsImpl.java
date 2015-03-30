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
import com.mnubo.platform.android.sdk.api.services.cache.impl.MnuboSmartObjectFileCachingServiceImpl;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSampleOnPublicSensorTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSamplesTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.CreateObjectTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.FindObjectTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.SearchSamplesTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.UpdateObjectTask;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;

import org.springframework.social.connect.Connection;

import java.io.File;

import static com.mnubo.platform.android.sdk.Mnubo.ConnectionOperations;
import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;

public class SmartObjectOperationsImpl extends AbstractMnuboOperations implements SmartObjectOperations {

    private MnuboSmartObjectFileCachingServiceImpl mnuboSmartObjectFileCachingService;

    public SmartObjectOperationsImpl(ConnectionOperations connectionOperations,
                                     Connection<MnuboClientApi> clientConnection,
                                     Connection<MnuboUserApi> userConnection,
                                     File applicationRootDir,
                                     boolean enableFailedAttemptCaching) {
        super(connectionOperations, clientConnection, userConnection);
        setOfflineCachingEnabled(enableFailedAttemptCaching);
        mnuboSmartObjectFileCachingService = new MnuboSmartObjectFileCachingServiceImpl(applicationRootDir, getUserConnectionRefresher(), getApiFetcher());
    }

    public void setMnuboSmartObjectFileCachingService(MnuboSmartObjectFileCachingServiceImpl mnuboSmartObjectFileCachingService) {
        this.mnuboSmartObjectFileCachingService = mnuboSmartObjectFileCachingService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findObject(final SdkId objectId, final CompletionCallBack<SmartObject> completionCallBack) {
        final FindObjectTask task = new FindObjectTask(getApiFetcher(), objectId, getUserConnectionRefresher());
        task.executeAsync(completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final SdkId objectId, final SmartObject object, final CompletionCallBack<Boolean> completionCallBack) {
        final UpdateObjectTask task = new UpdateObjectTask(getApiFetcher(), objectId, object, getUserConnectionRefresher());
        task.executeAsync(completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void searchSamples(final SdkId objectId, final String sensorName, final CompletionCallBack<Samples> completionCallBack) {
        final SearchSamplesTask task = new SearchSamplesTask(getApiFetcher(), objectId, sensorName, getUserConnectionRefresher());
        task.executeAsync(completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSamples(SdkId objectId, Samples samples) {
        final AddSamplesTask task = new AddSamplesTask(getApiFetcher(), objectId, samples, getUserConnectionRefresher());
        if (isOfflineCachingEnabled()) {
            task.executeSync(mnuboSmartObjectFileCachingService.getAddSamplesFailedAttemptCallback());
        } else {
            task.executeSync();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSamples(final SdkId objectId, final Samples samples, final CompletionCallBack<Boolean> completionCallBack) {
        final AddSamplesTask task = new AddSamplesTask(getApiFetcher(), objectId, samples, getUserConnectionRefresher());
        if (isOfflineCachingEnabled()) {
            task.executeAsync(completionCallBack, mnuboSmartObjectFileCachingService.getAddSamplesFailedAttemptCallback());
        } else {
            task.executeAsync(completionCallBack);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSampleOnPublicSensor(SdkId objectId, String sensorName, Sample sample) {
        final AddSampleOnPublicSensorTask task = new AddSampleOnPublicSensorTask(getApiFetcher(), objectId, sensorName, sample, getUserConnectionRefresher());
        task.executeSync();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSampleOnPublicSensor(SdkId objectId, String sensorName, Sample sample, CompletionCallBack<Boolean> completionCallBack) {
        final AddSampleOnPublicSensorTask task = new AddSampleOnPublicSensorTask(getApiFetcher(), objectId, sensorName, sample, getUserConnectionRefresher());
        task.executeAsync(completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createObject(SmartObject smartObject, Boolean updateIfExists, CompletionCallBack<Boolean> completionCallBack) {
        final CreateObjectTask task = new CreateObjectTask(getApiFetcher(), smartObject, updateIfExists, getUserConnectionRefresher());
        task.executeAsync(completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void retryFailedAttempts() {
        if (isOfflineCachingEnabled()) {
            this.mnuboSmartObjectFileCachingService.retryFailedAttempts();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFailedAttemptsCount() {
        if (isOfflineCachingEnabled()) {
            return this.mnuboSmartObjectFileCachingService.getFailedAttemptsCount();
        }
        return 0;
    }
}
