package com.changxiao.draggablecircledemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_circles_drawing_view, R.id.btn_draw_view, R.id.btn_circle_bar, R.id.btn_circle_timer_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_circles_drawing_view:
                startActivity(new Intent(this, CirclesDrawingViewActivity.class));
                break;
            case R.id.btn_draw_view:
                startActivity(new Intent(this, DrawViewActivity.class));
                break;
            case R.id.btn_circle_bar:
                startActivity(new Intent(this, CircleBarActivity.class));
                break;
            case R.id.btn_circle_timer_view:
                startActivity(new Intent(this, CircleTimerViewActivity.class));
                break;
        }
    }
}
