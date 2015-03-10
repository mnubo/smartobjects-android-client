package com.mnubo.platform.android.sdk.exceptions.client;

import com.mnubo.platform.android.sdk.exceptions.MnuboException;

/**
 * MnuboClientException is raised when a 400ish error is
 * the result of a request to the Mnubo Platform.
 */
public class MnuboClientException extends MnuboException {
    public MnuboClientException(Throwable t) {
        super(t);
    }

    public MnuboClientException(String detailMessage) {
        super(detailMessage);
    }

    public MnuboClientException(String detailMessage, Throwable t) {
        super(detailMessage, t);
    }
}
