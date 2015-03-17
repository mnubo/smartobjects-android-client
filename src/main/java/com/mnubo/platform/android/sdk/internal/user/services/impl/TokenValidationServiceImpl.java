package com.mnubo.platform.android.sdk.internal.user.services.impl;

import com.mnubo.platform.android.sdk.internal.connect.query.PlatformPath;
import com.mnubo.platform.android.sdk.internal.connect.query.PlatformQuery;
import com.mnubo.platform.android.sdk.internal.services.AbstractMnuboService;
import com.mnubo.platform.android.sdk.internal.user.services.TokenValidationService;

import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class TokenValidationServiceImpl extends AbstractMnuboService implements TokenValidationService {

    private final RestTemplate restTemplate;
    private final String accessToken;

    public TokenValidationServiceImpl(String platformBaseUrl, String accessToken, RestTemplate restTemplate) {
        super(platformBaseUrl, PlatformPath.collections);
        this.restTemplate = restTemplate;
        this.accessToken = accessToken;
    }

    @Override
    public String validate() {
        PlatformQuery query = new PlatformQuery(getPlatformBaseUrl(), "/oauth", PlatformPath.check_token);

        query.queryParam("token", this.accessToken);

        @SuppressWarnings("unchecked")
        Map<String, Object> checkResult = restTemplate.getForObject(query.buildUrl(), Map.class);

        if (checkResult != null) {
            return checkResult.get("user_name") != null ? checkResult.get("user_name").toString() : null;
        }
        return null;
    }
}
