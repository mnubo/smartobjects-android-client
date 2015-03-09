package com.mnubo.platform.android.sdk.internal.user.services;

public interface TokenValidationService {
    /**
     * Validate current user token
     *
     * @return Username associated with the supplied token
     */
    public String validate();
}
