package com.changxiao.coordinatorlayoutdemo.panel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.changxiao.coordinatorlayoutdemo.R;

public class PanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rl_container, QuickControlFragment.newInstance())
                .commitAllowingStateLoss();
    }
}
