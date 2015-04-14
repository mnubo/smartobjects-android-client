package com.mnubo.platform.android.sdk.exceptions.client;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class MnuboInvalidEmailAddressException extends MnuboClientException {

    private final static String INVALID_EMAIL_PATTERN = "^Invalid email address '.*'$";
    private final static Pattern PM_INVALID_EMAIL = Pattern.compile(INVALID_EMAIL_PATTERN);

    public MnuboInvalidEmailAddressException() {
        super("The provided email address is invalid.");
    }

    public static boolean matches(CharSequence s) {
        return !TextUtils.isEmpty(s) && PM_INVALID_EMAIL.matcher(s).matches();
    }
}
