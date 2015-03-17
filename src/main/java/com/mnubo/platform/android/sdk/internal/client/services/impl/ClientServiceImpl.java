package com.mnubo.platform.android.sdk.internal.client.services.impl;

import com.mnubo.platform.android.sdk.internal.client.services.ClientService;
import com.mnubo.platform.android.sdk.internal.connect.query.PlatformPath;
import com.mnubo.platform.android.sdk.internal.connect.query.PlatformQuery;
import com.mnubo.platform.android.sdk.internal.services.AbstractMnuboService;
import com.mnubo.platform.android.sdk.models.security.ResetPassword;
import com.mnubo.platform.android.sdk.models.security.UserConfirmation;
import com.mnubo.platform.android.sdk.models.users.User;

import org.apache.commons.lang3.Validate;
import org.springframework.web.client.RestTemplate;

public class ClientServiceImpl extends AbstractMnuboService implements ClientService {


    public ClientServiceImpl(String platformBaseUrl, RestTemplate restTemplate) {
        super(platformBaseUrl, PlatformPath.users, restTemplate);
    }

    @Override
    public void createUser(User user) {
        Validate.notNull(user, "The user to be created should not be null.");

        PlatformQuery query = this.getQuery();

        query.setBody(user);

        getRestTemplate().postForLocation(query.buildUrl(), query.getBody());
    }

    @Override
    public void confirmUserCreation(String username, UserConfirmation userConfirmation) {
        Validate.notBlank(username, "The username not be null or empty.");
        Validate.notNull(userConfirmation, "The user token is required to confirm the user.");

        PlatformQuery query = this.getQuery();
        query.setUri("/{username}/confirmation", username);
        query.setBody(userConfirmation);

        getRestTemplate().postForLocation(query.buildUrl(), query.getBody());
    }

    @Override
    public void resetPassword(String username) {
        Validate.notBlank(username, "The username not be null or empty.");

        PlatformQuery query = this.getQuery();
        query.setUri("/{username}/password", username);

        getRestTemplate().delete(query.buildUrl());
    }

    @Override
    public void confirmPasswordReset(String username, ResetPassword newPassword) {
        Validate.notBlank(username, "The username not be null or empty.");
        Validate.notNull(newPassword, "The new password should be defined.");

        PlatformQuery query = this.getQuery();
        query.setUri("/{username}/password", username);
        query.setBody(newPassword);

        getRestTemplate().postForLocation(query.buildUrl(), query.getBody());
    }
}
