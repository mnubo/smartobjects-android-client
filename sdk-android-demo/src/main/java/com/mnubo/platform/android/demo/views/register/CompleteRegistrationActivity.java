package com.mnubo.platform.android.demo.views.register;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mnubo.platform.android.demo.R;
import com.mnubo.platform.android.demo.views.common.AbstractCompleteActivity;
import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboInvalidRegistrationTokenException;
import com.mnubo.platform.android.sdk.models.security.UserConfirmation;

public class CompleteRegistrationActivity extends AbstractCompleteActivity {

    static final String ACTIVITY_TAG = CompleteRegistrationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_registration);
        bindFields();

        Button btnCompleteRegistration = (Button) findViewById(R.id.btn_complete_registration);
        btnCompleteRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptCompleteRegistration();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void attemptCompleteRegistration() {
        final String username, password, token;
        username = getUsername();
        password = getPassword();
        token = getToken();
        UserConfirmation userConfirmation = validateInput(username, password, token);

        if (userConfirmation != null) {
            showProgress(true);
            mnuboApi.getClientOperations().confirmUserCreationAsync(username, userConfirmation, new MnuboApi.CompletionCallBack<Boolean>() {
                @Override
                public void onCompletion(Boolean success, MnuboException error) {
                    showProgress(false);

                    if (error == null && success) {
                        Toast.makeText(getApplicationContext(), R.string.successful_registration_complete, Toast.LENGTH_SHORT).show();
                        logInNewUser(username, password, true);
                    } else if (error instanceof MnuboInvalidRegistrationTokenException) {
                        txtToken.setError(getString(R.string.error_incorrect_token));
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.failed_complete_registration, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void registerPhone() {

    }

    private UserConfirmation validateInput(String username, String password, String token) {
        if (TextUtils.isEmpty(username)) {
            txtUsername.setError(getString(R.string.error_invalid_username));
            return null;
        }
        if (TextUtils.isEmpty(password)) {
            txtPassword.setError(getString(R.string.error_incorrect_password));
            return null;
        }
        if (TextUtils.isEmpty(token)) {
            txtToken.setError(getString(R.string.error_field_required));
            return null;
        }
        return new UserConfirmation(token, password);
    }
}
