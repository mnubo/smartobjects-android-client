package com.mnubo.platform.android.sdk.api.operations.impl.tasks;

import com.mnubo.platform.android.sdk.api.operations.impl.MnuboResponse;

public interface Task<Result> {
    MnuboResponse<Result> execute();
}
