package com.mnubo.platform.android.sdk.exceptions.sdk;

import com.mnubo.platform.android.sdk.Strings;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;

/**
 * This exception is raised when the hostname provided
 * in the {@link com.mnubo.platform.android.sdk.Mnubo#init(android.content.Context, String, String, String)} call.
 *
 * For example, the hostname in the URL below is "mycompany.api.mnubo.com"
 *
 * https://mycompany.api.mnubo.com:443
 */
public class MnuboInvalidHostname extends MnuboException {

    public MnuboInvalidHostname(Throwable ex) {
        super(Strings.EXCEPTION_INVALID_HOSTNAME, ex);
    }
}
