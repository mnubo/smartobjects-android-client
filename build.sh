#!/bin/bash

if [ "$TRAVIS_PULL_REQUEST" = "false" ]; then
    echo 'RELEASE'
    # encrypted using `travis encrypt-file`
    openssl aes-256-cbc -K $encrypted_8bb023dc1fa3_key -iv $encrypted_8bb023dc1fa3_iv -in gradle/data.tar.enc -out gradle/data.tar -d

    tar -xvf gradle/data.tar -C gradle/
    mv gradle/keys.xml sdk-android-test/src/main/res/values/

    # forget closing and promote it fails half of the time
    ./gradlew -S clean sdk-android-lib:build sdk-android-test:connectedAndroidTest sdk-android-lib:uploadArchives
else
    echo 'BUILDING MR'
    ./gradlew -S clean sdk-android-lib:build sdk-android-test:connectedAndroidTest
fi