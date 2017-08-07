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

package com.mnubo.android.internal.connect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mnubo.android.utils.TimeUtils;

import lombok.NonNull;
import lombok.Value;

@Value
class MnuboConnection {

    public final static String KEY_USERNAME = "username";
    public final static String KEY_ACCESS_TOKEN = "access_token";
    public final static String KEY_REFRESH_TOKEN = "refresh_token";
    public final static String KEY_EXPIRE_TIME = "expire_time";

    @NonNull
    private final String username;

    private final Token token;

    @Value
    static class Token {
        @NonNull
        private final String accessToken;
        @NonNull
        private final String refreshToken;
        @NonNull
        private final Long expireTime;

        @JsonCreator
        public static Token fromJson(@JsonProperty("access_token") String accessToken,
                                     @JsonProperty("refresh_token") String refreshToken,
                                     @JsonProperty("expires_in") Long expiresIn) {
            return new Token(accessToken, refreshToken, TimeUtils.getCurrentTimeSeconds() + expiresIn);
        }

        public boolean hasExpired() {
            return TimeUtils.getCurrentTimeSeconds() >= this.getExpireTime();
        }
    }
}
