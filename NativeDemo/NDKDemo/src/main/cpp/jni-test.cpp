//
// Created by 肖昌 on 2021/10/28.
//

/*
 * 知识点：
 * 1. 如果native方法为对象方法（非静态），生成的JNI函数参数为（JNIEnv *env, jobject thiz）；
 * 如果native方法为类方法（静态），生成的JNI函数参数为（JNIEnv *env, jclass clazz）；
 */

#include <jni.h>
#include <string>
// 头文件位置: {sdk-path}/ndk-bundle/sysroot/usr/include/android/
#include <android/log.h>

// android 日志
// 定义输出的TAG
#define LOG_TAG "jni-test"
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGA(...) __android_log_print(ANDROID_LOG_FATAL, LOG_TAG, __VA_ARGS__)

/*
 * 简单的java调用c++
 */
// java调用c++获取字符串
extern "C"
JNIEXPORT jstring JNICALL
Java_com_chang_android_ndk_JNITest_get(JNIEnv *env, jobject thiz) {
    LOGI("invoke get in C++\n");
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

// java调用c++赋值
extern "C"
JNIEXPORT void JNICALL
Java_com_chang_android_ndk_JNITest_set(JNIEnv *env, jobject thiz, jstring str) {
    LOGI("invoke set from C++\n");
    char* string = (char*)env->GetStringUTFChars(str, NULL);
    LOGI("%s\n", string);
    env->ReleaseStringUTFChars(str, string);
}