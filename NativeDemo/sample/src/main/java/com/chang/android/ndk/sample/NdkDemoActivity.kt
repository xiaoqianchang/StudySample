package com.chang.android.ndk.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.chang.android.ndk.JNITest
import com.chang.android.ndk.JniCallbackTest
import com.chang.android.ndk.sample.databinding.ActivityNdkDemoBinding
import java.util.*
import kotlin.concurrent.thread

/**
 * NDKDemo module 调用入口.
 * <p>
 * Created by Nicholas Sean on 2021/11/2 7:15 下午.
 *
 * @version 1.0
 */
class NdkDemoActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "NdkDemoActivity"

        private lateinit var mBinding: ActivityNdkDemoBinding
        private lateinit var mJNITest: JNITest
        private lateinit var mJniCallbackTest: JniCallbackTest
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ndk_demo)
        init()
    }

    private fun init() {
        mJNITest = JNITest()
        handleInit()
        mJniCallbackTest = JniCallbackTest()
    }

    private fun handleInit() {
        Log.d(TAG, mJNITest.get())
        mJNITest.set("Hello, I am from java.")
    }

    fun onJniCallback1(view: View) {
        mJniCallbackTest.nativeDownload1("")
    }

    fun onJniCallback2(view: View) {
        thread(name = "jni-download2") {
            mJniCallbackTest.download(UUID.randomUUID().toString().replace("-", ""), "www.baidu.com") {
                    uid, total, progress -> Log.d(TAG, "onJniCallback2: uid: $uid, total: $total, progress: $progress") }
        }
    }

    fun onJniCallback3(view: View) {
        thread(name = "jni-download3") {
            mJniCallbackTest.nativeDownload3("www.baidu.com") { total, progress ->
                Log.d(TAG, "onJniCallback3: total: $total, progress: $progress")
                1
            }
        }
    }

    fun onJniStaticCallback(view: View) {
        mJniCallbackTest.nativeInstall1()
    }
}