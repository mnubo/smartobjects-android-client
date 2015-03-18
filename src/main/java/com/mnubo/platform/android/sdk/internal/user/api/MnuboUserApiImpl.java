/*
 * Copyright (c) 2015 Mnubo. Released under MIT License.
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy
 *     of this software and associated documentation files (the "Software"), to deal
 *     in the Software without restriction, including without limitation the rights
 *     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *     copies of the Software, and to permit persons to whom the Software is
 *     furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in
 *     all copies or substantial portions of the Software.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *     THE SOFTWARE.
 */

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
