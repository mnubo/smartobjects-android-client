package com.mnubo.platform.android.sdk.exceptions.client;

import com.mnubo.platform.android.sdk.Strings;

/**
 * This exception is raised when the credentials provided to
 * fetch a token are incorrect. Either the username/password combination
 * of the user currently login or the consumer_key/consumer_secret provided
 * in the {@link com.mnubo.platform.android.sdk.Mnubo#init(android.content.Context, String, String, String)}
 * function.
 */
public class MnuboBadCredentialsException extends MnuboClientException {
    public final static String BAD_CREDENTIALS = "Bad credentials";

    public MnuboBadCredentialsException() {
        super(Strings.EXCEPTION_BAD_CREDENTIALS);
    }
}
