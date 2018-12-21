package com.changxiao.runtimepermissionsdemo.rxpermissions.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.changxiao.runtimepermissionsdemo.R;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observable;
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

  private final String TAG = "RxPermissionsActivity";

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
        // 拒绝并且不再询问没有回调
        // Must be done during an initialization phase like onCreate
//        rxPermissions
//            .request(Manifest.permission.CALL_PHONE)
//            .subscribe(granted -> {
//              if (granted) { // Always true pre-M
//                // I can control the camera now
//                Toast.makeText(this,"已授予",Toast.LENGTH_SHORT).show();
//                callPhone();
//              } else {
//                // Oups permission denied
//                Toast.makeText(this,"权限未授予，功能无法使用",Toast.LENGTH_SHORT).show();
//              }
//            });

//        Observable.just(new Object())
//            .compose(rxPermissions.ensureEach(Manifest.permission.CALL_PHONE))
//            .subscribe(permission -> {
//              if (permission.granted) {
//                // `permission.name` is granted !
//                Toast.makeText(this,"已授予",Toast.LENGTH_SHORT).show();
//              } else if (permission.shouldShowRequestPermissionRationale) {
//                // Denied permission without ask never again（拒绝且没勾选不再询问的回调）
//                Toast.makeText(this,"权限未授予，without ask never again", Toast.LENGTH_SHORT).show();
//              } else {
//                // Denied permission with ask never again（拒绝且已勾选不再询问的回调）
//                // Need to go to the settings
//                Toast.makeText(this,"权限未授予，ask never again", Toast.LENGTH_SHORT).show();
//                openSettingActivity(this,  "没有此权限，无法开启这个功能，请开启权限。");
//              }
//            });

        rxPermissions
            .requestEach(Manifest.permission.CALL_PHONE)
            .subscribe(permission -> {
              if (permission.granted) {
                // `permission.name` is granted !
                Toast.makeText(this,"已授予",Toast.LENGTH_SHORT).show();
              } else if (permission.shouldShowRequestPermissionRationale) {
                // Denied permission without ask never again（拒绝且没勾选不再询问的回调）
                Toast.makeText(this,"权限未授予，without ask never again", Toast.LENGTH_SHORT).show();
              } else {
                // Denied permission with ask never again（拒绝且已勾选不再询问的回调）
                // Need to go to the settings
                Toast.makeText(this,"权限未授予，ask never again", Toast.LENGTH_SHORT).show();
                openSettingActivity(this,  "没有此权限，无法开启这个功能，请开启权限。");
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

  /**
   * 打开设置-应用-当前程序详情界面
   *
   * @param activity
   * @param message
   */
  private void openSettingActivity(final Activity activity, String message) {
    showMessageOKCancel(activity, message, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Log.d(TAG, "getPackageName(): " + activity.getPackageName());
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
      }
    });
  }

  /**
   * 显示自己定义的权限提示 Dialog
   *
   * @param context
   * @param message
   * @param okListener
   */
  private void showMessageOKCancel(final Activity context, String message, DialogInterface.OnClickListener okListener) {
    new AlertDialog.Builder(context)
        .setMessage(message)
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", null)
        .setCancelable(false)
        .show();

  }
}
