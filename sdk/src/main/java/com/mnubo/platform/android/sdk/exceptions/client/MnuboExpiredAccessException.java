package com.mnubo.platform.android.sdk.exceptions.client;

import com.mnubo.platform.android.sdk.Strings;

/**
 * This exception is raised when the attempt to refresh the user
 * access token failed because the refresh token is expired.
 * <p/>
 * When this exception happens, the user should be asked to log in
 * the exception.
 */
public class MnuboExpiredAccessException extends MnuboClientException {
    public MnuboExpiredAccessException() {
        super(Strings.EXCEPTION_REFRESH_TOKEN_EXPIRED);
    }
}
