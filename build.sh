#!/bin/bash

if [ "$TRAVIS_PULL_REQUEST" = "false" ]; then
    echo 'RELEASE'
    # encrypted using `travis encrypt-file`
    # encrypted variables can only be used for build on upstream, not forks
    openssl aes-256-cbc -K $encrypted_8bb023dc1fa3_key -iv $encrypted_8bb023dc1fa3_iv -in gradle/keys.xml.enc -out sdk-android-test/src/main/res/values/keys.xml -d
    openssl aes-256-cbc -K $encrypted_8bb023dc1fa3_key -iv $encrypted_8bb023dc1fa3_iv -in gradle/mnubo.secring.gpg.enc -out gradle/mnubo.secring.gpg -d
    ./gradlew -S clean sdk-android-lib:build sdk-android-test:connectedAndroidTest sdk-android-lib:uploadArchives sdk-android-lib:closeRepository sdk-android-lib:promoteRepository
else
    echo 'BUILDING MR'
    echo 'INTEGRATION TESTS ARE NOT RAN'
    ./gradlew -S clean sdk-android-lib:build
fi