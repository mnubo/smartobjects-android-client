package com.mnubo.platform.android.sdk;

import android.content.Context;
import android.util.Log;

import com.mnubo.platform.android.sdk.api.MnuboApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Log.class,
        Mnubo.class
})
public class MnuboTest {

    private final Context mockedContext = mock(Context.class);
    private final MnuboApi mockedApi = mock(MnuboApi.class);

    @Before
    public void setUp() throws Exception {
        mockStatic(Log.class);
    }

    @Test
    public void testGetApi() throws Exception {
        fail();
    }

    @Test
    public void testGetUsername() throws Exception {

    }
}