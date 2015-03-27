/*
 * Copyright (c) 2015 Mnubo. Released under MIT License.
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy
 *     of this software and associated documentation files (the "Software"), to deal
 *     in the Software without restriction, including without limitation the rights
 *     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *     copies of the Software, and to permit persons to whom the Software is
 *     furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in
 *     all copies or substantial portions of the Software.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *     THE SOFTWARE.
 */

package com.mnubo.platform.android.sdk.models.smartobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
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
public class SmartObjects implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 1L;

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
