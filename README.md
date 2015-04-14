# mnubo Android SDK #
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mnubo/sdk-android/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.mnubo/sdk-android)

This SDK provides you with a wrapper to use the mnubo's service easily from your Android application.
Basically this SDK sets up a connection with the mnubo API and ensure oAuth2 headers are present in
your calls to it.

## Artifacts ##
Sources, Javadoc and the library itself are located
[here](http://search.maven.org/#search|gav|1|g%3A%22com.mnubo%22%20AND%20a%3A%22sdk-android%22).

## Geting started ##

The library can be picked up from Github or MavenCentral. Add the aar to your project dependencies
or add this Gradle dependency to your build file :

```
// Using gradle and maven dependency resolution
compile('com.mnubo:sdk-android:1.1.5@aar') {
    transitive = true
}
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

The SDK must be initialized. To do so, call the init() function in your application startup. For
example :

```
@Override
public void onCreate() {
    //you can store the consumer_key // consumer_secret where you want
    //I used the string resources here
    Mnubo.init(this,
                "CONSUMER_KEY",
                "CONSUMER_SECRET",
                "mycompany.api.mnubo.com");
}
```

## Key and secret ##
The _CONSUMER\_KEY_ and _CONSUMER\_SECRET_ are provided to you by mnubo. The pair (key/secret) has a
predefined scope. This scope gives you permission to certain APIs.

Using only your client credentials (_CONSUMER\_KEY_ and _CONSUMER\_SECRET_), you can only use the
[ClientOperations](src/main/java/com/mnubo/platform/android/sdk/api/operations/ClientOperations.java)
 and [AuthenticationOperations](src/main/java/com/mnubo/platform/android/sdk/api/operations/AuthenticationOperations.java)
 interfaces, anything else will raise `MnuboAccessDeniedException`.

Once a user has logged in, you can use the other
[APIs](src/main/java/com/mnubo/platform/android/sdk/api/operations/).

## Using the SDK ##
Once initialized, you can do a very limited set of commands until the user of your application has
signed in.

To use the mnubo's SDK, you must have a `MnuboApi` object. The `MnuboApi` is used to perform all
the operations. If a User connection (based on a user token, grant\_type=password)
is available, it will be used, otherwise, a client connection (based on a client token,
grant\_type=client\_credentials) will be.

To get the `MnuboApi` object, use this (after initialization) :

```
MnuboApi mnuboApi = Mnubo.getApi();
```

## Sign in as a user ##

You can sign in on behalf of the user and start using the SDK to it's fullest by calling the
API like this:
```
mnuboApi.getAuthenticationOperations().logInAsync(username, password, new CompletionCallBack<Boolean>() {
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
All operations except the `AuthenticationOperations#logIn` will perform a token
refresh if the current access\_token has expired. Operations have both synchronous and asynchronous
signature.

Synchronous request are performed on the current thread. Asynchronous request runs in an `AsyncTask`
and the result is passed through the callback if it is available.

## Offline datastore
The mnubo Android SDK supports offline caching for requests that fails. If the request fails, the data
is persisted to the disk in the application cache folder. You can also provide another directory when
you enable the feature.

To enable it, simply call `enableFailedDataStore` like this :

```
Mnubo.enableFailedDataStore();
//or, if you want to write to a specific folder
Mnubo.enableFailedDataStore(new File(PATH));
```

## Examples
Suppose the user of the application is logged in (the user token is still valid. You want to see all
the objects that belongs to this user. In your Android `Activity`, you would do something like this:

```
final String username = mnuboApi.getAuthenticationOperations().getUsername();
mnuboApi.getUserOperations().findUserObjectsAsync(username, new CompletionCallBack<SmartObjects>() {

        @Override
        public void onCompletion(SmartObjects result, MnuboSdkException ex) {
                if (ex == null) {

                    UserObjects result = response.getResult();

                    //Do what you want with the objects

                } else {
                    //handle error
                }
            }
        });
```

## Documentation ##
Extensive documentation is available in the generated Javadoc.

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
If you intent to build the SDK from the sources, the build.gradle files will require various
properties that are used for publishing. To remove these requirements, get rid of the following
line in the gradle.build file :

```
apply from: 'gradle/gradle-mvn-push.gradle'
```
