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

package com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects;

import com.mnubo.platform.android.sdk.internal.connect.connection.MnuboConnectionManager;
import com.mnubo.platform.android.sdk.internal.tasks.impl.TaskWithRefreshImpl;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;

public class AddSampleOnPublicSensorTask extends TaskWithRefreshImpl<Boolean> {

    private final SdkId id;
    private final String sensorName;
    private final Sample sample;


    public AddSampleOnPublicSensorTask(SdkId id, String sensorName, Sample sample) {

        this.id = id;
        this.sample = sample;
        this.sensorName = sensorName;

    }

    @Override
    protected Boolean executeMnuboCall(MnuboConnectionManager connectionManager) {
        connectionManager.getCurrentConnection().getMnuboSDKApi().objectService().addSampleOnPublicSensor(id, sensorName, sample);
        return true;
    }

    @Override
    public String toString() {
        return "AddSampleOnPublicSensorTask{" +
                "id=" + id +
                ", sensorName='" + sensorName + '\'' +
                ", sample=" + sample +
                '}';
    }
}