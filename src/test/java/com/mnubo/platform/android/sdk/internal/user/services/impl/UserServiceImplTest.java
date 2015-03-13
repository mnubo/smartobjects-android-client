package com.mnubo.platform.android.sdk.internal.user.services.impl;

import com.mnubo.platform.android.sdk.internal.user.services.UserService;
import com.mnubo.platform.android.sdk.models.security.UpdatePassword;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;
import com.mnubo.platform.android.sdk.models.users.User;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


public class UserServiceImplTest extends AbstractServicesTest {

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        userService = mnuboUserApi.userService();
    }

    @Test
    public void getUserTestWithUserme() throws Exception {

        final User expectedUser = new User();
        expectedUser.setUsername("test");
        expectedUser.setLastname("lastname");
        expectedUser.setUsername("username");

        mockServer.expect(requestTo(expectedUrl("/users/test")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedUser), APPLICATION_JSON_UTF8));

        User user = userService.getUser("test");
        mockServer.verify();
    }

    @Test
    public void getUserTestAttributes() throws Exception {

        final User expectedUser = new User();
        expectedUser.setUsername("test");
        expectedUser.setLastname("lastname");
        expectedUser.setUsername("username");

        mockServer.expect(requestTo(expectedUrl("/users/test?attributes=attributes&attributes=attributes2")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedUser), APPLICATION_JSON_UTF8));

        List<String> attributes = Arrays.asList("attributes", "attributes2");
        User user = userService.getUser("test", attributes);

        mockServer.verify();
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockServer.expect(requestTo(expectedUrl("/users/test")))
                .andExpect(method(DELETE))
                .andRespond(withNoContent());

        userService.delete("test");
        mockServer.verify();
    }

    @Test
    public void updateUserTest() throws Exception {

        final User expectedUser = new User();
        expectedUser.setUsername("test");
        expectedUser.setLastname("lastname");
        expectedUser.setUsername("username");

        mockServer.expect(requestTo(expectedUrl("/users/test")))
                .andExpect(method(PUT))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(toJson(expectedUser)))
                .andRespond(withNoContent());

        userService.update("test", expectedUser);
        mockServer.verify();
    }

    @Test
    public void updatePasswordTest() throws Exception {

        final UpdatePassword updatePassword = new UpdatePassword("old", "new", "new");
        updatePassword.setPreviousPassword("old");
        updatePassword.setPassword("new");
        updatePassword.setConfirmedPassword("new");

        mockServer.expect(requestTo(expectedUrl("/users/test/password")))
                .andExpect(method(PUT))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(toJson(updatePassword)))
                .andRespond(withNoContent());

        userService.updatePassword("test", updatePassword);
        mockServer.verify();
    }

    @Test
    public void findSmartObjects() throws Exception {

        final SmartObjects expectedSmartObjects = new SmartObjects();

        mockServer.expect(requestTo(expectedUrl("/users/test/objects?details=false&show_history=false")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedSmartObjects), APPLICATION_JSON_UTF8));

        SmartObjects SmartObjects = userService.findUserObjects("test");
        mockServer.verify();
    }

    @Test
    public void findSmartObjectsWithDetails() throws Exception {

        final SmartObjects expectedSmartObjects = new SmartObjects();

        mockServer.expect(requestTo(expectedUrl("/users/test/objects?details=true&show_history=false")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedSmartObjects), APPLICATION_JSON_UTF8));

        SmartObjects SmartObjects = userService.findUserObjects("test", true);
        mockServer.verify();
    }

    @Test
    public void findSmartObjectsWithDetailsAndModel() throws Exception {
        final SmartObjects expectedSmartObjects = new SmartObjects();

        mockServer.expect(requestTo(expectedUrl("/users/test/objects?details=true&object_model=model&show_history=false")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedSmartObjects), APPLICATION_JSON_UTF8));

        SmartObjects SmartObjects = userService.findUserObjects("test", true, "model");
        mockServer.verify();
    }

    @Test
    public void findSmartObjectsWithDetailsAndModelAndHistory() throws Exception {

        mockServer.expect(requestTo(expectedUrl("/users/test/objects?details=true&object_model=model&show_history=true")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withNoContent());

        SmartObjects SmartObjects = userService.findUserObjects("test", true, "model", true);
        mockServer.verify();
    }

}
