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
