package com.mnubo.platform.android.demo.views.object;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mnubo.platform.android.demo.MnuboAbstractActivity;
import com.mnubo.platform.android.demo.R;
import com.mnubo.platform.android.demo.intentutils.Extras;
import com.mnubo.platform.android.demo.services.phone.PhoneRegistrationService;
import com.mnubo.platform.android.demo.views.login.LoginActivity;
import com.mnubo.platform.android.demo.views.user.UserChangePasswordActivity;
import com.mnubo.platform.android.demo.views.user.UserProfileActivity;
import com.mnubo.platform.android.sdk.Mnubo;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboExpiredAccessException;
import com.mnubo.platform.android.sdk.models.collections.Collection;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;
import com.mnubo.platform.android.sdk.models.users.User;

import java.util.ArrayList;
import java.util.List;

import static com.mnubo.platform.android.demo.views.object.UserObjectActivity.USEROBJECT_ACTIVITY_MODE;
import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;

public class UserObjectsActivity extends MnuboAbstractActivity {

    static final String ACTIVITY_TAG = UserObjectsActivity.class.getSimpleName();

    private boolean fetchingUser;
    private boolean fetchingUserObjects;

    private ArrayAdapter<SmartObject> userObjectsAdapter;
    private List<SmartObject> objects = new ArrayList<>();
    private ListView lstUserObjects;

    private ArrayAdapter<Collection> collectionsAdapter;
    private List<Collection> collections = new ArrayList<>();
    private ListView lstUserCollections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_objects);

        initListViews();

        Button addUserObject = (Button) findViewById(R.id.btn_add_userobject);
        addUserObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserObjectActivity.class));
            }
        });

        registerServiceReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!mnuboApi.getAuthenticationOperations().isUserConnected()) {
            super.showLogInActivity();
        } else {
            this.mnuboApi = Mnubo.getApi();
            startFetchingData();
        }

    }

    private void startFetchingData() {

        showProgress(true);

        fetchUserObjects();
        startService(new Intent(this, PhoneRegistrationService.class));

        //fetchUserData();
    }

    private void fetchUserData() {
        fetchingUser = true;
        String username = mnuboApi.getAuthenticationOperations().getUsername();
        mnuboApi.getUserOperations().getUserAsync(username, new CompletionCallBack<User>() {

            @Override
            public void onCompletion(User result, MnuboException ex) {
                if (ex == null) {

                    if (result != null) {
                        collections.clear();
                        collections.addAll(result.getCollections());
                    } else {
                        collections.clear();
                    }
                    collectionsAdapter.notifyDataSetChanged();
                }

                fetchingUser = false;
                stopProgress();
            }
        });
    }

    private void fetchUserObjects() {
        fetchingUserObjects = true;
        mnuboApi = Mnubo.getApi();
        final String username = mnuboApi.getAuthenticationOperations().getUsername();
        mnuboApi.getUserOperations().findUserObjectsAsync(
                username, false, null, new CompletionCallBack<SmartObjects>() {

                    @Override
                    public void onCompletion(SmartObjects result, MnuboException ex) {
                        if (ex == null) {

                            if (result != null) {
                                objects.clear();
                                objects.addAll(result.getSmartObjects());
                            } else {
                                objects.clear();
                            }
                            userObjectsAdapter.notifyDataSetChanged();

                        } else if (ex instanceof MnuboExpiredAccessException) {
                            logOutAndShowLogInActivity();
                            Toast.makeText(getApplicationContext(), R.string.error_connection_expired, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.error_authentication, Toast.LENGTH_SHORT).show();
                        }

                        fetchingUserObjects = false;
                        stopProgress();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_objects, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_password) {
            startActivity(new Intent(this, UserChangePasswordActivity.class));
            return true;
        }
        if (id == R.id.action_update_profile) {
            startActivity(new Intent(this, UserProfileActivity.class));
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            mnuboApi.getAuthenticationOperations().logOut();

            startActivity(new Intent(this, LoginActivity.class));

            Log.v(ACTIVITY_TAG, "User logged out.");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startCollectingService(String selectedObjectId) {
        Intent service = new Intent(this, PhoneRegistrationService.class);
        service.putExtra(Extras.EXTRA_USEROBJECT_UUID, selectedObjectId);
        startService(service);
    }

    public List<SmartObject> getObjects() {
        return objects;
    }

    private void initListViews() {
        this.lstUserObjects = (ListView) findViewById(R.id.lst_user_objects);

        this.userObjectsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                objects);

        this.lstUserObjects.setAdapter(this.userObjectsAdapter);

        this.lstUserObjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SmartObject selectedObject = (SmartObject) lstUserObjects.getItemAtPosition(position);
                Log.v(ACTIVITY_TAG, String.format("this is object : %s", selectedObject));

                showUserObjectActivity(selectedObject);
            }
        });

        this.lstUserCollections = (ListView) findViewById(R.id.lst_user_collections);

        this.collectionsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                collections);

        this.lstUserCollections.setAdapter(this.collectionsAdapter);

        this.lstUserCollections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO
            }
        });
    }

    private void showUserObjectActivity(SmartObject selectedObject) {
        Intent startUserObjectActivity = new Intent(this, UserObjectActivity.class);
        startUserObjectActivity.putExtra(Extras.EXTRA_USEROBJECT_UUID, selectedObject.getObjectId().toString());
        startUserObjectActivity.putExtra(Extras.EXTRA_USEROBJECT_MODE, USEROBJECT_ACTIVITY_MODE.EDIT.toString());
        startActivity(startUserObjectActivity);
    }

    private void stopProgress() {
        showProgress(fetchingUserObjects && fetchingUser);
    }

}
