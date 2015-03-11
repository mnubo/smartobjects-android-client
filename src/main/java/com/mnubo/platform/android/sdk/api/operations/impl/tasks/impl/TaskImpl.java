package com.mnubo.platform.android.sdk.api.operations.impl.tasks.impl;

import com.mnubo.platform.android.sdk.api.operations.impl.MnuboResponse;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.Task;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;

import static com.mnubo.platform.android.sdk.api.operations.impl.AbstractMnuboOperations.MnuboOperation;

public class TaskImpl<Result> implements Task<Result> {

    final MnuboOperation<Result> operation;

    public TaskImpl(MnuboOperation<Result> operation) {
        this.operation = operation;
    }

    @Override
    public MnuboResponse<Result> execute() {


        Result result = null;
        MnuboException error = null;
        try {
            result = operation.executeMnuboCall();

        } catch (MnuboException ex) {
            error = ex;

        } catch (Exception ex) {
            error = new MnuboException(ex);
        }

        return new MnuboResponse<>(result, error);

    }
}
