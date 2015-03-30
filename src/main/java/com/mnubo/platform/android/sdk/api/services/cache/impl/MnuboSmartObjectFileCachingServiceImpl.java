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

package com.mnubo.platform.android.sdk.api.services.cache.impl;

import com.mnubo.platform.android.sdk.api.services.cache.MnuboFileCachingService;
import com.mnubo.platform.android.sdk.api.store.MnuboEntity;
import com.mnubo.platform.android.sdk.api.store.impl.MnuboFileStore;
import com.mnubo.platform.android.sdk.internal.tasks.Task;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSamplesTask;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;

import java.io.File;
import java.util.List;

import static com.mnubo.platform.android.sdk.api.store.MnuboEntity.EntityType.ADD_SAMPLES;
import static com.mnubo.platform.android.sdk.internal.tasks.Task.ApiFetcher;
import static com.mnubo.platform.android.sdk.internal.tasks.impl.TaskWithRefreshImpl.ConnectionRefresher;

/**
 * This service is used to wrap your request. If an error occurs during the request, the data is
 * persisted to the disk.
 */
public class MnuboSmartObjectFileCachingServiceImpl implements MnuboFileCachingService {

    private final static String RETRY_QUEUE_NAME = "failed";

    private MnuboFileStore mnuboStore;
    private ConnectionRefresher connectionRefresher;
    private ApiFetcher apiFetcher;

    /**
     * Create a new instance of a {link com.mnubo.platform.android.sdk.api.services.fallback.MnuboFallbackService}
     */
    public MnuboSmartObjectFileCachingServiceImpl(File rootDir,
                                                  ConnectionRefresher connectionRefresher,
                                                  ApiFetcher apiFetcher) {
        this.mnuboStore = new MnuboFileStore(rootDir);
        this.connectionRefresher = connectionRefresher;
        this.apiFetcher = apiFetcher;
    }

    public void setMnuboStore(MnuboFileStore mnuboStore) {
        this.mnuboStore = mnuboStore;
    }

    /**
     * {@inheritDoc}
     */
    public int getFailedAttemptsCount() {

        final List<MnuboEntity> queue = mnuboStore.getEntities(RETRY_QUEUE_NAME);
        return queue != null ? queue.size() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void retryFailedAttempts() {
        List<MnuboEntity> entities = mnuboStore.getEntities(RETRY_QUEUE_NAME);
        for (MnuboEntity entity : entities) {
            switch (entity.getType()) {
                case ADD_SAMPLES:
                    SdkId id = (SdkId) entity.getIdData().get("id");
                    Samples samples = (Samples) entity.getValue();
                    final AddSamplesTask addSamplesTask = new AddSamplesTask(apiFetcher, id, samples, connectionRefresher);
                    addSamplesTask.executeSync(getAddSamplesFailedAttemptCallback());
                    break;
                case ADD_SAMPLE_PUBLIC:
                    //TODO
                    break;
            }
            mnuboStore.remove(entity);
        }
    }

    public FailedAttemptCallback getAddSamplesFailedAttemptCallback() {
        return new FailedAttemptCallback() {
            @Override
            public void onFailure(Task failedTask) {
                final AddSamplesTask addSamplesTask = (AddSamplesTask) failedTask;
                final MnuboEntity entity = new MnuboEntity(
                        ADD_SAMPLES,
                        addSamplesTask.getId().getId(),
                        addSamplesTask.getIdData(),
                        addSamplesTask.getSamples());
                mnuboStore.put(RETRY_QUEUE_NAME, entity);
            }
        };
    }
}
