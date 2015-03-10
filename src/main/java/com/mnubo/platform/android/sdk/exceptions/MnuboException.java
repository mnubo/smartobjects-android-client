package com.mnubo.platform.android.sdk.exceptions;

import com.mnubo.platform.android.sdk.Strings;

/**
 * MnuboException is the base exception. There should be
 * no other type of exception getting out of a MnuboApi call.
 */
public class MnuboException extends RuntimeException {

    /**
     * Build a MnuboException with the most
     * {@link com.mnubo.platform.android.sdk.Strings#EXCEPTION_SDK general message}
     * available.
     */
    public MnuboException() {
        super(Strings.EXCEPTION_SDK);
    }


    /**
     * Build a MnuboException with the most general exception
     * message available containing information of a previous exception.
     *
     * @param t previous exception
     */
    public MnuboException(Throwable t) {
        super(Strings.EXCEPTION_SDK, t);
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
     * Build a MnuboException with customized message
     * containing information of a previous exception..
     *
     * @param detailMessage customized exception message
     * @param t             previous exception
     */
    public MnuboException(String detailMessage, Throwable t) {
        super(detailMessage, t);
    }
}
