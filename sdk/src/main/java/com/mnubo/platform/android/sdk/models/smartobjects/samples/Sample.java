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

package com.mnubo.platform.android.sdk.models.smartobjects.samples;

import android.location.Location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Mnubo Sample is a liste of values for a specific sensor at
 * a moment in time. Coordinates are also recorded  for analytics.
 *
 * @see com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples
 */
@JsonInclude(Include.NON_DEFAULT)
public class Sample implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Object> value;
    private String name;
    private String timestamp;
    List<Double> coordinates = new ArrayList<>();

    public Sample() {
        value = new HashMap<>();
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

    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setSensorName(String name) {
        this.name = name;
    }

    public void setTimestamp(String ts) {
        this.timestamp = ts;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setLocation(final Location location) {
        if (location != null) {
            this.coordinates = Arrays.asList(location.getLatitude(), location.getLongitude());
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
                "timestamp='" + timestamp + '\'' +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
