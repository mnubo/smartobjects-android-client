package com.mnubo.platform.android.sdk.models.users;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.LinkedList;
import java.util.List;

/**
 * A list of {@link com.mnubo.platform.android.sdk.models.users.User}
 * <p/>
 * List implementation used is {@link java.util.LinkedList}
 *
 * @see com.mnubo.platform.android.sdk.models.users.User
 */
@JsonInclude(Include.NON_NULL)
public class Users {

    protected List<User> users;
    protected int count;

    public Users() {
        users = new LinkedList<>();
        count = 0;
    }

    public List<User> getUsers() {
        if (users == null) {
            users = new LinkedList<User>();
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
        String out = "";
        for (int i = 0; i < users.size(); i++) {

            out = out + "Name: " + users.get(i) + "\n";
        }
        return out;
    }
}
