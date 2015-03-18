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
package com.mnubo.platform.android.sdk.internal;

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

public abstract class MockedAbstractServiceTest {

    protected static final String PLATFORM_BASE_URL = "https://sandbox.mnubo.com";

    protected final Group expectedGroup = new Group();
    protected final Collection expectedCollection = new Collection();
    protected final SmartObjects expectedSmartObjects = new SmartObjects();
    protected final SmartObject expectedSmartObject = new SmartObject();
    protected final Users expectedUsers = new Users();
    protected final User expectedUser = new User();
    protected final Sample expectedSample = new Sample();
    protected final Samples expectedSamples = new Samples();

    protected final RestTemplate mockedRestTemplate = mock(RestTemplate.class);

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

    protected String expectedUrl(String uri) {
        return PLATFORM_BASE_URL + PATH + uri;
    }
}
