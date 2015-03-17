package com.mnubo.platform.android.sdk.internal.user.api;


import com.mnubo.platform.android.sdk.internal.connect.MnuboAPIErrorHandler;
import com.mnubo.platform.android.sdk.internal.user.services.CollectionService;
import com.mnubo.platform.android.sdk.internal.user.services.GroupService;
import com.mnubo.platform.android.sdk.internal.user.services.SmartObjectService;
import com.mnubo.platform.android.sdk.internal.user.services.TokenValidationService;
import com.mnubo.platform.android.sdk.internal.user.services.UserService;
import com.mnubo.platform.android.sdk.internal.user.services.impl.CollectionServiceImpl;
import com.mnubo.platform.android.sdk.internal.user.services.impl.GroupServiceImpl;
import com.mnubo.platform.android.sdk.internal.user.services.impl.SmartObjectServiceImpl;
import com.mnubo.platform.android.sdk.internal.user.services.impl.TokenValidationServiceImpl;
import com.mnubo.platform.android.sdk.internal.user.services.impl.UserServiceImpl;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.web.client.RestTemplate;

public class MnuboUserApiImpl extends AbstractOAuth2ApiBinding implements MnuboUserApi {

    private final UserService userService;
    private final SmartObjectService smartObjectService;
    private final GroupService groupService;
    private final CollectionService collectionService;
    private final TokenValidationService tokenValidationService;

    public MnuboUserApiImpl(final String accessToken, final String platformBaseUrl) {
        super(accessToken);

        this.userService = new UserServiceImpl(platformBaseUrl, getRestTemplate());
        this.smartObjectService = new SmartObjectServiceImpl(platformBaseUrl, getRestTemplate());
        this.groupService = new GroupServiceImpl(platformBaseUrl, getRestTemplate());
        this.collectionService = new CollectionServiceImpl(platformBaseUrl, getRestTemplate());
        this.tokenValidationService = new TokenValidationServiceImpl(platformBaseUrl, accessToken, getRestTemplate());
    }

    @Override
    protected void configureRestTemplate(RestTemplate restTemplate) {
        restTemplate.setErrorHandler(new MnuboAPIErrorHandler());
        //Force the use of SNI to fetch the proper certificate
        restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
    }

    @Override
    public UserService userService() {
        return this.userService;
    }

    @Override
    public SmartObjectService objectService() {
        return this.smartObjectService;
    }

    @Override
    public GroupService groupService() {
        return this.groupService;
    }

    @Override
    public CollectionService collectionService() {
        return this.collectionService;
    }

    @Override
    public TokenValidationService tokenValidationService() {
        return tokenValidationService;
    }


}
