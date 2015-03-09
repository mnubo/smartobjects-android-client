package com.mnubo.platform.android.sdk.exceptions.sdk;

import com.mnubo.platform.android.sdk.Strings;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;

/**
 * This exception is raised when you call
 * {@link com.mnubo.platform.android.sdk.Mnubo#getApi()} when
 * {@link com.mnubo.platform.android.sdk.Mnubo#init(android.content.Context, String, String, String)}
 * was not called yet.
 */
public class MnuboNotInitializedException extends MnuboException {

    public MnuboNotInitializedException() {
        super(Strings.EXCEPTION_NOT_INITIALIZED);
    }
}
