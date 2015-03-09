package com.mnubo.platform.android.sdk.api.operations;

import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.models.security.ResetPassword;
import com.mnubo.platform.android.sdk.models.security.UserConfirmation;
import com.mnubo.platform.android.sdk.models.users.User;

/**
 * The ClientOperations is an interface to perform queries on the mnubo API as the application.
 * These operations require the consumer key and secret, but now a user logged it to be successful.
 */
public interface ClientOperations {

    /**
     * This function allows you to register an user on the mnubo API.
     *
     * @param user the <code>User</code> that contains all the information
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void createUser(User user, MnuboApi.CompletionCallBack<Boolean> completionCallBack);

    /**
     *
     * @param username the username of the user that need a confirmation
     * @param confirmation the <code>UserConfirmation</code> contains user's password and
     *                     the confirmation token received by email
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void confirmUserCreation(String username, UserConfirmation confirmation, MnuboApi.CompletionCallBack<Boolean> completionCallBack);

    /**
     *  This functions allows you to initiate the reset  password for the specified username.
     *
     *  @see com.mnubo.platform.android.sdk.api.operations.ClientOperations#confirmPasswordReset(String, com.mnubo.platform.android.sdk.models.security.ResetPassword, com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack)
     * @param username the username of the user that need a password reset
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void resetPassword(String username, MnuboApi.CompletionCallBack<Boolean> completionCallBack);

    /**
     * This function allows you to finish the password reset process of a specific user.\
     *
     *
     *  @see com.mnubo.platform.android.sdk.api.operations.ClientOperations#resetPassword(String, com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack)
     * @param username  the username of the user that completes a password reset
     * @param resetPassword the <code>ResetPassword</code> contains the new password and
     *                      a confirmation with the token the user has received
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void confirmPasswordReset(String username, ResetPassword resetPassword, MnuboApi.CompletionCallBack<Boolean> completionCallBack);
}
