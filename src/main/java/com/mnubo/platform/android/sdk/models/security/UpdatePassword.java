package com.mnubo.platform.android.sdk.models.security;


import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdatePassword extends ChangePassword {

    public UpdatePassword(String previousPassword, String newPassword, String confirmedPassword) {
        super(newPassword, confirmedPassword);
        this.previousPassword = previousPassword;
    }

    @JsonProperty("previous_password")
    private String previousPassword;

    public String getPreviousPassword() {
        return previousPassword;
    }

    public void setPreviousPassword(String previousPassword) {
        this.previousPassword = previousPassword;
    }
}
