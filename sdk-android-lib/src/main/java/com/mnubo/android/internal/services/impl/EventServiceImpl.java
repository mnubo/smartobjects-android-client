/*
 * Copyright (c) 2015 Mnubo. Released under MIT License.
 *
 * 	Permission is hereby granted, free of charge, to any person obtaining a copy
 * 	of this software and associated documentation files (the "Software"), to deal
 * 	in the Software without restriction, including without limitation the rights
 * 	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * 	copies of the Software, and to permit persons to whom the Software is
 * 	furnished to do so, subject to the following conditions:
 *
 * 	The above copyright notice and this permission notice shall be included in
 * 	all copies or substantial portions of the Software.
 *
 * 	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * 	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * 	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * 	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * 	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * 	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * 	THE SOFTWARE.
 */

package com.mnubo.android.internal.services.impl;

import com.mnubo.android.exceptions.MnuboException;
import com.mnubo.android.internal.connect.MnuboConnectionManager;
import com.mnubo.android.internal.services.EventService;
import com.mnubo.android.models.Event;

import java.util.List;

import lombok.NonNull;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.mnubo.android.models.Event.EVENTS;
import static com.mnubo.android.models.SmartObject.OBJECTS_PATH;
import static com.mnubo.android.utils.HttpUtils.addPathVariables;
import static com.mnubo.android.utils.HttpUtils.buildBody;
import static com.mnubo.android.utils.HttpUtils.executeAndThrowOnFailure;


public class EventServiceImpl extends AbstractMnuboService implements EventService {

    public EventServiceImpl(MnuboConnectionManager connectionManager, HttpUrl platformBaseUrl) {
        super(connectionManager, platformBaseUrl, EVENTS);
    }

    @Override
    public void sendEvents(@NonNull String deviceId, @NonNull List<Event> events) throws MnuboException {
        Request request =
                requestBuilder()
                        .url(addPathVariables(getPlatformBaseUrl(), OBJECTS_PATH, deviceId, EVENTS))
                        .post(buildBody(events))
                        .build();
        executeAndThrowOnFailure(getOkHttpClient(), request);
    }
}
