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

package com.mnubo.platform.android.sdk.internal.connect.connection;

import com.mnubo.platform.android.sdk.internal.api.MnuboSDKApi;
import com.mnubo.platform.android.sdk.internal.connect.MnuboConnectionFactory;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.RefreshableConnection;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.impl.ClientConnection;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.impl.UserConnection;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@SuppressWarnings("unchecked")
public class MnuboConnectionManagerTest {

    private ConnectionRepository mockedConnectionRepository = mock(ConnectionRepository.class);
    private MnuboConnectionFactory mockedConnectionFactory = mock(MnuboConnectionFactory.class);


    @Test
    public void testGetCurrentConnectionNotLoggedIn() throws Exception {
        MnuboConnectionManager connectionManager = new MnuboConnectionManager(mockedConnectionFactory, mockedConnectionRepository);

        RefreshableConnection connection = connectionManager.getCurrentConnection();

        verify(mockedConnectionRepository).findPrimaryConnection(MnuboSDKApi.class);
        assertNotNull(connection);
        assertThat(connection, is(instanceOf(ClientConnection.class)));
    }

    @Test
    public void testGetCurrentConnectionLoggedIn() throws Exception {
        Connection<MnuboSDKApi> api = mock(Connection.class);
        when(mockedConnectionRepository.findPrimaryConnection(MnuboSDKApi.class)).thenReturn(api);
        MnuboConnectionManager connectionManager = new MnuboConnectionManager(mockedConnectionFactory, mockedConnectionRepository);

        RefreshableConnection connection = connectionManager.getCurrentConnection();

        verify(mockedConnectionRepository).findPrimaryConnection(MnuboSDKApi.class);
        assertNotNull(connection);
        assertThat(connection, is(instanceOf(UserConnection.class)));
    }

    @Test
    public void testLogOut() throws Exception {
        final String providerId = "provider_id";
        when(mockedConnectionFactory.getProviderId()).thenReturn(providerId);

        MnuboConnectionManager connectionManager = new MnuboConnectionManager(mockedConnectionFactory, mockedConnectionRepository);
        connectionManager.logOut();
        String username = connectionManager.getUsername();

        verify(mockedConnectionFactory).getProviderId();
        verify(mockedConnectionRepository).removeConnections(eq(providerId));
        assertThat(username, is(nullValue()));
    }

    @Test
    public void testLogIn() throws Exception {
        final String username = "username";
        final String password = "password";
        final AccessGrant token = new AccessGrant("token");
        OAuth2Operations operations = mock(OAuth2Operations.class);
        when(operations.exchangeCredentialsForAccess(eq(username), eq(password), isNull(MultiValueMap.class))).thenReturn(token);

        Connection<MnuboSDKApi> api = mock(Connection.class);
        final String providerId = "provider_id";
        when(mockedConnectionFactory.getProviderId()).thenReturn(providerId);
        when(mockedConnectionFactory.getOAuthOperations()).thenReturn(operations);
        when(mockedConnectionFactory.createConnection(eq(token))).thenReturn(api);

        MnuboConnectionManager connectionManager = new MnuboConnectionManager(mockedConnectionFactory, mockedConnectionRepository);
        connectionManager.logIn(username, password);

        verify(mockedConnectionFactory).getProviderId();
        verify(operations).exchangeCredentialsForAccess(eq(username), eq(password), isNull(MultiValueMap.class));
        verify(mockedConnectionRepository).removeConnections(eq(providerId));
        verify(mockedConnectionRepository).addConnection(eq(api));
        verify(mockedConnectionFactory).createConnection(eq(token));

        //logged in
        RefreshableConnection connection = connectionManager.getCurrentConnection();
        assertNotNull(connection);
        assertThat(connection, is(instanceOf(UserConnection.class)));
    }

    @Test(expected = HttpClientErrorException.class)
    public void testLogInInvalid() throws Exception {
        final String username = "username";
        final String password = "password";
        OAuth2Operations operations = mock(OAuth2Operations.class);
        when(operations.exchangeCredentialsForAccess(eq(username), eq(password), isNull(MultiValueMap.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        final String providerId = "provider_id";
        when(mockedConnectionFactory.getProviderId()).thenReturn(providerId);
        when(mockedConnectionFactory.getOAuthOperations()).thenReturn(operations);

        MnuboConnectionManager connectionManager = new MnuboConnectionManager(mockedConnectionFactory, mockedConnectionRepository);
        connectionManager.logIn(username, password);

        verify(mockedConnectionFactory).getProviderId();
        verify(mockedConnectionRepository).removeConnections(eq(providerId));
        verify(operations).exchangeCredentialsForAccess(eq(username), eq(password), isNull(MultiValueMap.class));
    }

    @Test
    public void testIsUserConnected() throws Exception {
        MnuboConnectionManager connectionManager = new MnuboConnectionManager(mockedConnectionFactory, mockedConnectionRepository);
        boolean connected = connectionManager.isUserConnected();
        assertFalse(connected);
    }

    @Test
    public void testIsUserConnectedLoggedIn() throws Exception {
        Connection<MnuboSDKApi> api = mock(Connection.class);
        when(mockedConnectionRepository.findPrimaryConnection(MnuboSDKApi.class)).thenReturn(api);
        MnuboConnectionManager connectionManager = new MnuboConnectionManager(mockedConnectionFactory, mockedConnectionRepository);

        boolean connected = connectionManager.isUserConnected();

        assertTrue(connected);
    }

    @Test
    public void testGetUsername() throws Exception {
        MnuboConnectionManager connectionManager = new MnuboConnectionManager(mockedConnectionFactory, mockedConnectionRepository);
        String username = connectionManager.getUsername();
        assertNull(username);
    }

    @Test
    public void testGetUsernameLoggedIn() throws Exception {
        final String loggedUser = "username";
        ConnectionKey key = new ConnectionKey("provider", loggedUser);
        Connection<MnuboSDKApi> api = mock(Connection.class);
        when(api.getKey()).thenReturn(key);
        when(mockedConnectionRepository.findPrimaryConnection(MnuboSDKApi.class)).thenReturn(api);
        MnuboConnectionManager connectionManager = new MnuboConnectionManager(mockedConnectionFactory, mockedConnectionRepository);


        String username = connectionManager.getUsername();
        assertNotNull(username);
        assertThat(username, is(equalTo(loggedUser)));
        connectionManager.logOut();

        String usernameAfterLogOut = connectionManager.getUsername();
        assertThat(usernameAfterLogOut, is(nullValue()));
    }
}