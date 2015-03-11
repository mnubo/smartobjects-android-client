package com.mnubo.platform.android.sdk.exceptions.sdk;

import com.mnubo.platform.android.sdk.Strings;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;

/**
 * This exception is raised when the thread running the call to
 * the Mnubo API is cancelled. It happens whenever {@link android.os.AsyncTask#onCancelled()}
 * is called.
 */
public class MnuboCancelledOperationException extends MnuboException {

    public MnuboCancelledOperationException() {
        super(Strings.EXCEPTION_CANCELLED_OPERATION);
    }
}
