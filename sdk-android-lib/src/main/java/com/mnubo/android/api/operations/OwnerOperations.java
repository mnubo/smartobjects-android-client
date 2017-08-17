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

package com.mnubo.android.api.operations;

import com.mnubo.android.api.CompletionCallback;
import com.mnubo.android.exceptions.MnuboException;
import com.mnubo.android.internal.services.OwnerService;
import com.mnubo.android.models.Owner;
import com.mnubo.android.models.SmartObject;

/**
 * The OwnerOperations interface allows you to query the mnubo API the user resources of the mnubo
 * API.
 */
public interface OwnerOperations extends OwnerService {

    void deleteAsync(CompletionCallback<Void> callback);

    @Deprecated
    void updateAsync(String username, Owner owner, CompletionCallback<Void> callback);

    @Deprecated
    void update(String username, Owner owner) throws MnuboException;

    void updateAsync(Owner owner, CompletionCallback<Void> callback);

    void createAsync(String password, Owner owner, CompletionCallback<Void> callback);

    void createObjectAsync(String deviceId, String objectType, SmartObject smartObject, CompletionCallback<Void> callback);

    void deleteObjectAsync(String deviceId, CompletionCallback<Void> callback);
}
