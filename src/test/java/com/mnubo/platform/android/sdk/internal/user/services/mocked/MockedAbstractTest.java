package com.mnubo.platform.android.sdk.internal.user.services.mocked;

import com.mnubo.platform.android.sdk.models.collections.Collection;
import com.mnubo.platform.android.sdk.models.groups.Group;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;
import com.mnubo.platform.android.sdk.models.users.User;
import com.mnubo.platform.android.sdk.models.users.Users;

import org.junit.Before;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static com.mnubo.platform.android.sdk.BuildConstants.PATH;
import static org.mockito.Mockito.mock;

public abstract class MockedAbstractTest {

    protected static final String PLATFORM_BASE_URL = "https://sandbox.mnubo.com";
    protected static final String USER_ACCESS_TOKEN = "ACCESS_TOKEN";

    protected final Group expectedGroup = new Group();
    protected final Collection expectedCollection = new Collection();
    protected final SmartObjects expectedSmartObjects = new SmartObjects();
    protected final SmartObject expectedSmartObject = new SmartObject();
    protected final Users expectedUsers = new Users();
    protected final User expectedUser = new User();
    protected final Sample expectedSample = new Sample();
    protected final Samples expectedSamples = new Samples();

    protected RestTemplate mockedRestTemplate = mock(RestTemplate.class);

    @Before
    public void setUp() throws Exception {
        expectedGroup.setOwner("owner");
        expectedGroup.setLabel("label");

        expectedCollection.setOwner("owner");
        expectedCollection.setLabel("label");

        expectedUser.setUsername("username");
        expectedUser.setFirstname("firstname");
        expectedUser.setLastname("lastname");
        expectedUsers.addUser(expectedUser);


        expectedSmartObject.setOwner("owner");
        expectedSmartObject.setDeviceId("deviceid");
        expectedSmartObject.setObjectId(new UUID(48, 16));
        expectedSmartObjects.addObject(expectedSmartObject);

        expectedSample.setSensorName("sensor_name");
        expectedSamples.addSample(expectedSample);
    }

    protected String buildPath(String uri) {
        return buildPath(PATH, uri);
    }

    protected String buildPath(String rootPath, String uri) {
        return PLATFORM_BASE_URL + rootPath + uri;
    }
}
