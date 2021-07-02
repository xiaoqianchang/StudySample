package com.chang.android.aidl.client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import com.chang.android.aidl.service.IRemoteService
import com.chang.android.aidl.service.Person

class MainActivity : AppCompatActivity() {

    private var mRemoteService: IRemoteService? = null

    companion object {
        private const val TAG = "MainActivity";
    }

    private val mConn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected()")
            mRemoteService = IRemoteService.Stub.asInterface(service)
            Toast.makeText(this@MainActivity, "绑定远程服务成功", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected()")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * service 所在的应用程序已安装：
     * 1. 如果 service 所在的进程未启动，去 startService 将会报出
     * java.lang.IllegalStateException: Not allowed to start service Intent { act=com.chang.android.aidl.server.IRemoteService pkg=com.chang.android.aidl.server }: app is in background uid null
     * 2. 如果 service 所在的进程已启动，去 startService 是可以正常启动的。
     * 3. 直接 bindService 启动并绑定服务。
     */
    fun onBindRemoteServe(view: View) {
        Log.d(TAG, "开始绑定远程服务")
        val intent = Intent("com.chang.android.aidl.server.IRemoteService")
        intent.setPackage("com.chang.android.aidl.server") // 此处的包名应该为远程服务所在的应用程序包名称
//        startService(intent)
        bindService(intent, mConn, Context.BIND_AUTO_CREATE)
    }

    fun onAddPerson(view: View) {
        val person = Person(
            "张三",
            20
        )
        mRemoteService?.addPerson(person)
        Toast.makeText(this, "添加person $person 成功", Toast.LENGTH_SHORT).show()
    }

    fun onRemovePerson(view: View) {
        mRemoteService?.removePerson(0)
    }

    fun onUpdatePerson(view: View) {
        val person = Person(
            "张三",
            22
        )
        mRemoteService?.updatePerson(person)
        Toast.makeText(this, "更新person $person 成功", Toast.LENGTH_SHORT).show()
    }

    fun onQueryAllPerson(view: View) {
        val queryAll = mRemoteService?.queryAll()
        Log.d(TAG, "所有person : ${queryAll.toString()}")
        Toast.makeText(this, "所有person : ${queryAll.toString()}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mConn)
    }
}