#!/bin/bash

flutter build apk --split-per-abi
cd ./build/app/outputs/flutter-apk
mv app-arm64-v8a-release.apk karamuc.apk
mv app-armeabi-v7a-release.apk karamuc-armeabi-v7a.apk
mv app-x86_64-release.apk karamuc-x86_64.apk
