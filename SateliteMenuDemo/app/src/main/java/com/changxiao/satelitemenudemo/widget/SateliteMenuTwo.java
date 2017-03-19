package com.changxiao.satelitemenudemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.changxiao.satelitemenudemo.R;

/**
 * 卫星式菜�
 * <p/>
 * Created by Chang.Xiao on 2016/9/14.
 *
 * @version 1.0
 */
public class SateliteMenuTwo extends ViewGroup implements View.OnClickListener {
    public enum Position {
        POS_LEFT_TOP, POS_LEFT_BOTTOM, POS_RIGHT_TOP, POS_RIGHT_BOTTOM, POS_RIGHT_TOP_CUSTOM
    }

    private final int LEFT_TOP = 0;
    private final int LEFT_BOTTOM = 1;
    private final int RIGHT_TOP = 2;
    private final int RIGHT_BOTTOM = 3;
    private final int RIGHT_TOP_CUSTOM = 4; // 右上角自定义
    private final int STATUS_OPEN = 0; //菜单的状�打开
    private final int STATUS_CLOSE = 1; //菜单的状�关闭

    private Position mPosition;
    private int mRadius;

    private int mStatus;
    private onMenuItemClickListener mMenuItemClickListener;
    private View mMenuButton;

    /**
     * Interface definition for a callback to be invoked when a MenuItem is clicked
     */
    public interface onMenuItemClickListener {
        /**
         * Called when a MenuItem has been clicked.
         *
         * @param view     The view that was clicked.
         * @param position The view position
         */
        void onItemClick(View view, int position);
    }

    public SateliteMenuTwo(Context context) {
        this(context, null);
    }

    public SateliteMenuTwo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SateliteMenuTwo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SateliteMenu);
        int position = typedArray.getInt(R.styleable.SateliteMenu_position, LEFT_TOP);
        //定义半径默认�
        float defRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
        switch (position) {
            case LEFT_TOP:
                mPosition = Position.POS_LEFT_TOP;
                break;
            case RIGHT_TOP:
                mPosition = Position.POS_RIGHT_TOP;
                break;
            case LEFT_BOTTOM:
                mPosition = Position.POS_LEFT_BOTTOM;
                break;
            case RIGHT_BOTTOM:
                mPosition = Position.POS_RIGHT_BOTTOM;
                break;
            case RIGHT_TOP_CUSTOM: // 右上角自定义
                mPosition = Position.POS_RIGHT_TOP_CUSTOM;
                break;
        }
        mRadius = (int) typedArray.getDimension(R.styleable.SateliteMenu_radius, defRadius);

        typedArray.recycle(); //回收

        mStatus = STATUS_CLOSE; //默认关闭状�

    }

    /**
     * Register a callback to be invoked when this view is clicked. If this view is not
     * clickable, it becomes clickable.
     *
     * @param menuItemClickListener
     */
    public void setOnMenuItemClickListener(onMenuItemClickListener menuItemClickListener) {
        if (!isClickable()) {
            setClickable(true);
        }
        this.mMenuItemClickListener = menuItemClickListener;
    }

    public void setPosition(Position position) {
        if (mPosition == position) {
            return;
        }
        this.mPosition = position;

        View child;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            child.clearAnimation();
        }
        //        invalidate(); //会触�测量、布局和绘�
        requestLayout(); //这里只要请求布局
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量子view
        for (int i = 0, count = getChildCount(); i < count; i++) {
            //需要传入父view的spec
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }

    }

    @Override //lt 左上� rb 右下� 如果 r<l �b<t 则无法显示了
    protected void onLayout(boolean changed, int l, int t, int r, int b) {//l=0, t=0  因为是相对于父view的位�
        layoutMenuButton();
        /*
        分析�
            menuButton距离每个item为radius�
            到item作直线，其夹角，应为90度均分�0/(item-1)=每个夹角的度数�
            有角度，就能求出正弦值sina�
            根据正弦公式：sina=a/c，且已知c=radius，求出a边长，即x坐标�
            有角度，就能求出正弦值cosa�
            余弦公式：cosa=b/c,且已知radius(斜边)，求出b边长，即y坐标
         */
        int count = getChildCount();
        double angle = 90.0f / (count - 1);//这里-2，是多减去了一个menuButton
        View child;
        int w, h;
        for (int i = 1; i < count; i++) {
            child = getChildAt(i);
            child.setVisibility(View.GONE);
            w = child.getMeasuredWidth();
            h = child.getMeasuredHeight();
            double sin = 0, cos = 0;
            //Math.toRadians：math.pi/180 * angle = 弧度   angel/180*pi<==>angel*pi/180
            sin = Math.sin(Math.toRadians(angle * (i - 1))); //第i个角度的 sin(0)=0   i-1即从0开�会有与屏幕直角边平行�math.sin需要传弧度�
            cos = Math.cos(Math.toRadians(angle * (i - 1)));// 邻直角边/斜边   cos(0)=1
            l = (int) (mRadius * sin); //对横边长
            t = (int) (mRadius * cos); //邻纵边长

            //左上，左�left�就是上面的l l递增    符合默认变化规则
            //左上，右�top�就是上面的t  t递减    符合默认变化规则

            //右上、右�left值一� 从右向左 递减
            if (mPosition == Position.POS_RIGHT_TOP || mPosition == Position.POS_RIGHT_BOTTOM) {
                l = getMeasuredWidth() - w - l;
            }
            //左下、右�top值一� 从上向下 递增
            if (mPosition == Position.POS_LEFT_BOTTOM || mPosition == Position.POS_RIGHT_BOTTOM) {
                t = getMeasuredHeight() - h - t;
            }
            // 右上角自定义
            if (mPosition == Position.POS_RIGHT_TOP_CUSTOM) {
                l = l + mMenuButton.getLeft();
                t = mMenuButton.getBottom() - h - t;
            }

            child.layout(l, t, l + w, t + h);

            final int pos = i;
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMenuItemClickListener != null) {
                        mMenuItemClickListener.onItemClick(v, pos);
                        itemAnim(pos);
                    }
                    mStatus = STATUS_CLOSE; //关闭状�
                }
            });

        }

    }

    /**
     * 菜单按钮设置layout
     */
    private void layoutMenuButton() {
        mMenuButton = getChildAt(0);
        int l = 0, t = 0;
        int w = mMenuButton.getMeasuredWidth();
        int h = mMenuButton.getMeasuredHeight();
        switch (mPosition) {
            case POS_LEFT_TOP:
                l = t = 0;
                break;
            case POS_RIGHT_TOP:
                l = getMeasuredWidth() - w;
                t = 0;
                break;
            case POS_LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - h;
                break;
            case POS_RIGHT_BOTTOM:
                l = getMeasuredWidth() - w;
                t = getMeasuredHeight() - h;
                break;
            case POS_RIGHT_TOP_CUSTOM: // 右上角自定义
                l = getMeasuredWidth() - w - mRadius;
                t = mRadius;
                break;
        }
        mMenuButton.layout(l, t, w + l, h + t);

        mMenuButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        rotateMenuBotton(mMenuButton, 360, 500);
        toggleMenu(500);

    }

    private void rotateMenuBotton(View view, int angle, int duration) {
        RotateAnimation anim = new RotateAnimation(0, angle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(duration);
        anim.setFillAfter(true); //view保持在动画结束位�
        view.startAnimation(anim);
    }

    /**
     * 展开/隐藏子菜�
     * 子菜单动�平移
     * // 开始menuitem处于展示、隐藏状态，xflag、yflag控制menuitem合拢时移动到终点(menubutton)的距�初始状态为(0,0)�
     */
    private void toggleMenu(int duration) {

        int count = getChildCount();
        for (int i = 1; i < count; i++) {
            final View child = getChildAt(i);
            /*
               平移动画 以layout中计算的长度 再乘��1
               close�
                   左上   r->l b->t
                   右上   l->r b->t
                   左下   r->l t->b
                   右下   l->r t->b
               open�
                   左上
                   右上
                   左下
                   右下

                */
            int xflag = 1, yflag = 1;
            //
            if (mPosition == Position.POS_LEFT_TOP || mPosition == Position.POS_LEFT_BOTTOM) {
                xflag = -1;
            }
            //
            if (mPosition == Position.POS_LEFT_TOP || mPosition == Position.POS_RIGHT_TOP) {
                yflag = -1;
            }
            // 右上角自定义
            if (mPosition == Position.POS_RIGHT_TOP_CUSTOM) {
                xflag = -1;
            }

            double angle = 90 / (count - 1);
            /*
             一个圆的弧度是2π,角度�60°   π/2�0度的弧度
             */
            int oppositeLen = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 1) * (i - 1))); //对边 横向
            int adjacentLen = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 1) * (i - 1))); //邻边 纵向
/*
 一个圆的弧度是2π,角度�60°  π/180，每角度对应的弧�  然后乘以角度�其所对应的弧�
 */
            //            int oppositeLen = (int) (mRadius * Math.sin(angle * Math.PI / 180 * (i-1))); //对边 横向
            //            int adjacentLen = (int) (mRadius * Math.cos(angle * Math.PI / 180 * (i-1))); //邻边 纵向


            int stopx = xflag * oppositeLen;
            int stopy = yflag * adjacentLen;
            AnimationSet set = new AnimationSet(true);
            if (mStatus == STATUS_OPEN) {//如是打开，则要关�
                //4个值是起始点和结束�相对于自身x、y的距�
                TranslateAnimation tranAnim = new TranslateAnimation(0, stopx, 0, stopy);
                tranAnim.setStartOffset(mRadius / 6);//偏移
                set.addAnimation(tranAnim);
                AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0);
                set.addAnimation(alphaAnim);
                set.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        setItemClickable(child, false);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            } else { //要打开
                TranslateAnimation tranAnim = new TranslateAnimation(stopx, 0, stopy, 0);
                set.addAnimation(tranAnim);
                AlphaAnimation alphaAnim = new AlphaAnimation(0.0f, 1.0f);
                set.addAnimation(alphaAnim);
                set.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        setItemClickable(child, false);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        setItemClickable(child, true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            set.setDuration(duration);
            set.setFillAfter(true);
            child.startAnimation(set);

        }

        if (mStatus == STATUS_OPEN) {
            mStatus = STATUS_CLOSE;
        } else {
            mStatus = STATUS_OPEN;
        }
    }

    /**
     * item点击动画
     *
     * @param position
     */
    private void itemAnim(int position) {
        View child;
        int count = getChildCount();
        for (int i = 1; i < count; i++) {
            child = getChildAt(i);
            if (position == i) {
                scaleBigAnim(child);
            } else {
                scaleSmallAnim(child);
            }
            setItemClickable(child, false);
        }
    }

    private void scaleBigAnim(View view) {
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 3f, 1.0f, 3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(alphaAnim);
        set.addAnimation(scaleAnim);
        set.setDuration(800);
        set.setFillAfter(true);
        view.startAnimation(set);
    }

    private void scaleSmallAnim(View view) {
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0, 1.0f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(alphaAnim);
        set.addAnimation(scaleAnim);
        set.setFillAfter(true);
        set.setDuration(500);
        view.startAnimation(set);
    }

    private void setItemClickable(View view, boolean flag) {
        view.setClickable(flag);
        view.setFocusable(flag);
    }
}
