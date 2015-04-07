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

package com.mnubo.platform.android.sdk.models.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

/**
 * Attributes are used in {@link com.mnubo.platform.android.sdk.models.smartobjects.SmartObject} and
 * {@link com.mnubo.platform.android.sdk.models.users.User}.
 * <p/>
 * {@link com.mnubo.platform.android.sdk.models.smartobjects.SmartObject} <code>Attribute</code> are
 * defined in the ObjectModel of the object. The type will automatically be resolved according to
 * the type defined for this <code>Attribute</code> in the definition.
 * <p/>
 * {@link com.mnubo.platform.android.sdk.models.users.User} <code>Attribute</code> are currently
 * used to store any information. The default type is STRING. Soon the User's Attribute will be
 * defined by a Model definition too.
 */
@JsonInclude(Include.NON_NULL)
public class Attribute implements Serializable, Parcelable {

    @JsonIgnore
    private static final long serialVersionUID = 1L;

    private String name;

    private String type;

    private String value;

    public Attribute() {

    }

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private Attribute(Parcel in) {
        this.name = in.readString();
        this.type = in.readString();
        this.value = in.readString();
    }


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }


/*
    *  Implements Parcelable
    */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.value);
    }

    public static final Parcelable.Creator<Attribute> CREATOR
            = new Parcelable.Creator<Attribute>() {
        public Attribute createFromParcel(Parcel in) {
            return new Attribute(in);
        }

        public Attribute[] newArray(int size) {
            return new Attribute[size];
        }
    };

}
