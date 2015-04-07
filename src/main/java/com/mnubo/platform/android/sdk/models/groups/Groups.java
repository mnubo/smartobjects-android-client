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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class Groups implements Parcelable, Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 1L;

    private List<Group> groups;
    private int count;

    public Groups() {
        groups = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public Groups(Parcel in) {
        this.count = in.readInt();
        this.groups = in.readArrayList(null);
    }

    public List<Group> getGroups() {
        if (groups == null) {
            groups = new ArrayList<>();
        }

        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Groups{" +
                "groups=" + groups +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Groups)) return false;

        Groups groups1 = (Groups) o;

        if (count != groups1.count) return false;
        if (groups != null ? !groups.equals(groups1.groups) : groups1.groups != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = groups != null ? groups.hashCode() : 0;
        result = 31 * result + count;
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
        dest.writeInt(this.count);
        dest.writeList(this.groups);
    }

    public static final Parcelable.Creator<Groups> CREATOR
            = new Parcelable.Creator<Groups>() {
        public Groups createFromParcel(Parcel in) {
            return new Groups(in);
        }

        public Groups[] newArray(int size) {
            return new Groups[size];
        }
    };
}
