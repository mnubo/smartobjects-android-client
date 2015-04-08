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

package com.mnubo.platform.android.sdk.internal.tasks.impl;

import com.mnubo.platform.android.sdk.internal.api.MnuboSDKApi;
import com.mnubo.platform.android.sdk.internal.connect.connection.MnuboConnectionManager;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.RefreshableConnection;
import com.mnubo.platform.android.sdk.internal.services.ClientService;
import com.mnubo.platform.android.sdk.internal.services.SmartObjectService;
import com.mnubo.platform.android.sdk.internal.services.UserService;

import org.junit.Before;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public abstract class AbstractTaskTest {
    protected RefreshableConnection connection = mock(RefreshableConnection.class);
    protected MnuboConnectionManager connectionManager = mock(MnuboConnectionManager.class);
    protected MnuboSDKApi mnuboSDKApi = mock(MnuboSDKApi.class);

    protected ClientService clientService = mock(ClientService.class);
    protected SmartObjectService smartObjectService = mock(SmartObjectService.class);
    protected UserService userService = mock(UserService.class);

    @Before
    public void setUp() throws Exception {
        when(mnuboSDKApi.clientService()).thenReturn(clientService);
        when(mnuboSDKApi.objectService()).thenReturn(smartObjectService);
        when(mnuboSDKApi.userService()).thenReturn(userService);
        when(connection.getMnuboSDKApi()).thenReturn(mnuboSDKApi);
        when(connectionManager.getCurrentConnection()).thenReturn(connection);

    }
}
