[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mnubo/sdk-android/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.mnubo/sdk-android)
[![Build Status](https://travis-ci.org/mnubo/smartobjects-android-client.svg?branch=master)](https://travis-ci.org/mnubo/smartobjects-android-client)

## Introduction ##

This SDK provides you with a wrapper to use the mnubo's service easily from your Android application.
Basically this SDK sets up a connection with the mnubo API and ensure oAuth2 headers are present in
your calls to it.


## Installation & Configuration ##

The library can be picked up from Github or MavenCentral. Add the aar to your project dependencies
or add this Gradle dependency to your build file :

```
// Using gradle and maven dependency resolution
compile('com.mnubo:sdk-android:3.0.0@aar') {
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

The SDK must be initialized (only once). To do so, call the `init()` function in your application startup. For
example :

```
@Override
public void onCreate() {

    Mnubo.init(
        getApplicationContext(),
        MnuboSDKConfig.withUrlAndKey("https://sandbox.api.mnubo.com", "CONSUMER_KEY"),
        new AuthenticationProblemCallback() {
            @Override
            public void onError() {
                //do something when login fails
            }
        });
}
```

## Key ##
The _CONSUMER\_KEY_ is provided to you by mnubo. The SDK can only be used by a logged in owner. If no
owner logs into the app, all operations will give you an access_denied.

## Using the SDK ##

Once initialized and logged in, you can get an instance of the `MnuboApi` and use it to interact
with mnubo servers.

```
MnuboApi mnuboApi = Mnubo.getApi();
```

### Logging in ###

You can sign in on behalf of the user and start using the SDK to it's fullest by calling the
API like this (note that this call performs Network IO, and you should not do it on the main thread):
```
boolean success = Mnubo.logIn(username, password);
```
### Check if user is logged in ###

You can know if the user is logged in like this :
```
boolean loggedIn = Mnubo.isLoggedIn();
```

### Available API operations ###
All operations will perform a token refresh if the current access\_token has expired.
Operations have both synchronous and asynchronous signature.

Synchronous request are performed on the current thread. Asynchronous request runs in an `AsyncTask`
and the result is passed through the callback if it is available.

Asynchronous call require a callback the will be invoked when the operation has completed:
```
Mnubo.getApi().getEventOperations().sendEventsAsync(deviceId, events, new CompletionCallback<Void>() {
    @Override
    public void onSuccess(Void result) {
        //it worked
    }

    @Override
    public void onFailure(MnuboException exception) {
        //it didn't work
    }
});
```

### Data store ###
The SDK comes with a store that allows you to store data. Currently, the store only allows to save
events.

```
Mnubo.getStore().writeEvents(deviceId, events);
//or
Mnubo.getStore().readEvents(new MnuboStore.ReadEventsCallback() {
    @Override
    public void process(String deviceId, List<Event> readEvents) {
        Mnubo.getApi().getEventOperations().sendEventsAsync(deviceId, readEvents, new CompletionCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                //success
            }

            @Override
            public void onFailure(MnuboException exception) {
               //failed
            }
        });
    }
    @Override
    public void error(File fileInError) {
        //the file could not be read or does not contains appropriate data
    }
});
```

The mnubo Android SDK allows you to write object to the disk. The default location is the application
cache directory (using the Android context provided in the init call) but you can
provide another directory if you want:
```
Mnubo.getStore().setRootDir(new File("/where/you/need"));
Mnubo.getStore().setSizeLimit(250); //default is 200 files
```

### Examples ###
Assuming you are logged in, this is how you post events:
```
List<Event> myEvents =
    Collections.singletonList(
        Event.builder()
        .eventType("my_event_type")
        .timeserie("timeserie1", "value")
        .build()
    );
Mnubo.getApi().getEventOperations().sendEventsAsync(deviceId, myEvents, new CompletionCallback<Void>() {
    @Override
    public void onSuccess(Void result) {
        showProgress(false, mProgressView, mUpdateWindTurbineView);
        Toast.makeText(getApplicationContext(), "It works, marvelous", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(MnuboException exception) {
        showProgress(false, mProgressView, mUpdateWindTurbineView);
        Toast.makeText(getApplicationContext(), "It didn't work, too sad.", Toast.LENGTH_SHORT).show();
        if (exception instanceof MnuboNetworkException) {
            Mnubo.getStore().writeEvents(deviceId, events);
        }
    }
});
```

## Demo ##
There is an application demo [here](demo/) that you can look at for example on
how to use the SDK.

---
## Important notes ##

Sources, Javadoc and the library itself are located
[here](http://search.maven.org/#search|gav|1|g%3A%22com.mnubo%22%20AND%20a%3A%22sdk-android%22).

Extensive documentation is available in the generated Javadoc.