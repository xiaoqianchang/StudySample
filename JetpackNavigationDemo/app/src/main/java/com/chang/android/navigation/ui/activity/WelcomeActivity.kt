package com.chang.android.navigation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chang.android.jetpacknavigationdemo.R

/**
 * Description: 启动页
 * <p>
 * Created by Chang.Xiao on 2021/3/11 4:44 PM.
 * 
 * @version 1.0
 */
class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }
}