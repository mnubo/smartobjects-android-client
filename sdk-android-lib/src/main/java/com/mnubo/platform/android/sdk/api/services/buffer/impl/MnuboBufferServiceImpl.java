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

package com.mnubo.platform.android.sdk.api.services.buffer.impl;

import android.util.Log;

import com.mnubo.platform.android.sdk.api.services.buffer.MnuboBufferService;
import com.mnubo.platform.android.sdk.api.store.MnuboDataStore;
import com.mnubo.platform.android.sdk.api.store.MnuboEntity;
import com.mnubo.platform.android.sdk.internal.connect.connection.MnuboConnectionManager;
import com.mnubo.platform.android.sdk.internal.tasks.Task;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSampleOnPublicSensorTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSamplesTask;

import org.apache.commons.lang3.Validate;

import java.util.List;

import static com.mnubo.platform.android.sdk.Strings.SDK_BUFFER_SERVICE_RETRYING;
import static com.mnubo.platform.android.sdk.Strings.SDK_BUFFER_SERVICE_RETRY_TASK;
import static com.mnubo.platform.android.sdk.Strings.SDK_DATA_STORE_UNABLE_TO_RETRIEVE;

/**
 * MnuboBufferServiceImpl implementation use a MnuboDataStore to persist the failed attempts. The
 * failed attempts are persisted to the [failed] directory.
 * <p/>
 * The Task that can be persisted at the moment are: <ul> <li>{@link
 * com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSamplesTask}</li> <li>{@link
 * com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSampleOnPublicSensorTask}</li>
 * </ul>
 */
public class MnuboBufferServiceImpl implements MnuboBufferService {

    private final static String TAG = MnuboBufferServiceImpl.class.getName();

    private final static String RETRY_QUEUE_NAME = "failed";

    private final MnuboDataStore mnuboDataStore;
    private final MnuboConnectionManager mnuboConnectionManager;

    private boolean isEnabled = false;

    public MnuboBufferServiceImpl(MnuboDataStore mnuboDataStore,
                                  MnuboConnectionManager mnuboConnectionManager) {
        Validate.notNull(mnuboDataStore, "The store cannot be null.");
        this.mnuboDataStore = mnuboDataStore;
        this.mnuboConnectionManager = mnuboConnectionManager;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * {@inheritDoc}
     */
    public int getFailedAttemptsCount() {

        final List<MnuboEntity> queue = mnuboDataStore.getEntities(RETRY_QUEUE_NAME);
        return queue != null ? queue.size() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void retryFailedAttempts() {
        List<MnuboEntity> entities = mnuboDataStore.getEntities(RETRY_QUEUE_NAME);

        if (entities != null) {
            Log.d(TAG, String.format(SDK_BUFFER_SERVICE_RETRYING, entities.size()));

            for (MnuboEntity entity : entities) {
                if (entity != null) {
                    if (entity.getValue() instanceof AddSamplesTask) {
                        AddSamplesTask addSamplesTask = (AddSamplesTask) entity.getValue();
                        Log.d(TAG, String.format(SDK_BUFFER_SERVICE_RETRY_TASK, addSamplesTask));
                        addSamplesTask.executeSync(mnuboConnectionManager, taskFailedAttemptCallback);
                    } else if (entity.getValue() instanceof AddSampleOnPublicSensorTask) {
                        AddSampleOnPublicSensorTask addSampleOnPublicSensorTask = (AddSampleOnPublicSensorTask) entity.getValue();
                        Log.d(TAG, String.format(SDK_BUFFER_SERVICE_RETRY_TASK, addSampleOnPublicSensorTask));
                        addSampleOnPublicSensorTask.executeSync(mnuboConnectionManager, taskFailedAttemptCallback);
                    }
                }

                mnuboDataStore.remove(entity);
            }
        } else {
            Log.d(TAG, SDK_DATA_STORE_UNABLE_TO_RETRIEVE);
        }

    }

    /**
     * This FailedAttemptCallback writes the failed Task to the disk.
     *
     * @return callback to save the Task in the store
     */
    @Override
    public FailedAttemptCallback getFailedAttemptCallback() {
        return this.taskFailedAttemptCallback;
    }

    private final FailedAttemptCallback taskFailedAttemptCallback =
            new FailedAttemptCallback() {
                @Override
                public void onFailure(Task failedTask) {
                    mnuboDataStore.put(RETRY_QUEUE_NAME, failedTask);
                }

            };
}
