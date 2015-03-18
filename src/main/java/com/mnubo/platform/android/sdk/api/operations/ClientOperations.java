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

import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.models.security.ResetPassword;
import com.mnubo.platform.android.sdk.models.security.UserConfirmation;
import com.mnubo.platform.android.sdk.models.users.User;

/**
 * The ClientOperations is an interface to perform queries on the mnubo API as the application.
 * These operations require the consumer key and secret, but now a user logged it to be successful.
 */
public interface ClientOperations {

    /**
     * This function allows you to register an user on the mnubo API.
     *
     * @param user               the <code>User</code> that contains all the information
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void createUser(User user, MnuboApi.CompletionCallBack<Boolean> completionCallBack);

    /**
     * @param username           the username of the user that need a confirmation
     * @param confirmation       the <code>UserConfirmation</code> contains user's password and
     *                           the confirmation token received by email
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void confirmUserCreation(String username, UserConfirmation confirmation, MnuboApi.CompletionCallBack<Boolean> completionCallBack);

    /**
     * This functions allows you to initiate the reset  password for the specified username.
     *
     * @param username           the username of the user that need a password reset
     * @param completionCallBack the callback that will be executed on completion of the request
     * @see com.mnubo.platform.android.sdk.api.operations.ClientOperations#confirmPasswordReset(String, com.mnubo.platform.android.sdk.models.security.ResetPassword, com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack)
     */
    void resetPassword(String username, MnuboApi.CompletionCallBack<Boolean> completionCallBack);

    /**
     * This function allows you to finish the password reset process of a specific user.\
     *
     * @param username           the username of the user that completes a password reset
     * @param resetPassword      the <code>ResetPassword</code> contains the new password and
     *                           a confirmation with the token the user has received
     * @param completionCallBack the callback that will be executed on completion of the request
     * @see com.mnubo.platform.android.sdk.api.operations.ClientOperations#resetPassword(String, com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack)
     */
    void confirmPasswordReset(String username, ResetPassword resetPassword, MnuboApi.CompletionCallBack<Boolean> completionCallBack);
}
