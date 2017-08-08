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

package com.mnubo.android;

import android.content.Context;
import android.util.Log;

import com.mnubo.android.api.AuthenticationProblemCallback;
import com.mnubo.android.api.MnuboApi;
import com.mnubo.android.api.MnuboStore;
import com.mnubo.android.config.MnuboSDKConfig;
import com.mnubo.android.config.SupportedIsp;
import com.mnubo.android.exceptions.MnuboAlreadyInitializedException;
import com.mnubo.android.exceptions.MnuboNotInitializedException;
import com.mnubo.android.internal.connect.MnuboConnectionManager;

import lombok.NonNull;

import static com.mnubo.android.internal.connect.MnuboConnectionManager.newMnuboConnectionManager;

/**
 * This class is the entry point to use the SDK. The init method must be called prior to anything
 * else.
 */
public class Mnubo {

    private static final String TAG = Mnubo.class.getName();
    private final static String SDK_INITIALIZED = "The Mnubo SDK has successfully started.";


    private final MnuboConnectionManager mnuboConnectionManager;
    private final MnuboStore mnuboStore;
    private final MnuboSDKConfig config;

    private static Mnubo instance = null;

    private Mnubo(@NonNull Context applicationContext,
                  @NonNull MnuboSDKConfig config,
                  @NonNull AuthenticationProblemCallback authenticationProblemCallback) {
        this.config = config;
        this.mnuboConnectionManager = newMnuboConnectionManager(config, applicationContext, authenticationProblemCallback);
        this.mnuboStore = new MnuboStore(applicationContext.getCacheDir());
    }

    /**
     * Init the Mnubo SDK. This method can only be called once.
     *
     * @param context Android context used for the connection store and the datastore
     * @param config config to reach Mnubo apis
     * @param authenticationProblemCallback callback to use when an authentication error occurs
     * @throws MnuboAlreadyInitializedException
     */
    public static void init(@NonNull Context context,
                            @NonNull MnuboSDKConfig config,
                            @NonNull AuthenticationProblemCallback authenticationProblemCallback) throws MnuboAlreadyInitializedException {
        if (instance != null) {
            throw new MnuboAlreadyInitializedException();
        }

        instance = new Mnubo(context, config, authenticationProblemCallback);

        Log.d(TAG, SDK_INITIALIZED);
    }

    /**
     * Returns a MnuboApi instance to be used to perform calls against the Mnubo system.
     *
     * @return an instance of {@link com.mnubo.android.api.MnuboApi}
     * @throws MnuboNotInitializedException if Mnubo.init was not called prior to this call
     */
    public static MnuboApi getApi() throws MnuboNotInitializedException {
        if (instance == null) {
            throw new MnuboNotInitializedException();
        }
        return new MnuboApi(instance.mnuboConnectionManager, instance.config);
    }

    /**
     * Returns the MnuboStore instance to be used to read and write data.
     *
     * @return an instance of {@link com.mnubo.android.api.MnuboStore}
     * @throws MnuboNotInitializedException if Mnubo.init was not called prior to this call
     */
    public static MnuboStore getStore() throws MnuboNotInitializedException {
        if (instance == null) {
            throw new MnuboNotInitializedException();
        }
        return instance.mnuboStore;
    }

    /**
     * Perform a login operation, you should run this asynchronously as it performs Network IO
     * @param username of the owner
     * @param password of the owner
     * @return true if succeed and a connection was acquired, false if it failed
     * @throws MnuboNotInitializedException if Mnubo.init was not called prior to this call
     */
    public static boolean logIn(String username, String password) throws MnuboNotInitializedException {
        if (instance == null) {
            throw new MnuboNotInitializedException();
        }
        return instance.mnuboConnectionManager.logIn(username, password);
    }

    /**
     * Perform a login operation, you should run this asynchronously as it performs Network IO
     * @param username username from the service provider used
     * @param token token from the service provider used, the token should belong to the username
     * @param isp identity service provider you used to fetch the token
     * @return true if succeed and a connection was acquired, false if it failed
     * @throws MnuboNotInitializedException if Mnubo.init was not called prior to this call
     */
    public static boolean logIn(String username, String token, SupportedIsp isp) throws MnuboNotInitializedException {
        if (instance == null) {
            throw new MnuboNotInitializedException();
        }
        return instance.mnuboConnectionManager.logIn(username, token, isp);
    }

    /**
     * Clears any connection available. Also clears the connection store.
     * @throws MnuboNotInitializedException if Mnubo.init was not called prior to this call
     */
    public static void logOut() throws MnuboNotInitializedException {
        if (instance == null) {
            throw new MnuboNotInitializedException();
        }
        instance.mnuboConnectionManager.logOut();
    }

    /**
     * Retrieve the username of the logged in owner.
     * @return a String which is the username
     * @throws MnuboNotInitializedException if Mnubo.init was not called prior to this call
     */
    public static String getUsername() throws MnuboNotInitializedException {
        if (instance == null) {
            throw new MnuboNotInitializedException();
        }
        return instance.mnuboConnectionManager.getUsername();
    }

    /**
     * @return true if logged in, false otherwise
     * @throws MnuboNotInitializedException if Mnubo.init was not called prior to this call
     */
    public static boolean isLoggedIn() throws MnuboNotInitializedException {
        if (instance == null) {
            throw new MnuboNotInitializedException();
        }
        return instance.mnuboConnectionManager.isLoggedIn();
    }
}
