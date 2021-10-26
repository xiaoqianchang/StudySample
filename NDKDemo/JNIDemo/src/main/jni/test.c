//
// Created by 肖昌 on 2021/10/26.
//
// test.c
#include "com_chang_jni_demo_JNITest.h"
#include <stdio.h>

JNIEXPORT jstring JNICALL Java_com_chang_jni_demo_JNITest_get
        (JNIEnv *env, jobject thiz) {
    printf("invoke get in c\n");
    return (*env)->NewStringUTF(env, "Hello from JNI !");
}

JNIEXPORT void JNICALL Java_com_chang_jni_demo_JNITest_set
(JNIEnv *env, jobject thiz, jstring string) {
    printf("invoke set from c\n");
    char* str = (char*)(*env)->GetStringUTFChars(env,string,NULL);
    printf("%s\n", str);
    (*env)->ReleaseStringUTFChars(env, string, str);
}
