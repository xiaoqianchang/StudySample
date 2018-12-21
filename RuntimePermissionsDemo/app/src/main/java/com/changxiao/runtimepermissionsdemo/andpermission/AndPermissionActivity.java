package com.changxiao.runtimepermissionsdemo.andpermission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.changxiao.runtimepermissionsdemo.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import java.util.List;

/**
 * AndPermission 使用方式
 *
 * 参考：
 * https://github.com/yanzhenjie/AndPermission
 *
 * Created by Chang.Xiao on 2018/12/18.
 * @version 1.0
 */
public class AndPermissionActivity extends AppCompatActivity implements OnClickListener {

  private final String TAG = "AndPermissionActivity";

  private Button mBtnCallPhone;
  private TextView mTvTelNum;
  private Button mBtnCamera;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_and_permission);

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
    switch (v.getId()){
      case R.id.btn_call_phone:
        AndPermission.with(this)
            .runtime()
            .permission(Permission.CALL_PHONE)
            .onGranted(permissions -> {
              callPhone();
            })
            .onDenied(new Action<List<String>>() {
              @Override
              public void onAction(List<String> permissions) {
                Toast.makeText(AndPermissionActivity.this, "权限未授予，功能无法使用", Toast.LENGTH_SHORT).show();
                if (AndPermission.hasAlwaysDeniedPermission(AndPermissionActivity.this, permissions)) {
                  openSettingActivity(AndPermissionActivity.this,  "没有此权限，无法开启这个功能，请开启权限。");
                }
              }
            })
            .start();
        break;
      case R.id.btn_camera:
        break;
      default:
        break;
    }
  }

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
