package com.xiaoc.accessibilitydemo;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

/**
 * APP智能安装、卸载辅助Service，通过自动化点击实现应用安装、卸载、强行停止功能。
 * <p>
 * Created by Chang.Xiao on 2018/5/8.
 *
 * @version 1.0
 */
public class MyAccessibilityService extends AccessibilityService {

    private static final String TAG = MyAccessibilityService.class.getSimpleName();

    public static int INVOKE_TYPE = 0;
    public static final int TYPE_KILL_APP = 1;
    public static final int TYPE_INSTALL_APP = 2;
    public static final int TYPE_UNINSTALL_APP = 3;

    public static void reset() {
        INVOKE_TYPE = 0;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        // 系统启动服务成功，弹出提示信息
        Toast.makeText(this,"连接服务成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.d(TAG,"onAccessibilityEvent被调用, packageName=" +
                accessibilityEvent.getPackageName() + ", eventType=" +
                accessibilityEvent.eventTypeToString(accessibilityEvent.getEventType()));
        processAccessibilityEvent(accessibilityEvent);
    }

    /**
     * 事件处理方法
     *
     * @param event     事件类型
     */
    private void processAccessibilityEvent(AccessibilityEvent event) {
        if (event.getSource() == null) {
            Log.d(TAG, "the source = null");
        } else {
            Log.d(TAG, "event = " + event.toString());
            switch (INVOKE_TYPE) {
                case TYPE_KILL_APP:
                    processKillApplication(event);
                    break;
                case TYPE_INSTALL_APP:
                    processInstallApplication(event);
                    break;
                case TYPE_UNINSTALL_APP:
                    processUninstallApplication(event);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    /**
     * 安装apk
     *
     * @param event
     */
    private void processInstallApplication(AccessibilityEvent event) {
        // 找到界面需要处理的控件
        findAndPerformActions(event, "安装");
        findAndPerformActions(event, "下一步");
        findAndPerformActions(event, "完成");
    }

    /**
     * 卸载apk
     *
     * @param event
     */
    private void processUninstallApplication(AccessibilityEvent event) {
        // 针对不同平台手机卸载按钮的文本显示不同
        findAndPerformActions(event, "确定");
        findAndPerformActions(event, "确认");
        findAndPerformActions(event, "卸载");
    }

    /**
     * 模拟用户点击操作
     *
     * @param event
     * @param text
     */
    private void findAndPerformActions(AccessibilityEvent event, String text) {
        if (event.getSource() != null) {
            // 判断当前界面为安装界面
            boolean isInstallPage = event.getPackageName().equals("com.android.packageinstaller");
            if (isInstallPage) { // 安装界面
                // 根据文本遍历当前视图树是否包含text文字属性，找到所有包含目标文字的节点(注意，
                // 这里是包含目标文字，而非完全与目标文字相同)，之后再遍历这个列表
                List<AccessibilityNodeInfo> action_nodes = event.getSource().findAccessibilityNodeInfosByText(text);
                if (action_nodes != null && !action_nodes.isEmpty()) {
                    AccessibilityNodeInfo node = null;
                    for (int i = 0; i < action_nodes.size(); i++) {
                        node = action_nodes.get(i);
                        // 执行按钮点击行为
                        if (node.getClassName().equals("android.widget.Button") && node.isEnabled()) {
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }
            }
        }
    }

    /**
     * 杀死进程
     *
     * @param event
     */
    private void processKillApplication(AccessibilityEvent event) {
        if (event.getSource() != null) {
            if (event.getPackageName().equals("com.android.settings")) {
                List<AccessibilityNodeInfo> stop_nodes = event.getSource().findAccessibilityNodeInfosByText("强行停止");
                if (stop_nodes!=null && !stop_nodes.isEmpty()) {
                    AccessibilityNodeInfo node;
                    for(int i=0; i<stop_nodes.size(); i++){
                        node = stop_nodes.get(i);
                        if (node.getClassName().equals("android.widget.Button")) {
                            if(node.isEnabled()){
                                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        }
                    }
                }

                List<AccessibilityNodeInfo> ok_nodes = event.getSource().findAccessibilityNodeInfosByText("确定");
                if (ok_nodes!=null && !ok_nodes.isEmpty()) {
                    AccessibilityNodeInfo node;
                    for(int i=0; i<ok_nodes.size(); i++){
                        node = ok_nodes.get(i);
                        if (node.getClassName().equals("android.widget.Button")) {
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            Log.d("action", "click ok");
                        }
                    }
                }
            }
        }
    }
}
