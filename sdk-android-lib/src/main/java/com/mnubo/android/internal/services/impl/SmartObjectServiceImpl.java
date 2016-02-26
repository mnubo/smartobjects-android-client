/*
 * Copyright (c) 2016 Mnubo. Released under MIT License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.mnubo.android.internal.services.impl;

import com.mnubo.android.exceptions.MnuboException;
import com.mnubo.android.internal.connect.MnuboConnectionManager;
import com.mnubo.android.internal.services.SmartObjectService;
import com.mnubo.android.models.SmartObject;

import lombok.NonNull;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.mnubo.android.models.SmartObject.OBJECTS_PATH;
import static com.mnubo.android.utils.HttpUtils.addPathVariables;
import static com.mnubo.android.utils.HttpUtils.buildBody;
import static com.mnubo.android.utils.HttpUtils.executeAndThrowOnFailure;

public class SmartObjectServiceImpl extends AbstractMnuboService implements SmartObjectService {

    public SmartObjectServiceImpl(MnuboConnectionManager mnuboConnectionManager, HttpUrl platformBaseUrl) {
        super(mnuboConnectionManager, platformBaseUrl, OBJECTS_PATH);
    }

    @Override
    public void update(@NonNull String deviceId, @NonNull SmartObject smartObject) throws MnuboException {
        Request request =
                requestBuilder()
                        .url(addPathVariables(getUrl(), deviceId))
                        .put(buildBody(smartObject))
                        .build();
        executeAndThrowOnFailure(getOkHttpClient(), request);
    }
}
