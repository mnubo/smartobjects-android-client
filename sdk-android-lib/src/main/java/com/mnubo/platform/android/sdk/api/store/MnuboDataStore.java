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

package com.mnubo.platform.android.sdk.api.store;

import java.util.List;

/**
 * Interface that allows storage of data in queue
 */
public interface MnuboDataStore {

    /**
     * Store the element in the queue.
     *
     * @param queueName   the queue name
     * @param mnuboEntity the entity that will be persisted on disk
     * @return true if storage is successful, false if not
     */
    boolean put(String queueName, Object mnuboEntity);

    /**
     * Remove the element from the store
     *
     * @param entity entity to be removed from the store
     */
    boolean remove(Object entity);

    /**
     * Get all the entities in a folder
     *
     * @param queueName queue name
     * @return List of MnuboEntity if no error, null otherwise
     */
    List<MnuboEntity> getEntities(String queueName);

    /**
     * Override the default size of queues. Default size depends on implementation.
     * Must be greater or equal to 0.
     * @param size maximum number of entities written in a queue,
     *             0 for unlimited
     */
    void setQueueMaximumSize(int size);

    /**
     * Gets the current queue size limit
     */
    int getQueueMaximumSize();
}
