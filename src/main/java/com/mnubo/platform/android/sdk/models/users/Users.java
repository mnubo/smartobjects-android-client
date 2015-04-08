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

package com.mnubo.platform.android.sdk.models.users;


import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A list of {@link com.mnubo.platform.android.sdk.models.users.User} <p/> List implementation used
 * is {@link java.util.ArrayList}
 *
 * @see com.mnubo.platform.android.sdk.models.users.User
 */
@JsonInclude(Include.NON_NULL)
public class Users implements Parcelable, Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 1L;

    private List<User> users = new ArrayList<>();
    private int count = 0;

    public Users() {
    }

    @SuppressWarnings("unchecked")
    public Users(Parcel in) {
        this.count = in.readInt();
        this.users = in.readArrayList(null);
    }

    public List<User> getUsers() {
        if (users == null) {
            users = new ArrayList<>();
        }

        if (users.size() != count) {
            count = users.size();
        }

        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        count = users.size();
    }

    public void addUser(User user) {
        this.users.add(user);
        count = count + 1;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Users{" +
                "users=" + users +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Users)) return false;

        Users users1 = (Users) o;

        if (count != users1.count) return false;
        if (users != null ? !users.equals(users1.users) : users1.users != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = users != null ? users.hashCode() : 0;
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
        dest.writeList(this.users);
    }

    public static final Parcelable.Creator<Users> CREATOR
            = new Parcelable.Creator<Users>() {
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        public Users[] newArray(int size) {
            return new Users[size];
        }
    };
}
