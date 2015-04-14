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

    public static Task<Boolean> newLogInTask(String username, String password) {
        return new LogInTask(username, password);
    }

    public static Task<SmartObjects> newFindUserObjectsTask(String username, boolean details, String objectModelName) {
        return new FindUserObjectsTask(username, details, objectModelName);
    }

    public static Task<User> newGetUserTask(String username) {
        return new GetUserTask(username);
    }

    public static Task<Boolean> newUpdateUserTask(String username, User updatedUser) {
        return new UpdateUserTask(username, updatedUser);
    }

    public static Task<Boolean> newUpdatePasswordTask(String username, UpdatePassword password) {
        return new UpdatePasswordTask(username, password);
    }

    public static Task<SmartObject> newFindObjectTask(SdkId objectId) {
        return new FindObjectTask(objectId);
    }

    public static Task<Boolean> newUpdateObjectTask(SdkId objectId, SmartObject updatedObject) {
        return new UpdateObjectTask(objectId, updatedObject);
    }

    public static Task<Samples> newSearchSamplesTask(SdkId objectId, String sensorName) {
        return new SearchSamplesTask(objectId, sensorName);
    }

    public static Task<Boolean> newAddSamplesTask(SdkId objectId, Samples samples) {
        return new AddSamplesTask(objectId, samples);
    }

    public static Task<Boolean> newAddSamplesOnPublicSensorTask(SdkId objectId, String sensorName, Sample sample) {
        return new AddSampleOnPublicSensorTask(objectId, sensorName, sample);
    }

    public static Task<Boolean> newCreateObjectTask(SmartObject object, Boolean updateIfExists) {
        return new CreateObjectTask(object, updateIfExists);
    }

    public static Task<Boolean> newDeleteObjectTask(SdkId id) {
        return new DeleteObjectTask(id);
    }

    public static Task<Boolean> newCreateUserTask(User object) {
        return new CreateUserTask(object);
    }

    public static Task<Boolean> newConfirmUserCreationTask(String username, UserConfirmation confirmation) {
        return new ConfirmUserCreationTask(username, confirmation);
    }

    public static Task<Boolean> newResetPasswordTask(String username) {
        return new ResetPasswordTask(username);
    }

    public static Task<Boolean> newConfirmPasswordResetTask(String username, ResetPassword password) {
        return new ConfirmPasswordResetTask(username, password);
    }
}
