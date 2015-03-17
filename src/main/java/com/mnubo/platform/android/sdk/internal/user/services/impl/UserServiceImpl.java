package com.mnubo.platform.android.sdk.internal.user.services.impl;

import com.mnubo.platform.android.sdk.internal.connect.query.PlatformPath;
import com.mnubo.platform.android.sdk.internal.connect.query.PlatformQuery;
import com.mnubo.platform.android.sdk.internal.services.AbstractMnuboService;
import com.mnubo.platform.android.sdk.internal.user.services.UserService;
import com.mnubo.platform.android.sdk.models.security.UpdatePassword;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;
import com.mnubo.platform.android.sdk.models.users.User;

import org.apache.commons.lang3.Validate;
import org.springframework.web.client.RestTemplate;

import java.util.List;


public class UserServiceImpl extends AbstractMnuboService implements UserService {

    private RestTemplate restTemplate;

    public UserServiceImpl(String platformBaseUrl, RestTemplate restTemplate) {
        super(platformBaseUrl, PlatformPath.users);
        this.restTemplate = restTemplate;
    }

    @Override
    public void delete(String username) {
        Validate.notBlank(username, "The username is required.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{username}", username);

        restTemplate.delete(query.buildUrl());
    }

    @Override
    public void update(String username, User user) {
        Validate.notNull(user, "The user to update shouldn't be null.");
        Validate.notBlank(username, "The username of the user to be updated is required");

        PlatformQuery query = this.getQuery();

        query.setUri("/{username}", username);
        query.setBody(user);

        restTemplate.put(query.buildUrl(), query.getBody());
    }

    @Override
    public void updatePassword(String username, UpdatePassword password) {
        Validate.notBlank(username, "The username shouldn't be null or empty.");
        Validate.notNull(password, "The update password shouldn't be null.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{username}/password", username);
        query.setBody(password);

        restTemplate.put(query.buildUrl(), query.getBody());
    }

    @Override
    public User getUser(String username) {
        return this.getUser(username, null);
    }

    @Override
    public User getUser(String username, List<String> attributes) {
        Validate.notBlank(username, "The username shouldn't be null or empty.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{username}", username);
        query.queryParams("attributes", attributes);

        return restTemplate.getForObject(query.buildUrl(), User.class);
    }

    @Override
    public SmartObjects findUserObjects(String username) {
        return this.findUserObjects(username, false, null, false);
    }

    @Override
    public SmartObjects findUserObjects(String username, boolean details) {

        return this.findUserObjects(username, details, null, false);
    }

    @Override
    public SmartObjects findUserObjects(String username, boolean details, String model) {
        return this.findUserObjects(username, details, model, false);
    }

    @Override
    public SmartObjects findUserObjects(String username, boolean details, String model, boolean showHistory) {
        Validate.notBlank(username, "The username shouldn't be null or empty.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{username}/objects", username);


        query.details(details);
        query.queryParam("object_model", model);
        query.queryParam("show_history", showHistory);

        return restTemplate.getForObject(query.buildUrl(), SmartObjects.class);
    }
}
