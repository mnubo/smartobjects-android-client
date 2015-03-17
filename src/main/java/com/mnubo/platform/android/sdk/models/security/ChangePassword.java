package com.mnubo.platform.android.sdk.models.security;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used to submit password changes or reset requiring a new password
 * and its confirmation.
 */
public abstract class ChangePassword {

    private String password;

    @JsonProperty("confirmed_password")
    private String confirmedPassword;

    ChangePassword(String password, String confirmedPassword) {
        this.password = password;
        this.confirmedPassword = confirmedPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmPassword) {
        confirmedPassword = confirmPassword;
    }

    @JsonIgnore
    public boolean bothPasswordsAreIdentical() {

        return TextUtils.equals(password, confirmedPassword);
    }

}
