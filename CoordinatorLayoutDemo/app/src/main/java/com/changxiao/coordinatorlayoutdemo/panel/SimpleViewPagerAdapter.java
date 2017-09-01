package com.changxiao.coordinatorlayoutdemo.panel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * $desc$
 * <p>
 * Created by Chang.Xiao on 2017/8/24.
 *
 * @versio.0
 */
public class SimpleViewPagerAdapter extends PagerAdapter {

    private final List<View> mViews = new ArrayList<>();//添加的Fragment的集合

    public void addV(View view) {
        mViews.add(view);
    }

    @Override
    public int getCount() {
        //返回Fragment的数量
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }
}
