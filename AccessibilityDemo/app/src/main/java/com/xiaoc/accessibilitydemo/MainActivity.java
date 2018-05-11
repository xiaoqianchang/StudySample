package com.xiaoc.accessibilitydemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 安装、卸载、强行停止动作的发起。
 * 
 * Created by Chang.Xiao on 2018/5/8.
 *
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String LOCAL_PACKAGE_NAME = "com.xiaoc.accessibilitydemo";
    private final String SERVICE_NAME = "com.xiaoc.accessibilitydemo.MyAccessibilityService";

    // 操作目标
    private final String TARGET_APP_NAME = "app-debug.apk";
    private final String TARGET_PACKAGE_NAME = "com.changxiao.draggablecircledemo";

    private Button activeBtn;
    private Button installBnt;
    private Button unInstallBnt;
    private Button killAppBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
        AccessibilityUtils.getInstance().init(this, LOCAL_PACKAGE_NAME, SERVICE_NAME);
        grantedPermission();
//        if (!AccessibilityUtils.getInstance().isAccessibilitySettingsOn()) {
//            AccessibilityUtils.getInstance().popOpenAlertDialog(this);
//        }
    }

    private void initView() {
        activeBtn = findViewById(R.id.active_btn);
        installBnt = findViewById(R.id.install_btn);
        unInstallBnt = findViewById(R.id.uninstall_btn);
        killAppBtn = findViewById(R.id.kill_app_btn);
    }

    private void initListener() {
        activeBtn.setOnClickListener(this);
        installBnt.setOnClickListener(this);
        unInstallBnt.setOnClickListener(this);
        killAppBtn.setOnClickListener(this);
    }

    private void grantedPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.active_btn:
                if (AccessibilityUtils.getInstance().isAccessibilitySettingsOn()) {
                    Toast.makeText(this, "辅助服务已打开", Toast.LENGTH_SHORT).show();
                } else {
                    // 前往辅助服务设置页面
                    Intent startIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(startIntent);
                }
                break;
            case R.id.install_btn:
                installApk();
                break;
            case R.id.uninstall_btn:
                AccessibilityUtils.getInstance().uninstallApp(TARGET_PACKAGE_NAME);
                break;
            case R.id.kill_app_btn:
                AccessibilityUtils.getInstance().killApp(TARGET_PACKAGE_NAME);
                break;
        }
    }

    private void installApk() {
        String outFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "Download/" + TARGET_APP_NAME;
        File installFile = new File(outFilePath);
        if(installFile.exists()){
            installFile.delete();
        }
        try {
            installFile.createNewFile();
            FileOutputStream out = new FileOutputStream(installFile);
            byte[] buffer = new byte[1024];
            InputStream in = MainActivity.this.getAssets().open(TARGET_APP_NAME);
            int count;
            while((count= in.read(buffer))!=-1){
                out.write(buffer, 0, count);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AccessibilityUtils.getInstance().inStallApp(outFilePath, TARGET_PACKAGE_NAME);
    }
}
