package com.changxiao.runtimepermissionsdemo.permissionsdispatcher.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.changxiao.runtimepermissionsdemo.R;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * PermissionsDispatcher 使用方式（和原生api实现最接近，每一步都可以回调监控）
 *
 * @RuntimePermissions  注解在其内部需要使用运行时权限的Activity或Fragment上
 * @NeedsPermission 注解在需要调用运行时权限的方法上，当用户给予权限时会执行该方法
 * @OnShowRationale 注解在用于向用户解释为什么需要调用该权限的方法上，只有当第一次请求权限被用户拒绝，下次请求权限之前会调用
 * @OnPermissionDenied  注解在当用户拒绝了权限请求时需要调用的方法上
 * @OnNeverAskAgain 注解在当用户选中了授权窗口中的不再询问复选框后并拒绝了权限请求时需要调用的方法，一般可以向用户解释为何申请此权限，并根据实际需求决定是否再次弹出权限请求对话框
 * 注意：被注解的方法不能是私有方法。
 *
 * 只有@RuntimePermissions和@NeedsPermission是必须的，其余注解均为可选。
 * 当使用了@RuntimePermissions和@NeedsPermission之后，需要点击菜单栏中Build菜单下的Make Project，
 * 或者按快捷键Ctrl + F9编译整个项目，编译器会在app\build\intermediates\classes\debug目录下与被注解
 * 的Activity同一个包下生成一个辅助类，名称为被注解的Activity名称+PermissionsDispatcher.class。
 *
 * 接下来可以调用辅助类里面的方法完成应用的权限请求了。在需要调用权限的位置调用辅助类里面的xxxWithCheck方法，
 * xxx是被@NeedsPermission注解的方法名。
 *
 * 之后，还需要重写该Activity的onRequestPermissionsResult()方法，其方法内调用辅助类的onRequestPermissionsResult()方法。
 *
 * 异常情况：
 * 1、魅族上checkSelfPermission一直返回0即hasSelfPermissions一直为true，参考 https://bbs.meizu.cn/thread-7065062-1-1.html
 *
 * 参考：
 * https://github.com/permissions-dispatcher/PermissionsDispatcher
 * http://www.cnblogs.com/duduhuo/p/6228426.html
 *
 * Created by Chang.Xiao on 2018/12/13.
 * @version 1.0
 */
@RuntimePermissions
public class PermissionsDispatcherActivity extends AppCompatActivity implements OnClickListener {

  private Button mBtnCallPhone;
  private TextView mTvTelNum;
  private Button mBtnCamera;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_permissions_dispatcher);

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
        //发起权限申请，名字为“该Activity名+PermissionsDispatcher.调用方法名+WithCheck”
        PermissionsDispatcherActivityPermissionsDispatcher.callPhoneWithCheck(PermissionsDispatcherActivity.this);
        break;
      case R.id.btn_camera:
        break;
      default:
        break;
    }
  }

  /**
   * 需要权限的方法
   */
  @NeedsPermission(Manifest.permission.CALL_PHONE)
  void callPhone(){
    String telNum = mTvTelNum.getText().toString().trim();
    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+telNum));
    try {
      startActivity(intent);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  @NeedsPermission(Manifest.permission.CAMERA)
  void showCamera() {
    // NOTE: Perform action that requires the permission. If this is run by PermissionsDispatcher, the permission will have been granted
  }

  /**
   * 唤出权限时的提示
   * @param request 所要申请的权限
   */
  @OnShowRationale(Manifest.permission.CALL_PHONE)
  void showRationaleForCallPhone(PermissionRequest request) {
    showRationaleDialog("使用此功能需要打开拨打电话的权限", request);
  }

  @OnShowRationale(Manifest.permission.CAMERA)
  void showRationaleForCamera(PermissionRequest request) {
    // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
    // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
    showRationaleDialog("使用此功能需要打开相机的权限", request);
  }

  /**
   * 被用户拒绝
   */
  @OnPermissionDenied(Manifest.permission.CALL_PHONE)
  void onCallPhoneDenied() {
    Toast.makeText(this,"权限未授予，功能无法使用",Toast.LENGTH_SHORT).show();
  }

  @OnPermissionDenied(Manifest.permission.CAMERA)
  void onCameraDenied() {
    // NOTE: Deal with a denied permission, e.g. by showing specific UI
    // or disabling certain functionality
    Toast.makeText(this, "权限未授予，功能无法使用", Toast.LENGTH_SHORT).show();
  }

  /**
   * 被拒绝并勾选不在提醒授权时，应用需提示用户未获取权限，需用户自己去设置中打开
   */
  @OnNeverAskAgain(Manifest.permission.CALL_PHONE)
  void onCallPhoneNeverAskAgain() {
    AskForPermission();
  }

  @OnNeverAskAgain(Manifest.permission.CAMERA)
  void onCameraNeverAskAgain() {
    AskForPermission();
  }

  /**
   * 告知用户具体需要权限的原因
   * @param messageResId
   * @param request
   */
  private void showRationaleDialog(String messageResId, final PermissionRequest request) {
    new AlertDialog.Builder(this)
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(@NonNull DialogInterface dialog, int which) {
            request.proceed();//请求权限
          }
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(@NonNull DialogInterface dialog, int which) {
            request.cancel();
          }
        })
        .setCancelable(false)
        .setMessage(messageResId)
        .show();
  }

  /**
   * 被拒绝并且不再提醒,提示用户去设置界面重新打开权限
   */
  private void AskForPermission() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("当前应用缺少拨打电话权限,请去设置界面打开");
    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {

      }
    });
    builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
        startActivity(intent);
      }
    });
    builder.create().show();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    // 将权限处理采用PermissionsDispatcher的处理方式
    PermissionsDispatcherActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
  }
}
