package com.mnubo.platform.android.sdk.models.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

/**
 * A Mnubo attribute is a name and a type. It is used to store data of any
 * kind.
 * <p/>
 * Attributes can be found in {@link com.mnubo.platform.android.sdk.models.smartobjects.SmartObject} and
 * {@link com.mnubo.platform.android.sdk.models.users.User}.
 */
@JsonInclude(Include.NON_NULL)
public class Attribute implements Serializable, Parcelable {


    public static final String DEFAULT_CATEGORY = "default";

    private String name;

    private String type;

    private String value;

    public Attribute() {

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
                ", name='" + name + '\'' +
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
