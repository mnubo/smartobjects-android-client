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

package com.mnubo.android.api;

import com.mnubo.android.api.operations.EventOperations;
import com.mnubo.android.api.operations.OwnerOperations;
import com.mnubo.android.api.operations.SmartObjectOperations;
import com.mnubo.android.api.operations.impl.EventOperationsImpl;
import com.mnubo.android.api.operations.impl.OwnerOperationsImpl;
import com.mnubo.android.api.operations.impl.SmartObjectOperationsImpl;
import com.mnubo.android.config.MnuboSDKConfig;
import com.mnubo.android.internal.connect.MnuboConnectionManager;

import lombok.Value;

/**
 * Mnubo API that is used by the application developers to perform requests on the Mnubo system.
 */
@Value
public class MnuboApi {

    private final OwnerOperations ownerOperations;

    private final SmartObjectOperations smartObjectOperations;

    private final EventOperations eventOperations;

    public MnuboApi(MnuboConnectionManager connectionManager, MnuboSDKConfig config) {

        this.ownerOperations = new OwnerOperationsImpl(connectionManager, config.getIngestionUrl());
        this.smartObjectOperations = new SmartObjectOperationsImpl(connectionManager,config.getIngestionUrl());
        this.eventOperations = new EventOperationsImpl(connectionManager, config.getIngestionUrl());
    }

}
