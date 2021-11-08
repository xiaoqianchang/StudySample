//
// Created by 肖昌 on 2021/11/2.
//

#include <jni.h>
#include <string>
#include <android/log.h>
#include <thread>
#include <iostream>

// Android log function wrappers
#define LOG_TAG "jni-callback-test"
//static const char* LOG_TAG = "jni-callback-test" // 定义 TAG 的另一种方式
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGA(...) __android_log_print(ANDROID_LOG_FATAL, LOG_TAG, __VA_ARGS__)

/*
 * JNI 回调java方法.
 */

/*
 * 1. 直接调用java方法回传值
 */
// FindClass 实现，根据 java 类名找到类，可以是调用该方法的类也可以是其它类，更灵活。
extern "C"
JNIEXPORT void JNICALL
Java_com_chang_android_ndk_JniCallbackTest_nativeDownload1(JNIEnv *env, jobject thiz, jstring url) {
    LOGI("invoke nativeDownload in C++");
    // 1. 直接用findClass找到Class, 也就是JniCallbackTest.class.
    jclass clazz = env->FindClass("com/chang/android/ndk/JniCallbackTest");
    if (clazz == 0) {
        LOGE("Unable to find class");
        return;
    }
    // 2. 根据方法名、方法签名找到需要调用的方法ID
    jmethodID callbackMethodId = env->GetMethodID(clazz, "onDownloadProgress1", "(JJ)I");
    if (callbackMethodId == NULL) {
        LOGE("find method onDownloadProgress error!");
        return;
    }

    // 3. 这时候要回调还没有jobject，那就根据clazz new一个
    jmethodID constructorMethodId = env->GetMethodID(clazz, "<init>", "()V");
    jobject jniCallbackTest = env->NewObject(clazz, constructorMethodId);

    // 4. 通过 JNIEnv 对象的 CallIntMethod 方法来完成最终的调用过程，ret是java层的返回值（这个有些场景很好用）
    jint ret = env->CallIntMethod(jniCallbackTest, callbackMethodId, 50l, 100l);

    return;
}
// 1. 直接调用java方法回传值
// GetObjectClass 实现，只能是调用该方法的类。
//extern "C"
//JNIEXPORT void JNICALL
//Java_com_chang_android_ndk_JniCallbackTest_nativeDownload1(JNIEnv *env, jobject thiz) {
//    LOGI("invoke nativeDownload in C++");
//    // 1. 直接用GetObjectClass找到Class, 也就是JniCallbackTest.class.
//    jclass clazz = env->GetObjectClass(thiz);
//    if (clazz == 0) {
//        LOGE("Unable to find class");
//        return;
//    }
//    // 2. 根据方法名、方法签名找到需要调用的方法ID
//    jmethodID callbackMethodId = env->GetMethodID(clazz, "onDownloadProgress1", "(JJ)I");
//    if (callbackMethodId == NULL) {
//        LOGE("find method onDownloadProgress error!");
//        return;
//    }
//    // 3. 通过 JNIEnv 对象的 CallIntMethod 方法来完成最终的调用过程，ret是java层的返回值（这个有些场景很好用）
//    jint ret = env->CallIntMethod(thiz, callbackMethodId, 50l, 100l);
//
//    return;
//}

/*
 * 2. 在其他线程里面回调到java层，通过NewGlobalRef，保存全局变量;
 */
JavaVM *g_VM;
jobject g_obj;
jint mNeedDetach;

// 函数指针：子线程执行下载，并回调到java层，这时候java层就不会阻塞，只是在等待回调
void download2(const char *uid, const char *url) { // 此处两个参数是引用传递
    JNIEnv *env;
    mNeedDetach = JNI_FALSE;

    LOGI( "子线程id为：%s", std::this_thread::get_id);

    // 获取当前native线程是否被附加到jvm环境中
    int getEnvStat = g_VM->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (getEnvStat == JNI_EDETACHED) {
        // 如果没有，主动附加到jvm环境中，获取到env
        if (g_VM->AttachCurrentThread(&env, NULL) != 0) {
            // 当前线程附加到jvm环境失败
            return;
        }
        mNeedDetach = JNI_TRUE;
    }

    // 通过全局变量g_obj获取到要回调的类
    jclass javaClass = env->GetObjectClass(g_obj);
    if (javaClass == 0) {
        LOGE("Unable to find class");
        g_VM->DetachCurrentThread();
        return;
    }

    // 获取要回调的方法ID
    jmethodID  javaCallbackId = env->GetMethodID(javaClass, "onDownloadProgress2", "(Ljava/lang/String;JJ)I");
    if (javaCallbackId == NULL) {
        LOGE("Unable to find method onDownloadProgress2");
        return;
    }

    LOGI("uid=%s, url=%s", uid, url);
    // 执行回调
    long progress = 0;
    long total = 100;
    int diff = 5;
    while (progress < total) {
        progress += diff;
        if (progress > 100) {
            env->CallIntMethod(g_obj, javaCallbackId, env->NewStringUTF(uid), total, 100l);
        } else {
            env->CallIntMethod(g_obj, javaCallbackId, env->NewStringUTF(uid), total, progress);
        }
        std::this_thread::sleep_for(std::chrono::seconds(1));
    }

    // 释放当前线程
    if (mNeedDetach) {
        g_VM->DetachCurrentThread();
    }
    env = NULL;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_chang_android_ndk_JniCallbackTest_nativeDownload2(JNIEnv *env, jobject thiz, jstring uid,
                                                           jstring url) {
    const char* uidC = NULL;
    const char* urlC = NULL;

    std::cout << "主线程的线程id为：" << std::this_thread::get_id << std::endl;
    LOGI( "主线程id为：%s", std::this_thread::get_id);

    // JavaVM是虚拟机在JNI中的表示，等下再其他线程回调java层需要用到
    env->GetJavaVM(&g_VM);
    // 生成一个全局引用保留下来，以便回调
    g_obj = env->NewGlobalRef(thiz);

    LOGI("jni接收jstring类型入参的值：uid=%s, url=%s \n", uid, url);

    // 1. 将传入的字符串转换为native Strings
    uidC = env->GetStringUTFChars(uid, JNI_FALSE);
    urlC = env->GetStringUTFChars(url, JNI_FALSE);

    if (uidC == NULL) { // uidC == NULL意味着JVM为native String分配内存失败
    }

    LOGI("将jstring类型入参的值转为native Strings：uid=%s, url=%s \n", uidC, urlC);

    // 此处使用c++语言开启一个线程执行
    std::thread downloadThread(download2, uidC, urlC);
    downloadThread.join();
}

/*
 * 3. 通过把接口jobject传递到c层下面去，然后在c层里面进行回调;
 */
void download3(const char *url, void *p) { // 此处两个参数是值传递
    if (p == NULL) {
        return;
    }

    JNIEnv *env;
    mNeedDetach = JNI_FALSE;

    // 获取当前native线程是否被附加到jvm环境中
    int getEnvStat = g_VM->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (getEnvStat == JNI_EDETACHED) {
        // 如果没有，主动附加到jvm环境中，获取到env
        if (g_VM->AttachCurrentThread(&env, NULL) != 0) {
            // 当前线程附加到jvm环境失败
            return;
        }
        mNeedDetach = JNI_TRUE;
    }

    // 强转回 callback
    jobject callback = (jobject)p;

    // 通过全局变量g_obj获取到要回调的类
    jclass javaClass = env->GetObjectClass(callback);
    if (javaClass == 0) {
        LOGE("Unable to find class");
        g_VM->DetachCurrentThread();
        return;
    }

    // 获取要回调的方法ID
    jmethodID  javaCallbackId = env->GetMethodID(javaClass, "onProgress", "(JJ)I");
    if (javaCallbackId == NULL) {
        LOGE("Unable to find method onDownloadProgress2");
        return;
    }

    // 执行回调
    int progress = 0;
    int total = 100;
    int diff = 5;
    while (progress < total) {
        progress += diff;
        if (progress > 100) {
            env->CallIntMethod(callback, javaCallbackId, total, 100);
        } else {
            env->CallIntMethod(callback, javaCallbackId, total, progress);
        }
        std::this_thread::sleep_for(std::chrono::seconds(1));
    }

    // 释放当前线程
    if (mNeedDetach) {
        g_VM->DetachCurrentThread();
    }
    env = NULL;

    // 释放全局引用的接口，生命周期自己把控
//    env->DeleteGlobalRef(callback);
//    callback = NULL;
}

// java 调用的方法里面的局部变量，在方法调用完毕，就会被 jvm 回收。那么调用到 jni 层的 native 方法也是一样！
// 也就是 jobject callback 这个局部变量在方法调用完毕就会被回收！即使你用全局变量保存依然一样！所以必须加个 NewGlobalRef 。
extern "C"
JNIEXPORT jint JNICALL
Java_com_chang_android_ndk_JniCallbackTest_nativeDownload3(JNIEnv *env, jobject thiz, jstring url,
                                                           jobject callback) {
    const char* urlC = NULL;

    LOGI( "主线程id为：%s", std::this_thread::get_id);

    // JavaVM是虚拟机在JNI中的表示，等下再其他线程回调java层需要用到
    env->GetJavaVM(&g_VM);

    LOGI("jni接收入参的值：url=%s，callback=%s \n", url, callback);

    // 1. 将传入的字符串转换为native Strings
    urlC = env->GetStringUTFChars(url, JNI_FALSE);

    // 生成一个全局引用，回调的时候findclass才不会为null
    jobject jcallbcak = env->NewGlobalRef(callback);

    // 把接口传进去，或者保存在一个结构体里面的属性， 进行传递也可以
    std::thread downloadThread(download3, urlC, jcallbcak);
    downloadThread.join();
    return 1;
}

/*
 * JNI 回调java静态方法.
 */
// 直接调用java静态方法回传值
extern "C"
JNIEXPORT void JNICALL
Java_com_chang_android_ndk_JniCallbackTest_nativeInstall1(JNIEnv *env, jobject thiz) {
    LOGI("invoke nativeInstall in C++");
    jclass clazz = env->FindClass("com/chang/android/ndk/JniCallbackTest");
    if (clazz == NULL) {
        LOGE("Unable to find class");
        return;
    }
    jmethodID callbackMethodId = env->GetStaticMethodID(clazz, "onInstallProgress1", "(JJ)I");
    if (callbackMethodId == NULL) {
        LOGE("find method onInstallProgress error!");
        return;
    }
    jint ret = env->CallStaticIntMethod(clazz, callbackMethodId, 50l, 100l);

    return;
}