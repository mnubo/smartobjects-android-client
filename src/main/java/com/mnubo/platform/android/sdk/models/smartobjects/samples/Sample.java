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

    @JsonIgnore
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
