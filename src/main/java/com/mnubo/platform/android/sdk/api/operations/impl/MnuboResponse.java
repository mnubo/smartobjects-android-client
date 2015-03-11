package com.mnubo.platform.android.sdk.api.operations.impl;

import com.mnubo.platform.android.sdk.exceptions.MnuboException;

public class MnuboResponse<Type> {

    private final Type result;
    private final MnuboException error;

    public MnuboResponse(Type result, MnuboException error) {
        this.result = result;
        this.error = error;
    }

    public Type getResult() {
        return result;
    }

    public MnuboException getError() {
        return error;
    }

    public boolean hasNoError() {
        return this.error == null;
    }

    public boolean isSuccess() {
        return hasNoError() && this.result != null &&
                (this.result instanceof Boolean ? (Boolean) result : true);
    }
}
