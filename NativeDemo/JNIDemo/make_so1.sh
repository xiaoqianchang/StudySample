#!/usr/bin/env bash
# 先进入源码目录
cd src/main/java
# 编译 java 文件
javac com/chang/jni/demo/JNITest.java
# 生成 JNI 头文件（在 JNIDemo/src/main/java/ 目录）
javah com.chang.jni.demo.JNITest

# 移动头文件到 jni 目录 (覆盖移动)
mv com_chang_jni_demo_JNITest.h ../jni

cd ../jni

# 编译输出 libjni-test.so
gcc -shared -I /Library/Java/JavaVirtualMachines/jdk1.8.0_221.jdk/Contents/Home/include -I /Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers -fPIC test.cpp -o libjni-test.so