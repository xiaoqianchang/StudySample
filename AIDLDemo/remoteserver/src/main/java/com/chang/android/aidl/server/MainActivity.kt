package com.chang.android.aidl.server

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onOpenRemoteServer(view: View) {
        Log.d(TAG, "点击开启远程服务")
        val intent = Intent(this, RemoteService::class.java)
        intent.setPackage(packageName) // 此处的包名应该为远程服务所在的应用程序包的名称
        startService(intent)
    }
}