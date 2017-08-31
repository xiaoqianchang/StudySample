package com.changxiao.coordinatorlayoutdemo;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.changxiao.coordinatorlayoutdemo.base.BaseActivity;
import com.changxiao.coordinatorlayoutdemo.base.ToolBarActivity;
import com.changxiao.coordinatorlayoutdemo.fragment.FragmentOne;
import com.changxiao.coordinatorlayoutdemo.fragment.FragmentThree;
import com.changxiao.coordinatorlayoutdemo.fragment.FragmentTwo;
import com.changxiao.coordinatorlayoutdemo1.R;

/**
 * http://blog.csdn.net/u010687392/article/details/46852565
 *
 * Created by Chang.Xiao on 2017/8/24.
 * 
 * @version 1.0
 */
public class ScrollingList2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_list2);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        SimpleFragmentPagerAdapter viewPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(FragmentOne.newInstance(), "TabOne");//添加Fragment
        viewPagerAdapter.addFragment(FragmentTwo.newInstance(), "TabTwo");
        viewPagerAdapter.addFragment(FragmentThree.newInstance(), "TabThree");
        mViewPager.setAdapter(viewPagerAdapter);//设置适配器

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText("TabOne"));//给TabLayout添加Tab
        mTabLayout.addTab(mTabLayout.newTab().setText("TabTwo"));
        mTabLayout.addTab(mTabLayout.newTab().setText("TabThree"));
        mTabLayout.setupWithViewPager(mViewPager);//给TabLayout设置关联ViewPager，如果设置了ViewPager，那么ViewPagerAdapter中的getPageTitle()方法返回的就是Tab上的标题

    }
}