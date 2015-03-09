package com.mnubo.platform.android.sdk.exceptions.client;

import com.mnubo.platform.android.sdk.Strings;

/**
 * Raised when the call invoked on the Mnubo API result in a
 * 403 Forbidden Access http error.
 *
 * This means that you performed an action that you are not allowed to do.
 * Fetching user data using the client token (which has a LIMITED scope) for example.
 *
 * Fortunately, the SDK do not allow this, but who knows...
 */
public class MnuboAccessDeniedException extends MnuboClientException {
    public MnuboAccessDeniedException() {
        super(Strings.EXCEPTION_ACCESS_DENIED);
    }

    public MnuboAccessDeniedException(Throwable t) {
        super(Strings.EXCEPTION_ACCESS_DENIED, t);
    }
}
