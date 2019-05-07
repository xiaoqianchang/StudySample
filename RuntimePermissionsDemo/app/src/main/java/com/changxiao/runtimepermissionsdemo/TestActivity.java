package com.changxiao.runtimepermissionsdemo;

import android.Manifest.permission;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * 测试
 * 
 * Created by Chang.Xiao on 2018/12/25.
 * @version 1.0
 */
@RuntimePermissions
public class TestActivity extends AppCompatActivity {

  private final String TAG = "TestActivity";

  private TextView textOut;

  private TelephonyManager telephonyManager;
  private PhoneStateListener listener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);

    textOut = (TextView) findViewById(R.id.textOut);

    // Get the telephony manager
    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

    // Create a new PhoneStateListener
    listener = new PhoneStateListener() {
      @Override
      public void onCallStateChanged(int state, String incomingNumber) {
        String stateString = "N/A";
        switch (state) {
          case TelephonyManager.CALL_STATE_IDLE:
            stateString = "Idle";
            break;
          case TelephonyManager.CALL_STATE_OFFHOOK:
            stateString = "Off Hook";
            break;
          case TelephonyManager.CALL_STATE_RINGING:
            stateString = "Ringing";
            break;
        }
        textOut.append(String.format("\nonCallStateChanged: %s",
            stateString));
      }
    };

    // Register the listener with the telephony manager
    telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

//    TestActivityPermissionsDispatcher.readPhoneStateWithCheck(this);

//    String androidId = getAndroidId(this);
//    Log.e(TAG, "androidId=" + androidId);
//    Toast.makeText(this, "getAndroidId=" + androidId, Toast.LENGTH_LONG).show();
  }

  @NeedsPermission(permission.READ_PHONE_STATE)
  void readPhoneState() {
    String imei = getIMEI(this); // 提示获取手机识别码权权限
    Log.e(TAG, "imei=" + imei);
    Toast.makeText(this, "getIMEI=" + imei, Toast.LENGTH_LONG).show();
  }

  @OnShowRationale(permission.READ_PHONE_STATE)
  void showRationale(PermissionRequest request) {
    showRationaleDialog("使用此功能需要打开拨打电话的权限", request);
  }

  /**
   * 获取IMEI
   * @param context 上下文
   * @return imei，获取不到返回空串
   */
  public static String getIMEI(Context context) {
    String imei = "";
    if (context != null) {
      try {
        TelephonyManager telephonyManager =
            (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
          imei = telephonyManager.getDeviceId();
        }
      } catch (SecurityException e) {
        e.printStackTrace();
        imei = "";
      } catch (Exception e) {
        e.printStackTrace();
        imei = "";
      }
    }
    return imei;
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

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    // 将权限处理采用PermissionsDispatcher的处理方式
    TestActivityPermissionsDispatcher
        .onRequestPermissionsResult(this, requestCode, grantResults);
  }
}
