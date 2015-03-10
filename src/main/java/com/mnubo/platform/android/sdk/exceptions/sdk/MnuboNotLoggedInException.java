package com.mnubo.platform.android.sdk.exceptions.sdk;

import com.mnubo.platform.android.sdk.Strings;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;

/**
 * This error is raised when the {@link com.mnubo.platform.android.sdk.api.MnuboApi}
 * user operations are used prior to user logging in.
 */
public class MnuboNotLoggedInException extends MnuboException {

    public MnuboNotLoggedInException() {
        super(Strings.EXCEPTION_NOT_LOGGED_IN);
    }
}
