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

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;

/**
 * The AuthenticationOperations is an interface to handle the user login process.
 */
public interface AuthenticationOperations {

    /**
     * Log in as a user to the mnubo API. It fetch an user access token along with it's
     * refreshUserConnection token that will be persisted on the device.
     *
     * If the access token expires, the refreshUserConnection token is used to refreshUserConnection it. If both have expired,
     * you should ask the user to log in again.
     *
     * @param username           username of the user
     * @param password           password of the user
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void logIn(String username, String password, CompletionCallBack<Boolean> completionCallBack);

    /**
     * Logs out the user. Removes the connection data from the device (access token, refreshUserConnection token,
     * username)
     */
    void logOut();

    /**
     * Useful method to know if a user is currently connected
     *
     * @return true if a user is connected, false otherwise
     */
    Boolean isUserConnected();

    /**
     * This method gives the username of the connected user.
     *
     * @return the username of the currently connected username, null if no use connected.
     */
    String getUsername();
}
