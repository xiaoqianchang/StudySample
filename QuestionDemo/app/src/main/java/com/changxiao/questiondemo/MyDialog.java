package com.changxiao.questiondemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

/**
 * $desc$
 * <p>
 * Created by Chang.Xiao on 2016/10/25.
 *
 * @version 1.0
 */

public class MyDialog extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //      setStyle(R.style.dialog, mBackStackId);

        View view=inflater.inflate(R.layout.dialog, container);
        Button positive=(Button) view.findViewById(R.id.positive);
        Button negative=(Button) view.findViewById(R.id.negative);
        positive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NotifySaveOrDeleteListener notify=(NotifySaveOrDeleteListener)getActivity();
                notify.onNotifySave();
                dismiss();
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NotifySaveOrDeleteListener notify=(NotifySaveOrDeleteListener)getActivity();
                notify.onNotifyDelete();
                dismiss();
            }
        });

        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);

    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //      WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        //
        //      params.height = LayoutParams.WRAP_CONTENT;
        //      params.width=200;
        //      params.gravity = Gravity.LEFT;
        //      getDialog().getWindow().setAttributes(params);

        getDialog().getWindow().setLayout(600, 400);


    }
    public interface NotifySaveOrDeleteListener{

        void onNotifySave();
        void onNotifyDelete();
    }
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }
}
