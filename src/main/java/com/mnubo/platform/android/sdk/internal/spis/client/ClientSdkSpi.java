package com.mnubo.platform.android.sdk.internal.spis.client;

import com.mnubo.platform.android.sdk.models.security.ResetPassword;
import com.mnubo.platform.android.sdk.models.security.UserConfirmation;
import com.mnubo.platform.android.sdk.models.users.User;

public interface ClientSdkSpi {

    void createUser(User user);

    void confirmUserCreation(String username, UserConfirmation userConfirmation);

    void resetPassword(String username);

    void confirmPasswordReset(String username, ResetPassword newPassword);
}
