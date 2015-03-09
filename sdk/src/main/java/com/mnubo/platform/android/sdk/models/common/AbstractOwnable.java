/*
 * ---------------------------------------------------------------------------
 * 
 * COPYRIGHT (c) 2014 Mnubo Inc. All Rights Reserved.
 * 
 * The copyright to the computer program(s) herein is the property of Mnubo Inc. The program(s) may be used and/or
 * copied only with the written permission from Mnubo Inc. or in accordance with the terms and conditions stipulated in
 * the agreement/contract under which the program(s) have been supplied.
 * 
 * Author: pgehl Date : Dec 9, 2014
 * 
 * ---------------------------------------------------------------------------
 */

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
