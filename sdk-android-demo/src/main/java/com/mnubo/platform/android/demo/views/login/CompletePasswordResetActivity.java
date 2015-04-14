package com.mnubo.platform.android.demo.views.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mnubo.platform.android.demo.R;
import com.mnubo.platform.android.demo.views.common.AbstractCompleteActivity;
import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.models.security.ResetPassword;

public class CompletePasswordResetActivity extends AbstractCompleteActivity {

    static final String ACTIVITY_TAG = CompletePasswordResetActivity.class.getSimpleName();

    private EditText txtPasswordConfirmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_password_reset);
        bindFields();

        txtPasswordConfirmed = (EditText) findViewById(R.id.txt_password_confirmed);
        Button btnCompletePasswordReset = (Button) findViewById(R.id.btn_complete_password_reset);
        btnCompletePasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptCompleteResetPassword();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void attemptCompleteResetPassword() {
        final String username, password, confirmPassword, token;
        username = getUsername();
        password = getPassword();
        confirmPassword = txtPasswordConfirmed.getText().toString();
        token = getToken();
        ResetPassword resetPassword = validateInput(username, password, confirmPassword, token);

        if (resetPassword != null && resetPassword.bothPasswordsAreIdentical()) {
            showProgress(true);
            mnuboApi.getClientOperations().confirmPasswordResetAsync(username, resetPassword, new MnuboApi.CompletionCallBack<Boolean>() {
                @Override
                public void onCompletion(Boolean success, MnuboException error) {
                    if (error == null && success) {
                        Toast.makeText(getApplicationContext(), R.string.successful_password_change, Toast.LENGTH_SHORT).show();
                        logInNewUser(username, password, false);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.failed_complete_password_reset, Toast.LENGTH_SHORT).show();
                    }
                    showProgress(false);
                }
            });
        }

    }

    private ResetPassword validateInput(String username, String password, String confirmPassword, String token) {
        if (TextUtils.isEmpty(username)) {
            txtUsername.setError(getString(R.string.error_invalid_username));
            return null;
        }
        if (TextUtils.isEmpty(password)) {
            txtPassword.setError(getString(R.string.error_incorrect_password));
            return null;
        }
        if (!TextUtils.equals(password, confirmPassword)) {
            txtPasswordConfirmed.setError(getString(R.string.error_not_same_password));
            return null;
        }
        if (TextUtils.isEmpty(token)) {
            txtToken.setError(getString(R.string.error_field_required));
            return null;
        }
        return new ResetPassword(token, password, confirmPassword);
    }
}
