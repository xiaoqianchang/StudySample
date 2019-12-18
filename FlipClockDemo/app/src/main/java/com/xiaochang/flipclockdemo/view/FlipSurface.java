package com.xiaochang.flipclockdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * $
 * <p>
 * Created by Chang.Xiao on 2019-12-06.
 *
 * @version 1.0
 */
public class FlipSurface extends SurfaceView implements Callback {
    static final int APPENDIX_LEFT_BIAS = 2;
    static final int AXIS_COLOR_HIGH = 579373192;
    static final int AXIS_COLOR_LOW = -16053493;
    static final int AXIS_COLOR_LOW_SHADOW = 1997212427;
    static final int AXIS_HEIGHT = 1;
    static final int BOTTOM_LEAF_COLOR_FROM = -13158601;
    static final int BOTTOM_LEAF_COLOR_TO = -13619152;
    static final int BOTTOM_THICK = 2;
    static final int BOTTOM_THICK_SHADOW = 1997607185;
    static final int COMMON_SHADOW = 1426063360;
    static final int GEAR_COLOR_FROM = -13227222;
    static final int GEAR_COLOR_TO = -14213348;
    static final int LIGHT_SHADOW = 570425344;
    static final int TOP_LEAF_COLOR_FROM = -14079703;
    static final int TOP_LEAF_COLOR_TO = -14079703;
    static final int TOP_THICK_SHADOW = 1997607185;
    float inR = 4.0f;
    AnimTask mAnimTask;
    Timer mAnimTimer;
    FontMetrics mAppendixFontMetrics;
    Paint mAppendixPaint = new Paint(AXIS_HEIGHT);
    Canvas mBMPcanvas;
    int mBackgroundColor = -65536;
    Bitmap mBitmap;
    GradientDrawable mBottomDrawable;
    ShapeDrawable mBottomShadow;
    ShapeDrawable mBottomThickShadow;
    int mBottomUnder = 6;
    boolean mDisplayGear = false;
    FontMetrics mFontMetrics;
    GradientDrawable mGearDrawable;
    int mGearHeight;
    int mGearWidth;
    boolean mHasSurface = false;
    String mLastAppendix;
    String mLastString;
    int mLeafHeight;
    int mLeafOut;
    int mLeafPeak = 3;
    int mLeafWidth;
    int mLeft;
    Matrix mMatrix = new Matrix();
    boolean mMovingLeafOnBack = true;
    Paint mPanelPaint = new Paint();
    Rect mRect;
    int mStringColor = -1;
    Paint mStringPaint = new Paint(AXIS_HEIGHT);
    SurfaceHolder mSurfaceHolder;
    int mTop;
    GradientDrawable mTopDrawable;
    ShapeDrawable mTopShadow;
    ShapeDrawable mTopThickShadow;
    float outR = 12.0f;

    class AnimTask extends TimerTask {

        /* renamed from: i */
        int f2i = 0;
        String mFrom;
        String mFromAppendix;
        Drawable mFromDrawable;
        int[] mHorizontalBias;
        String mTo;
        String mToAppendix;
        Drawable mToDrawable;
        int[] mVerticalBias;

        AnimTask(String from, String fromAppendix, String to, String toAppendix, boolean downwards) {
            this.mFrom = from;
            this.mTo = to;
            this.mFromAppendix = fromAppendix;
            this.mToAppendix = toAppendix;
            int[] iArr = new int[4];
            iArr[FlipSurface.AXIS_HEIGHT] = FlipSurface.this.mLeafOut / 2;
            iArr[2] = FlipSurface.this.mLeafOut;
            this.mHorizontalBias = iArr;
            int[] iArr2 = new int[4];
            iArr2[FlipSurface.AXIS_HEIGHT] = FlipSurface.this.mLeafOut;
            iArr2[2] = FlipSurface.this.mLeafHeight / 2;
            this.mVerticalBias = iArr2;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:17:0x002d, code lost:
            r3.f2i += mobi.intuitit.android.free.flipclock.FlipSurface.AXIS_HEIGHT;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0036, code lost:
            if (r3.f2i <= 6) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0038, code lost:
            cancel();
            r3.this$0.mLastString = r3.mTo;
            r3.this$0.mLastAppendix = r3.mToAppendix;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
            return;
         */
        public void run() {
            if (FlipSurface.this.mSurfaceHolder == null) {
                cancel();
                return;
            }
            synchronized (FlipSurface.this.mSurfaceHolder) {
                Canvas canvas = FlipSurface.this.mSurfaceHolder.lockCanvas();
                if (!drawFrame(canvas)) {
                    cancel();
                    return;
                }
                FlipSurface.this.mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

        private boolean drawFrame(Canvas canvas) {
            if (canvas == null) {
                return false;
            }
            canvas.drawColor(FlipSurface.this.mBackgroundColor);
            if (FlipSurface.this.mMovingLeafOnBack) {
                FlipSurface.this.drawLeavesBackground(canvas, this.f2i);
            } else if (FlipSurface.this.mBitmap != null) {
                canvas.drawBitmap(FlipSurface.this.mBitmap, 0.0f, 0.0f, null);
            }
            FlipSurface.this.mTopDrawable.setBounds(FlipSurface.this.mRect);
            FlipSurface.this.mBottomDrawable.setBounds(FlipSurface.this.mRect);
            FlipSurface.this.mTopShadow.setBounds(FlipSurface.this.mRect);
            FlipSurface.this.mBottomShadow.setBounds(FlipSurface.this.mRect);
            if (FlipSurface.this.mDisplayGear) {
                FlipSurface.this.drawGears(canvas, this.f2i);
            }
            canvas.save();
            canvas.translate((float) FlipSurface.this.mLeft, (float) FlipSurface.this.mTop);
            FlipSurface.this.mPanelPaint.setColor(FlipSurface.AXIS_COLOR_LOW);
            canvas.drawRect(FlipSurface.this.inR, (float) FlipSurface.this.mLeafHeight, ((float) FlipSurface.this.mLeafWidth) - FlipSurface.this.inR, (float) (FlipSurface.this.mLeafHeight + FlipSurface.AXIS_HEIGHT), FlipSurface.this.mPanelPaint);
            canvas.clipRect(-FlipSurface.this.mLeafOut, 0, FlipSurface.this.mLeafWidth + FlipSurface.this.mLeafOut, FlipSurface.this.mLeafHeight);
            Rect gearLeft = new Rect(0, FlipSurface.this.mLeafHeight - FlipSurface.this.mGearHeight, FlipSurface.this.mGearWidth, FlipSurface.this.mLeafHeight);
            Rect gearRight = new Rect(FlipSurface.this.mLeafWidth - FlipSurface.this.mGearWidth, FlipSurface.this.mLeafHeight - FlipSurface.this.mGearHeight, FlipSurface.this.mLeafWidth, FlipSurface.this.mLeafHeight);
            if (FlipSurface.this.mDisplayGear) {
                canvas.save();
                canvas.clipRect(gearLeft, Op.DIFFERENCE);
                canvas.clipRect(gearRight, Op.DIFFERENCE);
                FlipSurface.this.mTopDrawable.draw(canvas);
                canvas.restore();
            } else {
                FlipSurface.this.mTopDrawable.draw(canvas);
            }
            if (this.f2i > 0) {
                canvas.drawText(this.mTo, (float) (FlipSurface.this.mLeafWidth / 2), ((float) FlipSurface.this.mLeafHeight) - ((FlipSurface.this.mFontMetrics.ascent + FlipSurface.this.mFontMetrics.descent) / 2.0f), FlipSurface.this.mStringPaint);
            } else {
                canvas.drawText(this.mFrom, (float) (FlipSurface.this.mLeafWidth / 2), ((float) FlipSurface.this.mLeafHeight) - ((FlipSurface.this.mFontMetrics.ascent + FlipSurface.this.mFontMetrics.descent) / 2.0f), FlipSurface.this.mStringPaint);
            }
            if (this.f2i > 0 && this.f2i < 3) {
                Matrix matrix = FlipSurface.this.mMatrix;
                float[] fArr = new float[8];
                fArr[0] = 0.0f;
                fArr[FlipSurface.AXIS_HEIGHT] = 0.0f;
                fArr[2] = (float) FlipSurface.this.mLeafWidth;
                fArr[3] = 0.0f;
                fArr[4] = 0.0f;
                fArr[5] = (float) FlipSurface.this.mLeafHeight;
                fArr[6] = (float) FlipSurface.this.mLeafWidth;
                fArr[7] = (float) FlipSurface.this.mLeafHeight;
                float[] fArr2 = new float[8];
                fArr2[0] = (float) (-this.mHorizontalBias[this.f2i]);
                fArr2[FlipSurface.AXIS_HEIGHT] = (float) this.mVerticalBias[this.f2i];
                fArr2[2] = (float) (FlipSurface.this.mLeafWidth + this.mHorizontalBias[this.f2i]);
                fArr2[3] = (float) this.mVerticalBias[this.f2i];
                fArr2[4] = 0.0f;
                fArr2[5] = (float) FlipSurface.this.mLeafHeight;
                fArr2[6] = (float) FlipSurface.this.mLeafWidth;
                fArr2[7] = (float) FlipSurface.this.mLeafHeight;
                matrix.setPolyToPoly(fArr, 0, fArr2, 0, 4);
                canvas.concat(FlipSurface.this.mMatrix);
                if (FlipSurface.this.mDisplayGear) {
                    canvas.clipRect(gearLeft, Op.DIFFERENCE);
                    canvas.clipRect(gearRight, Op.DIFFERENCE);
                }
                FlipSurface.this.mPanelPaint.setColor(FlipSurface.AXIS_COLOR_LOW);
                canvas.drawRect(FlipSurface.this.outR, 0.0f, ((float) FlipSurface.this.mLeafWidth) - FlipSurface.this.outR, 2.0f, FlipSurface.this.mPanelPaint);
                FlipSurface.this.mPanelPaint.setColor(FlipSurface.AXIS_COLOR_HIGH);
                canvas.drawRect(FlipSurface.this.outR, 0.0f, ((float) FlipSurface.this.mLeafWidth) - FlipSurface.this.outR, 1.0f, FlipSurface.this.mPanelPaint);
                FlipSurface.this.mTopDrawable.draw(canvas);
                canvas.drawText(this.mFrom, (float) (FlipSurface.this.mLeafWidth / 2), ((float) FlipSurface.this.mLeafHeight) - ((FlipSurface.this.mFontMetrics.ascent + FlipSurface.this.mFontMetrics.descent) / 2.0f), FlipSurface.this.mStringPaint);
                if (this.f2i == FlipSurface.AXIS_HEIGHT) {
                    FlipSurface.this.mTopShadow.getPaint().setColor(FlipSurface.LIGHT_SHADOW);
                    FlipSurface.this.mTopShadow.draw(canvas);
                    FlipSurface.this.mTopShadow.getPaint().setColor(FlipSurface.COMMON_SHADOW);
                } else if (this.f2i == 2) {
                    FlipSurface.this.mTopShadow.draw(canvas);
                }
            }
            FlipSurface.this.mPanelPaint.setColor(FlipSurface.AXIS_COLOR_LOW_SHADOW);
            canvas.drawRect(FlipSurface.this.inR, (float) (FlipSurface.this.mLeafHeight - FlipSurface.AXIS_HEIGHT), ((float) FlipSurface.this.mLeafWidth) - FlipSurface.this.inR, (float) FlipSurface.this.mLeafHeight, FlipSurface.this.mPanelPaint);
            canvas.restore();
            int appendLeft = FlipSurface.this.mLeafOut + 2;
            canvas.save();
            canvas.translate((float) FlipSurface.this.mLeft, (float) (FlipSurface.this.mRect.height() + FlipSurface.this.mTop + FlipSurface.AXIS_HEIGHT));
            canvas.clipRect(-FlipSurface.this.mLeafOut, 0, FlipSurface.this.mLeafWidth + FlipSurface.this.mLeafOut, FlipSurface.this.mLeafHeight + 2 + FlipSurface.this.mBottomUnder);
            Rect gearLeft2 = new Rect(0, 0, FlipSurface.this.mGearWidth, FlipSurface.this.mGearHeight - FlipSurface.AXIS_HEIGHT);
            Rect gearRight2 = new Rect(FlipSurface.this.mLeafWidth - FlipSurface.this.mGearWidth, 0, FlipSurface.this.mLeafWidth, FlipSurface.this.mGearHeight - FlipSurface.AXIS_HEIGHT);
            if (FlipSurface.this.mDisplayGear) {
                canvas.save();
                canvas.clipRect(gearLeft2, Op.DIFFERENCE);
                canvas.clipRect(gearRight2, Op.DIFFERENCE);
                FlipSurface.this.mBottomDrawable.draw(canvas);
                canvas.restore();
            } else {
                FlipSurface.this.mBottomDrawable.draw(canvas);
            }
            if (this.f2i <= 5) {
                canvas.drawText(this.mFrom, (float) (FlipSurface.this.mLeafWidth / 2), (-(FlipSurface.this.mFontMetrics.ascent + FlipSurface.this.mFontMetrics.descent)) / 2.0f, FlipSurface.this.mStringPaint);
                if (this.mFromAppendix != null && this.mFromAppendix.length() > 0) {
                    canvas.drawText(this.mFromAppendix, (float) appendLeft, (-(FlipSurface.this.mFontMetrics.ascent + FlipSurface.this.mFontMetrics.descent)) / 2.0f, FlipSurface.this.mAppendixPaint);
                }
                if (this.f2i == 3) {
                    canvas.clipRect(-FlipSurface.this.mLeafOut, 0, FlipSurface.this.mLeafWidth + FlipSurface.this.mLeafOut, FlipSurface.this.mLeafHeight / 2);
                    FlipSurface.this.mBottomShadow.draw(canvas);
                }
                if (this.f2i == 4 || this.f2i == 5) {
                    FlipSurface.this.mBottomShadow.draw(canvas);
                }
            } else {
                canvas.drawText(this.mTo, (float) (FlipSurface.this.mLeafWidth / 2), (-(FlipSurface.this.mFontMetrics.ascent + FlipSurface.this.mFontMetrics.descent)) / 2.0f, FlipSurface.this.mStringPaint);
                if (this.mToAppendix != null && this.mToAppendix.length() > 0) {
                    canvas.drawText(this.mToAppendix, (float) appendLeft, (-(FlipSurface.this.mFontMetrics.ascent + FlipSurface.this.mFontMetrics.descent)) / 2.0f, FlipSurface.this.mAppendixPaint);
                }
            }
            if (this.f2i > 3 && this.f2i < 6) {
                Matrix matrix2 = FlipSurface.this.mMatrix;
                float[] fArr3 = new float[8];
                fArr3[0] = 0.0f;
                fArr3[FlipSurface.AXIS_HEIGHT] = 0.0f;
                fArr3[2] = (float) FlipSurface.this.mLeafWidth;
                fArr3[3] = 0.0f;
                fArr3[4] = 0.0f;
                fArr3[5] = (float) FlipSurface.this.mLeafHeight;
                fArr3[6] = (float) FlipSurface.this.mLeafWidth;
                fArr3[7] = (float) FlipSurface.this.mLeafHeight;
                float[] fArr4 = new float[8];
                fArr4[0] = 0.0f;
                fArr4[FlipSurface.AXIS_HEIGHT] = 0.0f;
                fArr4[2] = (float) FlipSurface.this.mLeafWidth;
                fArr4[3] = 0.0f;
                fArr4[4] = (float) (-this.mHorizontalBias[6 - this.f2i]);
                fArr4[5] = (float) (FlipSurface.this.mLeafHeight - this.mVerticalBias[6 - this.f2i]);
                fArr4[6] = (float) (FlipSurface.this.mLeafWidth + this.mHorizontalBias[6 - this.f2i]);
                fArr4[7] = (float) (FlipSurface.this.mLeafHeight - this.mVerticalBias[6 - this.f2i]);
                matrix2.setPolyToPoly(fArr3, 0, fArr4, 0, 4);
                canvas.concat(FlipSurface.this.mMatrix);
                if (FlipSurface.this.mDisplayGear) {
                    canvas.clipRect(gearLeft2, Op.DIFFERENCE);
                    canvas.clipRect(gearRight2, Op.DIFFERENCE);
                }
                Rect r = FlipSurface.this.mBottomShadow.getBounds();
                r.bottom += FlipSurface.AXIS_HEIGHT;
                FlipSurface.this.mBottomShadow.setBounds(r);
                FlipSurface.this.mBottomShadow.draw(canvas);
                FlipSurface.this.mBottomDrawable.draw(canvas);
                canvas.drawText(this.mTo, (float) (FlipSurface.this.mLeafWidth / 2), (-(FlipSurface.this.mFontMetrics.ascent + FlipSurface.this.mFontMetrics.descent)) / 2.0f, FlipSurface.this.mStringPaint);
                if (this.mToAppendix != null && this.mToAppendix.length() > 0) {
                    canvas.drawText(this.mToAppendix, (float) appendLeft, (-(FlipSurface.this.mFontMetrics.ascent + FlipSurface.this.mFontMetrics.descent)) / 2.0f, FlipSurface.this.mAppendixPaint);
                }
            }
            FlipSurface.this.mPanelPaint.setColor(FlipSurface.AXIS_COLOR_HIGH);
            canvas.drawRect(FlipSurface.this.inR, 0.0f, ((float) FlipSurface.this.mLeafWidth) - FlipSurface.this.inR, 2.0f, FlipSurface.this.mPanelPaint);
            canvas.restore();
            if (this.f2i == 3) {
                canvas.save();
                canvas.translate((float) FlipSurface.this.mLeft, (float) FlipSurface.this.mTop);
                FlipSurface.this.mPanelPaint.setColor(FlipSurface.AXIS_COLOR_HIGH);
                canvas.drawRect(0.0f, (float) (FlipSurface.this.mLeafHeight - FlipSurface.AXIS_HEIGHT), (float) FlipSurface.this.mLeafWidth, (float) FlipSurface.this.mLeafHeight, FlipSurface.this.mPanelPaint);
                FlipSurface.this.mPanelPaint.setColor(-14540254);
                canvas.drawRect(0.0f, (float) FlipSurface.this.mLeafHeight, (float) FlipSurface.this.mLeafWidth, (float) (FlipSurface.this.mLeafHeight + FlipSurface.AXIS_HEIGHT + 2), FlipSurface.this.mPanelPaint);
                canvas.restore();
            }
            return true;
        }
    }

    public FlipSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    private void initGraphics(int width, int height) {
        this.mStringPaint.setColor(this.mStringColor);
        this.mStringPaint.setStyle(Style.FILL);
//        this.mStringPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Pecot Bold.ttf"));
        this.mStringPaint.setTextAlign(Align.CENTER);
        this.mAppendixPaint.setColor(this.mStringColor);
        this.mAppendixPaint.setStyle(Style.FILL);
//        this.mAppendixPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Pecot Bold.ttf"));
        this.mAppendixPaint.setTextAlign(Align.LEFT);
        this.mPanelPaint.setColor(AXIS_COLOR_LOW);
        this.mPanelPaint.setStyle(Style.FILL);
        this.mTopDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-14079703, -14079703, -14079703, -14079703});
        this.mTopDrawable.setShape(GradientDrawable.RECTANGLE);
        this.mTopDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        setCornerRadii(this.mTopDrawable, this.outR, this.outR, this.inR, this.inR);
        float[] fArr = new float[8];
        fArr[0] = this.outR;
        fArr[AXIS_HEIGHT] = this.outR;
        fArr[2] = this.outR;
        fArr[3] = this.outR;
        fArr[4] = this.inR;
        fArr[5] = this.inR;
        fArr[6] = this.inR;
        fArr[7] = this.inR;
        this.mTopShadow = new ShapeDrawable(new RoundRectShape(fArr, null, null));
        this.mTopShadow.getPaint().setColor(COMMON_SHADOW);
        this.mBottomDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{BOTTOM_LEAF_COLOR_FROM, BOTTOM_LEAF_COLOR_TO});
        this.mBottomDrawable.setShape(GradientDrawable.RECTANGLE);
        this.mBottomDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        setCornerRadii(this.mBottomDrawable, this.inR, this.inR, this.outR, this.outR);
        float[] fArr2 = new float[8];
        fArr2[0] = this.inR;
        fArr2[AXIS_HEIGHT] = this.inR;
        fArr2[2] = this.inR;
        fArr2[3] = this.inR;
        fArr2[4] = this.outR;
        fArr2[5] = this.outR;
        fArr2[6] = this.outR;
        fArr2[7] = this.outR;
        this.mBottomShadow = new ShapeDrawable(new RoundRectShape(fArr2, null, null));
        this.mBottomShadow.getPaint().setColor(COMMON_SHADOW);
        float[] fArr3 = new float[8];
        fArr3[0] = this.outR;
        fArr3[AXIS_HEIGHT] = this.outR;
        fArr3[2] = this.outR;
        fArr3[3] = this.outR;
        fArr3[4] = this.inR;
        fArr3[5] = this.inR;
        fArr3[6] = this.inR;
        fArr3[7] = this.inR;
        this.mTopThickShadow = new ShapeDrawable(new RoundRectShape(fArr3, null, null));
        this.mTopThickShadow.getPaint().setColor(1997607185);
        float[] fArr4 = new float[8];
        fArr4[0] = this.inR;
        fArr4[AXIS_HEIGHT] = this.inR;
        fArr4[2] = this.inR;
        fArr4[3] = this.inR;
        fArr4[4] = this.outR;
        fArr4[5] = this.outR;
        fArr4[6] = this.outR;
        fArr4[7] = this.outR;
        this.mBottomThickShadow = new ShapeDrawable(new RoundRectShape(fArr4, null, null));
        this.mBottomThickShadow.getPaint().setColor(1997607185);
        this.mGearDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{GEAR_COLOR_TO, GEAR_COLOR_FROM, GEAR_COLOR_TO, GEAR_COLOR_TO});
        this.mGearDrawable.setShape(GradientDrawable.RECTANGLE);
        this.mGearDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        if (this.mBitmap != null) {
            this.mBitmap.recycle();
        }
        this.mBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        this.mBMPcanvas = new Canvas(this.mBitmap);
        drawLeavesBackground(this.mBMPcanvas, 0);
    }

    static void setCornerRadii(GradientDrawable drawable, float r0, float r1, float r2, float r3) {
        float[] fArr = new float[8];
        fArr[0] = r0;
        fArr[AXIS_HEIGHT] = r0;
        fArr[2] = r1;
        fArr[3] = r1;
        fArr[4] = r2;
        fArr[5] = r2;
        fArr[6] = r3;
        fArr[7] = r3;
        drawable.setCornerRadii(fArr);
    }

    public boolean flip(String from, String fromAppendix, String to, String toAppendix, boolean downwards) {
        if (this.mHasSurface && this.mAnimTimer != null) {
            if (this.mAnimTask != null) {
                this.mAnimTask.cancel();
            }
            this.mAnimTask = new AnimTask(from, fromAppendix, to, toAppendix, downwards);
            this.mAnimTimer.scheduleAtFixedRate(this.mAnimTask, 0, 80);
        }
        return false;
    }

    public boolean flipTo(String to, boolean downwards) {
        if (this.mLastString == null || TextUtils.equals(this.mLastString, to)) {
            return false;
        }
        return flip(this.mLastString, null, to, null, downwards);
    }

    public boolean flipTo(String to, String toAppendix, boolean downwards) {
        if (this.mLastString == null || TextUtils.equals(this.mLastString, to)) {
            return false;
        }
        return flip(this.mLastString, this.mLastAppendix, to, toAppendix, downwards);
    }

    public boolean flip(Drawable from, Drawable to, boolean downwards) {
        return false;
    }

    public boolean flipTo(Drawable to, boolean downwards) {
        return false;
    }

    public void clear() {
        try {
            synchronized (this.mSurfaceHolder) {
                Canvas canvas = this.mSurfaceHolder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(0, Mode.CLEAR);
                    this.mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        } catch (Exception e) {
        }
    }

    public void init(String start, String startAppendix, int background, int stringcolor) {
        this.mLastString = start;
        this.mLastAppendix = startAppendix;
        this.mBackgroundColor = background;
        this.mStringColor = stringcolor;
        this.mStringPaint.setColor(stringcolor);
        this.mAppendixPaint.setColor(stringcolor);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        int i;
        int i2;
        this.mSurfaceHolder = holder;
        int w = (width * 8) / 9;
        if (w > height - 10) {
            i = height - 10;
        } else {
            i = w;
        }
        this.mLeafWidth = i;
        this.mLeafHeight = this.mLeafWidth / 2;
        this.mLeafOut = this.mLeafWidth / 16;
        this.mLeft = (width - this.mLeafWidth) / 2;
        this.mTop = (height - this.mLeafWidth) / 2;
        int i3 = (this.mLeafWidth * 3) / 5;
        int i4 = (this.mLeafHeight * 6) / 5;
        int sizeW = (this.mLeafWidth * 3) / 4;
        int sizeH = (this.mLeafHeight * 3) / 2;
        if (sizeW > sizeH) {
            i2 = sizeH;
        } else {
            i2 = sizeW;
        }
        int sizeW2 = (i2 * 5) / 6;
        this.mStringPaint.setTextSize((float) sizeW2);
        this.mFontMetrics = this.mStringPaint.getFontMetrics();
        this.mAppendixPaint.setTextSize((float) (sizeW2 / 5));
        this.mAppendixFontMetrics = this.mAppendixPaint.getFontMetrics();
        this.mBottomUnder = 4;
        this.mRect = new Rect(0, 0, this.mLeafWidth, this.mLeafHeight);
        this.outR = (float) (this.mLeafWidth / 15);
        this.inR = this.outR / 4.0f;
        this.mGearWidth = this.mLeafWidth / 20;
        if (this.mGearWidth > 5) {
            this.mGearWidth = 5;
        }
        this.mGearHeight = (this.mLeafHeight * 2) / 7;
        if (this.mLeafHeight < 50) {
            this.mLeafPeak = 2;
        } else {
            this.mLeafPeak = 3;
        }
        initGraphics(width, height);
        if (this.mLastString != null) {
            set(this.mLastString, this.mLastAppendix);
        }
        this.mHasSurface = true;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.mSurfaceHolder = holder;
        this.mAnimTimer = new Timer();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (this.mAnimTimer != null) {
            this.mAnimTimer.cancel();
        }
        this.mAnimTimer = null;
        this.mHasSurface = false;
    }

    private void set(String str, String appendix) {
        if (str != null && str.length() >= AXIS_HEIGHT) {
            synchronized (this.mSurfaceHolder) {
                Canvas canvas = this.mSurfaceHolder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(this.mBackgroundColor);
                    if (this.mBitmap != null) {
                        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, null);
                    }
                    canvas.save();
                    canvas.translate((float) this.mLeft, (float) this.mTop);
                    this.mPanelPaint.setColor(AXIS_COLOR_LOW);
                    canvas.drawRect(this.inR, (float) this.mLeafHeight, ((float) this.mLeafWidth) - this.inR, (float) (this.mLeafHeight + AXIS_HEIGHT), this.mPanelPaint);
                    this.mTopDrawable.setBounds(this.mRect);
                    this.mTopDrawable.draw(canvas);
                    canvas.clipRect(-this.mLeafOut, 0, this.mLeafWidth + this.mLeafOut, this.mLeafHeight);
                    canvas.drawText(str, (float) (this.mLeafWidth / 2), ((float) this.mLeafHeight) - ((this.mFontMetrics.ascent + this.mFontMetrics.descent) / 2.0f), this.mStringPaint);
                    this.mPanelPaint.setColor(AXIS_COLOR_LOW_SHADOW);
                    canvas.drawRect(this.inR, (float) (this.mLeafHeight - AXIS_HEIGHT), ((float) this.mLeafWidth) - this.inR, (float) this.mLeafHeight, this.mPanelPaint);
                    canvas.restore();
                    canvas.save();
                    canvas.translate((float) this.mLeft, (float) (this.mRect.height() + this.mTop + AXIS_HEIGHT));
                    canvas.clipRect(-this.mLeafOut, 0, this.mLeafWidth + this.mLeafOut, this.mLeafHeight + 2 + this.mBottomUnder);
                    this.mBottomDrawable.setBounds(this.mRect);
                    this.mBottomDrawable.draw(canvas);
                    canvas.drawText(str, (float) (this.mLeafWidth / 2), (-(this.mFontMetrics.ascent + this.mFontMetrics.descent)) / 2.0f, this.mStringPaint);
                    if (appendix != null && appendix.length() > 0) {
                        canvas.drawText(appendix, (float) (this.mLeafOut + 2), (-(this.mFontMetrics.ascent + this.mFontMetrics.descent)) / 2.0f, this.mAppendixPaint);
                    }
                    this.mPanelPaint.setColor(AXIS_COLOR_HIGH);
                    canvas.drawRect(this.inR, 0.0f, ((float) this.mLeafWidth) - this.inR, 2.0f, this.mPanelPaint);
                    canvas.restore();
                    if (this.mDisplayGear) {
                        drawGears(canvas, 0);
                    }
                    this.mSurfaceHolder.unlockCanvasAndPost(canvas);
                    this.mLastString = str;
                    this.mLastAppendix = appendix;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void drawLeavesBackground(Canvas canvas, int step) {
        int mystep;
        int i = step;
        canvas.drawColor(0, Mode.CLEAR);
        canvas.save();
        canvas.translate((float) this.mLeft, (float) this.mTop);
        this.mRect.top -= this.mLeafPeak + AXIS_HEIGHT;
        this.mTopThickShadow.setBounds(this.mRect);
        this.mTopThickShadow.draw(canvas);
        this.mRect.top += this.mLeafPeak + AXIS_HEIGHT;
        this.mRect.top -= this.mLeafPeak;
        this.mTopDrawable.setBounds(this.mRect);
        this.mTopDrawable.draw(canvas);
        this.mRect.top += this.mLeafPeak;
        if (step < 3) {
            this.mRect.top -= (this.mLeafPeak + AXIS_HEIGHT) - step;
            this.mTopThickShadow.setBounds(this.mRect);
            this.mTopThickShadow.draw(canvas);
            this.mRect.top += (this.mLeafPeak + AXIS_HEIGHT) - step;
            this.mRect.top -= this.mLeafPeak - step;
            this.mTopDrawable.setBounds(this.mRect);
            this.mTopDrawable.draw(canvas);
            this.mRect.top += this.mLeafPeak - step;
        }
        this.mRect.top -= AXIS_HEIGHT;
        this.mTopThickShadow.setBounds(this.mRect);
        this.mTopThickShadow.draw(canvas);
        this.mRect.top += AXIS_HEIGHT;
        canvas.restore();
        canvas.save();
        canvas.translate((float) this.mLeft, (float) (this.mRect.height() + this.mTop + AXIS_HEIGHT));
        this.mRect.bottom += (this.mLeafPeak * 2) + AXIS_HEIGHT;
        this.mBottomThickShadow.setBounds(this.mRect);
        this.mBottomThickShadow.draw(canvas);
        this.mRect.bottom -= (this.mLeafPeak * 2) + AXIS_HEIGHT;
        this.mRect.bottom += this.mLeafPeak * 2;
        this.mBottomDrawable.setBounds(this.mRect);
        this.mBottomDrawable.draw(canvas);
        this.mRect.bottom -= this.mLeafPeak * 2;
        int a = 6 - this.mLeafPeak;
        if (step > a) {
            mystep = step - a;
        } else {
            mystep = 0;
        }
        for (int i2 = AXIS_HEIGHT; i2 >= 0; i2--) {
            int bbias = this.mLeafPeak * i2;
            this.mRect.bottom += bbias + AXIS_HEIGHT + mystep;
            this.mBottomThickShadow.setBounds(this.mRect);
            this.mBottomThickShadow.draw(canvas);
            this.mRect.bottom -= (bbias + AXIS_HEIGHT) + mystep;
            this.mRect.bottom += bbias + mystep;
            this.mBottomDrawable.setBounds(this.mRect);
            this.mBottomDrawable.draw(canvas);
            this.mRect.bottom -= bbias + mystep;
        }
        this.mRect.bottom += AXIS_HEIGHT;
        this.mBottomThickShadow.setBounds(this.mRect);
        this.mBottomThickShadow.draw(canvas);
        this.mRect.bottom -= AXIS_HEIGHT;
        canvas.restore();
    }

    /* access modifiers changed from: private */
    public void drawGears(Canvas canvas, int step) {
        canvas.save();
        canvas.translate((float) this.mLeft, (float) this.mTop);
        Rect gearBlank = new Rect(0, this.mLeafHeight - this.mGearHeight, this.mGearWidth, this.mLeafHeight + this.mGearHeight);
        this.mPanelPaint.setColor(AXIS_COLOR_LOW);
        canvas.drawRect(gearBlank, this.mPanelPaint);
        Rect gear = new Rect(gearBlank);
        gear.top += AXIS_HEIGHT;
        gear.bottom -= AXIS_HEIGHT;
        gear.right -= AXIS_HEIGHT;
        this.mGearDrawable.setBounds(gear);
        this.mGearDrawable.draw(canvas);
        int bias = this.mLeafWidth - this.mGearWidth;
        gearBlank.left += bias;
        gearBlank.right += bias;
        canvas.drawRect(gearBlank, this.mPanelPaint);
        gear.left += bias + AXIS_HEIGHT;
        gear.right += bias + AXIS_HEIGHT;
        this.mGearDrawable.setBounds(gear);
        this.mGearDrawable.draw(canvas);
        canvas.restore();
    }
}
