package com.chang.android.ndk.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.chang.android.jna.usage.JnaConstants
import com.chang.android.jna.usage.JnaUsageTest
import com.chang.android.ndk.sample.databinding.ActivityJnaDemoBinding

/**
 * JNADemo module 调用入口.
 * <p>
 * Created by Nicholas Sean on 2021/12/6 10:03 上午.
 *
 * @version 1.0
 */
class JnaDemoActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "JnaDemoActivity"

        private lateinit var mBinding: ActivityJnaDemoBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_jna_demo)
        init()
    }

    private fun init() {
        JnaConstants.isAppUse = true
    }

    fun onGetCompanyClick(view: View) {
        JnaUsageTest.getCompany()
    }
}