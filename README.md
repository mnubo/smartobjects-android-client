# Mnubo Android SDK #
This SDK provides you with a wrapper to use the Mnubo's service easily from your Android application.
Basically this SDK sets up a connection with the Mnubo API and ensure oAuth2 headers are present in
your calls to it.

## How to open this project ##
This project can be opened with Android Studio or Eclipse (Note : Eclipse is not an Android supported IDE anymore). New Android build system is based on Gradle, so you'll need that too.

 1. Download and install [Android Studio](http://developer.android.com/sdk/index.html)
 2. Download and install Gradle
 3. Clone this repo
 4. Start Android Studio
 5. Open the project
   1. File -> Open
   2. Go to the cloned repository and select the build.gradle file in the top directory
   3. Keep the default options (Use gradle wrapper)
   4. Android Studio should detect an existing project, and ask you what do to about it. Select "Open existing project"

## Gradle properties ##
The build.gradle files depends on properties that are different from user to user. These properties
should be defined in your $HOME/.gradle/gradle.properties file.


```
artifactory_user=MNUBO_ARTIFACTORY_USERNAME
artifactory_password=MNUBO_ARTIFACTORY_PASSWORD

NEXUS_USERNAME=YourSonatypeJiraUsername
NEXUS_PASSWORD=YourSonatypeJiraPassword

POM_DEVELOPER_ID=DEVELOPER_ID
POM_DEVELOPER_NAME=YOUR_NAME
POM_DEVELOPER_EMAIL=EMAIL

//obtained with gpg --gen-key
//to sign the artifact pushed to the Sonatype repository
signing.keyId=KEYIDAABC123
signing.password=PrivateKeyPassword
signing.secretKeyRingFile=/path/to/gpg/secring.gpg
```


## Geting started ##

The library can be picked up from Github or MavenCentral. Add the jar to your project dependencies
or add this Gradle dependency to your build file :

```
    // Using gradle and maven dependency resolution
    compile('com.mnubo.android:sdk:1.0.0@aar') {
        transitive = true
    }
    // If you drop it in the /libs folder
    compile fileTree(dir: 'libs', include: ['*.aar'])
```

You also need to exclude the following files from the packaging to avoid duplicate exception during
build :
```
packagingOptions {
        exclude 'META-INF/DEPENDENCIES.txt'
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/NOTICE.txt'
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/DEPENDENCIES'
        exclude 'META-INF/notice.txt'
        exclude 'META-INF/license.txt'
        exclude 'META-INF/dependencies.txt'
        exclude 'META-INF/LGPL2.1'
        exclude 'META-INF/ASL2.0'
    }
```

The SDK requires internet permission in order to work correctly. Add this to your
AndroidManifest.xml:

```
<uses-permission android:name="android.permission.INTERNET" />
```

The SDK must be initialized. To do so, call the init() function in your application startup. For
example :

```
    @Override
    public void onCreate() {
        //you can store the consumer_key // consumer_secret where you want
        //I used the string resources here
        Mnubo.init(this,//_this_ is an Android Context here
                    getString(R.string.mnubo_consumer_key),
                    getString(R.string.mnubo_consumer_secret),
                    "mycompany.api.mnubo.com");
    }
```

## Using the SDK ##

Once initialized, you can do a very limited set of commands until the user of your application has
signed in.

Using only your client credentials (_CONSUMER\_KEY_ and _CONSUMER\_SECRET_), you can only use the
ClientOperations and AuthenticationOperations interfaces, anything else would raise Unauthorized
Exception.

To use the Mnubo's SDK, you must have a `MnuboApi` object. The `MnuboApi` is used to perform all
the operations in your activities. If a User connection (based on a user token, grant\_type=password)
is available, it will be used, otherwise, a client connection (based on a client token,
grant\_type=client\_credentials).

To get the `MnuboApi` object, use this :

```
    MnuboApi mnuboApi = Mnubo.getApi();
```

## Sign in as a user ##

You can sign in on behalf of the user once and start using the SDK to it's fullest by calling the
API like this:
```
mnuboApi.getAuthenticationOperations().logIn(username, password, new CompletionCallBack<Boolean>() {
            @Override
            public void onCompletion(Boolean success, MnuboSdkException error) {
            if (error == null && success) {
                // Do something
            } else if (error instanceof MnuboBadCredentialsException) {
                // Display invalid credentials error
            } else {
                // Display error
            }
        }
    });
```

## Available API operations ##
All operations except the ClientOperations and the AuthenticationOperations will perform a token
refresh if the current access\_token has expired. Most of these operations requires a
CompletionCallBack, the request that don't are not using the network.

All of the operations are executed in a AsyncTask. Therefore, none of them run on the main thread.
Here is an exhaustive list of the available operations.

* UserOperations
  * `findUserObjects(final String username, final CompletionCallBack<UserObjects> completionCallBack)`
  * `findUserObjects(String username, final Boolean details, CompletionCallBack<SmartObjects> completionCallBack)`
  * `findUserObjects(String username, final Boolean details, final String objectModelName, CompletionCallBack<SmartObjects> completionCallBack)`
  * `getUser(final CompletionCallBack<User> completionCallBack)`
  * `update(final String username, final User updatedUser, final CompletionCallBack<Boolean> completionCallBack)`
  * `updatePassword(String username, UpdatePassword newPassword, MnuboApi.CompletionCallBack<Boolean> completionCallBack)`
* SmartObjectOperations
  * `findObject(final SdkId objectId, final CompletionCallBack<UserObject> completionCallBack)`
  * `update(final SdkId objectId, final UserObject object, final CompletionCallBack<Boolean> completionCallBack)`
  * `searchSamples(final SdkId objectId, final String sensorName, final CompletionCallBack<Samples> completionCallBack)`
  * `addSamples(final SdkId objectId, final Samples samples, final CompletionCallBack<Boolean> completionCallBack)`
  * `addSampleOnPublicSensor(SdkId objectId, String sensorName, Sample sample, CompletionCallBack<Boolean> completionCallBack)`
  * `createObject(SmartObject smartObject, Boolean updateIfExists, CompletionCallBack<Boolean> completionCallBack)`
* ClientOperations
  * `createUser(final User user, final CompletionCallBack<User> completionCallBack)`
  * `confirmUserCreation(final String username, final UserConfirmation userConfirmation, final CompletionCallBack<Boolean> completionCallBack)`
  * `resetPassword(final String username, final CompletionCallBack<Boolean> completionCallBack)`
  * `confirmPasswordReset(final String username, final ResetPassword resetPassword, final CompletionCallBack<Boolean> completionCallBack)`
* AuthenticationOperations
  * `logIn(final String username, final String password, final CompletionCallBack<Boolean> completionCallBack)`
  * `logOut()`
  * `isUserConnected()`
  * `getUsername()`

Note that if you are required to execute the request synchronously, on the current thread, you can
pass `null` as the completion callback. This can be useful when you work in an Android `Service`.

See the api doc [here](./src/main/com/mnubo/platform/android/sdk/api/README.md)

## Examples

Suppose the user of the application is logged in (the user token is still valid
(access or atleast the refresh)). You want to see all the objects that belongs to this user. In your
Android Activity, you should have something like this:

```
    mnuboApi.getUserOperations().findUserObjects(mnuboApi.getAuthenticationOperations().getUsername(), new CompletionCallBack<SmartObjects>() {

            @Override
            public void onCompletion(SmartObjects result, MnuboSdkException ex) {
                    if (ex == null) {

                        UserObjects result = response.getResult();

                        //Do what you want with the objects

                    } else {
                        //handle error which is in response.getError();
                    }
                }
            });
```

## Documentation ##
Extensive documentation is available in the appropriates directories of the repository or in the
generated Javadoc.

## ToDos ##
* Parcelable on Mnubo models
* Validate Android-GeoJson (type problem)
* spring-core import