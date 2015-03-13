package com.mnubo.platform.android.sdk.api;

import android.content.Context;

import org.junit.Test;

import static org.mockito.Mockito.mock;

public class MnuboApiFactoryTest {

    private final Context mockedContext = mock(Context.class);

    @Test(expected = NullPointerException.class)
    public void testInitNoContext() throws Exception {

        new MnuboApiFactory(null, "key", "secret", "hostname");

    }

    @Test(expected = NullPointerException.class)
    public void testInitNullKey() throws Exception {

        new MnuboApiFactory(mockedContext, null, "secret", "hostname");

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitEmptyKey() throws Exception {

        new MnuboApiFactory(mockedContext, "", "secret", "hostname");

    }

    @Test(expected = NullPointerException.class)
    public void testInitNullSecret() throws Exception {

        new MnuboApiFactory(mockedContext, "key", null, "hostname");

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitEmptySecret() throws Exception {

        new MnuboApiFactory(mockedContext, "key", "", "hostname");

    }

    @Test(expected = NullPointerException.class)
    public void testInitNullHostname() throws Exception {

        new MnuboApiFactory(mockedContext, "key", "secret", null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitEmptyHostname() throws Exception {

        new MnuboApiFactory(mockedContext, "key", "secret", "");

    }

    @Test
    public void testGetApi() throws Exception {

    }

    @Test
    public void testReadUsername() throws Exception {

    }

    @Test
    public void testDeleteSavedUsername() throws Exception {

    }
}