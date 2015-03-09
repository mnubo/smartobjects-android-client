package com.mnubo.platform.android.sdk.exceptions.server;

import com.mnubo.platform.android.sdk.exceptions.MnuboException;

/**
 * MnuboServerException is  raised when a 500ish error is
 * the result of a request to the Mnubo Platform.
 */
public class MnuboServerException extends MnuboException {
    public MnuboServerException(Throwable t) {
        super(t);
    }
}
