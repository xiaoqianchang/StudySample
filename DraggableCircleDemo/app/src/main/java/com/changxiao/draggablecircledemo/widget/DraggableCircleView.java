package com.changxiao.draggablecircledemo.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;

/**
 * 可绕圆圈轨迹拖拽的view
 * <p/>
 * Created by Chang.Xiao on 2016/8/29.
 *
 * @version 1.0
 */
public class DraggableCircleView extends View {

    private final String TAG = DraggableCircleView.class.getSimpleName();

    // Default dimension in dp/pt
    private static final float DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE = 5;
    private static final float DEFAULT_GAP_BETWEEN_NUMBER_AND_LINE = 5;
    private static final float DEFAULT_NUMBER_SIZE = 10;
    private static final float DEFAULT_LINE_LENGTH = 14;
    private static final float DEFAULT_LONGER_LINE_LENGTH = 23;
    private static final float DEFAULT_LINE_WIDTH = 0.5f;
    private static final float DEFAULT_CIRCLE_BUTTON_RADIUS = 15;
    private static final float DEFAULT_CIRCLE_STROKE_WIDTH = 1;
    private static final float DEFAULT_TIMER_NUMBER_SIZE = 50;
    private static final float DEFAULT_TIMER_TEXT_SIZE = 14;
    private static final float DEFAULT_GAP_BETWEEN_TIMER_NUMBER_AND_TEXT = 30;

    // Default color
    private static final int DEFAULT_CIRCLE_COLOR = 0xFFE9E2D9;
    private static final int DEFAULT_CIRCLE_BUTTON_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_LINE_COLOR = 0xFFE9E2D9;
    private static final int DEFAULT_HIGHLIGHT_LINE_COLOR = 0xFF68C5D7;
    private static final int DEFAULT_NUMBER_COLOR = 0x99866A60;
    private static final int DEFAULT_TIMER_NUMBER_COLOR = 0xFFFA7777;
    private static final int DEFAULT_TIMER_COLON_COLOR = 0x80FA7777;
    private static final int DEFAULT_TIMER_TEXT_COLOR = 0x99000000;

    private static final int DEFAULT_SMALL_TICK_MARK_NUM = 120;
    private static final int DEFAULT_BIG_TICK_MARK_NUM = 4;

    // Paint
    private Paint mCirclePaint;
    private Paint mHighlightLinePaint;
    private Paint mLinePaint;
    private Paint mCircleButtonPaint;
    private Paint mNumberPaint;
    private Paint mTimerNumberPaint;
    private Paint mTimerTextPaint;
    private Paint mTimerColonPaint;

    // Dimension
    private float mGapBetweenCircleAndLine;
    private float mGapBetweenNumberAndLine;
    private float mNumberSize;
    private float mLineLength;
    private float mLongerLineLength;
    private float mLineWidth;
    private float mCircleButtonRadius;
    private float mCircleStrokeWidth;
    private float mTimerNumberSize;
    private float mTimerTextSize;
    private float mGapBetweenTimerNumberAndText;

    // Color
    private int mCircleColor;
    private int mCircleButtonColor;
    private int mLineColor;
    private int mHighlightLineColor;
    private int mNumberColor;
    private int mTimerNumberColor;
    private int mTimerTextColor;

    // Parameters
    private float mCx;
    private float mCy;
    private float mRadius;
    private float mCurrentRadian; // 当前弧度
    private float mPreRadian;
    private boolean mInCircleButton;
    private int mCurrentTime; // seconds
    private String mHintText;

    // control
    private boolean mIsRepeatRound = true; //是否可重复旋转
    private int mSmallTickMarkNum; // 小刻度线数
    private int mBigTickMarkNum; // 大刻度线数

    public DraggableCircleView(Context context) {
        this(context, null);
    }

    public DraggableCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        Log.d(TAG, "initialize");
        // Set default dimension or read xml attributes
        mGapBetweenCircleAndLine = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE, getContext().getResources().getDisplayMetrics());
        mGapBetweenNumberAndLine = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP_BETWEEN_NUMBER_AND_LINE, getContext().getResources().getDisplayMetrics());
        mNumberSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_NUMBER_SIZE, getContext().getResources().getDisplayMetrics());
        mLineLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_LENGTH, getContext().getResources().getDisplayMetrics());
        mLongerLineLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LONGER_LINE_LENGTH, getContext().getResources().getDisplayMetrics());
        mLineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_WIDTH, getContext().getResources().getDisplayMetrics());
        mCircleButtonRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_BUTTON_RADIUS, getContext().getResources().getDisplayMetrics());
        mCircleStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_STROKE_WIDTH, getContext().getResources().getDisplayMetrics());
        mTimerNumberSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TIMER_NUMBER_SIZE, getContext().getResources().getDisplayMetrics());
        mTimerTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TIMER_TEXT_SIZE, getContext().getResources().getDisplayMetrics());
        mGapBetweenTimerNumberAndText = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP_BETWEEN_TIMER_NUMBER_AND_TEXT, getContext().getResources().getDisplayMetrics());

        // Set default color or read xml attributes
        mCircleColor = DEFAULT_CIRCLE_COLOR;
        mCircleButtonColor = DEFAULT_CIRCLE_BUTTON_COLOR;
        mLineColor = DEFAULT_LINE_COLOR;
        mHighlightLineColor = DEFAULT_HIGHLIGHT_LINE_COLOR;
        mNumberColor = DEFAULT_NUMBER_COLOR;
        mTimerNumberColor = DEFAULT_TIMER_NUMBER_COLOR;
        mTimerTextColor = DEFAULT_TIMER_TEXT_COLOR;

        // Set default data
        mSmallTickMarkNum = DEFAULT_SMALL_TICK_MARK_NUM;
        mBigTickMarkNum = DEFAULT_BIG_TICK_MARK_NUM;

        // Init all paints
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimerNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimerColonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // CirclePaint
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);

        // CircleButtonPaint
//        mCircleButtonPaint.setColor(mCircleButtonColor);
//        mCircleButtonPaint.setAntiAlias(true); // 设置抗锯齿平滑
//        mCircleButtonPaint.setStyle(Paint.Style.FILL);

        // HighlightLinePaint
        mHighlightLinePaint.setColor(mHighlightLineColor);
        mHighlightLinePaint.setStrokeWidth(mLineWidth);

        // LinePaint
//        mLinePaint.setColor(mLineColor);
//        mLinePaint.setStrokeWidth(mLineWidth);

        // NumberPaint
        mNumberPaint.setColor(mNumberColor);
        mNumberPaint.setTextSize(mNumberSize);
        mNumberPaint.setTextAlign(Paint.Align.CENTER);

        // TimerNumberPaint
        mTimerNumberPaint.setColor(mTimerNumberColor);
        mTimerNumberPaint.setTextSize(mTimerNumberSize);
        mTimerNumberPaint.setTextAlign(Paint.Align.CENTER);

        // TimerTextPaint
        mTimerTextPaint.setColor(mTimerTextColor);
        mTimerTextPaint.setTextSize(mTimerTextSize);
        mTimerTextPaint.setTextAlign(Paint.Align.CENTER);

        // TimerColonPaint
        mTimerColonPaint.setColor(DEFAULT_TIMER_COLON_COLOR);
        mTimerColonPaint.setTextAlign(Paint.Align.CENTER);
        mTimerColonPaint.setTextSize(mTimerNumberSize);

        mHintText = "show what";
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure");
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        this.mCx = width / 2;
        this.mCy = height / 2;
        // Radius
        if (mLineLength / 2 + mGapBetweenCircleAndLine + mCircleStrokeWidth >= mCircleButtonRadius) {
            this.mRadius = width / 2 - mCircleStrokeWidth / 2;
            Log.d(TAG, "No exceed");
        } else {
            this.mRadius = width / 2 - (mCircleButtonRadius - mGapBetweenCircleAndLine - mLineLength / 2 -
                    mCircleStrokeWidth / 2);
            Log.d(TAG, "Exceed");
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");
        // Content
        canvas.drawCircle(mCx, mCy, mRadius, mCirclePaint);
        // Scale line
        int interevalNum = 1;
        if (mSmallTickMarkNum % mBigTickMarkNum == 0) {
            interevalNum = mSmallTickMarkNum / mBigTickMarkNum;
        }
        canvas.save();
        for (int i = 0; i < mSmallTickMarkNum; i++) {
            canvas.save();
            canvas.rotate(360 / mSmallTickMarkNum * i, mCx, mCy);
            if (i % interevalNum == 0) {
                if (360 / mSmallTickMarkNum * i <= Math.toDegrees(mCurrentRadian)) {
                    canvas.drawLine(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                            mLongerLineLength, mHighlightLinePaint);
                } else {
                    canvas.drawLine(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                            mLongerLineLength, mLinePaint);
                }
            } else {
                if (360 / mSmallTickMarkNum * i <= Math.toDegrees(mCurrentRadian)) {
                    canvas.drawLine(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine + mLineLength, mHighlightLinePaint);
                } else {
                    canvas.drawLine(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine + mLineLength, mLinePaint);
                }
            }
            canvas.restore();
        }
        canvas.restore();
        // Number it is rubbish code
        float textLength = mNumberPaint.measureText("15");
        canvas.drawText("60", mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                mLongerLineLength + mGapBetweenNumberAndLine + getFontHeight(mNumberPaint), mNumberPaint);
        canvas.drawText("15", mCx + mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine - mLongerLineLength -
                textLength / 2 - mGapBetweenNumberAndLine, mCy + getFontHeight(mNumberPaint) / 2, mNumberPaint);
        canvas.drawText("30", mCx, getMeasuredHeight() / 2 + mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine -
                mLongerLineLength - mGapBetweenNumberAndLine, mNumberPaint);
        canvas.drawText("45", getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                mLongerLineLength + mGapBetweenNumberAndLine + textLength / 2, mCy + getFontHeight(mNumberPaint) / 2, mNumberPaint);
        // Circle button
        canvas.save();
        canvas.rotate((float) Math.toDegrees(mCurrentRadian), mCx, mCy);
        canvas.drawCircle(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                mLineLength / 2, mCircleButtonRadius, mCircleButtonPaint);
        canvas.restore();
        // TimerNumber
        canvas.save();
        canvas.drawText((mCurrentTime / 60 < 10 ? "0" + mCurrentTime / 60 : mCurrentTime / 60) + " " + (mCurrentTime % 60 < 10 ? "0" + mCurrentTime % 60 : mCurrentTime % 60), mCx, mCy + getFontHeight(mTimerNumberPaint) / 2, mTimerNumberPaint);
        canvas.drawText(":", mCx, mCy + getFontHeight(mTimerNumberPaint) / 2, mTimerColonPaint);
        canvas.restore();
        // Timer Text
        canvas.save();
        canvas.drawText(mHintText, mCx, mCy + getFontHeight(mTimerNumberPaint) / 2 + mGapBetweenTimerNumberAndText + getFontHeight(mTimerTextPaint) / 2, mTimerTextPaint);
        canvas.restore();
    }

    private float getFontHeight(Paint paint) {
        // FontMetrics sF = paint.getFontMetrics();
        // return sF.descent - sF.ascent;
        Rect rect = new Rect();
        paint.getTextBounds("1", 0, 1, rect);
        return rect.height();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // If the point in the circle button
                if (mInCircleButton(event.getX(), event.getY()) && isEnabled()) {
                    mInCircleButton = true;
                    mPreRadian = getRadian(event.getX(), event.getY());
                    Log.d(TAG, "In circle button");
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mInCircleButton && isEnabled()) {
                    float temp = getRadian(event.getX(), event.getY());
                    // 处理2 * Math.PI零界点
                    if (mPreRadian > Math.toRadians(270) && temp < Math.toRadians(90)) { // 顺时针穿过零界点
                        mPreRadian -= 2 * Math.PI;
                    } else if (mPreRadian < Math.toRadians(90) && temp > Math.toRadians(270)) { // 逆时针穿过零界点
                        mPreRadian = (float) (temp + (temp - 2 * Math.PI) - mPreRadian);
                    }
                    mCurrentRadian += (temp - mPreRadian);
                    mPreRadian = temp;
                    if (mIsRepeatRound) {
                        if (mCurrentRadian < 0) {
                            mCurrentRadian = 0;
                        }
                    } else {
                        if (mCurrentRadian > 2 * Math.PI) {
                            mCurrentRadian = (float) (2 * Math.PI);
                        } else if (mCurrentRadian < 0) {
                            mCurrentRadian = 0;
                        }
                    }
//                    if (mCircleTimerListener != null)
//                        mCircleTimerListener.onTimerSetValueChange(getCurrentTime());
                    mCurrentTime = (int) (60 / (2 * Math.PI) * mCurrentRadian * 60);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mInCircleButton && isEnabled()) {
                    mInCircleButton = false;
//                    if (mCircleTimerListener != null)
//                        mCircleTimerListener.onTimerSetValueChanged(getCurrentTime());
                }
                // 判断标准，开启缓冲动画
                /*if (mCurrentRadian - mPreRadian < Math.PI / 4) {
                    Log.e(TAG, "还原mCurrentRadian:" + mCurrentRadian + ", mPreRadian:" + mPreRadian);
                    // 还原
                    mCurrentRadian = mPreRadian;
                    postInvalidate();
                }*/
                break;
        }
        return true;
    }

    /**
     * Whether the down event inside circle button
     *
     * @param x
     * @param y
     * @return
     */
    private boolean mInCircleButton(float x, float y) {
        float r = mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine - mLineLength / 2;
        float x2 = (float) (mCx + r * Math.sin(mCurrentRadian));
        float y2 = (float) (mCy - r * Math.cos(mCurrentRadian));
        if (Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2)) < mCircleButtonRadius) {
            return true;
        }
        return false;
    }

    /**
     * Use tri to cal radian
     * 计算弧度
     *
     * @param x
     * @param y
     * @return
     */
    private float getRadian(float x, float y) {
        float alpha = (float) Math.atan((x - mCx) / (mCy - y));
        // Quadrant
        if (x > mCx && y > mCy) {
            // 2
            alpha += Math.PI;
        } else if (x < mCx && y > mCy) {
            // 3
            alpha += Math.PI;
        } else if (x < mCx && y < mCy) {
            // 4
            alpha = (float) (2 * Math.PI + alpha);
        }
        return alpha;
    }

    /**
     * 回弹动画
     *
     * @param progress 当前所占比例
     */
    private void animateIndicator(float progress) {
        Interpolator interpolator = new AnticipateOvershootInterpolator(1.8f);
        ObjectAnimator animation = ObjectAnimator.ofFloat(this, "progress", progress);
        animation.setDuration(3000);
        animation.setInterpolator(interpolator);
        animation.start();
    }

    /**
     * 设置小可都总数(平均分为多少份)
     *
     * @param bigTickMarkNum
     */
    public void setBigTickMarkNum(int bigTickMarkNum) {
        this.mBigTickMarkNum = bigTickMarkNum;
    }

    /**
     * 设置大刻度数
     *
     * @param smallTickMarkNum
     */
    public void setSmallTickMarkNum(int smallTickMarkNum) {
        this.mSmallTickMarkNum = smallTickMarkNum;
    }
}
