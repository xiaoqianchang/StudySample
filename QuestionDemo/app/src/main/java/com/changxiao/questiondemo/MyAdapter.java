package com.changxiao.questiondemo;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * $desc$
 * <p>
 * Created by Chang.Xiao on 2016/10/25.
 *
 * @version 1.0
 */

public class MyAdapter<T extends View> extends PagerAdapter {
    private Context mContext;
    private ArrayList<Page> pageviews;

    private int mLimit;
    private String TAG;
    private int deletePositon;
    private MainActivity main;
    private DaoHelp db;
    private boolean isNext = true;

    public MyAdapter(Context context, ArrayList<Page> pageviews, int limit,
                     MainActivity main, DaoHelp db) {
        this.mContext = context;
        this.pageviews = pageviews;
        mLimit = limit;
        this.main = main;
        this.db = db;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mLimit;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        Page p = pageviews.get(position % 4);

        container.removeView(p.getRootView());

    }

    private int curpage;
    private boolean SUPPORT_SCROLLER = true;

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Page page = null;

        // 得到对象
        page = pageviews.get(position % 4);

        // 设置滚动监听
        page.setOnViewpagerScrollerListener(main);

        //设置显示的页面 position
        page.setRow(position);
        //判断表中是否已经作答过
        if (isRecord(position+1)) {
            //取得数据库的答案
            SUPPORT_SCROLLER=false;
            int answer = takeData(position+1);
            Log.e(TAG, "answer" + answer);
            //如果该位子  已经存在数据库中 则设置数据库中的答案
            page.setSelect(answer);

        }
        // 更新数据 判断数据是否有答案



        View view = page.getRootView();
        container.addView(view);
        return view;
    }

    /**
     * 那道记录的数据
     *
     * @param page
     * @return
     */
    private int takeData(int page) {

        return db.getOneOfAnswer(page);
    }

    private boolean isRecord(int pos) {
        // Log.e(TAG, "db.query(pos)"+db.query(pos));
        return db.query(pos);

    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }
}
