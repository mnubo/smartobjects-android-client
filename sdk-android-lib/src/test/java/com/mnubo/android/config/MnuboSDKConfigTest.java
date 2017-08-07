/*
 * Copyright (c) 2017 Mnubo. Released under MIT License.
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

package com.mnubo.android.config;

import android.util.Log;

import com.mnubo.android.utils.TimeUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Properties;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;


@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Log.class,
})
public class MnuboSDKConfigTest {

    private final static Properties props = new Properties();

    static {
        props.put(MnuboSDKConfig.CONFIG_KEY, "key");
        props.put(MnuboSDKConfig.CONFIG_URL, "http://valid.com");
    }

    private final static Properties completeProps = new Properties();

    static {
        completeProps.put(MnuboSDKConfig.CONFIG_KEY, "key");
        completeProps.put(MnuboSDKConfig.CONFIG_URL, "http://valid.com");
        completeProps.put(MnuboSDKConfig.CONFIG_INGESTION_URL, "http://valid.com/ingestion");
        completeProps.put(MnuboSDKConfig.CONFIG_OAUTH_URL, "http://valid.com/oauth/token");
    }

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void testWithUrlAndKey() throws Exception {
        MnuboSDKConfig actual = MnuboSDKConfig.withUrlAndKey("http://valid.com", "key");
        assertThat(actual.getKey(), equalTo("key"));

        assertThat(actual.getOauthUrl().toString(), equalTo("http://valid.com/oauth/token"));
        assertThat(actual.getIngestionUrl().toString(), equalTo("http://valid.com/api/v3"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullKey() throws Exception {

        MnuboSDKConfig.withUrlAndKey("http://valid.com", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitEmptyKey() throws Exception {

        MnuboSDKConfig.withUrlAndKey("http://valid.com", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullHostname() throws Exception {

        MnuboSDKConfig.withUrlAndKey(null, "key");

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitEmptyHostname() throws Exception {

        MnuboSDKConfig.withUrlAndKey("", "key");

    }

    @Test
    public void testWithProperties() throws Exception {
        MnuboSDKConfig actual = MnuboSDKConfig.withProperties(props);
        assertThat(actual.getKey(), equalTo("key"));

        assertThat(actual.getOauthUrl().toString(), equalTo("http://valid.com/oauth/token"));
        assertThat(actual.getIngestionUrl().toString(), equalTo("http://valid.com/api/v3"));
    }

    @Test
    public void testWithFullProperties() throws Exception {
        MnuboSDKConfig actual = MnuboSDKConfig.withProperties(completeProps);
        assertThat(actual.getKey(), equalTo("key"));

        assertThat(actual.getOauthUrl().toString(), equalTo("http://valid.com/oauth/token"));
        assertThat(actual.getIngestionUrl().toString(), equalTo("http://valid.com/ingestion"));
    }
}