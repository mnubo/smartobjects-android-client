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
import com.mnubo.platform.android.sdk.internal.services.ClientService;
import com.mnubo.platform.android.sdk.models.security.ResetPassword;
import com.mnubo.platform.android.sdk.models.security.UserConfirmation;
import com.mnubo.platform.android.sdk.models.users.User;

import org.junit.Before;
import org.junit.Test;

import static com.mnubo.platform.android.sdk.Config.MnuboSDKConfig.DEFAULT_BASE_PATH;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class ClientServiceImplTest extends AbstractServicesTest {
    private ClientService clientService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.mnuboUserApi = new MnuboSDKApiImpl(CLIENT_ACCESS_TOKEN, PLATFORM_BASE_URL, DEFAULT_BASE_PATH);
        setUpMockServer();

        clientService = mnuboUserApi.clientService();
    }

    @Test
    public void createUser() throws Exception {

        final User userToCreate = new User();
        userToCreate.setUsername("test");
        userToCreate.setLastname("lastname");
        userToCreate.setUsername("username");

        userToCreate.setPassword("password");
        userToCreate.setConfirmedPassword("password");

        mockServiceServer.expect(requestTo(expectedUrl("/users")))
                .andExpect(method(POST))
                .andExpect(clientAuthMatch())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(toJson(userToCreate)))
                .andRespond(withSuccess(toJson(userToCreate), APPLICATION_JSON_UTF8));

        clientService.createUser(userToCreate);
        mockServiceServer.verify();
    }

    @Test
    public void testConfirmCreation() throws Exception {

        UserConfirmation userConfirmation = new UserConfirmation("token", "password");

        mockServiceServer.expect(requestTo(expectedUrl("/users/test/confirmation")))
                .andExpect(method(POST))
                .andExpect(clientAuthMatch())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(toJson(userConfirmation)))
                .andRespond(withSuccess());

        clientService.confirmUserCreation("test", userConfirmation);
        mockServiceServer.verify();
    }

    @Test
    public void testResetPassword() throws Exception {

        mockServiceServer.expect(requestTo(expectedUrl("/users/test/password")))
                .andExpect(method(DELETE))
                .andExpect(clientAuthMatch())
                .andRespond(withSuccess());

        clientService.resetPassword("test");
        mockServiceServer.verify();

    }

    @Test
    public void testConfirmPasswordReset() throws Exception {

        final ResetPassword password = new ResetPassword("token", "password", "password");
        mockServiceServer.expect(requestTo(expectedUrl("/users/test/password")))
                .andExpect(method(POST))
                .andExpect(clientAuthMatch())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(toJson(password)))
                .andRespond(withSuccess());

        clientService.confirmPasswordReset("test", password);
        mockServiceServer.verify();
    }
}