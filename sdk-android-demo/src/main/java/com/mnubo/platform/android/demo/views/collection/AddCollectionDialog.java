package com.mnubo.platform.android.demo.views.collection;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mnubo.platform.android.demo.R;
import com.mnubo.platform.android.demo.intentutils.Extras;
import com.mnubo.platform.android.sdk.Mnubo;
import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.models.collections.Collection;
import com.mnubo.platform.android.sdk.models.users.User;

public class AddCollectionDialog extends DialogFragment {

    static final String ACTIVITY_TAG = AddCollectionDialog.class.getSimpleName();

    private MnuboApi mnuboApi;

    private String username;
    private User currentUser = null;
    private EditText txtCollectionName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.mnuboApi = Mnubo.getApi();

        username = getArguments().getString(Extras.EXTRA_USERNAME);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_add_collection, null);
        txtCollectionName = (EditText) view.findViewById(R.id.txt_collection_name);

        builder.setView(view).setMessage(getString(R.string.dialog_add_collection))
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String collectionName = txtCollectionName.getText().toString();

                        final Collection collection = new Collection();
                        collection.setLabel(collectionName);
                        collection.setOwner(username);

                        if (currentUser == null) {
                            String username = mnuboApi.getAuthenticationOperations().getUsername();
                            mnuboApi.getUserOperations().getUserAsync(username, new MnuboApi.CompletionCallBack<User>() {
                                @Override
                                public void onCompletion(User result, MnuboException ex) {
                                    if (ex == null) {
                                        currentUser = result;
                                        addCollectionToUser(currentUser, collection);
                                    }
                                }
                            });
                        } else {
                            addCollectionToUser(currentUser, collection);
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void addCollectionToUser(final User user, final Collection collection) {

        user.getCollections().add(collection);
        mnuboApi.getUserOperations().updateAsync(this.username, user, new
                MnuboApi.CompletionCallBack<Boolean>() {
                    @Override
                    public void onCompletion(Boolean response, MnuboException ex) {
                        if (ex == null) {
                            Toast.makeText(getActivity(), R.string.successful_add_collection, Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

}
