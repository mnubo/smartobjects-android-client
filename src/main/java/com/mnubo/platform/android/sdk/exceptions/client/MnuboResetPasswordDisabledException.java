package com.mnubo.platform.android.sdk.exceptions.client;

import com.mnubo.platform.android.sdk.Strings;

/**
 * This exception is raised when you attempt to reset an user's
 * password while your Mnubo API platform configuration doesn't
 * allow it.
 */
public class MnuboResetPasswordDisabledException extends MnuboClientException {

    public MnuboResetPasswordDisabledException() {
        super(Strings.EXCEPTION_RESET_PASSWORD_DISABLED);
    }
}
