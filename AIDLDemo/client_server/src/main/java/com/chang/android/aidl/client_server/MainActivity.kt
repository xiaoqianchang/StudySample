package com.chang.android.aidl.client_server

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.chang.android.aidl.service.IDataCallback
import com.chang.android.aidl.service.Person

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * RemoteService 在 包名+remote 进程
     */
    fun onBindRemoteServe(view: View) {
        Log.d(TAG, "开始绑定远程服务")
        ClientManager.getInstance().init(this)
    }

    fun onAddPerson(view: View) {
        val person = Person(
            "张三",
            20
        )
        ClientManager.getInstance().onAddPerson(person)
        Toast.makeText(this, "添加person $person 成功", Toast.LENGTH_SHORT).show()
    }

    fun onRemovePerson(view: View) {
        ClientManager.getInstance().removePerson(0)
    }

    fun onUpdatePerson(view: View) {
        val person = Person(
            "张三",
            22
        )
        ClientManager.getInstance().updatePerson(person)
        Toast.makeText(this, "更新person $person 成功", Toast.LENGTH_SHORT).show()
    }

    fun onQueryAllPerson(view: View) {
        val queryAll = ClientManager.getInstance().queryAll()
        Log.d(TAG, "所有person : ${queryAll?.toString()}")
        Toast.makeText(this, "所有person : ${queryAll?.toString()}", Toast.LENGTH_SHORT).show()
    }

    fun onAsyncLoadAllPerson(view: View) {
        ClientManager.getInstance().loadAll(object : IDataCallback.Stub() {
            override fun onDataReady(list: MutableList<Person>?) {
                Log.d(TAG, "异步获取所有person成功 : ${list?.toString()}")
                Toast.makeText(this@MainActivity, "异步获取所有person成功 : ${list.toString()}", Toast.LENGTH_SHORT).show()
            }

            override fun onError(code: Int, message: String?) {
                Log.d(TAG, "异步获取所有person失败 : code=$code, message=$message")
                Toast.makeText(this@MainActivity, "异步获取所有person失败 : code=$code, message=$message\"", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        ClientManager.release()
    }
}