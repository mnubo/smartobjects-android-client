package com.mnubo.platform.android.sdk.api.operations;

import com.mnubo.platform.android.sdk.models.security.UpdatePassword;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;
import com.mnubo.platform.android.sdk.models.users.User;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;

/**
 * The UserOperations interface allows you to query the mnubo API the user resources of the
 * mnubo API.
 */
public interface UserOperations {
    /**
     * Fetch all of the objects that belongs to the username. Only minimal information about the
     * objects in the list will be returned.
     *
     * @param username           owner of the objects
     * @param completionCallBack the callback that will be executed on completion of the request
     * @see com.mnubo.platform.android.sdk.api.operations.UserOperations#findUserObjects(String, Boolean, com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack)
     */
    void findUserObjects(String username, CompletionCallBack<SmartObjects> completionCallBack);

    /**
     * Fetch all of the objects that belongs to the username.
     *
     * @param username           owner of the objects
     * @param details            true will return all data, false will return minimal data
     * @param completionCallBack the callback that will be executed on completion of the request
     * @see com.mnubo.platform.android.sdk.api.operations.UserOperations#findUserObjects(String, Boolean, String, com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack)
     */
    void findUserObjects(String username, final Boolean details, CompletionCallBack<SmartObjects> completionCallBack);

    /**
     * Fetch all of the objects that belongs to the username that has a specific object model.
     *
     * @param username           owner of the objects
     * @param details            true will return all data, false will return minimal data
     * @param objectModelName    specific object model name
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void findUserObjects(String username, final Boolean details, final String objectModelName, CompletionCallBack<SmartObjects> completionCallBack);

    /**
     * Fetch a specific user information.
     *
     * @param username           username of the user
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void getUser(String username, CompletionCallBack<User> completionCallBack);

    /**
     * This method allow you to update user's information
     *
     * @param username           username of the user to be updated
     * @param updatedUser        the user's data
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void update(String username, User updatedUser, CompletionCallBack<Boolean> completionCallBack);

    /**
     * This method allow you to update user's password. The user has to be currently logged in
     * to use this method.
     *
     * @see com.mnubo.platform.android.sdk.api.operations.ClientOperations#resetPassword(String, com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack)
     *
     * @param username username of the user to be updated
     * @param newPassword the <code>UpdatePassword</code> contains the old password, the new
     *                    password and it's confirmation.
     * @param completionCallBack the callback that will be executed on completion of the request
     */
    void updatePassword(String username, UpdatePassword newPassword, CompletionCallBack<Boolean> completionCallBack);
}
