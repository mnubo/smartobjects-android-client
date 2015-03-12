package com.mnubo.platform.android.sdk.api.operations;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;

/**
 * The AuthenticationOperations is an interface to handle the user login process.
 */
public interface AuthenticationOperations {

    /**
     * Log in as a user to the mnubo API. It fetch an user access token along with it's
     * refreshUserConnection token that will be persisted on the device.
     *
     * If the access token expires, the refreshUserConnection token is used to refreshUserConnection it. If both have expired,
     * you should ask the user to log in again.
     *
     * @param username username of the user
     * @param password password of the user
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void logIn(String username, String password, CompletionCallBack<Boolean> completionCallBack);

    /**
     * Logs out the user. Removes the connection data from the device (access token, refreshUserConnection token,
     * username)
     */
    void logOut();

    /**
     * Useful method to know if a user is currently connected
     *
     * @return true if a user is connected, false otherwise
     */
    Boolean isUserConnected();

    /**
     * This method gives the username of the connected user.
     * @return the username of the currently connected username, null if no use connected.
     *
     */
    String getUsername();
}
