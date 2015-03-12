package com.mnubo.platform.android.sdk.api.operations.impl;

import com.mnubo.platform.android.sdk.api.operations.AbstractOperationsTest;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.impl.TaskImpl;
import com.mnubo.platform.android.sdk.internal.user.services.UserService;

import org.junit.Before;
import org.junit.Test;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserOperationsImplTest extends AbstractOperationsTest {

    private final UserOperationsImpl userOperations = new UserOperationsImpl(mockedConnectionOperations, mockedClientApiConnection, mockedUserApiConnection);
    private final UserService mockedUserService = mock(UserService.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();
        userOperations.setAsyncTaskFactory(mockedAsyncTaskFactory);

        when(mockedUserApiConnection.getApi()).thenReturn(mockedUserApi);
        when(mockedUserApi.userService()).thenReturn(mockedUserService);
    }

    @Test
    public void testFindUserObjects() throws Exception {

    }

    @Test
    public void testFindUserObjectsDetails() throws Exception {

    }

    @Test
    public void testFindUserObjectsDetailsAndObjectModel() throws Exception {

    }

    @Test
    public void testGetUser() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testUpdatePassword() throws Exception {

    }
}