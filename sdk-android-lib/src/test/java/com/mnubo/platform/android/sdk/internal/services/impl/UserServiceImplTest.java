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

import com.mnubo.platform.android.sdk.Config.MnuboSDKConfig;
import com.mnubo.platform.android.sdk.internal.api.MnuboSDKApiImpl;
import com.mnubo.platform.android.sdk.internal.services.UserService;
import com.mnubo.platform.android.sdk.models.security.UpdatePassword;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;
import com.mnubo.platform.android.sdk.models.users.User;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.mnubo.platform.android.sdk.Config.MnuboSDKConfig.DEFAULT_BASE_PATH;
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
        this.mnuboUserApi = new MnuboSDKApiImpl(USER_ACCESS_TOKEN, PLATFORM_BASE_URL, DEFAULT_BASE_PATH);
        setUpMockServer();

        userService = mnuboUserApi.userService();
    }

    @Test
    public void getUserTestWithUserme() throws Exception {

        final User expectedUser = new User();
        expectedUser.setUsername("test");
        expectedUser.setLastname("lastname");
        expectedUser.setUsername("username");

        mockServiceServer.expect(requestTo(expectedUrl("/users/test")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedUser), APPLICATION_JSON_UTF8));

        User user = userService.getUser("test");
        mockServiceServer.verify();
    }

    @Test
    public void getUserTestAttributes() throws Exception {

        final User expectedUser = new User();
        expectedUser.setUsername("test");
        expectedUser.setLastname("lastname");
        expectedUser.setUsername("username");

        mockServiceServer.expect(requestTo(expectedUrl("/users/test?attributes=attributes&attributes=attributes2")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withSuccess(toJson(expectedUser), APPLICATION_JSON_UTF8));

        List<String> attributes = Arrays.asList("attributes", "attributes2");
        User user = userService.getUser("test", attributes);

        mockServiceServer.verify();
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockServiceServer.expect(requestTo(expectedUrl("/users/test")))
                .andExpect(method(DELETE))
                .andRespond(withNoContent());

        userService.delete("test");
        mockServiceServer.verify();
    }

    @Test
    public void updateUserTest() throws Exception {

        final User expectedUser = new User();
        expectedUser.setUsername("test");
        expectedUser.setLastname("lastname");
        expectedUser.setUsername("username");

        mockServiceServer.expect(requestTo(expectedUrl("/users/test")))
                .andExpect(method(PUT))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(toJson(expectedUser)))
                .andRespond(withNoContent());

        userService.update("test", expectedUser);
        mockServiceServer.verify();
    }

    @Test
    public void updatePasswordTest() throws Exception {

        final UpdatePassword updatePassword = new UpdatePassword("old", "new", "new");
        updatePassword.setPreviousPassword("old");
        updatePassword.setPassword("new");
        updatePassword.setConfirmedPassword("new");

        mockServiceServer.expect(requestTo(expectedUrl("/users/test/password")))
                .andExpect(method(PUT))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(toJson(updatePassword)))
                .andRespond(withNoContent());

        userService.updatePassword("test", updatePassword);
        mockServiceServer.verify();
    }

    @Test
    public void findUserObjectsWithDetailsAndModelAndHistory() throws Exception {

        mockServiceServer.expect(requestTo(expectedUrl("/users/test/objects?details=true&object_model=model&show_history=true")))
                .andExpect(method(GET))
                .andExpect(userAuthMatch())
                .andRespond(withNoContent());

        SmartObjects SmartObjects = userService.findUserObjects("test", true, "model", true);
        mockServiceServer.verify();
    }

}
