package com.mnubo.platform.android.sdk;

import android.content.Context;
import android.util.Log;

import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.api.MnuboApiFactory;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboNotInitializedException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Log.class,
        Mnubo.class
})
public class MnuboTest {

    private final Context mockedContext = mock(Context.class);
    private final MnuboApiFactory mockedApiFactory = mock(MnuboApiFactory.class);
    private final MnuboApi mockedApi = mock(MnuboApi.class);

    @Before
    public void setUp() throws Exception {
        mockStatic(Log.class);
        whenNew(MnuboApiFactory.class).withAnyArguments().thenReturn(mockedApiFactory);

        when(mockedApiFactory.getApi()).thenReturn(mockedApi);
    }

    @Test
    public void testGetApi() throws Exception {
        Mnubo.init(mockedContext, "key", "secret", "hostname");

        MnuboApi api = Mnubo.getApi();
        assertThat(api, is(equalTo(mockedApi)));

        verify(mockedApiFactory, only()).getApi();
    }

    @Test
    public void testGetUsername() throws Exception {

    }
}