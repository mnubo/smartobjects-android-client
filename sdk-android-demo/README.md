# Demo Application
This application is a demo that showcase some of the use case enabled by the Mnubo
Android SDK.

 - Login as a user
 - Register a user
 - Confirm a user registration
 - Reset password
 - Fetch all objects of a given user
 - Update a user
 - Fetch a specific object
 - Add samples of an object's sensor

## Dependencies
This project needs the `sdk-android-lib` subproject. If you want to use the demo without
the full SDK source change this line (in `sdk-android-demo/build.gradle`):
```
compile project(':sdk-android-lib')
```
for this one :
```
compile("com.mnubo:sdk-android:1.2.0@aar") {
    transitive = true
}
```