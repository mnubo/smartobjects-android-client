package com.mnubo.platform.android.sdk.models.collections;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mnubo.platform.android.sdk.models.common.AbstractOwnable;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Collection extends AbstractOwnable {
    private String label;

    @JsonProperty("id")
    private UUID collectionId;

    @JsonProperty("natural_key")
    private String naturalKey;

    public Collection() {
    }

    public Collection(Parcel in) {
        this.setOwner(in.readString());
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

    /*
     *  Implements Parcelable
    */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getOwner());
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
