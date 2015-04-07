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

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A list of {@link com.mnubo.platform.android.sdk.models.smartobjects.SmartObject} <p/> List
 * implementation used is {@link java.util.LinkedList}
 *
 * @see com.mnubo.platform.android.sdk.models.smartobjects.SmartObject
 */
@JsonInclude(Include.NON_EMPTY)
public class SmartObjects implements Parcelable, Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 1L;

    @JsonProperty("objects")
    private List<SmartObject> smartObjects;
    private int count;

    public SmartObjects() {
        smartObjects = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public SmartObjects(Parcel in) {
        this.count = in.readInt();
        this.smartObjects = in.readArrayList(null);
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
        return "SmartObjects{" +
                "smartObjects=" + smartObjects +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SmartObjects)) return false;

        SmartObjects that = (SmartObjects) o;

        if (count != that.count) return false;
        if (smartObjects != null ? !smartObjects.equals(that.smartObjects) : that.smartObjects != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = smartObjects != null ? smartObjects.hashCode() : 0;
        result = 31 * result + count;
        return result;
    }

    /*
     * Implements Parcelable
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeList(this.smartObjects);
    }

    public static final Parcelable.Creator<SmartObjects> CREATOR
            = new Parcelable.Creator<SmartObjects>() {
        public SmartObjects createFromParcel(Parcel in) {
            return new SmartObjects(in);
        }

        public SmartObjects[] newArray(int size) {
            return new SmartObjects[size];
        }
    };


}
