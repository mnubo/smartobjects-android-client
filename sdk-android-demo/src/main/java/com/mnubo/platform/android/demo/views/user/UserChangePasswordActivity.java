package com.mnubo.platform.android.demo.views.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mnubo.platform.android.demo.MnuboAbstractActivity;
import com.mnubo.platform.android.demo.R;
import com.mnubo.platform.android.sdk.Mnubo;
import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.models.security.UpdatePassword;

public class UserChangePasswordActivity extends MnuboAbstractActivity {

    private MnuboApi mnuboApi;

    private EditText txtOldPassword;
    private EditText txtNewPassword;
    private EditText txtConfirmedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_password);

        this.mnuboApi = Mnubo.getApi();

        txtOldPassword = (EditText) findViewById(R.id.txt_old_password);
        txtNewPassword = (EditText) findViewById(R.id.txt_new_password);
        txtConfirmedPassword = (EditText) findViewById(R.id.txt_password_confirmed);
        txtConfirmedPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.update_profile || id == EditorInfo.IME_NULL) {
                    attemptChangePassword();
                    return true;
                }
                return false;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.btn_update_profile);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptChangePassword();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_change_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void attemptChangePassword() {
        // Reset errors.
        txtOldPassword.setError(null);
        txtNewPassword.setError(null);
        txtConfirmedPassword.setError(null);

        // Store values at the time of the login attempt.
        String oldPassword = txtOldPassword.getText().toString();
        String newPassword = txtNewPassword.getText().toString();
        String confirmedPassword = txtConfirmedPassword.getText().toString();

        boolean validatePasswordCombination = validatePasswordCombination(oldPassword, newPassword, confirmedPassword);

        if (validatePasswordCombination) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            updatePassword(new UpdatePassword(oldPassword, newPassword, confirmedPassword));
        }
    }

    private boolean validatePasswordCombination(String oldPassword,
                                                String newPassword,
                                                String confirmedPassword) {
        if (TextUtils.isEmpty(oldPassword)) {
            txtOldPassword.setError(getString(R.string.error_invalid_password));
            txtOldPassword.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(newPassword)) {
            txtNewPassword.setError(getString(R.string.error_invalid_password));
            txtNewPassword.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(confirmedPassword)) {
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

    private void completePasswordChange() {
        txtConfirmedPassword.setText("");
        txtNewPassword.setText("");
        txtOldPassword.setText("");

        Toast.makeText(getApplicationContext(), R.string.successful_password_change, Toast.LENGTH_SHORT).show();
    }

    private void updatePassword(UpdatePassword newsPassword) {
        showProgress(false);
        mnuboApi.getUserOperations().updatePasswordAsync(mnuboApi.getAuthenticationOperations().getUsername(), newsPassword, new MnuboApi.CompletionCallBack<Boolean>() {
            @Override
            public void onCompletion(Boolean success, MnuboException error) {
                if (error == null && success) {
                    completePasswordChange();
                }
            }
        });
    }
}
