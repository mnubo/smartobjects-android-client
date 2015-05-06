package com.mnubo.platform.android.demo.views.user;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mnubo.platform.android.demo.MnuboAbstractActivity;
import com.mnubo.platform.android.demo.R;
import com.mnubo.platform.android.demo.intentutils.Extras;
import com.mnubo.platform.android.demo.views.collection.AddCollectionDialog;
import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.models.collections.Collection;
import com.mnubo.platform.android.sdk.models.users.User;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends MnuboAbstractActivity {

    static final String ACTIVITY_TAG = UserProfileActivity.class.getSimpleName();

    private User currentUser = null;

    private EditText txtFirstname;
    private EditText txtLastname;

    private ArrayAdapter<Collection> collectionsAdapter;
    private List<Collection> collections = new ArrayList<>();
    private ListView lstUserCollections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        txtFirstname = (EditText) findViewById(R.id.txt_user_firstname);
        txtLastname = (EditText) findViewById(R.id.txt_user_lastname);

        Button btnUpdate = (Button) findViewById(R.id.btn_update_profile);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });

        Button btnAddCollection = (Button) findViewById(R.id.btn_add_collection);
        btnAddCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddCollectionDialog();
            }
        });

        initListView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!mnuboApi.getAuthenticationOperations().isUserConnected()) {
            super.showLogInActivity();
        } else {
            fetchUserProfile();
        }

    }

    private void startAddCollectionDialog() {
        // Create and show the dialog.
        AddCollectionDialog addCollectionDialog = new AddCollectionDialog();

        Bundle args = new Bundle();
        args.putString(Extras.EXTRA_USERNAME, "david");
        addCollectionDialog.setArguments(args);

        addCollectionDialog.show(getFragmentManager(), "dialog");
    }


    private void initListView() {

        this.lstUserCollections = (ListView) findViewById(R.id.lst_user_collections);

        this.collectionsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                collections);

        this.lstUserCollections.setAdapter(this.collectionsAdapter);


    }

    private void bindUserWithForm() {
        if (currentUser != null) {

            txtFirstname.setText(currentUser.getFirstname());
            txtLastname.setText(currentUser.getLastname());

            collections.clear();
            collections.addAll(currentUser.getCollections());
            collectionsAdapter.notifyDataSetChanged();
        }
    }

    private void fetchUserProfile() {
        showProgress(true);
        String username = mnuboApi.getAuthenticationOperations().getUsername();
        this.mnuboApi.getUserOperations().getUserAsync(username, new MnuboApi.CompletionCallBack<User>() {
            @Override
            public void onCompletion(User result, MnuboException ex) {
                showProgress(false);
                if (ex == null) {
                    currentUser = result;
                    bindUserWithForm();
                }
            }
        });
    }

    private void updateUserProfile() {
        showProgress(true);

        String firstname = txtFirstname.getText().toString();
        String lastname = txtLastname.getText().toString();

        if (currentUser != null) {
            currentUser.setFirstname(firstname);
            currentUser.setLastname(lastname);
            this.mnuboApi.getUserOperations().updateAsync(currentUser.getUsername(), currentUser, new MnuboApi.CompletionCallBack<Boolean>() {
                @Override
                public void onCompletion(Boolean success, MnuboException ex) {
                    showProgress(false);
                    if (ex == null && success) {
                        Toast.makeText(getApplicationContext(), R.string.successful_user_update, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        showProgress(false);
    }
}
