#!/bin/bash
if [ "$TRAVIS_PULL_REQUEST" = "false" ]; then
    echo 'RELEASE'
    openssl aes-256-cbc -K $encrypted_f3cf6850dbb9_key -iv $encrypted_f3cf6850dbb9_iv -in gradle/mnubo.secring.gpg.enc -out gradle/mnubo.secring.gpg -d
    ./gradlew -S sdk-android-lib:clean sdk-android-lib:build sdk-android-lib:uploadArchives sdk-android-lib:closeRepository sdk-android-lib:promoteRepository
else
    echo 'BUILDING MR'
    ./gradlew -S sdk-android-lib:clean sdk-android-lib:build
fi