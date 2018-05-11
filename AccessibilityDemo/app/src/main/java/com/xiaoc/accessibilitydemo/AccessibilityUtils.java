package com.xiaoc.accessibilitydemo;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * 辅助服务检测、开启，应用自动卸载、安装
 * <p>
 * Created by Chang.Xiao on 2018/5/9.
 *
 * @version 1.0
 */
public class AccessibilityUtils {

    private static AccessibilityUtils accessibilityUtils;
    private Context mContext;
    private String mLocalPackageName;
    private String mAccessibilityClassAbsolutePath;
    private AccessibilityManager mAccessibilityManager;

    /**
     * 初始化
     *
     * @param context   上下文，推荐getApplicationContext()
     * @param localPackageName  本应用包名
     * @param accessibilityClassAbsolutePath    辅助服务类绝对路径
     */
    public void init(Context context, String localPackageName, String accessibilityClassAbsolutePath) {
        mContext = context.getApplicationContext();
        mLocalPackageName = localPackageName;
        mAccessibilityClassAbsolutePath = accessibilityClassAbsolutePath;
        mAccessibilityManager = (AccessibilityManager) mContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
    }

    public static AccessibilityUtils getInstance() {
        if (null == accessibilityUtils) {
            synchronized (AccessibilityUtils.class) {
                if (null == accessibilityUtils) {
                    accessibilityUtils = new AccessibilityUtils();
                }
            }
        }
        return accessibilityUtils;
    }

    /**
     * 检测当前辅助服务是否打开
     *
     * @return
     */
    public boolean isAccessibilitySettingsOn() {
        boolean isAccessibilityOn = false;
        try {
            int accessibilityEnabled = Settings.Secure.getInt(mContext.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
            TextUtils.SimpleStringSplitter stringColonSplitter = new TextUtils.SimpleStringSplitter(':');
            if (accessibilityEnabled == 1) {
                String settingValue = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
                if (settingValue != null) {
                    TextUtils.SimpleStringSplitter splitter = stringColonSplitter;
                    splitter.setString(settingValue);
                    while (splitter.hasNext()) {
                        String accessabilityService = splitter.next();
                        // 辅助服务关键路径，格式如"com.corget/com.corget.service.UnAccessibilityService"
                        String service = mLocalPackageName + "/" + mAccessibilityClassAbsolutePath;
                        if (accessabilityService.equalsIgnoreCase(service)) {
                            isAccessibilityOn = true;
                        }
                    }
                }
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return isAccessibilityOn;
    }

    /**
     * Check当前辅助服务是否启用
     *
     * @param serviceName serviceName
     * @return 是否启用
     */
    private boolean checkAccessibilityEnabled(String serviceName) {
        List<AccessibilityServiceInfo> accessibilityServices =
                mAccessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测制定包是否已安装
     *
     * @param packageName
     * @return
     */
    private boolean isApkInstalled(String packageName) {
        PackageManager mPackageManager = mContext.getPackageManager();
        // 获得所有已经安装的包信息
        List<PackageInfo> installedPackages = mPackageManager.getInstalledPackages(0);
        for(int i = 0; i < installedPackages.size(); i++) {
            if(installedPackages.get(i).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 安装apk
     *
     * packageURI = Uri.fromFile(new File(filePath));
     *             if (packageURI != null) {
     *                 Intent installIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
     *                 installIntent.setData(packageURI);
     *                 installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     *                 mContext.startActivity(installIntent);
     *             }
     *
     * @param filePath
     * @param packageName
     */
    public void inStallApp(String filePath, String packageName) {
        if (!isAccessibilitySettingsOn()) {
            Toast.makeText(mContext, "辅助服务未开启，安装应用失败",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // 如果Apk没有安装，则执行安装逻辑
        if(!isApkInstalled(packageName)) {
            MyAccessibilityService.INVOKE_TYPE = MyAccessibilityService.TYPE_INSTALL_APP;
            Intent intent = new Intent(Intent.ACTION_VIEW);

            Uri packageURI = null;
            // 版本在7.0以上是不能直接通过uri访问的
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                File file = new File(filePath);
                // 由于没有在Activity环境下启动Activity,设置下面的标签
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // 参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                packageURI = FileProvider.getUriForFile(mContext, "com.xiaoc.accessibilitydemo", file);
                // 添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                packageURI = Uri.fromFile(new File(filePath));
            }
            intent.setDataAndType(packageURI, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        } else {
            Toast.makeText(mContext, "应用已安装", Toast.LENGTH_SHORT).show();
        }
    }

    public void uninstallApp(String packageName) {
        if (!isAccessibilitySettingsOn()) {
            Toast.makeText(mContext, "辅助服务未开启，卸载应用失败", Toast.LENGTH_SHORT).show();
            return;
        }
        // 如果Apk已经安装，则可以执行卸载逻辑
        if (isApkInstalled(packageName)) {
            MyAccessibilityService.INVOKE_TYPE = MyAccessibilityService.TYPE_UNINSTALL_APP;
            Uri packageURI = Uri.parse("package:" + packageName);
            if (packageURI != null) {
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(uninstallIntent);
            }
        }else{
            Toast.makeText(mContext, "应用不存在，卸载失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void killApp(String packageName) {
        if (!isAccessibilitySettingsOn()) {
            Toast.makeText(mContext, "辅助服务未开启，卸载应用失败", Toast.LENGTH_SHORT).show();
            return;
        }
        // 如果Apk已经安装，则可以执行卸载逻辑
        if (isApkInstalled(packageName)) {
            MyAccessibilityService.INVOKE_TYPE = MyAccessibilityService.TYPE_KILL_APP;
            Intent killIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri packageURI = Uri.parse("package:" + packageName);
            if (packageURI != null) {
                killIntent.setData(packageURI);
                killIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(killIntent);
            }
        }else{
            Toast.makeText(mContext, "应用不存在，杀死失败", Toast.LENGTH_SHORT).show();
        }
    }

    public  void popOpenAlertDialog(final Context context) {
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setIcon(R.mipmap.ic_launcher);
        mDialogBuilder.setTitle("警告");
        mDialogBuilder.setMessage("检测到辅助服务没有开启，马上前往设置？");
        mDialogBuilder.setCancelable(false);
        mDialogBuilder.setPositiveButton("前往",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 前往辅助服务设置页面
                        Intent startIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        context.startActivity(startIntent);
                    }
                });
        mDialogBuilder.setNegativeButton("不了",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        mDialogBuilder.show();
    }
}
