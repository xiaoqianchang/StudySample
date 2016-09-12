package com.changxiao.draggablecircledemo.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;

import com.changxiao.draggablecircledemo.R;
import com.changxiao.draggablecircledemo.utils.ZRUtils;

import java.text.DecimalFormat;

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
public class DraggableCircleViewTwo extends View {

    private final String TAG = DraggableCircleView.class.getSimpleName();

    /**
     * 圆圈的类型，目标金额or达成日期
     */
    private int type;
    private static final int TYPE_TARGETMONEY = 0;
    private static final int TYPE_TARGETDATE = 1;

    // Default dimension in dp/pt
    private static final float DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE = 0;
    private static final float DEFAULT_GAP_BETWEEN_NUMBER_AND_LINE = 5;
    private static final float DEFAULT_TICK_MARK_VALUE_SIZE = 10;
    private static final float DEFAULT_LINE_LENGTH = 14;
    private static final float DEFAULT_LONGER_LINE_LENGTH = 23;
    private static final float DEFAULT_LINE_WIDTH = 1f;
    private static final float DEFAULT_CIRCLE_BUTTON_RADIUS = 15;
    private static final float DEFAULT_CIRCLE_STROKE_WIDTH = 1;
    private static final float DEFAULT_MIDDLE_VALUE_SIZE = 38;
    private static final float DEFAULT_MIDDLE_TEXT_SIZE = 32;
    private static final float DEFAULT_GAP_BETWEEN_TIMER_NUMBER_AND_TEXT = 30;

    // Default color
    private static final int DEFAULT_CIRCLE_COLOR = 0xFFE4358C;
    private static final int DEFAULT_CIRCLE_RING_COLOR = 0xFFE4358C;
    private static final int DEFAULT_INNER_CIRCLE_COLOR = Color.RED;
    private static final int DEFAULT_CIRCLE_BUTTON_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_LINE_COLOR = 0xFFE9E2D9;
    private static final int DEFAULT_HIGHLIGHT_LINE_COLOR = 0xFF68C5D7;
    private static final int DEFAULT_TICK_MARK_VALUE_COLOR = 0x99866A60;
    private static final int DEFAULT_MIDDLE_VALUE_COLOR = 0xFFE80F7B;
    private static final int DEFAULT_MIDDLE_TEXT_COLOR = 0xFFE80F7B;

    private static final int DEFAULT_SMALL_TICK_MARK_NUM = 10;
    private static final int DEFAULT_BIG_TICK_MARK_NUM = 0;

    // Paint
    private Paint mCirclePaint;
    private Paint mCircleRingPaint;
    private Paint mInnerCirclePaint;
    private Paint mHighlightLinePaint;
    private Paint mLinePaint;
    private Paint mCircleButtonPaint;
    private Paint mTickMarkValuePaint;
    private Paint mMiddleValuePaint;
    private Paint mMiddleTextPaint;

    // Dimension
    private float mGapBetweenCircleAndLine;
    private float mGapBetweenInnerCircleAndLine;
    private float mGapBetweenNumberAndLine;
    private float mTickMarkValueSize;
    private float mLineLength;
    private float mLongerLineLength;
    private float mLineWidth;
    private float mCircleButtonRadius;
    private float mCircleStrokeWidth;
    private float mCircleRingStrokeWidth;
    private float mInnerCircleStrokeWidth;
    private float mMiddleValueSize;
    private float mMiddleTextSize;
    private float mGapBetweenTimerNumberAndText;

    // Color
    private int mCircleColor;
    private int mCircleRingColor;
    private int mInnerCircleColor;
    private int mCircleButtonColor;
    private int mLineColor;
    private int mHighlightLineColor;
    private int mTickMarkValueColor;
    private int mMiddleValueColor;
    private int mMiddleTextColor;

    // Parameters
    private float mCx;
    private float mCy;
    private float mRadius; // 大圆半径
    private float mInnerRadius; // 大圆内圆半径
    private float mCurrentRadian; // 当前弧度
    private float mPreRadian;
    private boolean mInCircleButton;
    private double mCurrentValue;
    private String mMiddleText;
    private String mMiddleValue;
    private float mStartDegrees; // 起始角度

    // control
    private boolean mIsRepeatRound = true; //是否可重复旋转
    private int mSmallTickMarkNum; // 一圈小刻度线数
    private int mBigTickMarkNum; // 一圈大刻度线数
    private boolean mHasBigTickMark; // 是否有大刻度线
    private double[] mEachCircleTotal = new double[] {10, 20}; // 每圈的总金额，初始化数组大小自少为1
    private int mCurrentCircle = 0; // 当前是第几圈，从0开始计

    public DraggableCircleViewTwo(Context context) {
        this(context, null);
    }

    public DraggableCircleViewTwo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableCircleViewTwo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        Log.d(TAG, "initialize");
        // Set default dimension or read xml attributes
        mGapBetweenCircleAndLine = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE, getContext().getResources().getDisplayMetrics());
        mGapBetweenInnerCircleAndLine = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE, getContext().getResources().getDisplayMetrics());
        mGapBetweenNumberAndLine = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP_BETWEEN_NUMBER_AND_LINE, getContext().getResources().getDisplayMetrics());
        mTickMarkValueSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TICK_MARK_VALUE_SIZE, getContext().getResources().getDisplayMetrics());
        mLineLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_LENGTH, getContext().getResources().getDisplayMetrics());
        mLongerLineLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LONGER_LINE_LENGTH, getContext().getResources().getDisplayMetrics());
        mLineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_WIDTH, getContext().getResources().getDisplayMetrics());
        mCircleButtonRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_BUTTON_RADIUS, getContext().getResources().getDisplayMetrics());
        mCircleStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_STROKE_WIDTH, getContext().getResources().getDisplayMetrics());
        mCircleRingStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_LENGTH, getContext().getResources().getDisplayMetrics());
        mInnerCircleStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CIRCLE_STROKE_WIDTH, getContext().getResources().getDisplayMetrics());
        mMiddleValueSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_MIDDLE_VALUE_SIZE, getContext().getResources().getDisplayMetrics());
        mMiddleTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_MIDDLE_TEXT_SIZE, getContext().getResources().getDisplayMetrics());
        mGapBetweenTimerNumberAndText = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP_BETWEEN_TIMER_NUMBER_AND_TEXT, getContext().getResources().getDisplayMetrics());

        // Set default color or read xml attributes
        mCircleColor = DEFAULT_CIRCLE_COLOR;
        mCircleRingColor = DEFAULT_CIRCLE_RING_COLOR;
        mInnerCircleColor = DEFAULT_INNER_CIRCLE_COLOR;
        mCircleButtonColor = DEFAULT_CIRCLE_BUTTON_COLOR;
        mLineColor = DEFAULT_LINE_COLOR;
        mHighlightLineColor = DEFAULT_HIGHLIGHT_LINE_COLOR;
        mTickMarkValueColor = DEFAULT_TICK_MARK_VALUE_COLOR;
        mMiddleValueColor = DEFAULT_MIDDLE_VALUE_COLOR;
        mMiddleTextColor = DEFAULT_MIDDLE_TEXT_COLOR;

        // Set default data
        mSmallTickMarkNum = DEFAULT_SMALL_TICK_MARK_NUM;
        mBigTickMarkNum = DEFAULT_BIG_TICK_MARK_NUM;

        // Init paints
        initPaint();

        mMiddleText = "show what";
        mMiddleValue = "";

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DraggableCircleView);
        type = a.getInt(R.styleable.DraggableCircleView_type, TYPE_TARGETMONEY);// 默认为目标金额
        a.recycle();
    }

    private void initPaint() {
        // Init all paints
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 消除锯齿
        mCircleRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 消除锯齿
        mInnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 消除锯齿
        mCircleButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTickMarkValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMiddleValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMiddleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // CirclePaint
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE); // 绘制空心圆
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);

        // CircleRingPaint
        mCircleRingPaint.setColor(mCircleRingColor);
        mCircleRingPaint.setStyle(Paint.Style.STROKE); // 绘制空心圆
        mCircleRingPaint.setStrokeWidth(mCircleRingStrokeWidth); // this.mRadius - mCircleStrokeWidth / 2 - (this.mInnerRadius + mInnerCircleStrokeWidth / 2)

        // InnerCirclePaint
        mInnerCirclePaint.setColor(mInnerCircleColor);
        mInnerCirclePaint.setStyle(Paint.Style.STROKE); // 绘制空心圆
        mInnerCirclePaint.setStrokeWidth(mInnerCircleStrokeWidth);

        // CircleButtonPaint
        mCircleButtonPaint.setColor(mCircleButtonColor);
        mCircleButtonPaint.setAntiAlias(true); // 设置抗锯齿平滑
        mCircleButtonPaint.setStyle(Paint.Style.FILL);

        // HighlightLinePaint
        mHighlightLinePaint.setColor(mHighlightLineColor);
        mHighlightLinePaint.setStrokeWidth(mLineWidth);

        // LinePaint
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineWidth);

        // NumberPaint
        mTickMarkValuePaint.setColor(mTickMarkValueColor);
        mTickMarkValuePaint.setTextSize(mTickMarkValueSize);
        mTickMarkValuePaint.setTextAlign(Paint.Align.CENTER);

        // TimerNumberPaint
        mMiddleValuePaint.setColor(mMiddleValueColor);
        mMiddleValuePaint.setTextSize(mMiddleValueSize);
        mMiddleValuePaint.setTextAlign(Paint.Align.CENTER);

        // TimerTextPaint
        mMiddleTextPaint.setColor(mMiddleTextColor);
        mMiddleTextPaint.setTextSize(mMiddleTextSize);
        mMiddleTextPaint.setTextAlign(Paint.Align.CENTER);
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
            this.mRadius = width / 2 - (mCircleButtonRadius - mGapBetweenCircleAndLine - mGapBetweenInnerCircleAndLine - mLineLength / 2 -
                    mCircleStrokeWidth / 2 - mInnerCircleStrokeWidth / 2);
            Log.d(TAG, "Exceed");
        }
        float innerRadius = this.mRadius - mCircleStrokeWidth / 2 - mCircleRingStrokeWidth - mInnerCircleStrokeWidth / 2;
        this.mInnerRadius = innerRadius >= 0 ? innerRadius : 0;
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
        // Draw ring
        canvas.drawCircle(mCx, mCy, mInnerRadius + 1 + mCircleRingStrokeWidth / 2, mCircleRingPaint);
        // Draw inner circle
        canvas.drawCircle(mCx, mCy, mInnerRadius, mInnerCirclePaint);
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
            canvas.rotate(360 / mSmallTickMarkNum * i + mStartDegrees, mCx, mCy);
            if (mHasBigTickMark && i % interevalNum == 0) { // 长刻度线
                if (360 / mSmallTickMarkNum * i + mStartDegrees <= Math.toDegrees(mCurrentRadian)) { // 小球之前高亮mHighlightLinePaint
                    canvas.drawLine(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                            mLongerLineLength, mHighlightLinePaint);
                } else { // 小球之后正常mLinePaint
                    canvas.drawLine(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine +
                            mLongerLineLength, mLinePaint);
                }
            } else { // 短刻度线
                if (360 / mSmallTickMarkNum * i + mStartDegrees <= Math.toDegrees(mCurrentRadian)) {
                    canvas.drawLine(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine + mLineLength, mHighlightLinePaint);
                } else {
                    canvas.drawLine(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine, mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine + mLineLength, mLinePaint);
                }
            }
            canvas.restore();
        }
        canvas.restore();
        // Circle button
        /**
         * 要点：
         * 1.画小圆其实是在原点画小圆，根据拖动的弧度动态旋转画布
         * 2.canvas.save()和canvas.restore()必须要，不然画布旋转
         */
        canvas.save();
        canvas.rotate((float) Math.toDegrees(mCurrentRadian) + mStartDegrees, mCx, mCy);
        canvas.drawCircle(mCx, getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mCircleRingStrokeWidth / 2, mCircleButtonRadius, mCircleButtonPaint);
        //        Log.d(TAG, "mCircleButton mCx=" + mCx + ", mCy=" + (getMeasuredHeight() / 2 - mRadius + mCircleStrokeWidth / 2 + mGapBetweenCircleAndLine + mLineLength / 2));
        canvas.restore();
        // TimerNumber
        canvas.save();
        canvas.drawText(mMiddleValue, mCx, mCy + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getContext().getResources().getDisplayMetrics()) + getFontHeight(mMiddleValuePaint) / 2, mMiddleValuePaint);
        canvas.restore();
        // Timer Text
        canvas.save();
        canvas.drawText(mMiddleText, mCx, mCy - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getContext().getResources().getDisplayMetrics()) - getFontHeight(mMiddleTextPaint) / 2, mMiddleTextPaint);
        canvas.restore();
    }

    /**
     * 格式化双精度数据
     *
     * @param d
     * @return
     */
    public static String getDecimalFormat(double d) {
        try {
            DecimalFormat _df = new DecimalFormat("######0.0");
            return _df.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
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
                    } else if (mPreRadian < Math.toRadians(90) && temp > Math.toRadians(270)) { // 逆时针穿过零界点
                        mPreRadian = (float) (temp + (temp - 2 * Math.PI) - mPreRadian);
                        //                        Log.i(TAG, "逆时针mPreRadian: " +mPreRadian);
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
                    // 计算圈数
                    mCurrentCircle = getCurrentCircle(mCurrentRadian);
                    // 滚动计算当前金钱
                    //                    Log.i(TAG, "当前第"+ mCurrentCircle +"圈");
                    double beforeSum = 0;
                    if (mCurrentCircle == 0) {
                        mCurrentValue = mEachCircleTotal[mCurrentCircle] / (2 * Math.PI) * mCurrentRadian;
                    } else if (mCurrentCircle > 0 && mCurrentCircle < mEachCircleTotal.length)  {
                        for (int cur = 0; cur < mCurrentCircle; cur++) {
                            beforeSum += mEachCircleTotal[cur];
                        }
                        double tempMoney = mEachCircleTotal[mCurrentCircle] / (2 * Math.PI) * (mCurrentRadian - mCurrentCircle * 2 * Math.PI);
                        mCurrentValue = beforeSum + tempMoney;
                    } else {
                        // 当目前圈数大于给定数组的大小
                        for (int cur = 0; cur < mEachCircleTotal.length; cur++) {
                            beforeSum += mEachCircleTotal[cur];
                        }
                        double tempMoney = mEachCircleTotal[mEachCircleTotal.length - 1] / (2 * Math.PI) * (mCurrentRadian - (2 * Math.PI) * mEachCircleTotal.length);
                        mCurrentValue = beforeSum + tempMoney;
                    }
                    calculateMiddleValue();
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
     * 计算中间显示的文本值
     */
    private void calculateMiddleValue() {
        if (type == TYPE_TARGETMONEY) {
            mMiddleValue = String.valueOf(getDecimalFormat(mCurrentValue / 10000)) + "万";
        } else if (type == TYPE_TARGETDATE) {
            // 初始值
            mMiddleValue = String.valueOf(ZRUtils.getCurrentYear()) + "." + String.valueOf(ZRUtils.getCurrentMonth());
            int years = (int) ((mCurrentValue + ZRUtils.getCurrentMonth()) / 12);
            int months = (int) ((mCurrentValue + ZRUtils.getCurrentMonth()) % 12);
            if (((int) mCurrentValue + ZRUtils.getCurrentMonth()) % 12 == 0) {
                months = 12;
                years = years - 1;

            }
            Log.i(TAG, "mCurrentValue=" + mCurrentValue + ", years=" + years + ", months=" + months);
            mMiddleValue = String.valueOf(ZRUtils.getCurrentYear() + years) + "." + (months < 10 ? "0" + String.valueOf(months) : String.valueOf(months));
        }
    }

    private double calculateValue(float currentRadian) {
        double value = 0;
        float leftSection = 0; // 左区间(闭区间)
        float rightSection = 0; // 右区间(开区间)
        int i = 0;
        while (true) {
            if (i > 0) {
                leftSection += 2 * Math.PI;
            }
            rightSection += 2 * Math.PI;
            if (currentRadian >= leftSection && currentRadian < rightSection) {
                if (i == 0) {
                    value = mEachCircleTotal[i] * currentRadian;
                    return value;
                }
                for (int j = 0; j < i; j++) {
                    value += mEachCircleTotal[j];
                }
                value += mEachCircleTotal[i] * (currentRadian - leftSection);
                return value;
            }
            i = ++ i;
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

    private int getCurrentCircle(float currentRadian) {
        float leftSection = 0; // 左区间(闭区间)
        float rightSection = 0; // 右区间(开区间)
        int i = 0;
        while (true) {
            if (i > 0) {
                leftSection += 2 * Math.PI;
            }
            rightSection += 2 * Math.PI;
            if (currentRadian >= leftSection && currentRadian < rightSection) {
                return i;
            }
            i = ++ i;
        }
    }

    /**
     * 计算小刻度间的间隔值
     *
     * @return
     */
    private double getSmallTickMarkIntervalValue() {
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

    public Builder newBuilder() {
        return new Builder();
    }

    /**************************************public api***********************************************/

    public final class Builder {

        /**
         * 设置初始化角度
         *
         * @param startDegrees
         */
        public Builder setStartDegrees(int startDegrees) {
            mStartDegrees = startDegrees;
            mCurrentRadian = (float) Math.toRadians(startDegrees);
            return this;
        }

        /**
         * 设置小刻度总数(平均分为多少份)
         *
         * @param bigTickMarkNum
         */
        public Builder setBigTickMarkNum(int bigTickMarkNum) {
            mBigTickMarkNum = bigTickMarkNum;
            return this;
        }

        /**
         * 设置大刻度数
         *
         * @param smallTickMarkNum
         */
        public Builder setSmallTickMarkNum(int smallTickMarkNum) {
            mSmallTickMarkNum = smallTickMarkNum;
            return this;
        }

        /**
         * 当前圈的总金额
         *
         * @param eachCircleTotal
         */
        public Builder setEachCircleTotal(double[] eachCircleTotal) {
            mEachCircleTotal = eachCircleTotal;
            return this;
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
        public Builder setCurrentValue(int currentValue) {
            mCurrentValue = currentValue;
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
                    mCurrentRadian = (float) (i * 2 * Math.PI + 2 * Math.PI / mEachCircleTotal[i] * (currentValue - leftSection));
                    return this;
                }
            }
            // 当前大小超过给定没圈值得总和
            while (true) {
                Log.i(TAG, "mCurrentCircle=" + mCurrentCircle);
                mCurrentCircle ++;
                leftSection += mEachCircleTotal[mEachCircleTotal.length - 1];
                rightSection += mEachCircleTotal[mEachCircleTotal.length - 1];
                if (currentValue >= leftSection && currentValue < rightSection) {
                    mCurrentRadian = (float) (mCurrentCircle * 2 * Math.PI + 2 * Math.PI / mEachCircleTotal[mEachCircleTotal.length - 1] * (currentValue - leftSection));
                    break;
                }
                if (mEachCircleTotal[mEachCircleTotal.length - 1] == 0) {
                    // 始终找不到，小球停在原点
                    mCurrentCircle = 0;
                    break;
                }
            }
            invalidate();
            return this;
        }

        /**
         * Set color for circle
         *
         * @param circleColor
         */
        public Builder setCircleColor(int circleColor) {
            mCircleColor = circleColor;
            return this;
        }

        /**
         * Set color for circleRing
         *
         * @param circleRingColor
         */
        public Builder setCircleRingColor(int circleRingColor) {
            mCircleRingColor = circleRingColor;
            return this;
        }

        /**
         * Set color for circleRing
         *
         * @param circleRingStrokeWidth
         */
        public Builder setCircleRingStrokeWidth (int circleRingStrokeWidth) {
            mCircleRingStrokeWidth = circleRingStrokeWidth;
            return this;
        }

        /**
         * Set color for innerCircle
         *
         * @param innerCircleColor
         */
        public Builder setInnerCircleColor(int innerCircleColor) {
            mInnerCircleColor = innerCircleColor;
            return this;
        }

        /**
         * Set color for circleButton
         *
         * @param circleButtonColor
         */
        public Builder setCircleButtonColor(int circleButtonColor) {
            mCircleButtonColor = circleButtonColor;
            return this;
        }

        /**
         * Set color for tickMarkLine
         *
         * @param lineColor
         */
        public Builder setLineColor(int lineColor) {
            mLineColor = lineColor;
            return this;
        }

        /**
         * Set color for highlightTickMarkLine
         *
         * @param highlightLineColor
         */
        public Builder setHighlightLineColor(int highlightLineColor) {
            mHighlightLineColor = highlightLineColor;
            return this;
        }

        /**
         * Set color for tickMarkValue
         *
         * @param tickMarkValueColor
         */
        public Builder setTickMarkValueColor(int tickMarkValueColor) {
            mTickMarkValueColor = tickMarkValueColor;
            return this;
        }

        /**
         * Set color for middleValue
         *
         * @param middleValueColor
         */
        public Builder setMiddleValueColor(int middleValueColor) {
            mMiddleValueColor = middleValueColor;
            return this;
        }

        /**
         * Set color for middleText
         *
         * @param middleTextColor
         */
        public Builder setMiddleTextColor(int middleTextColor) {
            mMiddleTextColor = middleTextColor;
            return this;
        }

        /**
         * Set dimension for gapBetweenCircleAndLine
         *
         * @param gapBetweenCircleAndLine
         */
        public Builder setGapBetweenCircleAndLine(float gapBetweenCircleAndLine) {
            mGapBetweenCircleAndLine = gapBetweenCircleAndLine;
            return this;
        }

        /**
         * Set dimension for gapBetweenNumberAndLine
         *
         * @param gapBetweenNumberAndLine
         */
        public Builder setGapBetweenNumberAndLine(float gapBetweenNumberAndLine) {
            mGapBetweenNumberAndLine = gapBetweenNumberAndLine;
            return this;
        }

        /**
         * Set dimension for tickMarkValueSize
         *
         * @param tickMarkValueSize
         */
        public Builder setTickMarkValueSize(float tickMarkValueSize) {
            mTickMarkValueSize = tickMarkValueSize;
            return this;
        }

        /**
         * Set dimension for lineLength
         *
         * @param lineLength
         */
        public Builder setLineLength(float lineLength) {
            mLineLength = lineLength;
            return this;
        }

        /**
         * Set dimension for longerLineLength
         *
         * @param longerLineLength
         */
        public Builder setLongerLineLength(float longerLineLength) {
            mLongerLineLength = longerLineLength;
            return this;
        }

        /**
         * Set dimension for lineWidth
         *
         * @param lineWidth
         */
        public Builder setLineWidth(float lineWidth) {
            mLineWidth = lineWidth;
            return this;
        }

        /**
         * Set dimension for circleButtonRadius
         *
         * @param circleButtonRadius
         */
        public Builder setCircleButtonRadius(float circleButtonRadius) {
            mCircleButtonRadius = circleButtonRadius;
            return this;
        }

        /**
         * Set dimension for circleStrokeWidth
         *
         * @param circleStrokeWidth
         */
        public Builder setCircleStrokeWidth(float circleStrokeWidth) {
            mCircleStrokeWidth = circleStrokeWidth;
            return this;
        }

        /**
         * Set dimension for middleValueSize
         *
         * @param middleValueSize
         */
        public Builder setMiddleValueSize(float middleValueSize) {
            mMiddleValueSize = middleValueSize;
            return this;
        }

        /**
         * Set dimension for middleTextSize
         *
         * @param middleTextSize
         */
        public Builder setMiddleTextSize(float middleTextSize) {
            mMiddleTextSize = middleTextSize;
            return this;
        }

        /**
         * Set String for middleText
         *
         * @param middleText
         */
        public Builder setMiddleText(String middleText) {
            mMiddleText = middleText;
            return this;
        }

        /**
         * Set String for middleValueSize
         *
         * @param middleValue
         */
        public Builder setMiddleValue(String middleValue) {
            mMiddleValue = String.valueOf(Integer.parseInt(middleValue) < 10 ? "0" + middleValue : middleValue);
            return this;
        }

        public void build() {
            initPaint();
        }
    }
}
