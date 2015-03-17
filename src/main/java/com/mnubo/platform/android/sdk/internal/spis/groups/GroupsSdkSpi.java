package com.mnubo.platform.android.sdk.internal.spis.groups;

import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.groups.Group;
import com.mnubo.platform.android.sdk.models.users.Users;

public interface GroupsSdkSpi {
    //[/rest/groups/{groupid}],methods=[DELETE]
    void delete(SdkId groupId);

    //[/rest/groups],methods=[POST]
    Group create(Group group);

    //[/rest/groups/{groupid}],methods=[GET]
    Group findOne(SdkId groupId);

    //[/rest/groups/{groupid}/users],methods=[GET]
    Users listAllUsers(SdkId groupId, int resultSizeLimit);

    //[/rest/groups/{groupid}/users],methods=[GET]
    Users listAllUsers(SdkId groupId);

    //[/rest/groups/{groupid}/users/{username}],methods=[PUT]
    void addUser(SdkId groupId, String username);

    //[/rest/groups/{groupid}/users/{username}],methods=[DELETE]
    void removeUser(SdkId groupId, String username);
}
