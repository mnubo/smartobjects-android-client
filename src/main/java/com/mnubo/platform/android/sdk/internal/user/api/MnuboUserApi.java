package com.mnubo.platform.android.sdk.internal.user.api;


import com.mnubo.platform.android.sdk.internal.user.services.CollectionService;
import com.mnubo.platform.android.sdk.internal.user.services.GroupService;
import com.mnubo.platform.android.sdk.internal.user.services.SmartObjectService;
import com.mnubo.platform.android.sdk.internal.user.services.TokenValidationService;
import com.mnubo.platform.android.sdk.internal.user.services.UserService;

public interface MnuboUserApi {
    UserService userService();

    SmartObjectService objectService();

    TokenValidationService tokenValidationService();

    GroupService groupService();

    CollectionService collectionService();

}
