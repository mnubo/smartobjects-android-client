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

package com.mnubo.platform.android.sdk.api.operations;

import com.mnubo.platform.android.sdk.models.security.UpdatePassword;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;
import com.mnubo.platform.android.sdk.models.users.User;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;

/**
 * The UserOperations interface allows you to query the mnubo API the user resources of the
 * mnubo API.
 */
public interface UserOperations {
    /**
     * Fetch all of the objects that belongs to the username. Only minimal information about the
     * objects in the list will be returned.
     *
     * @param username           owner of the objects
     * @param completionCallBack the callback that will be executed on completion of the request
     * @see com.mnubo.platform.android.sdk.api.operations.UserOperations#findUserObjects(String, Boolean, com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack)
     */
    void findUserObjects(String username, CompletionCallBack<SmartObjects> completionCallBack);

    /**
     * Fetch all of the objects that belongs to the username.
     *
     * @param username           owner of the objects
     * @param details            true will return all data, false will return minimal data
     * @param completionCallBack the callback that will be executed on completion of the request
     * @see com.mnubo.platform.android.sdk.api.operations.UserOperations#findUserObjects(String, Boolean, String, com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack)
     */
    void findUserObjects(String username, final Boolean details, CompletionCallBack<SmartObjects> completionCallBack);

    /**
     * Fetch all of the objects that belongs to the username that has a specific object model.
     *
     * @param username           owner of the objects
     * @param details            true will return all data, false will return minimal data
     * @param objectModelName    specific object model name
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void findUserObjects(String username, final Boolean details, final String objectModelName, CompletionCallBack<SmartObjects> completionCallBack);

    /**
     * Fetch a specific user information.
     *
     * @param username           username of the user
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void getUser(String username, CompletionCallBack<User> completionCallBack);

    /**
     * This method allow you to update user's information
     *
     * @param username           username of the user to be updated
     * @param updatedUser        the user's data
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void update(String username, User updatedUser, CompletionCallBack<Boolean> completionCallBack);

    /**
     * This method allow you to update user's password. The user has to be currently logged in
     * to use this method.
     *
     * @param username           username of the user to be updated
     * @param newPassword        the <code>UpdatePassword</code> contains the old password, the new
     *                           password and it's confirmation.
     * @param completionCallBack the callback that will be executed on completion of the request
     * @see com.mnubo.platform.android.sdk.api.operations.ClientOperations#resetPassword(String, com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack)
     */
    void updatePassword(String username, UpdatePassword newPassword, CompletionCallBack<Boolean> completionCallBack);
}
