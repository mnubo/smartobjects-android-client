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

package com.mnubo.platform.android.sdk.models.smartobjects.samples;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample} is a list of values
 * for a specific sensor at a moment in time. Coordinates are
 * also recorded  for analytics.
 *
 * @see com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples
 */
@JsonInclude(Include.NON_DEFAULT)
public class Sample implements Parcelable, Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 1L;

    private Map<String, Object> value = new HashMap<>();
    @JsonProperty("name")
    private String sensorName;
    private String timestamp;
    private List<Double> coordinates = new ArrayList<>();

    public Sample() {
    }

    public Sample(String sensorName) {
        this.sensorName = sensorName;
    }

    @SuppressWarnings("unchecked")
    public Sample(Parcel in) {
        this.value = in.readHashMap(null);
        this.sensorName = in.readString();
        this.timestamp = in.readString();
        this.coordinates = in.readArrayList(null);
    }

    public Map<String, Object> getValue() {
        return value;
    }

    public void setValue(Map<String, Object> values) {
        this.value = values;
    }

    public void addValue(String key, Object value) {
        this.value.put(key, value);
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String name) {
        this.sensorName = name;
    }

    public void setTimestamp(String ts) {
        this.timestamp = ts;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setLocation(final Location location) {
        if (location != null) {
            this.coordinates = new ArrayList<>();
            this.coordinates.add(location.getLatitude());
            this.coordinates.add(location.getLongitude());
            if (location.getAltitude() != 0.0d) {
                this.coordinates.add(location.getAltitude());
            }
        }

    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    @JsonIgnore
    public Double getLatitude() {
        if (coordinates != null && coordinates.size() >= 1) {
            return coordinates.get(0);
        }
        return null;
    }

    @JsonIgnore
    public Double getLongitude() {
        if (coordinates != null && coordinates.size() >= 2) {
            return coordinates.get(1);
        }
        return null;
    }

    @JsonIgnore
    public Double getAltitude() {
        if (coordinates != null && coordinates.size() >= 3) {
            return coordinates.get(2);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Sample{" +
                "value=" + value +
                ", sensorName='" + sensorName + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sample)) return false;

        Sample sample = (Sample) o;

        if (coordinates != null ? !coordinates.equals(sample.coordinates) : sample.coordinates != null)
            return false;
        if (sensorName != null ? !sensorName.equals(sample.sensorName) : sample.sensorName != null)
            return false;
        if (timestamp != null ? !timestamp.equals(sample.timestamp) : sample.timestamp != null)
            return false;
        if (value != null ? !value.equals(sample.value) : sample.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (sensorName != null ? sensorName.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
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
        dest.writeMap(this.value);
        dest.writeString(this.sensorName);
        dest.writeString(this.timestamp);
        dest.writeList(this.coordinates);
    }

    public static final Parcelable.Creator<Sample> CREATOR
            = new Parcelable.Creator<Sample>() {
        public Sample createFromParcel(Parcel in) {
            return new Sample(in);
        }

        public Sample[] newArray(int size) {
            return new Sample[size];
        }
    };
}
