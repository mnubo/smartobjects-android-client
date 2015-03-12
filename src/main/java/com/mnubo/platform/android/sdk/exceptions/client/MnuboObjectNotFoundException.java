package com.mnubo.platform.android.sdk.exceptions.client;

import com.mnubo.platform.android.sdk.Strings;

/**
 * This exception is raised when you perform a request to
 * confirm an user registration but provide the wrong token.
 */
public class MnuboObjectNotFoundException extends MnuboClientException {
    public final static String OBJECT_NOT_FOUND = "Object not found";

    public MnuboObjectNotFoundException() {
        super(Strings.EXCEPTION_OBJECT_NOT_FOUND);
    }
}
