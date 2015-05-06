package com.mnubo.platform.android.demo.views.register;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mnubo.platform.android.demo.MnuboAbstractActivity;
import com.mnubo.platform.android.demo.R;
import com.mnubo.platform.android.sdk.Mnubo;
import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.models.users.User;

public class RegisterActivity extends MnuboAbstractActivity {

    private TextView txtUsername;
    private TextView txtPassword;
    private TextView txtConfirmedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtUsername = (TextView) findViewById(R.id.txt_username);
        txtPassword = (TextView) findViewById(R.id.txt_password);
        txtConfirmedPassword = (TextView) findViewById(R.id.txt_password_confirmed);

        Button btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //not logged, should be client api
        this.mnuboApi = Mnubo.getApi();
    }

    private void attemptRegister() {
        txtUsername.setError(null);
        txtPassword.setError(null);
        txtConfirmedPassword.setError(null);

        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        String confirmation = txtConfirmedPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            txtUsername.setError(getText(R.string.error_invalid_username));
            return;
        }
        if (validatePasswordCombination(password, confirmation)) {
            showProgress(true);
            createUser(username, password, confirmation);
        }
    }

    private void createUser(final String username, final String password, final String confirmation) {
        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setConfirmedPassword(confirmation);

        this.mnuboApi.getClientOperations().createUserAsync(user,
                new MnuboApi.CompletionCallBack<Boolean>() {
                    @Override
                    public void onCompletion(Boolean success, MnuboException ex) {
                        showProgress(false);
                        if (ex == null && success) {
                            Toast.makeText(getApplicationContext(), R.string.successful_registration, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.failed_registration, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validatePasswordCombination(String newPassword,
                                                String confirmedPassword) {
        if (TextUtils.isEmpty(newPassword)) {
            txtPassword.setError(getString(R.string.error_invalid_password));
            txtPassword.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(newPassword)) {
            txtConfirmedPassword.setError(getString(R.string.error_invalid_password));
            txtConfirmedPassword.requestFocus();
            return false;
        }
        if (!confirmedPassword.equals(newPassword)) {
            txtConfirmedPassword.setError(getString(R.string.error_not_same_password));
            txtConfirmedPassword.requestFocus();
            return false;
        }
        return true;
    }


}
