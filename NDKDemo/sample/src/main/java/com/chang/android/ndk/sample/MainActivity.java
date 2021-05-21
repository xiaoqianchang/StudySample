package com.chang.android.ndk.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.chang.android.jna.demo.JnaHmiTest;
import com.planet.light2345.seclib.SecService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String string = SecService.encryptString(this, "xiaochang");
//        String message = "调用so加密后的结果：" + string;
//        Log.e(TAG, message);
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // 加载三方动态库
        try {
            System.loadLibrary("sysi4hmi");
            Toast.makeText(this, "加载成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "加载成功");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "加载失败", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "加载失败");
        }

        new JnaHmiTest().testHmiMethod();
    }
}