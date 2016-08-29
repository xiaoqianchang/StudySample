package com.changxiao.draggablecircledemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.changxiao.draggablecircledemo.widget.DrawView;

public class DrawViewActivity extends AppCompatActivity {

    public LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_view);
        linearLayout = (LinearLayout) findViewById(R.id.root);

        final DrawView drawView = new DrawView(this);
        drawView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                drawView.currentX = event.getX();
                drawView.currentY = event.getY();
                //通过draw组件重绘
                drawView.invalidate();

                return true;
            }
        });
        linearLayout.addView(drawView);
    }
}
