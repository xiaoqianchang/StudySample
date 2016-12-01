package com.changxiao.doubleclickdetectdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.changxiao.doubleclickdetectdemo.utils.DoubleClickDetectUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_click)
    Button btnClick;

    @Bind(R.id.btn_double_click)
    Button btnDoubleClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_click, R.id.btn_double_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_click:
//                DoubleClickDetectUtils.getInstance().isDoubleClick();
                break;
            case R.id.btn_double_click:
                if (DoubleClickDetectUtils.getInstance().doubleClickDetect(btnDoubleClick)) {
                    Toast.makeText(this, "doubleClick", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
