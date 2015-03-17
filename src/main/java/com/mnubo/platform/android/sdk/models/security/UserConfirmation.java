package com.mnubo.platform.android.sdk.models.security;

/**
 * Used in the confirmation of a user creation.
 * Requires password along with the token received after the user registered.
 */
public class UserConfirmation {

    private String password;
    private String token;

    public UserConfirmation(String token, String password) {
        this.password = password;
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
