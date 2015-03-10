package com.mnubo.platform.android.sdk.internal.user.services.impl;

import com.mnubo.platform.android.sdk.internal.user.services.TokenValidationService;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TokenValidationServiceImplTest extends AbstractTestOperations {

    private TokenValidationService tokenValidationService;

    @Before
    public void setUp() throws Exception {
        tokenValidationService = new TokenValidationServiceImpl(PLATFORM_BASE_URL, USER_ACCESS_TOKEN, mockedRestTemplate);
    }

    @Test
    public void testValidate() throws Exception {

        Map<String, Object> result = new HashMap<>();
        result.put("user_name", "test@username.com");
        String calledUrl = buildPath("/oauth", String.format("/check_token?token=%s", USER_ACCESS_TOKEN));

        when(mockedRestTemplate.getForObject(calledUrl, Map.class)).thenReturn(result);

        String username = tokenValidationService.validate();
        assertThat(username, equalTo("test@username.com"));

        verify(mockedRestTemplate, only()).getForObject(calledUrl, Map.class);

    }

    @Test
    public void testValidateInvalidResult() throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("consumer_key", "KEY");
        String calledUrl = buildPath("/oauth", String.format("/check_token?token=%s", USER_ACCESS_TOKEN));

        when(mockedRestTemplate.getForObject(calledUrl, Map.class)).thenReturn(result);

        String username = tokenValidationService.validate();
        assertThat(username, nullValue());

        verify(mockedRestTemplate, only()).getForObject(calledUrl, Map.class);

    }

    @Test
    public void testValidateNullResult() throws Exception {
        String calledUrl = buildPath("/oauth", String.format("/check_token?token=%s", USER_ACCESS_TOKEN));

        when(mockedRestTemplate.getForObject(calledUrl, Map.class)).thenReturn(null);

        String username = tokenValidationService.validate();
        assertThat(username, nullValue());

        verify(mockedRestTemplate, only()).getForObject(calledUrl, Map.class);

    }
}