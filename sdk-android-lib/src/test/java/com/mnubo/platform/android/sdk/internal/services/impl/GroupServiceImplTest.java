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

import com.mnubo.platform.android.sdk.internal.api.MnuboSDKApiImpl;
import com.mnubo.platform.android.sdk.internal.services.GroupService;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.groups.Group;
import com.mnubo.platform.android.sdk.models.users.Users;

import org.junit.Before;
import org.junit.Test;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


public class GroupServiceImplTest extends AbstractServicesTest {
    private GroupService groupService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.mnuboUserApi = new MnuboSDKApiImpl(USER_ACCESS_TOKEN, PLATFORM_BASE_URL);
        setUpMockServer();

        groupService = mnuboUserApi.groupService();
    }

    @Test
    public void testDelete() throws Exception {

        mockServiceServer.expect(requestTo(expectedUrl("/groups/groupId?id_type=objectid")))
                .andExpect(method(DELETE))
                .andRespond(withNoContent());

        groupService.delete(SdkId.valueOf("groupId"));
        mockServiceServer.verify();
    }

    @Test
    public void testCreate() throws Exception {
        final Group groupToCreate = new Group();
        groupToCreate.setOwner("owner");
        groupToCreate.setLabel("label");

        mockServiceServer.expect(requestTo(expectedUrl("/groups")))
                .andExpect(method(POST))
                .andExpect(content().string(toJson(groupToCreate)))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andRespond(withSuccess(toJson(groupToCreate), APPLICATION_JSON_UTF8));

        groupService.create(groupToCreate);
        mockServiceServer.verify();
    }

    @Test
    public void testFindOne() throws Exception {
        final Group expectedGroup = new Group();
        expectedGroup.setOwner("owner");
        expectedGroup.setLabel("label");

        mockServiceServer.expect(requestTo(expectedUrl("/groups/groupId?id_type=objectid")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedGroup), APPLICATION_JSON_UTF8));

        Group group = groupService.findOne(SdkId.valueOf("groupId"));
        mockServiceServer.verify();
    }

    @Test
    public void testListAllUsers() throws Exception {
        final Users expectedUsers = new Users();

        mockServiceServer.expect(requestTo(expectedUrl("/groups/groupId/users?id_type=objectid")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedUsers), APPLICATION_JSON_UTF8));

        Users users = groupService.listAllUsers(SdkId.valueOf("groupId"));
        mockServiceServer.verify();
    }

    @Test
    public void testListAllUsersWithLimit() throws Exception {
        final Users expectedUsers = new Users();

        mockServiceServer.expect(requestTo(expectedUrl("/groups/groupId/users?id_type=objectid&limit=10")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedUsers), APPLICATION_JSON_UTF8));

        Users users = groupService.listAllUsers(SdkId.valueOf("groupId"), 10);
        mockServiceServer.verify();
    }

    @Test
    public void testAddUser() throws Exception {
        mockServiceServer.expect(requestTo(expectedUrl("/groups/groupId/users/username?id_type=objectid")))
                .andExpect(method(PUT))
                .andRespond(withNoContent());

        groupService.addUser(SdkId.valueOf("groupId"), "username");
        mockServiceServer.verify();
    }

    @Test
    public void testRemoveUser() throws Exception {
        mockServiceServer.expect(requestTo(expectedUrl("/groups/groupId/users/username?id_type=objectid")))
                .andExpect(method(DELETE))
                .andRespond(withNoContent());

        groupService.removeUser(SdkId.valueOf("groupId"), "username");
        mockServiceServer.verify();
    }

}