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
import com.mnubo.android.api.operations.OwnerOperations;
import com.mnubo.android.exceptions.MnuboException;
import com.mnubo.android.internal.connect.MnuboConnectionManager;
import com.mnubo.android.internal.services.OwnerService;
import com.mnubo.android.internal.services.impl.OwnerServiceImpl;
import com.mnubo.android.models.Owner;
import com.mnubo.android.models.SmartObject;

import java.util.concurrent.Callable;

import okhttp3.HttpUrl;

import static com.mnubo.android.utils.TaskUtils.executeAsync;

public class OwnerOperationsImpl implements OwnerOperations {

    private final OwnerService ownerService;

    public OwnerOperationsImpl(MnuboConnectionManager mnuboConnectionManager, HttpUrl baseUrl) {
        this.ownerService = new OwnerServiceImpl(mnuboConnectionManager, baseUrl);
    }

    @Override
    public void delete() throws MnuboException {
        ownerService.delete();
    }

    @Override
    public void deleteAsync(CompletionCallback<Void> callback) {
        executeAsync(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ownerService.delete();
                return null;
            }
        }, callback);
    }

    @Override
    public void updateAsync(final Owner owner, CompletionCallback<Void> callback) {
        executeAsync(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ownerService.update(owner);
                return null;
            }
        }, callback);
    }
    @Override
    public void update(Owner owner) throws MnuboException {
        ownerService.update(owner);
    }

    @Override
    public void create(String password, Owner owner) throws MnuboException {
        ownerService.create(password, owner);
    }

    @Override
    public void createAsync(final String password, final Owner owner, CompletionCallback<Void> callback) {
        executeAsync(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ownerService.create(password, owner);
                return null;
            }
        }, callback);
    }

    @Override
    public void createObject(final String deviceId, final String objectType, final SmartObject smartObject) throws MnuboException {
        ownerService.createObject(deviceId, objectType, smartObject);
    }

    @Override
    public void createObjectAsync(final String deviceId, final String objectType, final SmartObject smartObject, CompletionCallback<Void> callback) {
        executeAsync(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ownerService.createObject(deviceId, objectType, smartObject);
                return null;
            }
        }, callback);
    }

    @Override
    public void deleteObject(String deviceId) throws MnuboException {
        ownerService.deleteObject(deviceId);
    }

    @Override
    public void deleteObjectAsync(final String deviceId, CompletionCallback<Void> callback) {
        executeAsync(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ownerService.deleteObject(deviceId);
                return null;
            }
        }, callback);
    }
}
