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

    @Bind(R.id.draggable_circle_view_date)
    DraggableCircleViewTwo mDraggableCircleViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDraggableCircleView.newBuilder()
                .setSmallTickMarkNum(10)
                .setEachCircleTotal(new double[] {1000 * 360, 10000 * 360})
                .setCurrentValue(0)
//                .setCircleButtonColor(Color.WHITE)
                .setMiddleValueSize(getResources().getDimensionPixelSize(R.dimen.middle_value_size))
                .setMiddleTextSize(getResources().getDimensionPixelSize(R.dimen.middle_text_size))
//                .setCircleColor(Color.RED)
//                .setCircleRingColor(Color.RED)
//                .setInnerCircleColor(Color.BLACK)
                .setGapBetweenCircleAndLine(getResources().getDimension(R.dimen.padding_2))
                .setLineLength(getResources().getDimension(R.dimen.padding_7))
//                .setCircleRingStrokeWidth((int) getResources().getDimension(R.dimen.padding_22))
                .setMiddleText("目标金额")
                .setMiddleTextSize(getResources().getDimension(R.dimen.font_32))
                .setMiddleValueSize(getResources().getDimension(R.dimen.font_38))
                .build();

        mDraggableCircleViewDate.newBuilder()
                .setSmallTickMarkNum(6)
                .setEachCircleTotal(new double[] {12})
                .setCurrentValue(0)
                //                .setCircleButtonColor(Color.WHITE)
                .setMiddleValueSize(getResources().getDimensionPixelSize(R.dimen.middle_value_size))
                .setMiddleTextSize(getResources().getDimensionPixelSize(R.dimen.middle_text_size))
                //                .setCircleColor(Color.RED)
                //                .setCircleRingColor(Color.RED)
                //                .setInnerCircleColor(Color.BLACK)
                .setGapBetweenCircleAndLine(getResources().getDimension(R.dimen.padding_2))
                .setLineLength(getResources().getDimension(R.dimen.padding_7))
                //                .setCircleRingStrokeWidth((int) getResources().getDimension(R.dimen.padding_22))
                .setMiddleText("达成日期")
                .setMiddleTextSize(getResources().getDimension(R.dimen.font_32))
                .setMiddleValueSize(getResources().getDimension(R.dimen.font_38))
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
