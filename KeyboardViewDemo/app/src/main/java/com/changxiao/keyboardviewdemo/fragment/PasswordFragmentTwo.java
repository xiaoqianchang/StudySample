package com.changxiao.keyboardviewdemo.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.changxiao.keyboardviewdemo.R;
import com.changxiao.keyboardviewdemo.widget.NumberKeyboardView;
import com.changxiao.keyboardviewdemo.widget.PasswordView;
import com.jungly.gridpasswordview.GridPasswordView;

/**
 * $desc$
 * <p>
 * Created by Chang.Xiao on 2017/3/20.
 *
 * @version 1.0
 */

public class PasswordFragmentTwo extends DialogFragment {

    private NumberKeyboardView mPassword;

    public static PasswordFragment newInstace(){
        PasswordFragment passWordFragment = new PasswordFragment();

        return passWordFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    // 继承BottomSheetDialogFragment时onStart()可注释掉
    @Override
    public void onStart() {
        super.onStart();

        Window win = getDialog().getWindow();
        win.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        layoutParams.width = (int) (dm.widthPixels);
        win.setAttributes(layoutParams);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.keyboard_view, container);
        init(view);
        return view;
    }

    private void init(View view){
        mPassword = (NumberKeyboardView) view.findViewById(R.id.view_keyboard);
    }
}
