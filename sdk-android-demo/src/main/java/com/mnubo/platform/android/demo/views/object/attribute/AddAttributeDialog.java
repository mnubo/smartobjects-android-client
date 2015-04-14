package com.mnubo.platform.android.demo.views.object.attribute;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mnubo.platform.android.demo.R;
import com.mnubo.platform.android.demo.intentutils.Extras;
import com.mnubo.platform.android.demo.views.object.UserObjectActivity;
import com.mnubo.platform.android.sdk.Mnubo;
import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.models.common.Attribute;
import com.mnubo.platform.android.sdk.models.common.IdType;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;

public class AddAttributeDialog extends DialogFragment {

    static final String ACTIVITY_TAG = AddAttributeDialog.class.getSimpleName();

    private MnuboApi mnuboApi;

    static final String[] NAMES = {"a_string_attribute", "a_boolean_attribute", "a_int_attribute"};

    private UserObjectActivity userObjectActivity;

    private String objectId;
    private SmartObject currentSmartObject = null;

    private Spinner spnAttributeName;
    private EditText txtAttributeValue;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.mnuboApi = Mnubo.getApi();
        this.userObjectActivity = (UserObjectActivity) getActivity();

        objectId = getArguments().getString(Extras.EXTRA_USEROBJECT_UUID);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this.userObjectActivity);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_add_attribute, null);
        txtAttributeValue = (EditText) view.findViewById(R.id.txt_attribute_value);

        spnAttributeName = (Spinner) view.findViewById(R.id.spn_attribute_name);
        spnAttributeName.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,
                NAMES));

        builder.setView(view).setMessage(getString(R.string.dialog_add_attribute))
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String attributeName = spnAttributeName.getSelectedItem().toString();
                        String attributeValue = txtAttributeValue.getText().toString();

                        final Attribute attribute = new Attribute();
                        attribute.setName(attributeName);
                        attribute.setValue(attributeValue);

                        if (currentSmartObject == null) {
                            mnuboApi.getSmartObjectOperations().findObjectAsync(SdkId.build(objectId, IdType.objectid), new MnuboApi.CompletionCallBack<SmartObject>() {
                                @Override
                                public void onCompletion(SmartObject result, MnuboException ex) {
                                    if (ex == null) {
                                        currentSmartObject = result;
                                        addAttributeToObject(currentSmartObject, attribute);
                                    }
                                }
                            });
                        } else {
                            addAttributeToObject(currentSmartObject, attribute);
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

    private void addAttributeToObject(final SmartObject object, final Attribute newAttribute) {

        object.getAttributes().add(newAttribute);
        mnuboApi.getSmartObjectOperations().updateAsync(SdkId.build(this.objectId, IdType.objectid), object, new
                MnuboApi.CompletionCallBack<Boolean>() {
                    @Override
                    public void onCompletion(Boolean success, MnuboException ex) {
                        if (ex == null && success) {
                            userObjectActivity.refresh();
                            Toast.makeText(getActivity(), R.string.successful_add_attribute, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
