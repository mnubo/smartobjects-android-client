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

package com.mnubo.platform.android.sdk.internal.user.services.mocked;

import com.mnubo.platform.android.sdk.internal.MockedAbstractServiceTest;
import com.mnubo.platform.android.sdk.internal.services.GroupService;
import com.mnubo.platform.android.sdk.internal.services.impl.GroupServiceImpl;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.groups.Group;
import com.mnubo.platform.android.sdk.models.users.Users;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class GroupServiceImplTestMocked extends MockedAbstractServiceTest {

    private GroupService groupService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        groupService = new GroupServiceImpl(PLATFORM_BASE_URL, mockedRestTemplate);

    }

    @Test
    public void testDelete() throws Exception {

        String calledUrl = expectedUrl("/groups/groupId?id_type=objectid");

        groupService.delete(SdkId.valueOf("groupId"));

        verify(mockedRestTemplate, only()).delete(calledUrl);
    }

    @Test
    public void testCreate() throws Exception {

        String calledUrl = expectedUrl("/groups");
        when(mockedRestTemplate.postForObject(calledUrl, expectedGroup, Group.class)).thenReturn(expectedGroup);

        groupService.create(expectedGroup);
        verify(mockedRestTemplate, only()).postForObject(calledUrl, expectedGroup, Group.class);
    }

    @Test
    public void testFindOne() throws Exception {

        String calledUrl = expectedUrl("/groups/groupId?id_type=objectid");
        when(mockedRestTemplate.getForObject(calledUrl, Group.class)).thenReturn(expectedGroup);

        Group group = groupService.findOne(SdkId.valueOf("groupId"));
        verify(mockedRestTemplate, only()).getForObject(calledUrl, Group.class);
    }

    @Test
    public void testListAllUsers() throws Exception {


        String calledUrl = expectedUrl("/groups/groupId/users?id_type=objectid");
        when(mockedRestTemplate.getForObject(calledUrl, Users.class)).thenReturn(expectedUsers);

        Users users = groupService.listAllUsers(SdkId.valueOf("groupId"));
        verify(mockedRestTemplate, only()).getForObject(calledUrl, Users.class);
    }

    @Test
    public void testListAllUsersWithLimit() throws Exception {
        final Users expectedUsers = new Users();

        String calledUrl = expectedUrl("/groups/groupId/users?id_type=objectid&limit=10");
        when(mockedRestTemplate.getForObject(calledUrl, Users.class)).thenReturn(expectedUsers);

        Users users = groupService.listAllUsers(SdkId.valueOf("groupId"), 10);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, Users.class);
    }

    @Test
    public void testAddUser() throws Exception {
        String calledUrl = expectedUrl("/groups/groupId/users/username?id_type=objectid");

        groupService.addUser(SdkId.valueOf("groupId"), "username");

        verify(mockedRestTemplate, only()).put(calledUrl, null);
    }

    @Test
    public void testRemoveUser() throws Exception {
        String calledUrl = expectedUrl("/groups/groupId/users/username?id_type=objectid");

        groupService.removeUser(SdkId.valueOf("groupId"), "username");

        verify(mockedRestTemplate, only()).delete(calledUrl);
    }

}