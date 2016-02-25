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
        completeProps.put(MnuboSDKConfig.CONFIG_RESTITUTION_URL, "http://valid.com/restitution");
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
        assertThat(actual.getRestitutionUrl().toString(), equalTo("http://valid.com/search"));

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
        assertThat(actual.getRestitutionUrl().toString(), equalTo("http://valid.com/search"));
    }

    @Test
    public void testWithFullProperties() throws Exception {
        MnuboSDKConfig actual = MnuboSDKConfig.withProperties(completeProps);
        assertThat(actual.getKey(), equalTo("key"));

        assertThat(actual.getOauthUrl().toString(), equalTo("http://valid.com/oauth/token"));
        assertThat(actual.getIngestionUrl().toString(), equalTo("http://valid.com/ingestion"));
        assertThat(actual.getRestitutionUrl().toString(), equalTo("http://valid.com/restitution"));
    }
}