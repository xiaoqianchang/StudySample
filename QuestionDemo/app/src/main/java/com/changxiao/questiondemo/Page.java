package com.changxiao.questiondemo;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * $desc$
 * <p>
 * Created by Chang.Xiao on 2016/10/25.
 *
 * @version 1.0
 */

public class Page {
    private TextView qustion_tv;
    private Button prebtn;
    private Button nextbtn;
    private int mWidth;
    private int mHeight;
    private int mLeft;
    private int mTop;
    private List<questions> data;
    private RadioGroup group;
    private questions question;
    private DaoHelp db;
    private Context mContext;
    private View view;
    private RadioButton rbt1;
    private RadioButton rbt2;
    private RadioButton rbt3;
    private RadioButton rbt4;
    private TextView explaination_tv;
    private String tag;
    private int vpNext;
    private ImageView controller;

    public Page(Context context, DaoHelp db) {
        this.mContext = context;
        this.db = db;

        data = db.getQuestionData();
        getdefaultWH();
        onCreateView();
        initViews();
    }

    public View getRootView() {

        return view;
    }

    private void onCreateView() {
        // TODO Auto-generated method stub
        LayoutInflater infalter = ((MainActivity) mContext).getLayoutInflater();
        view = infalter.inflate(R.layout.activity_fragment, null);

    }

    private void dataShow(int row) {
        question = data.get(row);
        qustion_tv.setText(question.getQuestion());

        rbt1.setText(question.getAnswer_a());
        rbt2.setText(question.getAnswer_b());
        rbt3.setText(question.getAnswer_c());
        rbt4.setText(question.getAnswer_d());
        String answer = null;
        int a = question.getBingo();
        if (a == 0)
            answer = "A";
        if (a == 1)
            answer = "B";
        if (a == 2)
            answer = "C";
        if (a == 3)
            answer = "D";
        explaination_tv.setText("答案:" + answer + " 。"
                + question.getExplanation());

    }

    private void initViews() {
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        qustion_tv = (TextView) view.findViewById(R.id.questoion);
        explaination_tv = (TextView) view.findViewById(R.id.explaination);
        controller = (ImageView) view.findViewById(R.id.controller);
        explaination_tv.setVisibility(View.INVISIBLE);
        group = (RadioGroup) view.findViewById(R.id.radiogroup);
        group.setOnCheckedChangeListener(new radioChangeListener());

        rbt1 = (RadioButton) group.findViewById(R.id.rb1);
        rbt2 = (RadioButton) group.findViewById(R.id.rb2);
        rbt3 = (RadioButton) group.findViewById(R.id.rb3);
        rbt4 = (RadioButton) group.findViewById(R.id.rb4);

        LinearLayout linearlayout = (LinearLayout) view
                .findViewById(R.id.container);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linearlayout
                .getLayoutParams();
        params.leftMargin = mLeft;
        params.topMargin = mTop;

        LinearLayout.LayoutParams qustionParams = (LinearLayout.LayoutParams) qustion_tv
                .getLayoutParams();
        qustionParams.width = (int) (mWidth * 0.65);
        LinearLayout.LayoutParams explainationParams = (android.widget.LinearLayout.LayoutParams) explaination_tv
                .getLayoutParams();
        explainationParams.width = (int) (mWidth * 0.65);
        // 是否隐藏答案
        controller.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!explaination_tv.isShown()) {
                    // controller.setColorFilter(0xaa0000ff,PorterDuff.Mode.DST_OUT);
                    controller.setColorFilter(0xaa0000ff,
                            PorterDuff.Mode.SRC_IN);
                    explaination_tv.setVisibility(View.VISIBLE);
                } else {
                    controller.clearColorFilter();
                    explaination_tv.setVisibility(View.INVISIBLE);
                }

            }
        });

    }

    class radioChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb1:
                    // 保存数据库记录
                    db.save(0, mCurpage + 1);
                    Next();

                    break;
                case R.id.rb2:
                    db.save(1, mCurpage + 1);
                    Next();
                    break;
                case R.id.rb3:
                    db.save(2, mCurpage + 1);
                    Next();
                    break;
                case R.id.rb4:
                    db.save(3, mCurpage + 1);
                    Next();
                    break;

            }
            // TODO Auto-generated method stub

        }

    }

    private void Next() {
        // 当查看历史题目 修改 不支持滚动
        if (callback != null && isCan) {

            callback.scrollNext(vpNext);
        }

    }

    private boolean isCan = true;

    private void getdefaultWH() {
        DisplayMetrics m = new DisplayMetrics();
        ((MainActivity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(m);
        mWidth = m.widthPixels;
        mHeight = m.heightPixels;
        mLeft = mWidth / 7;
        mTop = mHeight / 10;

    }

    private int mCurpage;
    private String TAG;

    public void setRow(int row) {
        // 清除 重新初始化
        clearCheck();
        // 更新数据
        dataShow(row);
        // 记录滑动下一页的数据
        vpNext = row + 1;
        // 当前页数
        mCurpage = row;
    }

    /**
     * 设置已经有的记录
     *
     * @param select
     */
    public void setSelect(int select) {
        this.isCan = false;
        switch (select) {

            case 0:
                rbt1.setChecked(true);

                break;
            case 1:
                rbt2.setChecked(true);

                break;
            case 2:
                rbt3.setChecked(true);

                break;
            case 3:
                rbt4.setChecked(true);

                break;

        }

    }

    public void setAutoScroll(boolean iscan) {
        this.isCan = iscan;
    }

    public void clearCheck() {
        isCan=true;
        group = (RadioGroup) view.findViewById(R.id.radiogroup);
        rbt1 = (RadioButton) group.findViewById(R.id.rb1);
        rbt2 = (RadioButton) group.findViewById(R.id.rb2);
        rbt3 = (RadioButton) group.findViewById(R.id.rb3);
        rbt4 = (RadioButton) group.findViewById(R.id.rb4);
        group.clearCheck();

    }

    /**
     * 自动滚动回调
     *
     * @author Administrator
     *
     */
    public interface ViewPageScrollerListener {

        void scrollNext(int page);

        void scrollPre(int page);

    }

    private ViewPageScrollerListener callback;

    public void setOnViewpagerScrollerListener(ViewPageScrollerListener callback) {
        this.callback = callback;
    }
}
