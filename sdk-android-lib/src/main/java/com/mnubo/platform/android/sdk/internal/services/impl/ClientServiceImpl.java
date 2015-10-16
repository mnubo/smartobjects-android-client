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
import com.mnubo.platform.android.sdk.internal.services.ClientService;
import com.mnubo.platform.android.sdk.models.security.ResetPassword;
import com.mnubo.platform.android.sdk.models.security.UserConfirmation;
import com.mnubo.platform.android.sdk.models.users.User;

import org.apache.commons.lang3.Validate;
import org.springframework.web.client.RestTemplate;

public class ClientServiceImpl extends AbstractMnuboService implements ClientService {


    public ClientServiceImpl(String platformBaseUrl, RestTemplate restTemplate, String path) {
        super(platformBaseUrl, PlatformPath.users, restTemplate, path);
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
