package com.changxiao.runtimepermissionsdemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.changxiao.runtimepermissionsdemo.R;

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
 *
 * 针对于 shouldShowRequestPermissionRationale 方法返回值的情况 如下表格：
 * 某个权限请求次数     系统权限框显示前(shouldShowRequestPermissionRationale返回值)    系统权限框显示后(shouldShowRequestPermissionRationale返回值)
 * ---------------------------------------------------------------------------------------------------------------------------------------------
 * first                false                                                           true
 * second               true                                                            true
 * 勾选不在提醒并拒绝     true                                                            false
 * 再次请求权限           false                                                           false
 *
 * 第一次安装并且要请求某个权限：系统权限框显示前 onRequestPermissionsResult
 *
 * 总结为：
 * 1、APP没有申请这个权限的话，返回false
 * 2、用户拒绝时，勾选了不再提示的话，返回false
 * 3、用户拒绝，但是没有勾选不再提示的话，返回true
 * 因此如果想在第一次就给用户提示，需要记录权限是否申请过，没有申请过的话，强制弹窗提示，而不能根据这个方法的返回值来。
 *
 * 异常情况：
 * 1、魅族上checkSelfPermission一直返回0即hasSelfPermissions一直为true，参考 https://bbs.meizu.cn/thread-7065062-1-1.html
 *
 * 参考：
 * https://testerhome.com/topics/5181
 * https://juejin.im/post/59e01ece51882578c6736db7
 * <p>
 * Created by Chang.Xiao on 2016/11/22.
 *
 * @version 1.0
 */

public class PermissionHelper {

    private static final String TAG = PermissionHelper.class.getSimpleName();

    /**
     * permission_group code
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

    public static final String PERMISSION_READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_BODY_SENSORS = Manifest.permission.BODY_SENSORS;
    public static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    private static final String[] requestPermissions = {
            PERMISSION_READ_CALENDAR,
            PERMISSION_CAMERA,
            PERMISSION_GET_ACCOUNTS,
            PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_RECORD_AUDIO,
            PERMISSION_READ_PHONE_STATE,
            PERMISSION_BODY_SENSORS,
            PERMISSION_READ_SMS,
            PERMISSION_READ_EXTERNAL_STORAGE,
    };

    /**
     * 通过权限组请求权限
     *
     * 局限：当权限组中申请某个权限，而这个权限刚好没在manifest.xml中申请，而是同组的另外权限的话，该方法不可用。
     *
     * @param activity
     * @param requestCode
     * @param onPermissionListener
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

    /**
     * 直接通过指定的权限名称请求权限
     *
     * @param activity
     * @param onPermissionListener
     * @param requestCode
     * @param permissions Manifest.permission.WRITE_CONTACTS
     */
    public static void requestPermission(final Activity activity, OnPermissionListener onPermissionListener, final int requestCode, final String... permissions) {
        if (activity == null) {
            return;
        }
        if (PermissionUtils.hasSelfPermissions(activity, permissions)) {
            onPermissionListener.onPermissionGranted(requestCode);
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(activity, permissions)) {
                showRationale(activity, requestCode, permissions);
            } else {
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
            }
        }
    }

    /**
     * show 基本原理
     *
     * @param activity
     * @param requestCode
     * @param requestPermission
     */
    private static void showRationale(final Activity activity, final int requestCode, final String... requestPermission) {
        String[] permissionsHint = activity.getResources().getStringArray(R.array.permissions);
        showMessageOKCancel(activity, "Rationale: " + permissionsHint[requestCode], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(activity, requestPermission, requestCode);
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
    private static void showMessageOKCancel(final Activity context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
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
                // 不再询问已勾选
                onPermissionListener.onPremissionNeverAskAgain(requestCode);
                String[] permissionsHint = activity.getResources().getStringArray(R.array.permissions);
                openSettingActivity(activity,  permissionsHint[requestCode]);
            } else {
                onPermissionListener.onPremissionDenied(requestCode);
            }
        }
    }

    /**
     * 打开设置-应用-当前程序详情界面
     *
     * @param activity
     * @param message
     */
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
