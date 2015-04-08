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

package com.mnubo.platform.android.sdk.models.collections;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mnubo.platform.android.sdk.models.common.AbstractOwnable;

import java.io.Serializable;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Collection extends AbstractOwnable implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 1L;

    private String label;

    @JsonProperty("id")
    private UUID collectionId;

    @JsonProperty("natural_key")
    private String naturalKey;

    public Collection() {
    }

    public Collection(Parcel in) {
        this.owner = in.readString();
        this.label = in.readString();
        this.collectionId = UUID.fromString(in.readString());
        this.naturalKey = in.readString();
    }

    public UUID getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(final UUID aCollectionId) {
        collectionId = aCollectionId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getNaturalKey() {
        return naturalKey;
    }

    public void setNaturalKey(final String aNaturalKey) {
        naturalKey = aNaturalKey;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "label='" + label + '\'' +
                ", collectionId=" + collectionId +
                ", naturalKey='" + naturalKey + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Collection)) return false;

        Collection that = (Collection) o;

        if (collectionId != null ? !collectionId.equals(that.collectionId) : that.collectionId != null)
            return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (naturalKey != null ? !naturalKey.equals(that.naturalKey) : that.naturalKey != null)
            return false;
        if (owner != null ? !owner.equals(that.owner) : that.owner != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (collectionId != null ? collectionId.hashCode() : 0);
        result = 31 * result + (naturalKey != null ? naturalKey.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
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
        dest.writeString(this.owner);
        dest.writeString(this.label);
        dest.writeString(this.collectionId.toString());
        dest.writeString(this.naturalKey);
    }

    public static final Parcelable.Creator<Collection> CREATOR
            = new Parcelable.Creator<Collection>() {
        public Collection createFromParcel(Parcel in) {
            return new Collection(in);
        }

        public Collection[] newArray(int size) {
            return new Collection[size];
        }
    };
}
