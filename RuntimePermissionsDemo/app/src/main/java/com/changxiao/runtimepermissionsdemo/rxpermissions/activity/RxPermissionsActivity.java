package com.changxiao.runtimepermissionsdemo.rxpermissions.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.changxiao.runtimepermissionsdemo.R;
import com.tbruyelle.rxpermissions2.RxPermissions;
import permissions.dispatcher.NeedsPermission;

/**
 * RxPermissions 使用方式
 *
 * 参考：
 * https://github.com/tbruyelle/RxPermissions
 *
 * Created by Chang.Xiao on 2018/12/13.
 * @version 1.0
 */
public class RxPermissionsActivity extends Activity implements OnClickListener {

  private Button mBtnCallPhone;
  private TextView mTvTelNum;
  private Button mBtnCamera;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rx_permissions);

    initView();
  }

  private void initView() {
    mBtnCallPhone = findViewById(R.id.btn_call_phone);
    mBtnCallPhone.setOnClickListener(this);
    mTvTelNum = findViewById(R.id.tv_telNum);

    mBtnCamera = findViewById(R.id.btn_camera);
    mBtnCamera.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    final RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity or Fragment instance
    switch (v.getId()) {
      case R.id.btn_call_phone:
        // Must be done during an initialization phase like onCreate
        rxPermissions
            .request(Manifest.permission.CALL_PHONE)
            .subscribe(granted -> {
              if (granted) { // Always true pre-M
                // I can control the camera now
                Toast.makeText(this,"已授予",Toast.LENGTH_SHORT).show();
                callPhone();
              } else {
                // Oups permission denied
                Toast.makeText(this,"权限未授予，功能无法使用",Toast.LENGTH_SHORT).show();
              }
            });
        break;
      case R.id.btn_camera:
        // Must be done during an initialization phase like onCreate
        rxPermissions
            .request(Manifest.permission.CAMERA)
            .subscribe(granted -> {
              if (granted) { // Always true pre-M
                // I can control the camera now
                Toast.makeText(this,"已授予",Toast.LENGTH_SHORT).show();
                callPhone();
              } else {
                // Oups permission denied
                Toast.makeText(this,"权限未授予，功能无法使用",Toast.LENGTH_SHORT).show();
              }
            });
        break;
    }
  }

  /**
   * 需要权限的方法
   */
  private void callPhone(){
    String telNum = mTvTelNum.getText().toString().trim();
    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+telNum));
    try {
      startActivity(intent);
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
