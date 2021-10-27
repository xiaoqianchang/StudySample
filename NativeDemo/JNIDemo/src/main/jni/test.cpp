//
// Created by 肖昌 on 2021/10/26.
//
// test.cpp
#include "com_chang_jni_demo_JNITest.h"
#include <stdio.h>

// jni 调用 java
void callJavaMethod(JNIEnv *env, jobject thiz) {
    // 1. 根据 java 类名找到类
    jclass clazz = env->FindClass("com/chang/jni/demo/JNITest");
    if (clazz == NULL) {
        printf("find class JNITest error!");
        return;
    }
    // 2. 根据方法名、方法签名找到方法
    jmethodID id = env->GetStaticMethodID(clazz, "methodCalledByJni", "(Ljava/lang/String;)V");
    if (id == NULL) {
        printf("find method methodCalledByJni error!");
    }
    // 3. 通过 JNIEnv 对象的 CallStaticVoidMethod 方法来完成最终的调用过程
    jstring msg = env->NewStringUTF("msg send by callJavaMethod in test.cpp.");
    env->CallStaticVoidMethod(clazz, id, msg);
}

/**
java 调用 jni
*/
JNIEXPORT jstring JNICALL Java_com_chang_jni_demo_JNITest_get
        (JNIEnv *env, jobject thiz) {
    printf("invoke get in c++\n");
    return env->NewStringUTF("Hello from JNI !");
}

JNIEXPORT void JNICALL Java_com_chang_jni_demo_JNITest_set
(JNIEnv *env, jobject thiz, jstring string) {
    printf("invoke set from c++\n");
    char* str = (char*)env->GetStringUTFChars(string,NULL);
    printf("%s\n", str);
    // 测试 jni 调用 java
    callJavaMethod(env, thiz);
    env->ReleaseStringUTFChars(string, str);
}
