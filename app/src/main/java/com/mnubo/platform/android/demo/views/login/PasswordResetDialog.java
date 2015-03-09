package com.mnubo.platform.android.demo.views.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.mnubo.platform.android.demo.R;

public class PasswordResetDialog extends DialogFragment {

    private EditText txtUsername;
    private LoginActivity loginActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_password_reset, null);

        loginActivity = (LoginActivity) getActivity();

        txtUsername = (EditText) view.findViewById(R.id.txt_username);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(loginActivity);
        builder.setMessage(R.string.reset_password)
                .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String username = txtUsername.getText().toString();
                        loginActivity.resetPassword(username);

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.setView(view);
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
