package com.changxiao.runtimepermissionsdemo.easypermissions.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.changxiao.runtimepermissionsdemo.R;
import java.util.List;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * EasyPermissions 使用方式
 *
 * 参考：
 * https://github.com/googlesamples/easypermissions
 *
 * Created by Chang.Xiao on 2018/12/14.
 * @version 1.0
 */
public class EasyPermissionsActivity extends AppCompatActivity implements OnClickListener, EasyPermissions.PermissionCallbacks {

  private final String TAG = "RxPermissionsActivity";

  private Button btn_check, btn_getSingle, btn_getMulti;
  private TextView tv_log;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_easy_permissions);

    initView();
  }

  private void initView() {
    btn_check = findViewById(R.id.btn_check);
    btn_getSingle = findViewById(R.id.btn_getSingle);
    btn_getMulti = findViewById(R.id.btn_getMulti);
    tv_log = findViewById(R.id.tv_log);

    btn_check.setOnClickListener(this);
    btn_getSingle.setOnClickListener(this);
    btn_getMulti.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_getSingle:
        EasyPermissions.requestPermissions(this,
            "接下来需要获取WRITE_EXTERNAL_STORAGE权限",
            R.string.yes,
            R.string.no,
            0,
            Manifest.permission.WRITE_EXTERNAL_STORAGE);
        break;
      case R.id.btn_getMulti:
        EasyPermissions.requestPermissions(this,
            "接下来需要获取WRITE_EXTERNAL_STORAGE和WRITE_EXTERNAL_STORAGE权限",
            R.string.yes,
            R.string.no,
            1,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO);
        break;
    }
  }

  /**
   * 获得权限后的回调
   *
   * @AfterPermissionGranted注解为了提供方便，但可以添加也可以不添加，是可选的。
   * 好处：
   * 使用了该注解，当权限请求被用户同意后，会根据请求code来执行，相应的含有@AfterPermissionGranted注解的方法。
   * 简化了请求成功操作流程，不需要在EasyPermissions.onRequestPermissionsResult()的回调监听器中请求成功的方法中
   * ，再次手动调用，获取权限后需要操作的逻辑代码。
   */
  @AfterPermissionGranted(0)
  private void afterGet(){
    Toast.makeText(this, "已获取权限，让我们干爱干的事吧！", Toast.LENGTH_SHORT).show();
  }

  /**
   * 重写onRequestPermissionsResult，用于接受请求结果
   *
   * @param requestCode
   * @param permissions
   * @param grantResults
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    // Forward results to EasyPermissions
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
  }

  /**
   * 请求权限成功。
   * 可以弹窗显示结果，也可执行具体需要的逻辑操作
   *
   * @param requestCode
   * @param perms
   */
  @Override
  public void onPermissionsGranted(int requestCode, List<String> perms) {
    // Some permissions have been granted
    // ...
    switch (requestCode){
      case 0:
        Toast.makeText(this, "已获取WRITE_EXTERNAL_STORAGE权限", Toast.LENGTH_SHORT).show();
        break;
      case 1:
        Toast.makeText(this, "已获取WRITE_EXTERNAL_STORAGE和WRITE_EXTERNAL_STORAGE权限", Toast.LENGTH_SHORT).show();
        break;
    }
  }

  /**
   * 请求权限失败
   *
   * @param requestCode
   * @param perms
   */
  @Override
  public void onPermissionsDenied(int requestCode, List<String> perms) {
    // Some permissions have been denied
    // ...
    Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
    //处理权限名字字符串
    StringBuffer sb = new StringBuffer();
    for (String str : perms){
      sb.append(str);
      sb.append("\n");
    }
    sb.replace(sb.length() - 2,sb.length(),"");

    switch (requestCode){
      case 0:
        Toast.makeText(this, "已拒绝权限" + perms.get(0), Toast.LENGTH_SHORT).show();
        break;
      case 1:
        Toast.makeText(this, "已拒绝WRITE_EXTERNAL_STORAGE和WRITE_EXTERNAL_STORAGE权限"+ perms.get(0), Toast.LENGTH_SHORT).show();
        break;
    }

    // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
    // This will display a dialog directing them to enable the permission in app settings.
    if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
      Toast.makeText(this, "已拒绝权限" + sb + "并不再询问" , Toast.LENGTH_SHORT).show();
      new AppSettingsDialog
          .Builder(this)
          .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
          .setPositiveButton("好")
          .setNegativeButton("不行")
          .build()
          .show();
    }
  }

  /**
   * 打开设置界面后返回
   *
   * @param requestCode
   * @param resultCode
   * @param data
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      //当从软件设置界面，返回当前程序时候
      case AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE:

        //执行Toast显示或者其他逻辑处理操作
        break;

    }
  }
}
