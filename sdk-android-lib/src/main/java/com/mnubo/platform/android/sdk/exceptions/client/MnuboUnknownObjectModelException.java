package com.mnubo.platform.android.sdk.exceptions.client;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class MnuboUnknownObjectModelException extends MnuboClientException {

    private final static String INVALID_EMAIL_PATTERN = "^Unknown object model .*$";
    private final static Pattern PM_INVALID_EMAIL = Pattern.compile(INVALID_EMAIL_PATTERN);

    public MnuboUnknownObjectModelException() {
        super("The object model name you provided does not exists.");
    }

    public static boolean matches(CharSequence s) {
        return !TextUtils.isEmpty(s) && PM_INVALID_EMAIL.matcher(s).matches();
    }
}
