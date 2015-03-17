package com.mnubo.platform.android.sdk.models.security;

/**
 * Used during the RestPassword flow. This requires a password and its confirmation
 * along with the token received after initiating the reset password process.
 */
public class ResetPassword extends ChangePassword {

    public ResetPassword(String token, String newPassword, String confirmedPassword) {
        super(newPassword, confirmedPassword);
        this.token = token;

    }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
