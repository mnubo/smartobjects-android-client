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

package com.mnubo.platform.android.sdk.internal.tasks;

import com.mnubo.platform.android.sdk.internal.tasks.impl.authentication.LogInTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.ConfirmPasswordResetTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.ConfirmUserCreationTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.CreateUserTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.ResetPasswordTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSampleOnPublicSensorTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSamplesTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.CreateObjectTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.FindObjectTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.SearchSamplesTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.UpdateObjectTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.user.FindUserObjectsTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.user.GetUserTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.user.UpdatePasswordTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.user.UpdateUserTask;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.security.ResetPassword;
import com.mnubo.platform.android.sdk.models.security.UpdatePassword;
import com.mnubo.platform.android.sdk.models.security.UserConfirmation;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;
import com.mnubo.platform.android.sdk.models.users.User;

import static com.mnubo.platform.android.sdk.Mnubo.ConnectionOperations;
import static com.mnubo.platform.android.sdk.internal.tasks.Task.ApiFetcher;
import static com.mnubo.platform.android.sdk.internal.tasks.impl.TaskWithRefreshImpl.ConnectionRefresher;

public class TaskFactory {

    public static Task<Boolean> newLogInTask(String username, String password, ConnectionOperations connectionOperations) {
        return new LogInTask(username, password, connectionOperations);
    }

    public static Task<SmartObjects> newFindUserObjectsTask(ApiFetcher apiFetcher, String username, boolean details, String objectModelName, ConnectionRefresher connectionRefresher) {
        return new FindUserObjectsTask(apiFetcher, username, details, objectModelName, connectionRefresher);
    }

    public static Task<User> newGetUserTask(ApiFetcher apiFetcher, String username, ConnectionRefresher connectionRefresher) {
        return new GetUserTask(apiFetcher, username, connectionRefresher);
    }

    public static Task<Boolean> newUpdateUserTask(ApiFetcher apiFetcher, String username, User updatedUser, ConnectionRefresher connectionRefresher) {
        return new UpdateUserTask(apiFetcher, username, updatedUser, connectionRefresher);
    }

    public static Task<Boolean> newUpdatePasswordTask(ApiFetcher apiFetcher, String username, UpdatePassword password, ConnectionRefresher connectionRefresher) {
        return new UpdatePasswordTask(apiFetcher, username, password, connectionRefresher);
    }

    public static Task<SmartObject> newFindObjectTask(ApiFetcher apiFetcher, SdkId objectId, ConnectionRefresher connectionRefresher) {
        return new FindObjectTask(apiFetcher, objectId, connectionRefresher);
    }

    public static Task<Boolean> newUpdateObjectTask(ApiFetcher apiFetcher, SdkId objectId, SmartObject updatedObject, ConnectionRefresher connectionRefresher) {
        return new UpdateObjectTask(apiFetcher, objectId, updatedObject, connectionRefresher);
    }

    public static Task<Samples> newSearchSamplesTask(ApiFetcher apiFetcher, SdkId objectId, String sensorName, ConnectionRefresher connectionRefresher) {
        return new SearchSamplesTask(apiFetcher, objectId, sensorName, connectionRefresher);
    }

    public static Task<Boolean> newAddSamplesTask(ApiFetcher apiFetcher, SdkId objectId, Samples samples, ConnectionRefresher connectionRefresher) {
        return new AddSamplesTask(apiFetcher, objectId, samples, connectionRefresher);
    }

    public static Task<Boolean> newAddSamplesOnPublicSensor(ApiFetcher apiFetcher, SdkId objectId, String sensorName, Sample sample, ConnectionRefresher connectionRefresher) {
        return new AddSampleOnPublicSensorTask(apiFetcher, objectId, sensorName, sample, connectionRefresher);
    }

    public static Task<Boolean> newCreateObjectTask(ApiFetcher apiFetcher, SmartObject object, Boolean updateIfExists, ConnectionRefresher connectionRefresher) {
        return new CreateObjectTask(apiFetcher, object, updateIfExists, connectionRefresher);
    }

    public static Task<Boolean> newCreateUserTask(ApiFetcher apiFetcher, User object, ConnectionRefresher connectionRefresher) {
        return new CreateUserTask(apiFetcher, object, connectionRefresher);
    }

    public static Task<Boolean> newConfirmUserCreationTask(ApiFetcher apiFetcher, String username, UserConfirmation confirmation, ConnectionRefresher connectionRefresher) {
        return new ConfirmUserCreationTask(apiFetcher, username, confirmation, connectionRefresher);
    }

    public static Task<Boolean> newResetPasswordTask(ApiFetcher apiFetcher, String username, ConnectionRefresher connectionRefresher) {
        return new ResetPasswordTask(apiFetcher, username, connectionRefresher);
    }

    public static Task<Boolean> newConfirmPasswordResetTask(ApiFetcher apiFetcher, String username, ResetPassword password, ConnectionRefresher connectionRefresher) {
        return new ConfirmPasswordResetTask(apiFetcher, username, password, connectionRefresher);
    }
}
