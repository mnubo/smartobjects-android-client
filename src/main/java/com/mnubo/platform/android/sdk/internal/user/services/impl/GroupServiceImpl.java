package com.mnubo.platform.android.sdk.internal.user.services.impl;

import com.mnubo.platform.android.sdk.internal.connect.query.PlatformPath;
import com.mnubo.platform.android.sdk.internal.connect.query.PlatformQuery;
import com.mnubo.platform.android.sdk.internal.services.AbstractMnuboService;
import com.mnubo.platform.android.sdk.internal.user.services.GroupService;
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
