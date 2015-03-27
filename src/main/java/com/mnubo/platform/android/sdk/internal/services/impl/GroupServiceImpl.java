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
import com.mnubo.platform.android.sdk.internal.services.AbstractMnuboService;
import com.mnubo.platform.android.sdk.internal.services.GroupService;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.groups.Group;
import com.mnubo.platform.android.sdk.models.users.Users;

import org.apache.commons.lang3.Validate;
import org.springframework.web.client.RestTemplate;

public class GroupServiceImpl extends AbstractMnuboService implements GroupService {


    public GroupServiceImpl(String platformBaseUrl, RestTemplate restTemplate) {
        super(platformBaseUrl, PlatformPath.groups, restTemplate);
    }

    @Override
    public void delete(SdkId groupId) {
        Validate.notNull(groupId, "The groupId is required.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{groupId}", groupId.getId());
        query.idType(groupId.getIdType());

        getRestTemplate().delete(query.buildUrl());
    }

    @Override
    public Group create(Group group) {
        Validate.notNull(group, "The group you are creating shouldn't be null.");

        PlatformQuery query = this.getQuery();

        query.setBody(group);

        return getRestTemplate().postForObject(query.buildUrl(), query.getBody(), Group.class);
    }

    @Override
    public Group findOne(SdkId groupId) {
        Validate.notNull(groupId, "The groupId is required.");
        PlatformQuery query = this.getQuery();

        query.setUri("/{groupId}", groupId.getId());
        query.idType(groupId.getIdType());

        return getRestTemplate().getForObject(query.buildUrl(), Group.class);
    }

    @Override
    public Users listAllUsers(SdkId groupId) {
        return this.listAllUsers(groupId, 0);
    }

    @Override
    public Users listAllUsers(SdkId groupId, int resultSizeLimit) {
        Validate.notNull(groupId, "The groupId is required.");
        PlatformQuery query = this.getQuery();

        query.setUri("/{groupId}/users", groupId.getId());
        query.idType(groupId.getIdType());
        query.limit(resultSizeLimit);

        return getRestTemplate().getForObject(query.buildUrl(), Users.class);
    }

    @Override
    public void addUser(SdkId groupId, String username) {
        Validate.notNull(groupId, "The groupId is required.");
        Validate.notBlank(username, "The username is required.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{groupId}/users/{username}", groupId.getId(), username);
        query.idType(groupId.getIdType());

        getRestTemplate().put(query.buildUrl(), null);
    }

    @Override
    public void removeUser(SdkId groupId, String username) {
        Validate.notNull(groupId, "The groupId is required.");
        Validate.notBlank(username, "The username is required.");

        PlatformQuery query = this.getQuery();

        query.setUri("/{groupId}/users/{username}", groupId.getId(), username);
        query.idType(groupId.getIdType());

        getRestTemplate().delete(query.buildUrl());
    }
}
