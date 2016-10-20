#!/bin/bash
if [ "$TRAVIS_PULL_REQUEST" = "false" ]; then
    echo 'RELEASE'
    # encrypted using `travis encrypt-file`
    openssl aes-256-cbc -K $encrypted_8bb023dc1fa3_key -iv $encrypted_8bb023dc1fa3_iv -in gradle/mnubo.secring.gpg.enc -out gradle/mnubo.secring.gpg -d
    ./gradlew -S sdk-android-lib:clean sdk-android-lib:build sdk-android-lib:uploadArchives sdk-android-lib:closeRepository sdk-android-lib:promoteRepository
else
    echo 'BUILDING MR'
    ./gradlew -S sdk-android-lib:clean sdk-android-lib:build
fi