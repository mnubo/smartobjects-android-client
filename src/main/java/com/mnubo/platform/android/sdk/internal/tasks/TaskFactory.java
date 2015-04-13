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

import com.mnubo.platform.android.sdk.internal.connect.connection.MnuboConnectionManager;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.RefreshableConnection;
import com.mnubo.platform.android.sdk.internal.tasks.impl.authentication.LogInTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.ConfirmPasswordResetTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.ConfirmUserCreationTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.CreateUserTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.ResetPasswordTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSampleOnPublicSensorTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSamplesTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.CreateObjectTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.DeleteObjectTask;
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

public class TaskFactory {

    public static Task<Boolean> newLogInTask(MnuboConnectionManager connectionManager, String username, String password) {
        return new LogInTask(connectionManager, username, password);
    }

    public static Task<SmartObjects> newFindUserObjectsTask(RefreshableConnection refreshableConnection, String username, boolean details, String objectModelName) {
        return new FindUserObjectsTask(refreshableConnection, username, details, objectModelName);
    }

    public static Task<User> newGetUserTask(RefreshableConnection refreshableConnection, String username) {
        return new GetUserTask(refreshableConnection, username);
    }

    public static Task<Boolean> newUpdateUserTask(RefreshableConnection refreshableConnection, String username, User updatedUser) {
        return new UpdateUserTask(refreshableConnection, username, updatedUser);
    }

    public static Task<Boolean> newUpdatePasswordTask(RefreshableConnection refreshableConnection, String username, UpdatePassword password) {
        return new UpdatePasswordTask(refreshableConnection, username, password);
    }

    public static Task<SmartObject> newFindObjectTask(RefreshableConnection refreshableConnection, SdkId objectId) {
        return new FindObjectTask(refreshableConnection, objectId);
    }

    public static Task<Boolean> newUpdateObjectTask(RefreshableConnection refreshableConnection, SdkId objectId, SmartObject updatedObject) {
        return new UpdateObjectTask(refreshableConnection, objectId, updatedObject);
    }

    public static Task<Samples> newSearchSamplesTask(RefreshableConnection refreshableConnection, SdkId objectId, String sensorName) {
        return new SearchSamplesTask(refreshableConnection, objectId, sensorName);
    }

    public static Task<Boolean> newAddSamplesTask(RefreshableConnection refreshableConnection, SdkId objectId, Samples samples) {
        return new AddSamplesTask(refreshableConnection, objectId, samples);
    }

    public static Task<Boolean> newAddSamplesOnPublicSensorTask(RefreshableConnection refreshableConnection, SdkId objectId, String sensorName, Sample sample) {
        return new AddSampleOnPublicSensorTask(refreshableConnection, objectId, sensorName, sample);
    }

    public static Task<Boolean> newCreateObjectTask(RefreshableConnection refreshableConnection, SmartObject object, Boolean updateIfExists) {
        return new CreateObjectTask(refreshableConnection, object, updateIfExists);
    }

    public static Task<Boolean> newDeleteObjectTask(RefreshableConnection refreshableConnection, SdkId id) {
        return new DeleteObjectTask(refreshableConnection, id);
    }

    public static Task<Boolean> newCreateUserTask(RefreshableConnection refreshableConnection, User object) {
        return new CreateUserTask(refreshableConnection, object);
    }

    public static Task<Boolean> newConfirmUserCreationTask(RefreshableConnection refreshableConnection, String username, UserConfirmation confirmation) {
        return new ConfirmUserCreationTask(refreshableConnection, username, confirmation);
    }

    public static Task<Boolean> newResetPasswordTask(RefreshableConnection refreshableConnection, String username) {
        return new ResetPasswordTask(refreshableConnection, username);
    }

    public static Task<Boolean> newConfirmPasswordResetTask(RefreshableConnection refreshableConnection, String username, ResetPassword password) {
        return new ConfirmPasswordResetTask(refreshableConnection, username, password);
    }
}
