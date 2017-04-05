package com.changxiao.keyboardviewdemo.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;

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
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_password);
        setCanceledOnTouchOutside(true);
    }
}
