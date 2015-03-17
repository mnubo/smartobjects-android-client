package com.mnubo.platform.android.sdk.internal.client.connect;

import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

class MnuboClientAdapter implements ApiAdapter<MnuboClientApi> {

    public boolean test(MnuboClientApi mnuboClientApi) {
        return true;
    }

    public void setConnectionValues(MnuboClientApi mnuboClientApi, ConnectionValues values) {
    }

    public UserProfile fetchUserProfile(MnuboClientApi mnuboClientApi) {
        throw new UnsupportedOperationException();
    }

    public void updateStatus(MnuboClientApi mnuboClientApi, String message) {
        throw new UnsupportedOperationException();
    }

}