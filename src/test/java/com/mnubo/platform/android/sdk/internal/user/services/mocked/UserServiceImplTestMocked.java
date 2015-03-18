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
import com.mnubo.platform.android.sdk.internal.user.services.UserService;
import com.mnubo.platform.android.sdk.internal.user.services.impl.UserServiceImpl;
import com.mnubo.platform.android.sdk.models.security.UpdatePassword;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;
import com.mnubo.platform.android.sdk.models.users.User;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTestMocked extends MockedAbstractServiceTest {

    private UserService userService;

    private final User expectedUser = new User();
    private final SmartObjects expectedSmartObjects = new SmartObjects();
    private final SmartObject expectedSmartObject = new SmartObject();

    @Before
    public void setUp() throws Exception {
        userService = new UserServiceImpl(PLATFORM_BASE_URL, mockedRestTemplate);

        expectedUser.setUsername("test");
        expectedUser.setLastname("lastname");
        expectedUser.setFirstname("firstname");

        expectedSmartObject.setDeviceId("device_id");
        expectedSmartObjects.addObject(expectedSmartObject);
    }

    @Test
    public void getUserWithUsername() throws Exception {

        String calledUrl = expectedUrl("/users/test");
        when(mockedRestTemplate.getForObject(calledUrl, User.class)).thenReturn(expectedUser);

        User user = userService.getUser("test");

        assertUser(user, expectedUser);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, User.class);
    }

    @Test
    public void getUserWithUsernameAndAttributes() throws Exception {

        String calledUrl = expectedUrl("/users/test?attributes=attributes&attributes=attributes2");
        when(mockedRestTemplate.getForObject(calledUrl, User.class)).thenReturn(expectedUser);

        List<String> attributes = Arrays.asList("attributes", "attributes2");
        User user = userService.getUser("test", attributes);

        assertUser(user, expectedUser);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, User.class);
    }

    @Test
    public void deleteUserTest() throws Exception {
        String calledUrl = expectedUrl("/users/test");

        userService.delete("test");

        verify(mockedRestTemplate, only()).delete(calledUrl);

    }

    @Test
    public void updateUserTest() throws Exception {
        String calledUrl = expectedUrl("/users/test");

        userService.update("test", expectedUser);
        verify(mockedRestTemplate, only()).put(calledUrl, expectedUser);
    }

    @Test
    public void updatePasswordTest() throws Exception {
        final UpdatePassword updatePassword = new UpdatePassword("old", "new", "new");
        String calledUrl = expectedUrl("/users/test/password");

        userService.updatePassword("test", updatePassword);

        verify(mockedRestTemplate, only()).put(calledUrl, updatePassword);
    }

    @Test
    public void findUserObjects() throws Exception {

        String calledUrl = expectedUrl("/users/test/objects?details=false&show_history=false");
        when(mockedRestTemplate.getForObject(calledUrl, SmartObjects.class)).thenReturn(expectedSmartObjects);

        SmartObjects smartObjects = userService.findUserObjects("test");
        assertUserObjects(smartObjects, expectedSmartObjects);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, SmartObjects.class);
    }

    @Test
    public void findUserObjectsWithDetails() throws Exception {

        final SmartObjects expectedSmartObjects = new SmartObjects();

        String calledUrl = expectedUrl("/users/test/objects?details=true&show_history=false");
        when(mockedRestTemplate.getForObject(calledUrl, SmartObjects.class)).thenReturn(expectedSmartObjects);

        SmartObjects smartObjects = userService.findUserObjects("test", true);

        assertUserObjects(smartObjects, expectedSmartObjects);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, SmartObjects.class);
    }

    @Test
    public void findUserObjectsWithDetailsAndModel() throws Exception {
        final SmartObjects expectedSmartObjects = new SmartObjects();

        String calledUrl = expectedUrl("/users/test/objects?details=true&object_model=model&show_history=false");
        when(mockedRestTemplate.getForObject(calledUrl, SmartObjects.class)).thenReturn(expectedSmartObjects);

        SmartObjects smartObjects = userService.findUserObjects("test", true, "model");
        assertUserObjects(smartObjects, expectedSmartObjects);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, SmartObjects.class);
    }

    @Test
    public void findUserObjectsWithDetailsAndModelAndHistory() throws Exception {

        String calledUrl = expectedUrl("/users/test/objects?details=true&object_model=model&show_history=true");
        when(mockedRestTemplate.getForObject(calledUrl, SmartObjects.class)).thenReturn(expectedSmartObjects);

        SmartObjects smartObjects = userService.findUserObjects("test", true, "model", true);
        assertUserObjects(smartObjects, expectedSmartObjects);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, SmartObjects.class);
    }

    private void assertUser(User actual, User expected) throws Exception {
        if (expected == null) {
            assertThat(actual, nullValue());
            return;
        }

        assertThat(actual.getFirstname(), equalTo(expected.getFirstname()));
        assertThat(actual.getLastname(), equalTo(expected.getLastname()));
        assertThat(actual.getUsername(), equalTo(expected.getUsername()));
    }

    private void assertUserObjects(SmartObjects actual, SmartObjects expected) throws Exception {
        if (expected == null) {
            assertThat(actual, nullValue());
            return;
        }

        assertThat(actual.getCount(), equalTo(expected.getCount()));
        assertThat(actual.getSmartObjects().size(), equalTo(expected.getSmartObjects().size()));

        if (expected.getSmartObjects().size() > 0) {
            assertThat(actual.getSmartObjects().get(0), equalTo(expected.getSmartObjects().get(0)));
        }
    }
}
