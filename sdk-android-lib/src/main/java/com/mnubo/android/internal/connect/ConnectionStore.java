/*
 * Copyright (c) 2015 Mnubo. Released under MIT License.
 *
 * 	Permission is hereby granted, free of charge, to any person obtaining a copy
 * 	of this software and associated documentation files (the "Software"), to deal
 * 	in the Software without restriction, including without limitation the rights
 * 	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * 	copies of the Software, and to permit persons to whom the Software is
 * 	furnished to do so, subject to the following conditions:
 *
 * 	The above copyright notice and this permission notice shall be included in
 * 	all copies or substantial portions of the Software.
 *
 * 	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * 	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * 	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * 	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * 	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * 	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * 	THE SOFTWARE.
 */

package com.mnubo.android.internal.connect;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.common.base.Preconditions;

import lombok.NonNull;

class ConnectionStore {
    private static final String TAG = ConnectionStore.class.getName();

    private final static String SHARED_PREFERENCES_KEY = "com.mnubo.android.sdk.connectionstore";
    private final static String KEY_PREFIX = "mnubo.";
    private final static String KEY_USERNAME = KEY_PREFIX + MnuboConnection.KEY_USERNAME;
    private final static String KEY_ACCESS_TOKEN = KEY_PREFIX + MnuboConnection.KEY_ACCESS_TOKEN;
    private final static String KEY_REFRESH_TOKEN = KEY_PREFIX + MnuboConnection.KEY_REFRESH_TOKEN;
    private final static String KEY_EXPIRE_TIME = KEY_PREFIX + MnuboConnection.KEY_EXPIRE_TIME;
    private final SharedPreferences sharedPreferences;

    public ConnectionStore(Context context) {
        Preconditions.checkArgument(context != null, "context cannot be null");

        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void saveConnection(@NonNull MnuboConnection connection) {
        Log.d(TAG, "Persist user connection.");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, connection.getUsername());
        editor.putString(KEY_ACCESS_TOKEN, connection.getToken().getAccessToken());
        editor.putString(KEY_REFRESH_TOKEN, connection.getToken().getRefreshToken());
        editor.putLong(KEY_EXPIRE_TIME, connection.getToken().getExpireTime());
        editor.apply();
    }

    public MnuboConnection getConnection() {
        if (hasConnection()) {
            String username = sharedPreferences.getString(KEY_USERNAME, null);
            String accessToken = sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
            String refreshToken = sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
            Long expiresTime = sharedPreferences.getLong(KEY_EXPIRE_TIME, -1);
            return new MnuboConnection(username, new MnuboConnection.Token(accessToken, refreshToken, expiresTime));
        } else {
            return null;
        }
    }

    public boolean hasConnection() {
        return sharedPreferences.contains(KEY_USERNAME) &&
                sharedPreferences.contains(KEY_ACCESS_TOKEN) &&
                sharedPreferences.contains(KEY_REFRESH_TOKEN) &&
                sharedPreferences.contains(KEY_EXPIRE_TIME);
    }
}
