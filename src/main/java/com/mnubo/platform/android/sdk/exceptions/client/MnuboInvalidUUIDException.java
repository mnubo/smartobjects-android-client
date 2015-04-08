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

package com.mnubo.platform.android.sdk.exceptions.client;

import android.text.TextUtils;

import com.mnubo.platform.android.sdk.exceptions.server.MnuboServerException;

import java.util.regex.Pattern;

/**
 * This exception is raised when you perform a request for a user that doesn't exists in the Mnubo
 * API.
 */
public class MnuboInvalidUUIDException extends MnuboClientException {
    private static String EXCEPTION_INVALID_UUID = "The value you provided is not a valid UUID.";

    public final static String INVALID_UUID_PATTERN = "^Invalid UUID string: .*$";
    private final static Pattern PATTERN = Pattern.compile(INVALID_UUID_PATTERN);

    public MnuboInvalidUUIDException() {
        super(EXCEPTION_INVALID_UUID);
    }

    public static boolean matches(CharSequence s) {
        return !TextUtils.isEmpty(s) && PATTERN.matcher(s).matches();
    }
}
