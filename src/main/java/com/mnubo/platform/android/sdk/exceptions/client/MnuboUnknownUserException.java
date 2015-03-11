package com.mnubo.platform.android.sdk.exceptions.client;

import com.mnubo.platform.android.sdk.Strings;

/**
 * This exception is raised when you perform a request for a
 * user that doesn't exists in the Mnubo API.
 */
public class MnuboUnknownUserException extends MnuboClientException {

    public MnuboUnknownUserException() {
        super(Strings.EXCEPTION_UNKNOWN_USER);
    }
}
