package com.chang.android.ndk;

/**
 * 简单 get、set native 方法和 C/C++ 交互。
 * <p>
 * Created by Nicholas Sean on 2021/10/25 7:00 PM.
 *
 * @version 1.0
 */
public class JNITest {

    static {
        System.loadLibrary("native-lib");
    }

    /*
    java 调用 c，下面两个方法由native-lib.cpp实现
     */
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    /**
     * Print log from JNI.
     */
    public native void printLog();
    /*
    java 调用 c，下面两个方法由jni-test.cpp实现
     */
    public native String get();
    public native void set(String str);
}
