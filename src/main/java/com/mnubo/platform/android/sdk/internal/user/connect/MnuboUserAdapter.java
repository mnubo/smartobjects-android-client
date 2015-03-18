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

package com.mnubo.platform.android.sdk.internal.user.connect;

import com.mnubo.platform.android.sdk.Mnubo;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

class MnuboUserAdapter implements ApiAdapter<MnuboUserApi> {

    public boolean test(MnuboUserApi mnuboUserApi) {
        return true;
    }

    public void setConnectionValues(MnuboUserApi mnuboUserApi, ConnectionValues values) {
        String username = Mnubo.getUsername();
        if (username != null) {
            values.setProviderUserId(username);
        }
    }

    public UserProfile fetchUserProfile(MnuboUserApi mnuboUserApi) {
        return null;
//        User currentUser = mnuboUserApi.userService().getUser();
//        return new UserProfileBuilder().setName(currentUser.getFullname())
//                .setUsername(currentUser.getUsername()).build();
    }

    public void updateStatus(MnuboUserApi mnuboUserApi, String message) {
        throw new UnsupportedOperationException();
    }

}