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

package com.mnubo.platform.android.sdk.models.groups;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mnubo.platform.android.sdk.models.common.AbstractOwnable;

import java.io.Serializable;
import java.util.UUID;

@JsonInclude(Include.NON_NULL)
public class Group extends AbstractOwnable implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 1L;

    private String label;

    private UUID id;

    private String type;

    @JsonProperty("natural_key")
    private String naturalKey;

    public Group() {
    }

    public Group(Parcel in) {
        this.owner = in.readString();
        this.label = in.readString();
        final String idFromParcel = in.readString();
        this.id = !TextUtils.isEmpty(idFromParcel) ? UUID.fromString(idFromParcel) : null;
        this.type = in.readString();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID aGroupId) {
        id = aGroupId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String aOwner) {
        this.owner = aOwner;
    }

    public String getType() {
        return type;
    }

    public void setType(String aType) {
        type = aType;
    }

    public String getNaturalKey() {
        return naturalKey;
    }

    public void setNaturalKey(String aNaturalKey) {
        naturalKey = aNaturalKey;
    }

    @Override
    public String toString() {
        return "Group{" +
                "label='" + label + '\'' +
                ", owner='" + owner + '\'' +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", naturalKey='" + naturalKey + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;

        Group group = (Group) o;

        if (id != null ? !id.equals(group.id) : group.id != null) return false;
        if (label != null ? !label.equals(group.label) : group.label != null) return false;
        if (naturalKey != null ? !naturalKey.equals(group.naturalKey) : group.naturalKey != null)
            return false;
        if (owner != null ? !owner.equals(group.owner) : group.owner != null) return false;
        if (type != null ? !type.equals(group.type) : group.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (naturalKey != null ? naturalKey.hashCode() : 0);
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
        dest.writeString(owner);
        dest.writeString(label);
        dest.writeString(this.id != null ? this.id.toString() : null);
        dest.writeString(type);
    }

    public static final Parcelable.Creator<Group> CREATOR
            = new Parcelable.Creator<Group>() {
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
