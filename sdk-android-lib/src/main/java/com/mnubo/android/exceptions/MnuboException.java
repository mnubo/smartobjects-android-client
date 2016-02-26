/*
 * Copyright (c) 2016 Mnubo. Released under MIT License.
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

package com.mnubo.android.exceptions;

/**
 * MnuboException is the base exception. There should be no other type of exception getting out of a
 * MnuboApi call.
 */
public class MnuboException extends Exception {

    private static final String EXCEPTION_SDK = "Error consuming Mnubo REST API";


    /**
     * Build a MnuboException with the most {@link #EXCEPTION_SDK general message} available.
     */
    public MnuboException() {
        super(EXCEPTION_SDK);
    }


    /**
     * Build a MnuboException with the most general exception message available containing
     * information of a previous exception.
     *
     * @param t previous exception
     */
    public MnuboException(Throwable t) {
        super(EXCEPTION_SDK, t);
    }

    /**
     * Build a MnuboException with customized message.
     *
     * @param detailMessage customized exception message
     */
    public MnuboException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Build a MnuboException with customized message containing information of a previous
     * exception..
     *
     * @param detailMessage customized exception message
     * @param t             previous exception
     */
    public MnuboException(String detailMessage, Throwable t) {
        super(detailMessage, t);
    }
}
