package com.chang.android.aidl.server

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
        startService(RemoteService.getIntent(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(RemoteService.getIntent(this))
    }
}