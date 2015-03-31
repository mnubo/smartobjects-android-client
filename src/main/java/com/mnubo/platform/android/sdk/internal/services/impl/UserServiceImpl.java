/*
 * Copyright (c) 2015 Mnubo. Released under MIT License.
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy
 *     of this software and associated documentation files (the "Software"), to deal
 *     in the Software without restriction, including without limitation the rights
 *     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *     copies of the Software, and to permit persons to whom the Software is
 *     furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in
 *     all copies or substantial portions of the Software.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *     THE SOFTWARE.
 */

package com.mnubo.platform.android.sdk.internal.services.impl;

import com.mnubo.platform.android.sdk.internal.connect.query.PlatformPath;
import com.mnubo.platform.android.sdk.internal.connect.query.PlatformQuery;
import com.mnubo.platform.android.sdk.internal.services.UserService;
import com.mnubo.platform.android.sdk.models.security.UpdatePassword;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;
import com.mnubo.platform.android.sdk.models.users.User;

import org.apache.commons.lang3.Validate;
import org.springframework.web.client.RestTemplate;

import java.util.List;


public class UserServiceImpl extends AbstractMnuboService implements UserService {

    public UserServiceImpl(String platformBaseUrl, RestTemplate restTemplate) {
        super(platformBaseUrl, PlatformPath.users, restTemplate);
    }

    @Override
    public void delete(String username) {
        Validate.notBlank(username, "The username is required.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{username}", username);

        getRestTemplate().delete(query.buildUrl());
    }

    @Override
    public void update(String username, User user) {
        Validate.notNull(user, "The user to update shouldn't be null.");
        Validate.notBlank(username, "The username of the user to be updated is required");

        PlatformQuery query = this.getQuery();

        query.setUri("/{username}", username);
        query.setBody(user);

        getRestTemplate().put(query.buildUrl(), query.getBody());
    }

    @Override
    public void updatePassword(String username, UpdatePassword password) {
        Validate.notBlank(username, "The username shouldn't be null or empty.");
        Validate.notNull(password, "The update password shouldn't be null.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{username}/password", username);
        query.setBody(password);

        getRestTemplate().put(query.buildUrl(), query.getBody());
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

        return getRestTemplate().getForObject(query.buildUrl(), User.class);
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

        return getRestTemplate().getForObject(query.buildUrl(), SmartObjects.class);
    }
}
