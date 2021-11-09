package com.chang.android.ndk;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * JNI 回调（c调java）的方法.
 * 1. java中定义方法，在当前函数（同一个线程）里面回调，直接用findClass或者GetObjectClass，进行回调;
 * 2. 在其他线程里面回调到java层，通过NewGlobalRef，保存全局变量;
 * 3. 通过把接口jobject传递到c层下面去，然后在c层里面进行回调;
 * 参考： https://www.jianshu.com/p/e576c7e1c403
 *
 * 注意：
 * 1. Java和c交互时，native方法的调用建议放到子线程，因为当native方法比较耗时时会体现在java这边的主线程里。
 * c里面没有主线程的概念，即使c里面是在线程里面处理逻辑，在有callback给java时，还是提现在主线程。
 * 2. c调用java定义的方法，不管是否为静态，方法可以定义为 private ；
 * <p>
 * Created by Nicholas Sean on 2021/11/2 8:13 下午.
 *
 * @version 1.0
 */
public class JniCallbackTest {

    private static final String TAG = "JniCallbackTest";

    /********************* JNI 回调 start **************************/
    /*
    方式一: 直接调用
     */
    // 触发下载（java调c）
    public native void nativeDownload1(String url);

    // 进度回调（c调java）
    private int onDownloadProgress1(long total, long progress) {
        Log.d(TAG, "onDownloadProgress1: total: " + total + ", progress: " + progress);
        return 1;
    }

    /*
    方式二: 子线程调用
     */
    public native void nativeDownload2(String uid, String url);
    private int onDownloadProgress2(String uid, long total, long progress) {
        OnProgressListener listener = mProgressListeners.get(uid);
        if (listener != null) {
            listener.onProgress(uid, total, progress);
            if (progress >= total) {
                mProgressListeners.remove(uid);
            }
        }
        return 1;
    }

    public interface OnProgressListener {
        int onProgress(String uid, long total, long progress);
    }
    private Map<String, OnProgressListener> mProgressListeners = new HashMap();
    // 下载单个任务
    public void download(String url) {
        download(UUID.randomUUID().toString().replace("-", ""), url, null);
    }
    // 同时下载多个任务，并回调每个任务的进度
    public void download(String uid, String url, OnProgressListener listener) {
        mProgressListeners.put(uid, listener);
        nativeDownload2(uid, url);
    }

    /*
    方式三: 传递回调进入c
     */
    public interface OnProgressCallback {
        int onProgress(long total, long progress);
    }
    public native int nativeDownload3(String url, OnProgressCallback callback);
    /*********************** JNI 回调 end ************************/

    /*********************** JNI 回调静态方法 start ************************/
    // 触发安装（java调c）
    public native void nativeInstall1();

    // 进度回调（c调java静态方法）
    private static int onInstallProgress1(long total, long progress) {
        Log.d(TAG, "onInstallProgress1: total: " + total + ", progress: " + progress);
        return 1;
    }
    /*********************** JNI 回调静态方法 end ************************/
}
