package com.mnubo.platform.android.demo.model.phone;

import android.location.Location;
import android.text.TextUtils;

import com.mnubo.platform.android.sdk.models.collections.Collection;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;

public class Phone {

    private static String OBJECT_TYPE = "object_type.type";
    private static String PHONE_OBJECT_TYPE = "phone";
    private static String PHONE_ID = "identifier.phone_id";
    private static String PHONE_DATA_MODEL = "phone_data.model";
    private static String PHONE_DATA_CAPACITY = "phone_data.capacity";
    private static String PHONE_DATA_SOFT_VERSION = "phone_data.software_version";
    private static String PHONE_DATA_BUNDLE_ID = "phone_data.bundle_id";
    private static String PHONE_DATA_BUNDLE_ID_VALUE = "com.connectedevice.connectedwatch";
    private static String PHONE_DATA_APP_NAME = "phone_data.app_name";
    private static String PHONE_DATA_APP_VERSION = "phone_data.app_version";
    private static String OBJECT_MODEL_NAME = "connectedevice";

    private final SmartObject smartObject;

    public Phone() {
        this.smartObject = new SmartObject();
        this.smartObject.setObjectModelName(OBJECT_MODEL_NAME);
        this.smartObject.setAttribute(OBJECT_TYPE, PHONE_OBJECT_TYPE);
        this.smartObject.setAttribute(PHONE_DATA_BUNDLE_ID, PHONE_DATA_BUNDLE_ID_VALUE);
        setPhoneCollection();
    }

    public SmartObject getSmartObject() {
        return smartObject;
    }

    public Phone(SmartObject smartObject) {
        this.smartObject = smartObject;
    }

    public static Boolean isPhone(final SmartObject smartObject) {
        return !TextUtils.isEmpty(smartObject.getObjectModelName())
                && smartObject.getObjectModelName().contains(OBJECT_MODEL_NAME);
    }

    public String getPhoneModel() {
        return this.smartObject.getAttribute(PHONE_DATA_MODEL);
    }

    public Phone deviceId(final String deviceId) {
        this.smartObject.setDeviceId(deviceId);
        return this;
    }

    public Phone owner(final String owner) {
        this.smartObject.setOwner(owner);
        return this;
    }

    public Phone registrationDate(final String registrationDate) {
        this.smartObject.setRegistrationDate(registrationDate);
        return this;
    }

    public Phone registationLocation(final Location location) {
        this.smartObject.setRegistrationLocationWithLocation(location);
        return this;
    }

    public Phone phoneId(final String phoneId) {
        this.smartObject.setAttribute(PHONE_ID, phoneId);
        return this;
    }

    public Phone phoneModel(final String phoneModel) {
        this.smartObject.setAttribute(PHONE_DATA_MODEL, phoneModel);
        return this;
    }

    public Phone phoneCapacity(final String phoneCapacity) {
        this.smartObject.setAttribute(PHONE_DATA_CAPACITY, phoneCapacity);
        return this;
    }

    public Phone phoneSoftwareVersion(final String phoneSoftwareVersion) {
        this.smartObject.setAttribute(PHONE_DATA_SOFT_VERSION, phoneSoftwareVersion);
        return this;
    }

    public Phone phoneAppName(final String phoneAppName) {
        this.smartObject.setAttribute(PHONE_DATA_APP_NAME, phoneAppName);
        return this;
    }

    public Phone phoneAppVersion(final String phoneAppVersion) {
        this.smartObject.setAttribute(PHONE_DATA_APP_VERSION, phoneAppVersion);
        return this;
    }

    private void setPhoneCollection() {
        Collection collection = new Collection();
        collection.setNaturalKey("ios_android_devices");

        this.smartObject.addToCollectionsWithNaturalKey(collection);
    }


}
