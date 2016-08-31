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
 *
 * * API：
 * 1.setSmallTickMarkNum();设置小刻度线的数量，默认10条
 * 2.setBigTickMarkNum();设置大刻度线的数量，默认0条
 * 3.setEachCircleTotal(new int[] {});设置每圈的总值，默认每圈都是10
 * 4.setCurrentValue();设置小球的位置(并计算当前的弧度值、当前第几圈(从第零圈开始))
 * <p>
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

    private static final int DEFAULT_SMALL_TICK_MARK_NUM = 10;
    private static final int DEFAULT_BIG_TICK_MARK_NUM = 0;

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
    private int mCurrentValue;
    private String mHintText;

    // control
    private boolean mIsRepeatRound = true; //是否可重复旋转
    private int mSmallTickMarkNum; // 一圈小刻度线数
    private int mBigTickMarkNum; // 一圈大刻度线数
    private boolean mHasBigTickMark; // 是否有大刻度线
    private int[] mEachCircleTotal = new int[] {10, 20}; // 每圈的总金额，初始化数组大小自少为1
    private int mCurrentCircle = 0; // 当前是第几圈，从0开始计

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
        int interevalNum = 1; // 两个大刻度间隔的小刻度数
        if (mBigTickMarkNum > 0) {
            mHasBigTickMark = true;
            if (mSmallTickMarkNum % mBigTickMarkNum == 0) {
                interevalNum = mSmallTickMarkNum / mBigTickMarkNum;
            }
        } else {
            mHasBigTickMark = false;
            interevalNum = mSmallTickMarkNum;
        }
        canvas.save();
        for (int i = 0; i < mSmallTickMarkNum; i++) {
            canvas.save();
            canvas.rotate(360 / mSmallTickMarkNum * i, mCx, mCy);
            if (mHasBigTickMark && i % interevalNum == 0) { // 长刻度线
                if (360 / mSmallTickMarkNum * i <= Math.toDegrees(mCurrentRadian)) { // 小球之前高亮mHighlightLinePaint
                    canvas.drawLine(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                            mLongerLineLength, mHighlightLinePaint);
                } else { // 小球之后正常mLinePaint
                    canvas.drawLine(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                            mLongerLineLength, mLinePaint);
                }
            } else { // 短刻度线
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
        /*float textLength = mNumberPaint.measureText("15");
        canvas.drawText("60", mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                mLongerLineLength + mGapBetweenNumberAndLine + getFontHeight(mNumberPaint), mNumberPaint);
        canvas.drawText("15", mCx + mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine - mLongerLineLength -
                textLength / 2 - mGapBetweenNumberAndLine, mCy + getFontHeight(mNumberPaint) / 2, mNumberPaint);
        canvas.drawText("30", mCx, getMeasuredHeight() / 2 + mRadius - mCircleStrokeWidth / 2 - mGapBetweenCircleAndLine -
                mLongerLineLength - mGapBetweenNumberAndLine, mNumberPaint);
        canvas.drawText("45", getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                mLongerLineLength + mGapBetweenNumberAndLine + textLength / 2, mCy + getFontHeight(mNumberPaint) / 2, mNumberPaint);*/
        // Circle button
        /**
         * 要点：
         * 1.画小圆其实是在原点画小圆，根据拖动的弧度动态旋转画布
         * 2.canvas.save()和canvas.restore()必须要，不然画布旋转
         */
        canvas.save();
        canvas.rotate((float) Math.toDegrees(mCurrentRadian), mCx, mCy);
        canvas.drawCircle(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                mLineLength / 2, mCircleButtonRadius, mCircleButtonPaint);
//        Log.d(TAG, "mCircleButton mCx=" + mCx + ", mCy=" + (getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine + mLineLength / 2));
        canvas.restore();
        // TimerNumber
        canvas.save();
        canvas.drawText(String.valueOf(mCurrentValue), mCx, mCy + getFontHeight(mTimerNumberPaint) / 2, mTimerNumberPaint);
        canvas.restore();
        // Timer Text
        canvas.save();
        canvas.drawText(/*mHintText*/"第" + mCurrentCircle + "圈，每个刻度的间隔值是" + getSmallTickMarkIntervalValue(), mCx, mCy + getFontHeight(mTimerNumberPaint) / 2 + mGapBetweenTimerNumberAndText + getFontHeight(mTimerTextPaint) / 2, mTimerTextPaint);
        canvas.restore();
    }

    private float getFontHeight(Paint paint) {
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
//                        Log.i(TAG, "顺时针mPreRadian: " +mPreRadian);
                        mCurrentCircle++;
                    } else if (mPreRadian < Math.toRadians(90) && temp > Math.toRadians(270)) { // 逆时针穿过零界点
                        mPreRadian = (float) (temp + (temp - 2 * Math.PI) - mPreRadian);
//                        Log.i(TAG, "逆时针mPreRadian: " +mPreRadian);
                        if (mCurrentCircle != 0) {
                            mCurrentCircle--;
                        }
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
                    // 滚动计算当前金钱
//                    Log.i(TAG, "当前第"+ mCurrentCircle +"圈");
                    int beforeSum = 0;
                    if (mCurrentCircle == 0) {
                        mCurrentValue = (int) (mEachCircleTotal[mCurrentCircle] / (2 * Math.PI) * mCurrentRadian);
                    } else if (mCurrentCircle > 0 && mCurrentCircle < mEachCircleTotal.length)  {
                        for (int cur = 0; cur < mCurrentCircle; cur++) {
                            beforeSum += mEachCircleTotal[cur];
                        }
                        int tempMoney = (int) (mEachCircleTotal[mCurrentCircle] / (2 * Math.PI) * temp);
                        mCurrentValue = beforeSum + tempMoney;
                    } else {
                        // 当目前圈数大于给定数组的大小
                        for (int cur = 0; cur < mEachCircleTotal.length; cur++) {
                            beforeSum += mEachCircleTotal[cur];
                        }
                        int tempMoney = (int) (mEachCircleTotal[mEachCircleTotal.length - 1] / (2 * Math.PI) * (mCurrentRadian - (2 * Math.PI) * mEachCircleTotal.length));
                        mCurrentValue = beforeSum + tempMoney;
                    }
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

    private void calculateMoney(float mCurrentRadian, float temp) {
        /*if (mEachCircleTotal.length == 0) {
            mCurrentValue = (int) (mEachCircleTotal[0] / (2 * Math.PI) * mCurrentRadian);
            return;
        }
        if (temp == 0) {
            return;
        }
        for (int i = 0; i < mEachCircleTotal.length; i++) {
            if (mCurrentRadian >= i  * 2 * Math.PI && mCurrentRadian < (i + 1) * 2 * Math.PI) {
                int tempS = 0;
                for (int j = 0; j < i; j++) {
                    tempS += mEachCircleTotal[j];
                }
                mCurrentValue = tempS + (int) (mEachCircleTotal[i] / (2 * Math.PI) * temp);
                break;
            }
        }*/
        if (mCurrentRadian >= 0 && mCurrentRadian < 2 * Math.PI) {
            Log.i(TAG, "当前第"+ 0 +"圈" + " mCurrentValue=" + mCurrentValue + " mCurrentRadian=" + mCurrentRadian + " temp=" + temp);
            mCurrentValue = (int) (mEachCircleTotal[0] / (2 * Math.PI) * temp);
        } else if (mCurrentRadian >= 2*Math.PI && mCurrentRadian < 4 * Math.PI) {
            Log.i(TAG, "当前第"+ 1 +"圈" + " mCurrentValue=" + mCurrentValue + " mCurrentRadian=" + mCurrentRadian + " temp=" + temp);
            mCurrentValue = mEachCircleTotal[0] + (int) (mEachCircleTotal[1] / (2 * Math.PI) * temp);
        } else if (mCurrentRadian >= 4*Math.PI && mCurrentRadian < 6 * Math.PI) {
            Log.i(TAG, "当前第"+ 2 +"圈" + " mCurrentValue=" + mCurrentValue + " mCurrentRadian=" + mCurrentRadian + " temp=" + temp);
            mCurrentValue = mEachCircleTotal[0] + mEachCircleTotal[1] + (int) (mEachCircleTotal[2] / (2 * Math.PI) * temp);
        }
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
     * 计算小刻度间的间隔值
     *
     * @return
     */
    private int getSmallTickMarkIntervalValue() {
        return (mCurrentCircle < mEachCircleTotal.length ? mEachCircleTotal[mCurrentCircle] : mEachCircleTotal[mEachCircleTotal.length - 1]) / mSmallTickMarkNum;
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

    /**************************************public api***********************************************/

    /**
     * 设置小刻度总数(平均分为多少份)
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

    /**
     * 当前圈的总金额
     *
     * @param eachCircleTotal
     */
    public void setEachCircleTotal(int[] eachCircleTotal) {
        this.mEachCircleTotal = eachCircleTotal;
    }

    /**
     * 控件显示的起始值
     *
     * 规则：
     * 1.设定值在给定的每圈数组和之内则正常计算位置；
     * 2.设定值超过给定的每圈数组和则按照数组最后的间隔值累加并计算返回位置；
     * <p>
     *
     * @param currentValue
     */
    public void setCurrentValue(int currentValue) {
        this.mCurrentValue = currentValue;
        // 当前初始化值所对应的弧度值
        int leftSection = 0; // 左区间(闭区间)
        int rightSection = 0; // 右区间(开区间)
        for (int i = 0; i < mEachCircleTotal.length; i ++) {
            if (i > 0) {
                leftSection += mEachCircleTotal[i - 1];
            }
            rightSection += mEachCircleTotal[i];
            mCurrentCircle = i;
            if (currentValue >= leftSection && currentValue < rightSection) {
                this.mCurrentRadian = (float) (i * 2 * Math.PI + 2 * Math.PI / mEachCircleTotal[i] * (currentValue - leftSection));
                return;
            }
        }
        // 当前大小超过给定没圈值得总和
        while (true) {
            Log.i(TAG, "mCurrentCircle=" + mCurrentCircle);
            mCurrentCircle ++;
            leftSection += mEachCircleTotal[mEachCircleTotal.length - 1];
            rightSection += mEachCircleTotal[mEachCircleTotal.length - 1];
            if (currentValue >= leftSection && currentValue < rightSection) {
                this.mCurrentRadian = (float) (mCurrentCircle * 2 * Math.PI + 2 * Math.PI / mEachCircleTotal[mEachCircleTotal.length - 1] * (currentValue - leftSection));
                break;
            }
            if (mEachCircleTotal[mEachCircleTotal.length - 1] == 0) {
                // 始终找不到，小球停在原点
                mCurrentCircle = 0;
                break;
            }
        }
        invalidate();
    }
}
