package com.mnubo.platform.android.sdk.api;

import com.mnubo.platform.android.sdk.api.operations.AuthenticationOperations;
import com.mnubo.platform.android.sdk.api.operations.ClientOperations;
import com.mnubo.platform.android.sdk.api.operations.SmartObjectOperations;
import com.mnubo.platform.android.sdk.api.operations.UserOperations;
import com.mnubo.platform.android.sdk.api.operations.impl.AuthenticationOperationsImpl;
import com.mnubo.platform.android.sdk.api.operations.impl.ClientOperationsImpl;
import com.mnubo.platform.android.sdk.api.operations.impl.SmartObjectOperationsImpl;
import com.mnubo.platform.android.sdk.api.operations.impl.UserOperationsImpl;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;

import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.api.MnuboApiFactory.ConnectionOperations;

/**
 * Mnubo API that is used by the application developers to perform
 * requests on the Mnubo system.
 */
public class MnuboApi {

    private final UserOperations userOperations;

    private final SmartObjectOperations smartObjectOperations;

    private final ClientOperations clientOperations;

    private final AuthenticationOperations authenticationOperations;

    protected MnuboApi(ConnectionOperations connectionOperations, Connection<MnuboClientApi> clientConnection, Connection<MnuboUserApi> userConnection) {

        this.userOperations = new UserOperationsImpl(connectionOperations, clientConnection, userConnection);
        this.smartObjectOperations = new SmartObjectOperationsImpl(connectionOperations, clientConnection, userConnection);
        this.clientOperations = new ClientOperationsImpl(connectionOperations, clientConnection, userConnection);
        this.authenticationOperations = new AuthenticationOperationsImpl(connectionOperations, clientConnection, userConnection);
    }


    public UserOperations getUserOperations() {
        return userOperations;
    }

    public SmartObjectOperations getSmartObjectOperations() {
        return smartObjectOperations;
    }

    public ClientOperations getClientOperations() {
        return clientOperations;
    }

    public AuthenticationOperations getAuthenticationOperations() {
        return authenticationOperations;
    }

    /**
     * Interface definition for a callback to be invoked when an action an request to
     * mnubo API as returned, successful or not.
     *
     * @param <Result> The type of result that will be sent in the {link #onCompletion} method
     */
    public static interface CompletionCallBack<Result> {

        /**
         * This method is called when the request is done, success or not.
         * The user must ensure the result is not null and that no error arose
         * when performing the request
         *
         * @param result the result of the async request
         * @param error  any error that might have occurred during the async request
         */
        void onCompletion(Result result, MnuboException error);

    }
}
