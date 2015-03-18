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
 */package com.mnubo.platform.android.sdk.api.operations.impl;

import com.mnubo.platform.android.sdk.api.operations.AbstractOperationsTest;
import com.mnubo.platform.android.sdk.internal.user.services.UserService;
import com.mnubo.platform.android.sdk.models.security.UpdatePassword;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;
import com.mnubo.platform.android.sdk.models.users.User;

import org.junit.Before;
import org.junit.Test;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserOperationsImplTest extends AbstractOperationsTest {

    private final UserOperationsImpl userOperations = new UserOperationsImpl(mockedConnectionOperations, mockedClientApiConnection, mockedUserApiConnection);
    private final UserService mockedUserService = mock(UserService.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<SmartObjects> mockedSmartObjectsCallback = mock(CompletionCallBack.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<User> mockedUserCallback = mock(CompletionCallBack.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<Boolean> mockedSuccessCallback = mock(CompletionCallBack.class);

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        userOperations.setAsyncTaskFactory(mockedAsyncTaskFactory);

        when(mockedUserApiConnection.getApi()).thenReturn(mockedUserApi);
        when(mockedUserApi.userService()).thenReturn(mockedUserService);
    }

    @Test
    public void testFindUserObjects() throws Exception {

        final String username = "username";

        userOperations.findUserObjects(username, null);

        verify(mockedUserApiConnection, only()).getApi();
        verify(mockedUserApi, only()).userService();
        verify(mockedUserService, only()).findUserObjects(username);
    }

    @Test
    public void testFindUserObjectsWithCallback() throws Exception {

        final String username = "username";

        userOperations.findUserObjects(username, mockedSmartObjectsCallback);

        verify(mockedAsyncTask, only()).execute();
    }

    @Test
    public void testFindUserObjectsDetails() throws Exception {


        final String username = "username";

        userOperations.findUserObjects(username, true, null);

        verify(mockedUserApiConnection, only()).getApi();
        verify(mockedUserApi, only()).userService();
        verify(mockedUserService, only()).findUserObjects(username, true);
    }

    @Test
    public void testFindUserObjectsDetailsWithCallback() throws Exception {


        final String username = "username";

        userOperations.findUserObjects(username, true, mockedSmartObjectsCallback);

        verify(mockedAsyncTask, only()).execute();
    }

    @Test
    public void testFindUserObjectsDetailsAndObjectModel() throws Exception {
        final String username = "username";
        final String objectModelName = "objectModelName";

        userOperations.findUserObjects(username, true, objectModelName, null);

        verify(mockedUserApiConnection, only()).getApi();
        verify(mockedUserApi, only()).userService();
        verify(mockedUserService, only()).findUserObjects(username, true, objectModelName);
    }

    @Test
    public void testFindUserObjectsDetailsAndObjectModelWithCallback() throws Exception {
        final String username = "username";
        final String objectModelName = "objectModelName";

        userOperations.findUserObjects(username, true, objectModelName, mockedSmartObjectsCallback);

        verify(mockedAsyncTask, only()).execute();
    }

    @Test
    public void testGetUser() throws Exception {

        final String username = "username";

        userOperations.getUser(username, null);

        verify(mockedUserApiConnection, only()).getApi();
        verify(mockedUserApi, only()).userService();
        verify(mockedUserService, only()).getUser(username);
    }

    @Test
    public void testGetUserWithCallback() throws Exception {

        final String username = "username";

        userOperations.getUser(username, mockedUserCallback);

        verify(mockedAsyncTask, only()).execute();
    }

    @Test
    public void testUpdate() throws Exception {
        final String username = "username";
        final User updatedUser = new User();
        updatedUser.setUsername(username);

        userOperations.update(username, updatedUser, null);

        verify(mockedUserApiConnection, only()).getApi();
        verify(mockedUserApi, only()).userService();
        verify(mockedUserService, only()).update(username, updatedUser);
    }

    @Test
    public void testUpdateWithCallback() throws Exception {
        final String username = "username";
        final User updatedUser = new User();
        updatedUser.setUsername(username);

        userOperations.update(username, updatedUser, mockedSuccessCallback);

        verify(mockedAsyncTask, only()).execute();
    }

    @Test
    public void testUpdatePassword() throws Exception {
        final String username = "username";
        final UpdatePassword updatePassword = new UpdatePassword("old", "new", "new");

        userOperations.updatePassword(username, updatePassword, null);

        verify(mockedUserApiConnection, only()).getApi();
        verify(mockedUserApi, only()).userService();
        verify(mockedUserService, only()).updatePassword(username, updatePassword);
    }

    @Test
    public void testUpdatePasswordWithCallback() throws Exception {
        final String username = "username";
        final UpdatePassword updatePassword = new UpdatePassword("old", "new", "new");

        userOperations.updatePassword(username, updatePassword, mockedSuccessCallback);

        verify(mockedAsyncTask, only()).execute();
    }
}