package com.mnubo.platform.android.sdk.internal.user.services.mocked;

import com.mnubo.platform.android.sdk.internal.user.services.GroupService;
import com.mnubo.platform.android.sdk.internal.user.services.impl.GroupServiceImpl;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.groups.Group;
import com.mnubo.platform.android.sdk.models.users.Users;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class GroupServiceImplTestMocked extends MockedAbstractTest {

    private GroupService groupService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        groupService = new GroupServiceImpl(PLATFORM_BASE_URL, mockedRestTemplate);

    }

    @Test
    public void testDelete() throws Exception {

        String calledUrl = buildPath("/groups/groupId?id_type=objectid");

        groupService.delete(SdkId.valueOf("groupId"));

        verify(mockedRestTemplate,  only()).delete(calledUrl);
    }

    @Test
    public void testCreate() throws Exception {

        String calledUrl = buildPath("/groups");
        when(mockedRestTemplate.postForObject(calledUrl, expectedGroup, Group.class)).thenReturn(expectedGroup);

        groupService.create(expectedGroup);
        verify(mockedRestTemplate,  only()).postForObject(calledUrl, expectedGroup, Group.class);
    }

    @Test
    public void testFindOne() throws Exception {

        String calledUrl = buildPath("/groups/groupId?id_type=objectid");
        when(mockedRestTemplate.getForObject(calledUrl, Group.class)).thenReturn(expectedGroup);

        Group group = groupService.findOne(SdkId.valueOf("groupId"));
        verify(mockedRestTemplate,  only()).getForObject(calledUrl, Group.class);
    }

    @Test
    public void testListAllUsers() throws Exception {


        String calledUrl = buildPath("/groups/groupId/users?id_type=objectid");
        when(mockedRestTemplate.getForObject(calledUrl, Users.class)).thenReturn(expectedUsers);

        Users users = groupService.listAllUsers(SdkId.valueOf("groupId"));
        verify(mockedRestTemplate,  only()).getForObject(calledUrl, Users.class);
    }

    @Test
    public void testListAllUsersWithLimit() throws Exception {
        final Users expectedUsers = new Users();

        String calledUrl = buildPath("/groups/groupId/users?id_type=objectid&limit=10");
        when(mockedRestTemplate.getForObject(calledUrl, Users.class)).thenReturn(expectedUsers);

        Users users = groupService.listAllUsers(SdkId.valueOf("groupId"), 10);
        verify(mockedRestTemplate,  only()).getForObject(calledUrl, Users.class);
    }

    @Test
    public void testAddUser() throws Exception {
        String calledUrl = buildPath("/groups/groupId/users/username?id_type=objectid");

        groupService.addUser(SdkId.valueOf("groupId"), "username");

        verify(mockedRestTemplate,  only()).put(calledUrl, null);
    }

    @Test
    public void testRemoveUser() throws Exception {
        String calledUrl = buildPath("/groups/groupId/users/username?id_type=objectid");

        groupService.removeUser(SdkId.valueOf("groupId"), "username");

        verify(mockedRestTemplate,  only()).delete(calledUrl);
    }

}