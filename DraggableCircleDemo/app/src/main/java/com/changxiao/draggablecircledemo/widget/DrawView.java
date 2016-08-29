package com.changxiao.draggablecircledemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * $desc$
 * <p/>
 * Created by Chang.Xiao on 2016/8/29.
 *
 * @version 1.0
 */
public class DrawView extends View {
    public float currentX = 50;
    public float currentY = 50;

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(currentX,currentY,10,paint);
    }
}
