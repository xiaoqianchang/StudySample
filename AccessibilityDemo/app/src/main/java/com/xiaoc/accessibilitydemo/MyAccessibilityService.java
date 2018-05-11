package com.xiaoc.accessibilitydemo;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo == null) {
            Log.d(TAG, "the source = null");
        } else {
            Log.d(TAG, "event = " + event.toString());
            int eventType = event.getEventType();
            if (eventType== AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                    eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                // 有两个同时都被触发的可能，为了防止二次处理的情况，这里我们使用了一个Map来过滤掉重复事件。
                switch (INVOKE_TYPE) {
                    case TYPE_KILL_APP:
                        processKillApplication(event);
                        //                            handled = iterateNodesAndHandle(nodeInfo);
                        break;
                    case TYPE_INSTALL_APP:
                        processInstallApplication(event);
                        //                            handled = iterateNodesAndHandle(nodeInfo, "开启", "安装", "下一步", "完成");
                        break;
                    case TYPE_UNINSTALL_APP:
                        processUninstallApplication(event);
                        //                            handled = iterateNodesAndHandle(nodeInfo, "确定", "确认", "卸载");
                        break;
                    default:
                        break;
                }
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
//        findAndPerformActions(event, "开启"); // 针对系统有设置“禁止安装”来源不明的应用 的弹框
//        findAndPerformActions(event, "安装");
//        findAndPerformActions(event, "下一步");
//        findAndPerformActions(event, "完成");

        findAndPerformActions(event, "开启", "安装", "下一步", "完成");
    }

    /**
     * 卸载apk
     *
     * @param event
     */
    private void processUninstallApplication(AccessibilityEvent event) {
        // 针对不同平台手机卸载按钮的文本显示不同
//        findAndPerformActions(event, "确定");
//        findAndPerformActions(event, "确认");
//        findAndPerformActions(event, "卸载");

        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo != null) {
            // 判断当前界面为安装界面
            if (event.getPackageName().equals("com.android.packageinstaller")) { // 系统安装界面
                CharSequence className = event.getClassName();
                if (className.equals("com.android.packageinstaller.UninstallerActivity")) {
                    // 卸载
                    // 根据文本遍历当前视图树是否包含text文字属性，找到所有包含目标文字的节点(注意，
                    // 这里是包含目标文字，而非完全与目标文字相同)，之后再遍历这个列表
                    findAndPerformActions(event, "确定", "确认", "卸载");
                }
            }
        }
    }

    /**
     * 模拟用户点击操作
     *
     * @param event
     * @param text
     */
    private void findAndPerformActions(AccessibilityEvent event, String text) {
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo != null) {
            // 判断当前界面为安装界面
            boolean isInstallPage = event.getPackageName().equals("com.android.packageinstaller");
            if (isInstallPage) { // 系统安装界面
                CharSequence className = event.getClassName();
               if (className.equals("com.android.packageinstaller.UninstallerActivity")) {
                    // 卸载
                    // 根据文本遍历当前视图树是否包含text文字属性，找到所有包含目标文字的节点(注意，
                    // 这里是包含目标文字，而非完全与目标文字相同)，之后再遍历这个列表
                    List<AccessibilityNodeInfo> action_nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText(text);
                    if (action_nodes != null && !action_nodes.isEmpty()) {
                        AccessibilityNodeInfo node = null;
                        for (int i = 0; i < action_nodes.size(); i++) {
                            node = action_nodes.get(i);
                            // 执行按钮点击行为
                            if ((node.getClassName().equals("android.widget.Button")
                                    || node.getClassName().equals("android.widget.TextView")) // 某些按钮是Textview
                                    && node.isEnabled()) {
                                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        }
                    }
                }
                // 安装
                // 根据文本遍历当前视图树是否包含text文字属性，找到所有包含目标文字的节点(注意，
                // 这里是包含目标文字，而非完全与目标文字相同)，之后再遍历这个列表
                List<AccessibilityNodeInfo> action_nodes = event.getSource().findAccessibilityNodeInfosByText(text);
                if (action_nodes != null && !action_nodes.isEmpty()) {
                    AccessibilityNodeInfo node = null;
                    for (int i = 0; i < action_nodes.size(); i++) {
                        node = action_nodes.get(i);
                        // 执行按钮点击行为
                        if ((node.getClassName().equals("android.widget.Button")
                                || node.getClassName().equals("android.widget.TextView")) // 某些按钮是Textview
                                && node.isEnabled()) {
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }
            }
        }
    }

    /**
     * 模拟用户点击操作
     *
     * @param event
     * @param params
     */
    private void findAndPerformActions(AccessibilityEvent event, String... params) {
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo != null) {
            // 判断当前界面为安装界面
            if (event.getPackageName().equals("com.android.packageinstaller")) { // 系统安装界面
                CharSequence className = event.getClassName();
                if (className.equals("com.android.packageinstaller.UninstallerActivity")) {
                    // 卸载
                    // 根据文本遍历当前视图树是否包含text文字属性，找到所有包含目标文字的节点(注意，
                    // 这里是包含目标文字，而非完全与目标文字相同)，之后再遍历这个列表
                    if (params != null && params.length > 0) {
                        for (int j = 0; j < params.length; j++) {
                            List<AccessibilityNodeInfo> action_nodes = getRootInActiveWindow().findAccessibilityNodeInfosByText(params[j]);
                            if (action_nodes != null && !action_nodes.isEmpty()) {
                                AccessibilityNodeInfo node = null;
                                for (int i = 0; i < action_nodes.size(); i++) {
                                    node = action_nodes.get(i);
                                    // 执行按钮点击行为
                                    if ((node.getClassName().equals("android.widget.Button")
                                            || node.getClassName().equals("android.widget.TextView")) // 某些按钮是Textview
                                            && node.isEnabled()) {
                                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    }
                                }
                            }
                        }
                    }
                }
                // 安装
                // 根据文本遍历当前视图树是否包含text文字属性，找到所有包含目标文字的节点(注意，
                // 这里是包含目标文字，而非完全与目标文字相同)，之后再遍历这个列表
                if (params != null && params.length > 0) {
                    for (int j = 0; j < params.length; j++) {
                        List<AccessibilityNodeInfo> action_nodes = event.getSource().findAccessibilityNodeInfosByText(params[j]);
                        if (action_nodes != null && !action_nodes.isEmpty()) {
                            AccessibilityNodeInfo node = null;
                            for (int i = 0; i < action_nodes.size(); i++) {
                                node = action_nodes.get(i);
                                // 执行按钮点击行为
                                if ((node.getClassName().equals("android.widget.Button")
                                        || node.getClassName().equals("android.widget.TextView")) // 某些按钮是Textview
                                        && node.isEnabled()) {
                                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                }
                            }
                        }
                    }
                }
            } else if (event.getPackageName().equals("com.android.settings")) {
                for (int j = 0; j < params.length; j++) {
                    List<AccessibilityNodeInfo> action_nodes = event.getSource().findAccessibilityNodeInfosByText(params[j]);
                    if (action_nodes != null && !action_nodes.isEmpty()) {
                        AccessibilityNodeInfo node = null;
                        for (int i = 0; i < action_nodes.size(); i++) {
                            node = action_nodes.get(i);
                            // 执行按钮点击行为
                            if ((node.getClassName().equals("android.widget.Button")
                                    || node.getClassName().equals("android.widget.TextView")) // 某些按钮是Textview
                                    && node.isEnabled()) {
                                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean iterateNodesAndHandle(AccessibilityNodeInfo nodeInfo, String... params) {
        if (nodeInfo != null) {
            int childCount = nodeInfo.getChildCount();
            if ("android.widget.Button".equals(nodeInfo.getClassName())
                    || "android.widget.TextView".equals(nodeInfo.getClassName())
                    && nodeInfo.isEnabled()) {
                String nodeContent = nodeInfo.getText().toString();
                Log.d("TAG", "content is " + nodeContent);
                if (params != null && params.length > 0) {
                    for (int i = 0; i < params.length; i++) {
                        if (params[i].equals(nodeContent)) {
                            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            return true;
                        }
                    }
                }
            } else if ("android.widget.ScrollView".equals(nodeInfo.getClassName())) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo childNodeInfo = nodeInfo.getChild(i);
                if (iterateNodesAndHandle(childNodeInfo)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 杀死进程
     *
     * @param event
     */
    private void processKillApplication(AccessibilityEvent event) {
        findAndPerformActions(event, "强行停止", "确定");
    }
}
