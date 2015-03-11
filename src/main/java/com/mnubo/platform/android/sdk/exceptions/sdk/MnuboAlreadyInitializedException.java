package com.mnubo.platform.android.sdk.exceptions.sdk;

import com.mnubo.platform.android.sdk.Strings;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;

/**
 * This exception is raised when you run call the {@link com.mnubo.platform.android.sdk.Mnubo#init(android.content.Context, String, String, String)}
 * function more than once.
 */
public class MnuboAlreadyInitializedException extends MnuboException {

    public MnuboAlreadyInitializedException() {
        super(Strings.EXCEPTION_ALREADY_INITIALIZED);
    }
}
