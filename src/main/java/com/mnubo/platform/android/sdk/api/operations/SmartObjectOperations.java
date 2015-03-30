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

package com.mnubo.platform.android.sdk.api.operations;

import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;

/**
 * The SmartObjectOperations is an interface to query the object resources of the
 * mnubo API.
 *
 * @see com.mnubo.platform.android.sdk.models.common.SdkId
 * @see com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack
 */
public interface SmartObjectOperations {
    /**
     * This function is used to fetch an object on the mnubo API.
     *
     * @param objectId           SdkId built with a device_id or an object_id
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void findObject(SdkId objectId, CompletionCallBack<SmartObject> completionCallBack);

    /**
     * This function update the object matching the provided id with the information contained in
     * the provided <code>object</code>.
     *
     * @param objectId           SdkId built with a device_id or an object_id
     * @param object             the updated {link SmartObject}
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void update(SdkId objectId, SmartObject object, CompletionCallBack<Boolean> completionCallBack);

    /**
     * This function will fetch all of the samples recorded for a specific object's sensor.
     *
     * @param objectId           SdkId built with a device_id or an object_id
     * @param sensorName         the name of the sensor the <code>Samples</code> will be fetched from.
     *                           that sensor must belong to the object matching the <code>objectId</code>
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void searchSamples(SdkId objectId, String sensorName, CompletionCallBack<Samples> completionCallBack);

    /**
     * This function will add samples data to the specified object's sensor synchronously.
     * This function supports offline data store if the request fails.
     *
     * @param objectId SdkId built with a device_id or an object_id
     * @param samples  the list of <code>Samples</code> to be recorded
     */
    void addSamples(SdkId objectId, Samples samples);

    /**
     * This function will add samples data to the specified object's sensor asynchronously.
     * This function supports offline data store if the request fails.
     *
     * @param objectId           SdkId built with a device_id or an object_id
     * @param samples            the list of <code>Samples</code> to be recorded
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void addSamples(SdkId objectId, Samples samples, CompletionCallBack<Boolean> completionCallBack);

    /**
     * This function allows you to add one sample data to a publicly available object's sensor
     * synchronously.
     * This function supports offline data store if the request fails.
     *
     * @param objectId   SdkId built with a device_id or an object_id
     * @param sensorName the name of the sensor the <code>Samples</code> will be fetched from.
     *                   that sensor must belong to the object matching the <code>objectId</code>
     * @param sample     the <code>Sample</code> to be added
     */
    void addSampleOnPublicSensor(SdkId objectId, String sensorName, Sample sample);

    /**
     * This function allows you to add one sample data to a publicly available object's sensor
     * asynchronously.
     * This function supports offline data store if the request fails.
     *
     * @param objectId           SdkId built with a device_id or an object_id
     * @param sensorName         the name of the sensor the <code>Samples</code> will be fetched from.
     *                           that sensor must belong to the object matching the <code>objectId</code>
     * @param sample             the <code>Sample</code> to be added
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void addSampleOnPublicSensor(SdkId objectId, String sensorName, Sample sample, CompletionCallBack<Boolean> completionCallBack);

    /**
     * This function allows you to add an object in the mnubo platform.
     *
     * @param smartObject        the <code>SmartObject</code> to be added
     *                           Its owner field must be set to the username of the user who owns perform
     *                           the action.
     * @param updateIfExists     the object will be updated it exists, if false and the object exist an
     *                           error will be sent in the callback
     *                           {link com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallbacl#onCompletion} method
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void createObject(SmartObject smartObject, Boolean updateIfExists, CompletionCallBack<Boolean> completionCallBack);
}
