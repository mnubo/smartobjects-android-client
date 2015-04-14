package com.mnubo.platform.android.demo.views.object;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mnubo.platform.android.demo.MnuboAbstractActivity;
import com.mnubo.platform.android.demo.R;
import com.mnubo.platform.android.demo.intentutils.Extras;
import com.mnubo.platform.android.demo.model.phone.Phone;
import com.mnubo.platform.android.demo.services.phone.PhoneEventService;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.models.common.IdType;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;

import static com.mnubo.platform.android.demo.model.phone.PhoneEvent.Type;
import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;

public class UserObjectActivity extends MnuboAbstractActivity {

    static final String ACTIVITY_TAG = UserObjectActivity.class.getSimpleName();

    static public enum USEROBJECT_ACTIVITY_MODE {
        CREATE, EDIT
    }

    static public enum OBJECT_MODEL {
        phone
    }

    private String objectId;
    private SmartObject currentObject;

    private USEROBJECT_ACTIVITY_MODE mode = USEROBJECT_ACTIVITY_MODE.CREATE;
    private OBJECT_MODEL objectModel = OBJECT_MODEL.phone;

    private Spinner spnObjectModel;
    private ArrayAdapter<OBJECT_MODEL> objectModelsAdapter;

    private EditText txtDeviceId;
    private EditText txtRegistrationDate;
    private Button createUpdateUserObject;

    private ViewStub phoneStub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_object);

        parseBundle(getIntent().getExtras());

        spnObjectModel = (Spinner) findViewById(R.id.spn_object_model);
        objectModelsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, OBJECT_MODEL.values());
        spnObjectModel.setAdapter(objectModelsAdapter);

        txtDeviceId = (EditText) findViewById(R.id.txt_device_id);
        txtRegistrationDate = (EditText) findViewById(R.id.txt_reg_date);
        phoneStub = (ViewStub) findViewById(R.id.stub_phone);

        createUpdateUserObject = (Button) findViewById(R.id.btn_create_or_update_userobject);

        if (mode == USEROBJECT_ACTIVITY_MODE.CREATE) {
            txtDeviceId.setEnabled(true);
            createUpdateUserObject.setText(getText(R.string.btn_create_phone));
        } else if (mode == USEROBJECT_ACTIVITY_MODE.EDIT) {
            spnObjectModel.setEnabled(false);
            txtDeviceId.setEnabled(false);

            txtRegistrationDate.setVisibility(View.VISIBLE);
            findViewById(R.id.lbl_reg_date).setVisibility(View.VISIBLE);
            createUpdateUserObject.setText(getText(R.string.btn_update_phone));
            createUpdateUserObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateObject();
                }
            });
        }

        phoneStub.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                bindButtons();
            }
        });

        displayAppropriateStub();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!mnuboApi.getAuthenticationOperations().isUserConnected()) {
            super.showLogInActivity();
        } else {
            fetchUserObject();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_object, menu);
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

    private void parseBundle(Bundle extras) {
        if (extras != null) {
            this.objectId = extras.getString(Extras.EXTRA_USEROBJECT_UUID);

            String extraMode = extras.getString(Extras.EXTRA_USEROBJECT_MODE);
            if (!TextUtils.isEmpty(extraMode)) {
                this.mode = USEROBJECT_ACTIVITY_MODE.valueOf(extraMode);
            }


            String objectModel = extras.getString(Extras.EXTRA_USEROBJECT_OBJECTMODEL);
            if (!TextUtils.isEmpty(objectModel)) {
                this.objectModel = OBJECT_MODEL.valueOf(objectModel);
            }
        }
    }

    private void fetchUserObject() {
        if (!TextUtils.isEmpty(this.objectId)) {
            this.mnuboApi.getSmartObjectOperations().findObjectAsync(SdkId.build(objectId, IdType.objectid), new CompletionCallBack<SmartObject>() {
                @Override
                public void onCompletion(SmartObject result, MnuboException ex) {
                    showProgress(false);
                    if (ex == null) {
                        currentObject = result;
                        bindUserObjectToForm(result);
                    }
                }
            });
        }
    }

    private void bindUserObjectToForm(final SmartObject smartObject) {

        txtDeviceId.setText(smartObject.getDeviceId());
        txtRegistrationDate.setText(smartObject.getRegistrationDate());

        bindStub(smartObject);

    }

    private void bindStub(final SmartObject smartObject) {
        if (Phone.isPhone(smartObject)) {
            Phone phone = new Phone(smartObject);
            EditText txtPhoneModel = (EditText) findViewById(R.id.txt_phone_model);
            txtPhoneModel.setText(phone.getPhoneModel());
        }
    }

    public void refresh() {
        fetchUserObject();
    }

    private void displayAppropriateStub() {
        switch (this.objectModel) {
            case phone:
                phoneStub.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void bindButtons() {
        this.createUpdateUserObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateObject();
            }
        });
        bindAppropriateStubButton();
    }

    private void bindAppropriateStubButton() {
        Button btnPostPhoneInstallEvent = (Button) findViewById(R.id.btn_phone_post_install_event);
        btnPostPhoneInstallEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postEvent(Type.install);
            }
        });

        Button btnPostLaunchedEvent = (Button) findViewById(R.id.btn_phone_post_launched_event);
        btnPostLaunchedEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postEvent(Type.launch);
            }
        });
    }

    private void postEvent(Type type) {
        Intent intent = new Intent(getApplicationContext(), PhoneEventService.class);
        intent.putExtra(Extras.EXTRA_EVENT_TYPE, type.toString());
        intent.putExtra(Extras.EXTRA_USEROBJECT_UUID, objectId);
        startService(intent);
    }

    private void updateObject() {
        showProgress(true);

        switch (this.objectModel) {
            case phone:
                EditText txtModel = (EditText) findViewById(R.id.txt_phone_model);
                String modelValue = txtModel.getText().toString();
                Phone phone = new Phone(this.currentObject);
                phone.phoneModel(modelValue);
                mnuboApi.getSmartObjectOperations().updateAsync(SdkId.build(this.objectId, IdType.objectid), phone.getSmartObject(), new CompletionCallBack<Boolean>() {
                    @Override
                    public void onCompletion(Boolean result, MnuboException error) {
                        showProgress(false);
                        if (error == null && result) {
                            Toast.makeText(getApplicationContext(), R.string.successful_phone_update, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.failed_phone_update, Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                break;
        }


    }
}
