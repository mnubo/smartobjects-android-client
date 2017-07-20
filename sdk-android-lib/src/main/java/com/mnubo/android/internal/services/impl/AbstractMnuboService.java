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
import com.mnubo.android.internal.connect.interceptor.AccessTokenAuthenticationInterceptor;
import com.mnubo.android.utils.HttpUtils;

import lombok.Getter;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

abstract class AbstractMnuboService {

    @Getter
    private final HttpUrl url;

    @Getter
    private final HttpUrl platformBaseUrl;

    private final MnuboConnectionManager mnuboConnectionManager;

    AbstractMnuboService(MnuboConnectionManager mnuboConnectionManager,
                         HttpUrl platformBaseUrl,
                         String path) {
        this.mnuboConnectionManager = mnuboConnectionManager;
        this.platformBaseUrl = platformBaseUrl;
        this.url = platformBaseUrl.newBuilder()
                .addPathSegment(path)
                .build();
    }

    protected String getUsername() {
        return mnuboConnectionManager.getUsername();
    }

    protected Request.Builder requestBuilder() {
        return new Request.Builder()
                .url(getUrl());
    }

    protected OkHttpClient getOkHttpClient() throws MnuboException {
        return mnuboConnectionManager.getUserAuthenticatedHttpClient();
    }
}
