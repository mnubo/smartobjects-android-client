/*
 * Copyright (c) 2017 Mnubo. Released under MIT License.
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

package com.mnubo.android.api.operations.impl;

import com.mnubo.android.api.CompletionCallback;
import com.mnubo.android.api.operations.EventOperations;
import com.mnubo.android.exceptions.MnuboException;
import com.mnubo.android.internal.connect.MnuboConnectionManager;
import com.mnubo.android.internal.services.EventService;
import com.mnubo.android.internal.services.impl.EventServiceImpl;
import com.mnubo.android.models.Event;

import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.HttpUrl;

import static com.mnubo.android.utils.TaskUtils.executeAsync;

public class EventOperationsImpl implements EventOperations {

    private final EventService eventService;

    public EventOperationsImpl(MnuboConnectionManager mnuboConnectionManager, HttpUrl baseUrl) {
        this.eventService = new EventServiceImpl(mnuboConnectionManager, baseUrl);
    }

    @Override
    public void sendEventsAsync(final String deviceId, final List<Event> events, CompletionCallback<Void> callback) {
        executeAsync(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                eventService.sendEvents(deviceId, events);
                return null;
            }
        }, callback);
    }

    @Override
    public void sendEvents(String deviceId, List<Event> events) throws MnuboException {
        eventService.sendEvents(deviceId, events);
    }
}
