package com.mnubo.platform.android.sdk.internal.user.connect;

import com.mnubo.platform.android.sdk.Mnubo;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

public class MnuboUserAdapter implements ApiAdapter<MnuboUserApi> {

    public boolean test(MnuboUserApi mnuboUserApi) {
        return true;
    }

    public void setConnectionValues(MnuboUserApi mnuboUserApi, ConnectionValues values) {
        String username = Mnubo.getUsername();
        if (username != null) {
            values.setProviderUserId(username);
        }
    }

    public UserProfile fetchUserProfile(MnuboUserApi mnuboUserApi) {
        return null;
//        User currentUser = mnuboUserApi.userService().getUser();
//        return new UserProfileBuilder().setName(currentUser.getFullname())
//                .setUsername(currentUser.getUsername()).build();
    }

    public void updateStatus(MnuboUserApi mnuboUserApi, String message) {
        throw new UnsupportedOperationException();
    }

}