package com.mnubo.platform.android.sdk.exceptions.client;

import com.mnubo.platform.android.sdk.Strings;

/**
 * This exception is raised when you perform a request to
 * confirm an user registration but provide the wrong token.
 */
public class MnuboInvalidPreviousPasswordException extends MnuboClientException {
    public final static String INVALID_PREVIOUS_PASSWORD = "Invalid previous password.";

    public MnuboInvalidPreviousPasswordException() {
        super(Strings.EXCEPTION_INVALID_PREVIOUS_PASSWORD);
    }
}
