package com.mnubo.platform.android.sdk.internal.user.services.impl;

import com.mnubo.platform.android.sdk.internal.AbstractServicesTest;
import com.mnubo.platform.android.sdk.internal.user.services.UserService;
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

/**
 * @RunWith(PowerMockRunner.class)
 @PrepareForTest({
 Log.class,
 PlainSocketFactory.class,
 SSLSocketFactory.class,
 HttpComponentsClientHttpRequestFactory.class,
 ConnManagerParams.class,
 AbstractHttpClient.class
 })


 private final PlainSocketFactory mockedPlainSocketFactory = mock(PlainSocketFactory.class);
 private final SSLSocketFactory mockedSSLSocketFactory = mock(SSLSocketFactory.class);
 private final SchemeRegistry mockedSchemeRegistry = mock(SchemeRegistry.class);
 private final DefaultHttpClient mockedHttpClient = mock(DefaultHttpClient.class);
 private final HttpParams mockedHttpParams = mock(HttpParams.class);

 @Before
 public void setUp() throws Exception {
 mockStatic(Log.class);
 mockStatic(PlainSocketFactory.class);
 mockStatic(SchemeRegistry.class);
 mockStatic(SSLSocketFactory.class);
 mockStatic(ConnManagerParams.class);

 whenNew(SchemeRegistry.class).withAnyArguments().thenReturn(mockedSchemeRegistry);
 whenNew(DefaultHttpClient.class).withAnyArguments().thenReturn(mockedHttpClient);
 when(PlainSocketFactory.getSocketFactory()).thenReturn(mockedPlainSocketFactory);
 when(SSLSocketFactory.getSocketFactory()).thenReturn(mockedSSLSocketFactory);
 when(mockedHttpClient.getParams()).thenReturn(mockedHttpParams);
 }

 */
public class UserServiceImplServicesTest extends AbstractServicesTest {

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

        String calledUrl = buildPath("/users/test");
        when(mockedRestTemplate.getForObject(calledUrl, User.class)).thenReturn(expectedUser);

        User user = userService.getUser("test");

        assertUser(user, expectedUser);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, User.class);
    }

    @Test
    public void getUserWithUsernameAndAttributes() throws Exception {

        String calledUrl = buildPath("/users/test?attributes=attributes&attributes=attributes2");
        when(mockedRestTemplate.getForObject(calledUrl, User.class)).thenReturn(expectedUser);

        List<String> attributes = Arrays.asList("attributes", "attributes2");
        User user = userService.getUser("test", attributes);

        assertUser(user, expectedUser);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, User.class);
    }

    @Test
    public void deleteUserTest() throws Exception {
        String calledUrl = buildPath("/users/test");

        userService.delete("test");

        verify(mockedRestTemplate, only()).delete(calledUrl);

    }

    @Test
    public void updateUserTest() throws Exception {
        String calledUrl = buildPath("/users/test");

        userService.update("test", expectedUser);
        verify(mockedRestTemplate, only()).put(calledUrl, expectedUser);
    }

    @Test
    public void updatePasswordTest() throws Exception {
        final UpdatePassword updatePassword = new UpdatePassword("old", "new", "new");
        String calledUrl = buildPath("/users/test/password");

        userService.updatePassword("test", updatePassword);

        verify(mockedRestTemplate, only()).put(calledUrl, updatePassword);
    }

    @Test
    public void findUserObjects() throws Exception {

        String calledUrl = buildPath("/users/test/objects?details=false&show_history=false");
        when(mockedRestTemplate.getForObject(calledUrl, SmartObjects.class)).thenReturn(expectedSmartObjects);

        SmartObjects smartObjects = userService.findUserObjects("test");
        assertUserObjects(smartObjects, expectedSmartObjects);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, SmartObjects.class);
    }

    @Test
    public void findUserObjectsWithDetails() throws Exception {

        final SmartObjects expectedSmartObjects = new SmartObjects();

        String calledUrl = buildPath("/users/test/objects?details=true&show_history=false");
        when(mockedRestTemplate.getForObject(calledUrl, SmartObjects.class)).thenReturn(expectedSmartObjects);

        SmartObjects smartObjects = userService.findUserObjects("test", true);

        assertUserObjects(smartObjects, expectedSmartObjects);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, SmartObjects.class);
    }

    @Test
    public void findUserObjectsWithDetailsAndModel() throws Exception {
        final SmartObjects expectedSmartObjects = new SmartObjects();

        String calledUrl = buildPath("/users/test/objects?details=true&object_model=model&show_history=false");
        when(mockedRestTemplate.getForObject(calledUrl, SmartObjects.class)).thenReturn(expectedSmartObjects);

        SmartObjects smartObjects = userService.findUserObjects("test", true, "model");
        assertUserObjects(smartObjects, expectedSmartObjects);
        verify(mockedRestTemplate, only()).getForObject(calledUrl, SmartObjects.class);
    }

    @Test
    public void findUserObjectsWithDetailsAndModelAndHistory() throws Exception {

        String calledUrl = buildPath("/users/test/objects?details=true&object_model=model&show_history=true");
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
