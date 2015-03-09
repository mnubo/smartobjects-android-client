/*
 * ---------------------------------------------------------------------------
 * 
 * COPYRIGHT (c) 2014 Mnubo Inc. All Rights Reserved.
 * 
 * The copyright to the computer program(s) herein is the property of Mnubo Inc. The program(s) may be used and/or
 * copied only with the written permission from Mnubo Inc. or in accordance with the terms and conditions stipulated in
 * the agreement/contract under which the program(s) have been supplied.
 * 
 * Author: pgehl Date : Aug 29, 2014
 * 
 * ---------------------------------------------------------------------------
 */

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
