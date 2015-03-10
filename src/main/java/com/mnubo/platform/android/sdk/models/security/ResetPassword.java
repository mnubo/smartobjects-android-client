/*
 * ---------------------------------------------------------------------------
 * 
 * COPYRIGHT (c) 2014 Mnubo Inc. All Rights Reserved.
 * 
 * The copyright to the computer program(s) herein is the property of Mnubo Inc. The program(s) may be used and/or
 * copied only with the written permission from Mnubo Inc. or in accordance with the terms and conditions stipulated in
 * the agreement/contract under which the program(s) have been supplied.
 * 
 * Author: pgehl Date : Dec 15, 2014
 * 
 * ---------------------------------------------------------------------------
 */

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
