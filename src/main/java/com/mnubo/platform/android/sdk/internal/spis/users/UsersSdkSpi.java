package com.mnubo.platform.android.sdk.internal.spis.users;

import com.mnubo.platform.android.sdk.models.security.UpdatePassword;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;
import com.mnubo.platform.android.sdk.models.users.User;

import java.util.List;

public interface UsersSdkSpi {

    //[/rest/users/{username}],methods=[DELETE]
    void delete(String username);

    //[/rest/users/{username}],methods=[PUT]
    void update(String username, User user);

    //[/rest/users/{username}/password],methods=[PUT]
    void updatePassword(String username, UpdatePassword password);

    //[/rest/users/{username}],methods=[GET]
    User getUser(String username);

    User getUser(String username, List<String> attributes);

    //[/rest/users/{username}/objects],methods=[GET]
    SmartObjects findUserObjects(String username);

    SmartObjects findUserObjects(String username, boolean details);

    SmartObjects findUserObjects(String username, boolean details, String model);

    SmartObjects findUserObjects(String username, boolean details, String model, boolean showHistory);

}
