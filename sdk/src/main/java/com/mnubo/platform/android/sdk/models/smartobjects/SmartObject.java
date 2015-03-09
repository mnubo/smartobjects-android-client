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

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mnubo.platform.android.sdk.models.collections.Collection;
import com.mnubo.platform.android.sdk.models.common.AbstractOwnable;
import com.mnubo.platform.android.sdk.models.common.Attribute;

import org.geojson.Feature;
import org.geojson.LngLatAlt;
import org.geojson.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.mnubo.platform.android.sdk.Constants.DEVICE_ID;
import static com.mnubo.platform.android.sdk.Constants.OBJECT_MODEL;

/**
 * A Smart object on the Mnubo Platform, it belongs to a single
 * {@link com.mnubo.platform.android.sdk.models.users.User} and can
 * be in different {@link com.mnubo.platform.android.sdk.models.collections.Collection}
 * <p/>
 * It also has a list of {@link com.mnubo.platform.android.sdk.models.common.Attribute}
 *
 * @see com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects
 * @see com.mnubo.platform.android.sdk.models.common.AbstractOwnable
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmartObject extends AbstractOwnable {
    @JsonProperty("object_id")
    private UUID objectId;

    @JsonProperty(DEVICE_ID)
    private String deviceId;

    @JsonProperty(OBJECT_MODEL)
    private String objectModelName;

    @JsonProperty("registration_date")
    private String registrationDate;

    @JsonProperty("registration_location")
    private Feature registrationLocation;

    private List<Attribute> attributes = new ArrayList<Attribute>();

    private List<Collection> collections = new ArrayList<Collection>();

    public SmartObject() {
    }

    private SmartObject(Parcel in) {
        this.setOwner(in.readString());
        this.objectId = UUID.fromString(in.readString());
        this.deviceId = in.readString();
        this.objectModelName = in.readString();
        this.registrationDate = in.readString();
        //TODO give geojson-jackson-android a try
        //this.registrationLocation = in.readParcelable(Feature.class.getClassLoader());
        this.attributes = Arrays.asList(in.createTypedArray(Attribute.CREATOR));
        this.collections = Arrays.asList(in.createTypedArray(Collection.CREATOR));
    }

    public UUID getObjectId() {
        return objectId;
    }

    public void setObjectId(UUID objectId) {
        this.objectId = objectId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getObjectModelName() {
        return objectModelName;
    }

    public void setObjectModelName(String objectModelName) {
        this.objectModelName = objectModelName;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Feature getRegistrationLocation() {
        return registrationLocation;
    }

    public void setRegistrationLocation(Feature registrationLocation) {
        this.registrationLocation = registrationLocation;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public void addToCollectionsWithNaturalKey(final Collection collection) {

        for (int index = 0; index < collections.size(); index++) {
            if (collections.get(index).getNaturalKey() != null) {
                if (collections.get(index).getNaturalKey().equals(collection.getNaturalKey())) {
                    collections.set(index, collection);
                    return;
                }
            }
        }
        collections.add(collection);
    }

    public void setRegistrationLocationWithLocation(final Location location) {

        if (location == null) {
            return;
        }

        Feature feature = new Feature();
        Point point = new Point();
        LngLatAlt coord = new LngLatAlt();

        coord.setLatitude(location.getLatitude());
        coord.setLongitude(location.getLongitude());
        coord.setAltitude(location.getAltitude());

        point.setCoordinates(coord);

        feature.setGeometry(point);
        this.setRegistrationLocation(feature);
    }

    public String getAttribute(final String name) {
        if (this.getAttributes() != null && this.getAttributes().size() > 0) {
            for (Attribute attribute : this.getAttributes()) {
                if (attribute.getName().equals(name)) {
                    return attribute.getValue();
                }
            }
        }
        return null;
    }

    public void setAttribute(final String name, final String value) {
        Attribute attribute = new Attribute();
        attribute.setName(name);
        attribute.setValue(value);
        this.getAttributes().add(attribute);
    }

    @Override
    public String toString() {
        return String.format("ObjectID : %s", this.objectId != null ? this.objectId.toString() : "null");
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
        dest.writeString(this.getOwner());
        dest.writeString(this.objectId.toString());
        dest.writeString(this.deviceId);
        dest.writeString(this.objectModelName);
        dest.writeString(this.registrationDate);
        //TODO give geojson-jackson-android a try
        //dest.writeParcelable(this.registrationLocation, flags);
        dest.writeTypedArray(this.attributes.toArray(new Parcelable[this.attributes.size()]), flags);
        dest.writeTypedArray(this.collections.toArray(new Parcelable[this.collections.size()]), flags);

    }

    public static final Parcelable.Creator<SmartObject> CREATOR
            = new Parcelable.Creator<SmartObject>() {
        public SmartObject createFromParcel(Parcel in) {
            return new SmartObject(in);
        }

        public SmartObject[] newArray(int size) {
            return new SmartObject[size];
        }
    };
}
