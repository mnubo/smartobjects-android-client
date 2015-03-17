package com.mnubo.platform.android.sdk.models.common;

/**
 * Are and objectid are basically the same but they translate to
 * different query parameters which are currently required by the
 * Mnubo API.
 * <p/>
 * deviceid and natural are also the same. They are human readable unique
 * identifier to be used along with the uuid of the identity.
 * <p/>
 * objectid and deviceid shouldn't be and they will be removed in a near future.
 * The objectid was originally the uuid of a {@link com.mnubo.platform.android.sdk.models.smartobjects.SmartObject} and
 * the deviceid was the natural key of an {@link com.mnubo.platform.android.sdk.models.smartobjects.SmartObject}
 *
 * @see com.mnubo.platform.android.sdk.models.common.SdkId
 */
public enum IdType {

    deviceid, objectid, natural, uuid;

    public boolean isUUID() {
        return this.equals(uuid) || this.equals(objectid);
    }

    public boolean isString() {
        return !isUUID();
    }

}
