package com.mnubo.platform.android.sdk.models.common;

import android.os.Parcelable;
import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Abstract class implemented by object that have an owner
 * field.
 *
 * @see com.mnubo.platform.android.sdk.models.collections.Collection
 * @see com.mnubo.platform.android.sdk.models.smartobjects.SmartObject
 */
public abstract class AbstractOwnable implements Parcelable {
    @JsonProperty("owner")
    private String owner;

    @JsonIgnore
    public boolean isOwner(final String username) {
        return TextUtils.equals(username, getOwner());
    }

    @JsonIgnore
    public boolean hasOwner() {
        return !TextUtils.isEmpty(getOwner());
    }

    @JsonIgnore
    public boolean hasNoOwner() {
        return !hasOwner();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

}
