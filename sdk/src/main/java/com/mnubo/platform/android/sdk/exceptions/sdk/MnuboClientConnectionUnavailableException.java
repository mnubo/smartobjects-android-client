package com.mnubo.platform.android.sdk.exceptions.sdk;

import com.mnubo.platform.android.sdk.Strings;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;

/**
 * This exception is raised when you attempt to perform a
 * {@link com.mnubo.platform.android.sdk.api.operations.ClientOperations client operations}
 * but the SDK wasn't able to get an client access_token mostly due to
 * network error or Mnubo API problem.
 */
public class MnuboClientConnectionUnavailableException extends MnuboException {

    public MnuboClientConnectionUnavailableException() {
        super(Strings.EXCEPTION_CLIENT_CONNECTION_UNAVAILABLE);
    }
}
