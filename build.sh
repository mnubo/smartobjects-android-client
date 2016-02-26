#!/bin/bash
if [ "$TRAVIS_PULL_REQUEST" = "false" ]; then
    openssl aes-256-cbc -K $encrypted_f3cf6850dbb9_key -iv $encrypted_f3cf6850dbb9_iv -in gradle/mnubo.secring.gpg.enc -out gradle/mnubo.secring.gpg -d
    ./gradlew -S sdk-android-lib:clean sdk-android-lib:build sdk-android-lib:uploadArchives sdk-android-lib:closeRepository sdk-android-lib:promoteRepository
else
    ./gradlew -S sdk-android-lib:clean sdk-android-lib:build
fi