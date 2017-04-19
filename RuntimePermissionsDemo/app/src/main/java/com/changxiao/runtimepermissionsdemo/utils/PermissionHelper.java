package com.changxiao.runtimepermissionsdemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.changxiao.runtimepermissionsdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * android >=M 的权限申请统一处理
 *
 * 6.0权限的基本知识，以下是需要单独申请的权限，共分为9组，每组只要有一个权限申请成功了，就默认整组权限都可以使用了。
 * group:android.permission-group.CONTACTS
 * permission:android.permission.WRITE_CONTACTS
 * permission:android.permission.GET_ACCOUNTS
 * permission:android.permission.READ_CONTACTS
 *
 * group:android.permission-group.PHONE
 * permission:android.permission.READ_CALL_LOG
 * permission:android.permission.READ_PHONE_STATE
 * permission:android.permission.CALL_PHONE
 * permission:android.permission.WRITE_CALL_LOG
 * permission:android.permission.USE_SIP
 * permission:android.permission.PROCESS_OUTGOING_CALLS
 * permission:com.android.voicemail.permission.ADD_VOICEMAIL
 *
 * group:android.permission-group.CALENDAR
 * permission:android.permission.READ_CALENDAR
 * permission:android.permission.WRITE_CALENDAR
 *
 * group:android.permission-group.CAMERA
 * permission:android.permission.CAMERA
 *
 * group:android.permission-group.SENSORS
 * permission:android.permission.BODY_SENSORS
 *
 * group:android.permission-group.LOCATION
 * permission:android.permission.ACCESS_FINE_LOCATION
 * permission:android.permission.ACCESS_COARSE_LOCATION
 *
 * group:android.permission-group.STORAGE
 * permission:android.permission.READ_EXTERNAL_STORAGE
 * permission:android.permission.WRITE_EXTERNAL_STORAGE
 *
 * group:android.permission-group.MICROPHONE
 * permission:android.permission.RECORD_AUDIO
 *
 * group:android.permission-group.SMS
 * permission:android.permission.READ_SMS
 * permission:android.permission.RECEIVE_WAP_PUSH
 * permission:android.permission.RECEIVE_MMS
 * permission:android.permission.RECEIVE_SMS
 * permission:android.permission.SEND_SMS
 * permission:android.permission.READ_CELL_BROADCASTS
 *
 * notice:
 * 很多手机对原生系统做了修改，比如小米4的6.0的shouldShowRequestPermissionRationale
 * 就一直返回false，而且在申请权限时，如果用户选择了拒绝，则不会再弹出对话框了
 * <p>
 * Created by Chang.Xiao on 2016/11/22.
 *
 * @version 1.0
 */

public class PermissionHelper {

    private static final String TAG = PermissionHelper.class.getSimpleName();

    /**
     * permission_group
     */
    public static final int CODE_CALENDAR = 0;
    public static final int CODE_CAMERA = 1;
    public static final int CODE_CONTACTS = 2;
    public static final int CODE_LOCATION = 3;
    public static final int CODE_MICROPHONE = 4;
    public static final int CODE_PHONE = 5;
    public static final int CODE_SENSORS = 6;
    public static final int CODE_SMS = 7;
    public static final int CODE_STORAGE = 8;
    public static final int CODE_MULTI_PERMISSION = 100;

    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_BODY_SENSORS = Manifest.permission.BODY_SENSORS;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;

    private static final String[] requestPermissions = {
            PERMISSION_GET_ACCOUNTS,
            PERMISSION_READ_PHONE_STATE,
            PERMISSION_READ_CALENDAR,
            PERMISSION_CAMERA,
            PERMISSION_BODY_SENSORS,
            PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_RECORD_AUDIO,
            PERMISSION_READ_SMS
    };

    /**
     * Requests permission.
     *
     * @param activity
     * @param requestCode request code, e.g. if you need request CAMERA permission,parameters is PermissionHelper.CODE_CAMERA
     */
    public static void requestPermission(final Activity activity, final int requestCode, OnPermissionListener onPermissionListener) {
        if (activity == null) {
            return;
        }

        if (requestCode < 0 || requestCode >= requestPermissions.length) {
            Log.w(TAG, "requestPermission illegal requestCode:" + requestCode);
            return;
        }

        final String requestPermission = requestPermissions[requestCode];

        // 如果是6.0以下的手机，ActivityCompat.checkSelfPermission()会始终等于PERMISSION_GRANTED，
        // 但是，如果用户关闭了你申请的权限，ActivityCompat.checkSelfPermission(),会导致程序崩溃(java.lang.RuntimeException: Unknown exception code: 1 msg null)，
        // 你可以使用try{}catch(){},处理异常，也可以在这个地方，低于23就什么都不做，
        // 个人建议try{}catch(){}单独处理，提示用户开启权限。
        //        if (Build.VERSION.SDK_INT < 23) {
        //            return;
        //        }

        if (PermissionUtils.hasSelfPermissions(activity, new String[] { requestPermission })) {
            onPermissionListener.onPermissionGranted(requestCode);
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(activity, new String[] { requestPermission })) {
                showRationale(activity, requestCode, requestPermission);
            } else {
                ActivityCompat.requestPermissions(activity, new String[] { requestPermission }, requestCode);
            }
        }
    }

    private static void showRationale(final Activity activity, final int requestCode, final String requestPermission) {
        //TODO
        String[] permissionsHint = activity.getResources().getStringArray(R.array.permissions);
        showMessageOKCancel(activity, "Rationale: " + permissionsHint[requestCode], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{requestPermission},
                        requestCode);
                Log.d(TAG, "showMessageOKCancel requestPermissions:" + requestPermission);
            }
        });
    }

    private static void showMessageOKCancel(final Activity context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

    /**
     * @param activity
     * @param requestCode  Need consistent with requestPermission
     * @param permissions
     * @param grantResults
     */
    public static void onRequestPermissionsResult(final Activity activity, final int requestCode, @NonNull String[] permissions,
                                                @NonNull int[] grantResults, OnPermissionListener onPermissionListener) {
        if (activity == null) {
            return;
        }

        if (requestCode < 0 || requestCode >= requestPermissions.length) {
            Log.w(TAG, "requestPermissionsResult illegal requestCode:" + requestCode);
            return;
        }

        if (PermissionUtils.getTargetSdkVersion(activity) < 23 && !PermissionUtils.hasSelfPermissions(activity, permissions)) {
            onPermissionListener.onPremissionDenied(requestCode);
            return;
        }
        if (PermissionUtils.verifyPermissions(grantResults)) {
            onPermissionListener.onPermissionGranted(requestCode);
        } else {
            if (!PermissionUtils.shouldShowRequestPermissionRationale(activity, permissions)) {
                onPermissionListener.onPremissionNeverAskAgain(requestCode);
//                String[] permissionsHint = activity.getResources().getStringArray(R.array.permissions);
//                openSettingActivity(activity,  "Result" + permissionsHint[requestCode]);
            } else {
                onPermissionListener.onPremissionDenied(requestCode);
            }
        }
    }

    private static void openSettingActivity(final Activity activity, String message) {

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

    public interface OnPermissionListener {
        void onPermissionGranted(int requestCode);
        void onPremissionDenied(int requestCode);
        void onPremissionNeverAskAgain(int requestCode);
    }
}
