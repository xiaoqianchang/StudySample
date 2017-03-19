package com.changxiao.satelitemenudemo.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * Help to create a dialog.
 *
 * @author Midas.
 * @version 1.0
 * @createDate 2015-11-03
 * @lastUpdate 2016-01-09
 */
public class ZRPopupUtil {

    public static final int WIDTH = 0;
    public static final int HEIGHT = 1;

    /**
     * create a popup menu
     *
     * @param context
     * @param contentView
     * @return
     */
    public static Dialog makePopup(Context context, View contentView) {
        Dialog dialog = new Dialog(context, ZRResourceManager.getResourceID(
                "ZRPopupDialogStyle", "style"));
        Window window = dialog.getWindow();
        LayoutParams windowParams = new LayoutParams();
        int[] size = getScreenSize(context);
        windowParams.x = 0;
        windowParams.y = size[HEIGHT];

        // 设置window的布局参数
        window.setAttributes(windowParams);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(contentView);
        // 显示的大小是contentView 的大小
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    public static Dialog makeCenterPopup(Context context, View contentView) {
        Dialog dialog = new Dialog(context, ZRResourceManager.getResourceID(
                "ZRPopupDialogStyle", "style"));
        Window window = dialog.getWindow();

        LayoutParams lp = window.getAttributes();
        lp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(contentView);
        // 显示的大小是contentView 的大小
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    /**
     * 添加心愿指示窗口
     *
     * @param context
     * @param contentView
     * @return
     */
    public static Dialog makeAddWishTipPopup(Context context, View contentView) {
        Dialog dialog = new Dialog(context, ZRResourceManager.getResourceID(
                "ZRPopupDialogStyle", "style"));
        Window window = dialog.getWindow();

        LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = window.getWindowManager().getDefaultDisplay().getHeight();
        //设置显示位置
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(contentView);
        // 显示的大小是contentView 的大小
        dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
                window.getWindowManager().getDefaultDisplay().getHeight());
        return dialog;
    }

    /**
     * get device size
     *
     * @param context
     * @return
     */
    public static int[] getScreenSize(Context context) {
        int[] deviceSize = new int[2];
        int w = 0;
        int h = 0;
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        w = dm.widthPixels;
        h = dm.heightPixels;
        deviceSize[WIDTH] = w;
        deviceSize[HEIGHT] = h;
        return deviceSize;
    }

}
