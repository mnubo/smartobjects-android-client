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

import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.RefreshableConnection;
import com.mnubo.platform.android.sdk.internal.tasks.impl.TaskWithRefreshImpl;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;

import java.util.HashMap;
import java.util.Map;

public class AddSamplesTask extends TaskWithRefreshImpl<Boolean> {

    final SdkId id;
    final Samples samples;


    public AddSamplesTask(RefreshableConnection refreshableConnection,SdkId id, Samples samples) {
        super(refreshableConnection);
        this.id = id;
        this.samples = samples;

    }

    @Override
    protected Boolean executeMnuboCall() {
        refreshableConnection.getMnuboSDKApi().objectService().addSamples(id, samples);
        return true;
    }

    public SdkId getId() {
        return id;
    }

    public Samples getSamples() {
        return samples;
    }

    public Map<String, Object> getIdData() {
        Map<String, Object> idData = new HashMap<>();
        idData.put("id", id);
        return idData;
    }
}