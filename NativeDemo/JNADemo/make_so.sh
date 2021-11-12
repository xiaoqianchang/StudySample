#!/usr/bin/env bash

cd src/main/cpp

# 编译输出 libJNADemo.so
gcc -shared -I /Library/Java/JavaVirtualMachines/jdk1.8.0_221.jdk/Contents/Home/include -I /Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers -fPIC jna-usage.c -o libJNADemo.so

# 移动动态库到 libs/armeabi-v7a 目录 (覆盖移动)
mv libJNADemo.so ../jniLibs/armeabi-v7a