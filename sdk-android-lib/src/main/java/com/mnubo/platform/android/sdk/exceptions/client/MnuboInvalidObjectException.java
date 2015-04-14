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

import java.util.regex.Pattern;

/**
 * This exception is raised when you perform a request to create an {@link
 * com.mnubo.platform.android.sdk.models.smartobjects.SmartObject} but the information supplied is
 * invalid.
 * <p/>
 * Known reasons for this error :
 * <p/>
 * <ul> <li></li> </ul>
 */
public class MnuboInvalidObjectException extends MnuboClientException {

    private final static String MISSING_OBJECT_MODEL_PATTERN = "^.* default message \\[Missing object model\\]\\]$";
    private final static Pattern OM_PATTERN = Pattern.compile(MISSING_OBJECT_MODEL_PATTERN);

    private final static String MISSING_DEVICE_ID_PATTERN = "^Invalid id$";
    private final static Pattern DI_PATTERN = Pattern.compile(MISSING_DEVICE_ID_PATTERN);

    private final static String UNKNOWN_ATTRIBUTE_PATTERN = "^Attribute .* undefined in object model$";
    private final static Pattern UA_PATTERN = Pattern.compile(UNKNOWN_ATTRIBUTE_PATTERN);

    public MnuboInvalidObjectException() {
        super("The SmartObject data is invalid : None or invalid device id, no objectModelName, etc. See the documentation for full explanation.");
    }

    public static boolean matches(CharSequence s) {
        return !TextUtils.isEmpty(s) &&
                (OM_PATTERN.matcher(s).matches() || DI_PATTERN.matcher(s).matches() || UA_PATTERN.matcher(s).matches());
    }
}
