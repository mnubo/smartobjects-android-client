package com.mnubo.platform.android.sdk.exceptions.client;

import com.mnubo.platform.android.sdk.Strings;

/**
 * This exception is raised when you perform a request to
 * confirm an user registration but provide the wrong token.
 */
public class MnuboInvalidRegistrationTokenException extends MnuboClientException {
    public final static String REGISTRATION_INVALID_TOKEN = "Two Steps User Registration: Invalid Token received for the User.";

    public MnuboInvalidRegistrationTokenException() {
        super(Strings.EXCEPTION_INVALID_REGISTRATION_TOKEN);
    }
}
