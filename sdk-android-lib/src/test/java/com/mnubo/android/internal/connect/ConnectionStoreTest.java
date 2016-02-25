/*
 * Copyright (c) 2016 Mnubo. Released under MIT License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.mnubo.android.internal.connect;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mnubo.android.utils.TimeUtils;

import org.bouncycastle.asn1.cms.Time;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Log.class,
        TimeUtils.class,
})
public class ConnectionStoreTest {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private ConnectionStore testee;

    @Before
    public void setUp() throws Exception {
        mockStatic(Log.class);
        mockStatic(TimeUtils.class);
        when(TimeUtils.getCurrentTimeSeconds()).thenReturn(0L);
        context = mock(Context.class);
        editor = mock(SharedPreferences.Editor.class);
        sharedPreferences = mock(SharedPreferences.class);

        when(sharedPreferences.edit()).thenReturn(editor);

        when(context.getSharedPreferences(eq("com.mnubo.android.sdk.connectionstore"), eq(0x0000)))
                .thenReturn(sharedPreferences);

        testee = new ConnectionStore(context);
    }

    @Test
    public void testClear() throws Exception {
        testee.clear();

        verify(editor, times(1)).clear();
        verify(editor, times(1)).apply();
    }

    @Test
    public void testSaveConnection() throws Exception {
        testee.saveConnection(new MnuboConnection("username", new MnuboConnection.Token("access", "refresh", 500L)));

        verify(editor, times(1)).putString(eq("mnubo.username"), eq("username"));
        verify(editor, times(1)).putString(eq("mnubo.access_token"), eq("access"));
        verify(editor, times(1)).putString(eq("mnubo.refresh_token"), eq("refresh"));
        verify(editor, times(1)).putLong(eq("mnubo.expire_time"), eq(500L));
        verify(editor, times(1)).apply();
    }

    @Test
    public void testGetConnection_hasConnection() throws Exception {
        when(sharedPreferences.contains(eq("mnubo.username"))).thenReturn(true);
        when(sharedPreferences.contains(eq("mnubo.access_token"))).thenReturn(true);
        when(sharedPreferences.contains(eq("mnubo.refresh_token"))).thenReturn(true);
        when(sharedPreferences.contains(eq("mnubo.expire_time"))).thenReturn(true);


        when(sharedPreferences.getString(eq("mnubo.username"), anyString())).thenReturn("username");
        when(sharedPreferences.getString(eq("mnubo.access_token"), anyString())).thenReturn("access");
        when(sharedPreferences.getString(eq("mnubo.refresh_token"), anyString())).thenReturn("refresh");
        when(sharedPreferences.getLong(eq("mnubo.expire_time"), anyLong())).thenReturn(500L);

        MnuboConnection connection = testee.getConnection();
        assertThat(connection.getUsername(), equalTo("username"));
        assertThat(connection.getToken(), equalTo(new MnuboConnection.Token("access", "refresh", 500L)));

    }

    @Test
    public void testGetConnection_hasNoConnection() throws Exception {
        when(sharedPreferences.contains(eq("mnubo.username"))).thenReturn(false);

        MnuboConnection connection = testee.getConnection();
        assertThat(connection, equalTo(null));
    }

    @Test
    public void testHasConnection() throws Exception {
        when(sharedPreferences.contains(eq("mnubo.username"))).thenReturn(true);
        when(sharedPreferences.contains(eq("mnubo.access_token"))).thenReturn(true);
        when(sharedPreferences.contains(eq("mnubo.refresh_token"))).thenReturn(true);
        when(sharedPreferences.contains(eq("mnubo.expire_time"))).thenReturn(true);

        assertThat(testee.hasConnection(), equalTo(true));
    }

    @Test
    public void testHasConnection_NotInTheStore() throws Exception {
        when(sharedPreferences.contains(eq("mnubo.username"))).thenReturn(true);
        when(sharedPreferences.contains(eq("mnubo.access_token"))).thenReturn(true);
        when(sharedPreferences.contains(eq("mnubo.refresh_token"))).thenReturn(true);
        when(sharedPreferences.contains(eq("mnubo.expire_time"))).thenReturn(false);

        assertThat(testee.hasConnection(), equalTo(false));
    }
}