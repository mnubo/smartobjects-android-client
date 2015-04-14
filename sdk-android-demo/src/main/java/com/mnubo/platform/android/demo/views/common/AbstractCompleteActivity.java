package com.mnubo.platform.android.demo.views.common;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.mnubo.platform.android.demo.MnuboAbstractActivity;
import com.mnubo.platform.android.demo.R;
import com.mnubo.platform.android.demo.services.phone.PhoneRegistrationService;
import com.mnubo.platform.android.demo.views.object.UserObjectsActivity;
import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;

public class AbstractCompleteActivity extends MnuboAbstractActivity {


    protected EditText txtUsername;
    protected EditText txtPassword;
    protected EditText txtToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void bindFields() {
        txtUsername = (EditText) findViewById(R.id.txt_username);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        txtToken = (EditText) findViewById(R.id.txt_token);
    }

    protected void logInNewUser(final String username, final String password, final Boolean justRegistered) {
        this.mnuboApi.getAuthenticationOperations().logInAsync(username, password,
                new MnuboApi.CompletionCallBack<Boolean>() {
                    @Override
                    public void onCompletion(Boolean response, MnuboException ex) {
                        if (ex == null && response) {
                            startActivity(new Intent(getApplicationContext(), UserObjectsActivity.class));

                            if (justRegistered) {
                                startActivity(new Intent(getApplicationContext(), PhoneRegistrationService.class));
                            }

                            finish();
                        }
                    }
                });
    }


    protected String getToken() {
        return txtToken.getText().toString();
    }

    protected String getPassword() {
        return txtPassword.getText().toString();
    }

    protected String getUsername() {
        return txtUsername.getText().toString();
    }
}
