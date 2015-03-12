package com.mnubo.platform.android.sdk.exceptions.client;

import com.mnubo.platform.android.sdk.Strings;

/**
 * This error is raised when the user exists but it is deactivated.
 * This can happen if the user registered but didn't confirm the registration
 * while it is required.
 */
public class MnuboUserDisabledException extends MnuboClientException {
    public final static String USER_DISABLED = "User is disabled";

    public MnuboUserDisabledException() {
        super(Strings.EXCEPTION_USER_DISABLED);
    }

}
