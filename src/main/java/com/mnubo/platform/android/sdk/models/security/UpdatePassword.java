/*
 * ---------------------------------------------------------------------------
 * 
 * COPYRIGHT (c) 2014 Mnubo Inc. All Rights Reserved.
 * 
 * The copyright to the computer program(s) herein is the property of Mnubo Inc. The program(s) may be used and/or
 * copied only with the written permission from Mnubo Inc. or in accordance with the terms and conditions stipulated in
 * the agreement/contract under which the program(s) have been supplied.
 * 
 * Author: pgehl Date : Oct 3, 2014
 * 
 * ---------------------------------------------------------------------------
 */

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
