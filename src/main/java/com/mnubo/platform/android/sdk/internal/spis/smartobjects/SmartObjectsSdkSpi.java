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

package com.mnubo.platform.android.sdk.internal.spis.smartobjects;

import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.common.ValueType;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;
import com.mnubo.platform.android.sdk.models.users.Users;

import java.util.List;

public interface SmartObjectsSdkSpi {
    void delete(SdkId objectId);

    SmartObject create(SmartObject object, boolean updateIfExists);

    void update(SdkId objectId, SmartObject object);

    SmartObject findOne(SdkId objectId);

    SmartObject findOne(SdkId objectId, List<String> attributes);

    Samples searchSamples(SdkId objectId, String sensorName);

    Samples searchSamples(SdkId objectId, String sensorName, ValueType value);

    Samples searchSamples(SdkId objectId, String sensorName, ValueType value,
                          String from, String to);

    Samples searchSamples(SdkId objectId, String sensorName, ValueType value,
                          String from, String to, int resultSizeLimit);

    void addSamples(SdkId objectId, Samples samples);

    void addSampleOnPublicSensor(SdkId objectId, String sensorName, Sample expectedSample);

    Users listOwnersHistory(SdkId objectid);

    Users listOwnersHistory(SdkId objectid, boolean details);

}
