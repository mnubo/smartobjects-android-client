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

import com.mnubo.platform.android.sdk.mocks.MockParcel;
import com.mnubo.platform.android.sdk.models.collections.Collection;
import com.mnubo.platform.android.sdk.models.common.Attribute;
import com.mnubo.platform.android.sdk.models.groups.Group;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UsersTest {

    @Test
    public void testWriteToParcel() throws Exception {
        Users users = new Users();

        Parcel parcel = MockParcel.obtain();
        users.writeToParcel(parcel, 0);

        Users usersFromParcel = Users.CREATOR.createFromParcel(parcel);

        assertEquals(users, usersFromParcel);
    }

    @Test
    public void testWriteToParcelWithData() throws Exception {
        Users users = new Users();
        User user = new User();
        user.setUsername("username");
        user.setFirstname("firstname");
        user.setLastname("lastname");
        user.setPassword("password");
        user.setConfirmedPassword("confirmedPassword");
        final ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("name", "value"));
        user.setAttributes(attributes);

        final ArrayList<Collection> collections = new ArrayList<>();
        collections.add(new Collection());
        user.setCollections(collections);

        final ArrayList<Group> groups = new ArrayList<>();
        groups.add(new Group());
        user.setGroups(groups);

        users.addUser(user);

        Parcel parcel = MockParcel.obtain();
        users.writeToParcel(parcel, 0);

        Users usersFromParcel = Users.CREATOR.createFromParcel(parcel);

        assertEquals(users, usersFromParcel);
    }
}