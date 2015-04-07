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


import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of {@link com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample}
 *
 * @see com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples
 */
@JsonInclude(Include.NON_NULL)
public class Samples implements Parcelable, Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 2L;

    private List<Sample> samples;

    public Samples() {
        samples = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public Samples(Parcel in) {
        samples = in.readArrayList(null);
    }


    public List<Sample> getSamples() {
        return samples;
    }

    public void setSamples(List<Sample> samples) {
        this.samples = samples;
    }

    public void addSample(Sample sample) {
        this.samples.add(sample);
    }

    @Override
    public String toString() {
        return "Samples{" +
                "samples=" + samples +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Samples)) return false;

        Samples samples1 = (Samples) o;

        if (samples != null ? !samples.equals(samples1.samples) : samples1.samples != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return samples != null ? samples.hashCode() : 0;
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
        dest.writeList(this.samples);
    }

    public static final Parcelable.Creator<Samples> CREATOR
            = new Parcelable.Creator<Samples>() {
        public Samples createFromParcel(Parcel in) {
            return new Samples(in);
        }

        public Samples[] newArray(int size) {
            return new Samples[size];
        }
    };
}
