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
    /* MnuboConnectionManager */
    public final static String USER_CONNECTION_AVAILABLE = "Returning available user connection.";
    public final static String CLIENT_CONNECTION_AVAILABLE = "Unavailable user connection, returning client connection.";
    public final static String USER_LOG_OUT = "User logs out.";
    public final static String USER_LOG_IN = "User log in. Username : [%s]";

    /* ClientConnection */
    public final static String FETCH_CLIENT_TOKEN_SUCCESS = "Client token was successfully acquired.";
    public final static String FETCH_CLIENT_TOKEN = "Fetching client token...";
    public final static String BUILDING_CLIENT_CONNECTION = "Client connection unavailable, building it.";
    public final static String CLIENT_CONNECTION_API = "Returning a client connection mnubo API.";
    public final static String CLIENT_CONNECTION_UNAVAILABLE = "Impossible to use a working client connection.";

    /* UserConnection */
    public final static String FETCH_USER_TOKEN_SUCCESS = "User token was successfully acquired.";
    public final static String FETCH_USER_TOKEN = "Fetching user token...";
    public final static String FETCH_REFRESH_USER_TOKEN_SUCCESS = "User token was successfully refreshed.";
    public final static String FETCH_REFRESH_USER_TOKEN = "Refreshing user token...";
    public final static String PERSIST_USER_TOKEN = "Persist user connection.";
    public final static String UPDATE_PERSISTED_USER_TOKEN = "Persist user connection.";
    public final static String USER_CONNECTION_UNAVAILABLE = "No user connection available.";
    public final static String REMOVE_PERSISTED_USER_CONNECTION = "Removing all user connections.";

    /* Init */
    public final static String SDK_INITIALIZED = "The Mnubo SDK has successfully started.";
    public final static String SDK_URL_DEBUG = "Building Mnubo API url with the following : [protocol=%s], [hostname=%s], [port=%s]";


    /* Tasks */
    public final static String SDK_ENABLE_DATA_STORE = "The SDK will now persist failed attempts to [%s].";
    public final static String SDK_DATA_STORE_RETRYING = "Retrying [%s] previously failed attempts.";
    public final static String SDK_DATA_STORE_UNABLE_TO_RETRIEVE = "Unable to retrieve failed attempts.";


    /* Tasks */
    public final static String SDK_EXECUTING_TASK = "Executing task.";
    public final static String SDK_ERROR_EXECUTING_TASK = "Error executing task : ";

}
