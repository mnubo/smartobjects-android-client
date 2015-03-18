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

package com.mnubo.platform.android.sdk.exceptions.sdk;

import com.mnubo.platform.android.sdk.Strings;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;

/**
 * This exception is raised when the hostname provided
 * in the {@link com.mnubo.platform.android.sdk.Mnubo#init(android.content.Context, String, String, String)} call.
 *
 * For example, the hostname in the URL below is "mycompany.api.mnubo.com"
 *
 * https://mycompany.api.mnubo.com:443
 */
public class MnuboInvalidHostname extends MnuboException {

    public MnuboInvalidHostname(Throwable ex) {
        super(Strings.EXCEPTION_INVALID_HOSTNAME, ex);
    }
}
