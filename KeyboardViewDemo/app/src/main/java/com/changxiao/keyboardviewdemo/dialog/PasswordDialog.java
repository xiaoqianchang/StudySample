package com.changxiao.keyboardviewdemo.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.changxiao.keyboardviewdemo.R;

/**
 * $desc$
 * <p>
 * Created by Chang.Xiao on 2017/4/5.
 *
 * @version 1.0
 */

public class PasswordDialog extends AppCompatDialog {

    public PasswordDialog(Context context) {
        super(context);
    }

    public PasswordDialog(Context context, int theme) {
        super(context, theme);
    }

    protected PasswordDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_password);

        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager windowManager = window.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        windowparams.width = display.getWidth(); // 设置dialog的宽度为当前手机屏幕的宽度
//        window.setWindowAnimations(R.style.animationBottomTranslate);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setAttributes(windowparams);
    }
}
