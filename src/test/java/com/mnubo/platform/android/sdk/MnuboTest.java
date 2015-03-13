package com.mnubo.platform.android.sdk;

import android.content.Context;
import android.util.Log;

import com.mnubo.platform.android.sdk.api.MnuboApi;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Log.class,
        PlainSocketFactory.class,
        SSLSocketFactory.class,
        HttpComponentsClientHttpRequestFactory.class
})
public class MnuboTest {

    private final Context mockedContext = mock(Context.class);
    private final PlainSocketFactory mockedPlainSocketFactory = mock(PlainSocketFactory.class);
    private final SSLSocketFactory mockedSSLSocketFactory = mock(SSLSocketFactory.class);
    private final SchemeRegistry mockedSchemeRegistry = mock(SchemeRegistry.class);

    @Before
    public void setUp() throws Exception {
        mockStatic(Log.class);
        mockStatic(PlainSocketFactory.class);
        mockStatic(SchemeRegistry.class);
        mockStatic(SSLSocketFactory.class);

        whenNew(SchemeRegistry.class).withAnyArguments().thenReturn(mockedSchemeRegistry);
        when(PlainSocketFactory.getSocketFactory()).thenReturn(mockedPlainSocketFactory);
        when(SSLSocketFactory.getSocketFactory()).thenReturn(mockedSSLSocketFactory);
    }

    @Test(expected = NullPointerException.class)
    public void testInitNoContext() throws Exception {

        Mnubo.init(null, "key", "secret", "hostname");

    }

    @Test(expected = NullPointerException.class)
    public void testInitNullKey() throws Exception {

        Mnubo.init(mockedContext, null, "secret", "hostname");

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitEmptyKey() throws Exception {

        Mnubo.init(mockedContext, "", "secret", "hostname");

    }

    @Test(expected = NullPointerException.class)
    public void testInitNullSecret() throws Exception {

        Mnubo.init(mockedContext, "key", null, "hostname");

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitEmptySecret() throws Exception {

        Mnubo.init(mockedContext, "key", "", "hostname");

    }

    @Test(expected = NullPointerException.class)
    public void testInitNullHostname() throws Exception {

        Mnubo.init(mockedContext, "key", "secret", null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitEmptyHostname() throws Exception {

        Mnubo.init(mockedContext, "key", "secret", "");

    }

    @Test
    public void testGetApi() throws Exception {
        Mnubo.init(mockedContext, "key", "secret", "hostname");
        MnuboApi api = Mnubo.getApi();
    }

    @Test
    public void testGetUsername() throws Exception {

    }
}