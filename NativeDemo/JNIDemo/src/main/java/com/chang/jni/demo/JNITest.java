package com.chang.jni.demo;

/**
 * 简单 get、set native 方法和 C/C++ 交互。
 * <p>
 * Created by Nicholas Sean on 2021/10/25 7:00 PM.
 *
 * @version 1.0
 */
public class JNITest {

    static {
        String projectPath = System.getProperty("user.dir");
        String soLibPath = projectPath + "/JNIDemo/src/main/jni/";
        // load 加载方式
        System.load(soLibPath + "libjni-test.so");

        // loadLibrary 加载方式（在mac上有问题，UnsatisfiedLinkError: no jni-test in java.library.path）
//        System.setProperty("java.library.path", soLibPath);
//        System.loadLibrary("jni-test");
    }

    public static void main(String[] args) {
        String soProperty = System.getProperty("java.library.path");
        System.out.println(soProperty);
        JNITest jniTest = new JNITest();
        System.out.println(jniTest.get());
        jniTest.set("Hello, I am from java.");
    }

    /*
    java 调用 JNI
     */
    public native String get();
    public native void set(String str);

    /*
    JNI 调用 Java 静态方法
     */
    public static void staticMethodCalledByJni(String msgFromJni) {
        System.out.println("staticMethodCalledByJni, msg: " + msgFromJni);
    }
    /*
    JNI 调用 Java 方法
     */
    public void methodCalledByJni(String msgFromJni) {
        System.out.println("methodCalledByJni, msg: " + msgFromJni);
    }
}
