#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "System.out"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

/*
 * 简单的java调用c++
 */
// java调用c++获取字符串
extern "C" JNIEXPORT jstring JNICALL
Java_com_chang_android_ndk_JNITest_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

// java调用c++获取打印日志
extern "C"
JNIEXPORT void JNICALL
Java_com_chang_android_ndk_JNITest_printLog(JNIEnv *env, jobject thiz) {
    LOGI("I am from jni.");
}