package com.mnubo.platform.android.sdk;

import android.content.Context;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Log.class,
        Mnubo.class
})
public class MnuboTest {

    private final Context mockedContext = mock(Context.class);

    @Before
    public void setUp() throws Exception {
        mockStatic(Log.class);
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
}