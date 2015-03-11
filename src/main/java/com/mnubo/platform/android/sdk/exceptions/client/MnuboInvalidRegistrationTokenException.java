package com.mnubo.platform.android.sdk.exceptions.client;

import com.mnubo.platform.android.sdk.Strings;

/**
 * This exception is raised when you perform a request to
 * confirm an user registration but provide the wrong token.
 */
public class MnuboInvalidRegistrationTokenException extends MnuboClientException {

    public MnuboInvalidRegistrationTokenException() {
        super(Strings.EXCEPTION_INVALID_REGISTRATION_TOKEN);
    }
}
