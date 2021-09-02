package com.chang.android.tboxnet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.chang.android.tboxnet.databinding.ActivityMainBinding
import com.chang.android.tboxnet.socket.SocketHelper
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    fun onTestTBox(view: View) {
        val host = mBinding.edtHostInput.text.toString().trim()
        val port = mBinding.edtPortInput.text.toString().trim()
        if (TextUtils.isEmpty(host)) {
            showToast("请输入正确的IP")
            return
        }
        if (TextUtils.isEmpty(port)) {
            showToast("请输入正确的PORT")
            return
        }
        SocketHelper.setLogListener {
            mBinding.edtLog.setText(it)
        }
        thread(name = "tbox") {
            SocketHelper.connect(host, port.toInt())
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}