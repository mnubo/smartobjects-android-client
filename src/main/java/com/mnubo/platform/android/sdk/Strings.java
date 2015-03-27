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

package com.mnubo.platform.android.sdk;

/**
 * Various strings used in the SDK
 */
public interface Strings {
    public static String FETCH_CLIENT_TOKEN_SUCCESS = "Client token was successfully acquired.";
    public static String FETCH_USER_TOKEN_SUCCESS = "User token was successfully acquired.";
    public static String FETCH_REFRESH_USER_TOKEN_SUCCESS = "User token was successfully refreshed.";

    public static String SDK_INITIALIZED = "The Mnubo SDK has successfully started.";

    public static String EXCEPTION_SDK = "Error consuming Mnubo REST API";
    public static String EXCEPTION_ALREADY_INITIALIZED = "Mnubo.init() method can only be called once.";
    public static String EXCEPTION_NOT_INITIALIZED = "Mnubo.init() must be called prior to this call.";
    public static String EXCEPTION_REFRESH_TOKEN_EXPIRED = "Connection cannot be refreshed, the token has expired. Consider signing back in.";
    public static String EXCEPTION_CANCELLED_OPERATION = "The operation was cancelled.";
    public static String EXCEPTION_BAD_CREDENTIALS = "Invalid credentials were supplied.";
    public static String EXCEPTION_RESET_PASSWORD_DISABLED = "Reset password is not enabled for the consumer key used.";
    public static String EXCEPTION_UNKNOWN_USER = "The user was not found.";
    public static String EXCEPTION_INVALID_HOSTNAME = "Provided hostname is invalid. The hostname is [example.com] is this URL https://example.com:443.";
    public static String EXCEPTION_NOT_LOGGED_IN = "The user is not logged in. Please log in before using these operations.";
    public static String EXCEPTION_CLIENT_CONNECTION_UNAVAILABLE = "Unable to authenticate as the client.";
    public static String EXCEPTION_USER_DISABLED = "The user is disabled. Either manually deactivated or never activated after registration.";
    public static String EXCEPTION_ACCESS_DENIED = "The access was denied by the Mnubo API.";
    public static String EXCEPTION_INVALID_REGISTRATION_TOKEN = "The token provided to confirm the user registration is invalid.";
    public static String EXCEPTION_INVALID_PREVIOUS_PASSWORD = "The provided previous password is incorrect. The password was not updated.";
    public static String EXCEPTION_OBJECT_NOT_FOUND = "The requested object was not found.";
    public static String EXCEPTION_USER_ALREADY_EXIST = "The username you are trying to register already exists.";

}
