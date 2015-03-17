/*
 * ---------------------------------------------------------------------------
 * 
 * COPYRIGHT (c) 2014 Mnubo Inc. All Rights Reserved.
 * 
 * The copyright to the computer program(s) herein is the property of Mnubo Inc. The program(s) may be used and/or
 * copied only with the written permission from Mnubo Inc. or in accordance with the terms and conditions stipulated in
 * the agreement/contract under which the program(s) have been supplied.
 * 
 * Author: dmasse Date : Aug 20, 2014
 * 
 * ---------------------------------------------------------------------------
 */

package com.mnubo.platform.android.sdk.models.smartobjects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A list of {@link com.mnubo.platform.android.sdk.models.smartobjects.SmartObject}
 * <p/>
 * List implementation used is {@link java.util.LinkedList}
 *
 * @see com.mnubo.platform.android.sdk.models.smartobjects.SmartObject
 */
@JsonInclude(Include.NON_EMPTY)
public class SmartObjects {
    @JsonProperty("objects")
    private List<SmartObject> smartObjects;
    private int count;

    public SmartObjects() {
        smartObjects = new ArrayList<>();
    }

    public List<SmartObject> getSmartObjects() {
        if (smartObjects == null) {
            smartObjects = new LinkedList<>();
        }
        if (smartObjects.size() != count) {
            count = smartObjects.size();
        }
        return smartObjects;
    }

    public void setSmartObjects(List<SmartObject> smartObjects) {
        this.smartObjects = smartObjects;
        count = smartObjects.size();
    }

    public void addObject(SmartObject smartObject) {
        smartObjects.add(smartObject);
        count++;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("");

        for (SmartObject object : smartObjects) {
            out.append(String.format("ObjectId: %s, owner: %s\n", object.getObjectId(), object.getOwner()));
        }
        return out.toString();
    }


}
