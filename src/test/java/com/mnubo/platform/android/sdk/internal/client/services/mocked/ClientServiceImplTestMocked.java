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
 */package com.mnubo.platform.android.sdk.internal.client.services.mocked;

import com.mnubo.platform.android.sdk.internal.MockedAbstractServiceTest;
import com.mnubo.platform.android.sdk.internal.client.services.ClientService;
import com.mnubo.platform.android.sdk.internal.client.services.impl.ClientServiceImpl;
import com.mnubo.platform.android.sdk.models.security.ResetPassword;
import com.mnubo.platform.android.sdk.models.security.UserConfirmation;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ClientServiceImplTestMocked extends MockedAbstractServiceTest {

    private ClientService clientService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        clientService = new ClientServiceImpl(PLATFORM_BASE_URL, mockedRestTemplate);
    }

    @Test
    public void testCreateUser() throws Exception {

        String calledUrl = expectedUrl("/users");

        clientService.createUser(expectedUser);
        verify(mockedRestTemplate, only()).postForLocation(calledUrl, expectedUser);

    }

    @Test
    public void testConfirmCreation() throws Exception {

        UserConfirmation userConfirmation = new UserConfirmation("token", "password");

        String calledUrl = expectedUrl("/users/test/confirmation");
        when(mockedRestTemplate.postForLocation(calledUrl, userConfirmation)).thenReturn(new URI(PLATFORM_BASE_URL));

        clientService.confirmUserCreation("test", userConfirmation);
        verify(mockedRestTemplate, only()).postForLocation(calledUrl, userConfirmation);

    }

    @Test
    public void testResetPassword() throws Exception {
        String calledUrl = expectedUrl("/users/test/password");

        clientService.resetPassword("test");

        verify(mockedRestTemplate, only()).delete(calledUrl);
    }

    @Test
    public void testConfirmPasswordUpdate() throws Exception {
        ResetPassword resetPassword = new ResetPassword("token", "password", "password");

        String calledUrl = expectedUrl("/users/test/password");
        when(mockedRestTemplate.postForLocation(calledUrl, resetPassword)).thenReturn(new URI(PLATFORM_BASE_URL));

        clientService.confirmPasswordReset("test", resetPassword);

        verify(mockedRestTemplate, only()).postForLocation(calledUrl, resetPassword);

    }
}