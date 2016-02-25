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

package com.mnubo.android;

import android.content.Context;
import android.util.Log;

import com.mnubo.android.api.AuthenticationProblemCallback;
import com.mnubo.android.config.MnuboSDKConfig;
import com.mnubo.android.exceptions.MnuboAlreadyInitializedException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import okhttp3.HttpUrl;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Log.class
})
@PowerMockIgnore("javax.net.*")
public class MnuboTest {

    private final Context mockedContext = mock(Context.class);
    private final AuthenticationProblemCallback authenticationProblemCallback = new AuthenticationProblemCallback() {
        @Override
        public void onError() {
            throw new IllegalArgumentException();
        }
    };

    MnuboSDKConfig dummyCfg = MnuboSDKConfig.builder()
            .key("key")
            .ingestionUrl(HttpUrl.parse("http://example.com"))
            .oauthUrl(HttpUrl.parse("http://example.com"))
            .restitutionUrl(HttpUrl.parse("http://example.com"))
            .build();

    @Before
    public void setUp() throws Exception {
        mockStatic(Log.class);
    }

    @Test(expected = NullPointerException.class)
    public void testInitNoContext() throws Exception {
        Mnubo.init(null, dummyCfg, authenticationProblemCallback);
    }

    @Test(expected = NullPointerException.class)
    public void testInitNullKey() throws Exception {
        Mnubo.init(mockedContext, null, authenticationProblemCallback);
    }
}