package com.changxiao.draggablecircledemo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.changxiao.draggablecircledemo.widget.DraggableCircleView;
import com.changxiao.draggablecircledemo.widget.DraggableCircleViewTwo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.draggable_circle_view)
    DraggableCircleViewTwo mDraggableCircleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDraggableCircleView.newBuilder()
                .setSmallTickMarkNum(360)
                .setEachCircleTotal(new double[] {1000 * 360, 10000 * 360})
                .setCurrentValue(0)
                .setCircleButtonColor(Color.RED)
                .setMiddleValueSize(getResources().getDimensionPixelSize(R.dimen.middle_value_size))
                .setMiddleTextSize(getResources().getDimensionPixelSize(R.dimen.middle_text_size))
                .build();
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
