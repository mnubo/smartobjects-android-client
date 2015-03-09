# Mnubo Android SDK #
This project contains two things:
* The SDK
* An application demo using it to show basic functionality

The documentation for the SDK is available in the "sdk" folder.

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

The "app" is an Android application that requires the "sdk" which is an Android Library. There are three product flavors : 
*  local : for development with local platform (http, port:8081, path:"/rest")
*  staging : for development with staging platform (https, port:443, path:"/api/v3") **v3 is temporary here**
*  production : for release application with production platform(https, port:443, path:"/api/v2")

Each product flavor is either built for debug or for release. You can select the current build flavor from within Android Studio. Only the production flavor is available on MavenCentral
