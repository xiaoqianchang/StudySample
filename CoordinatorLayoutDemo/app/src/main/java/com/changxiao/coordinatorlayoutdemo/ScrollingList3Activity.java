package com.changxiao.coordinatorlayoutdemo;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.changxiao.coordinatorlayoutdemo.fragment.FragmentOne;
import com.changxiao.coordinatorlayoutdemo.fragment.FragmentThree;
import com.changxiao.coordinatorlayoutdemo.fragment.FragmentTwo;
import com.changxiao.coordinatorlayoutdemo1.R;

public class ScrollingList3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_list3);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);//设置ToolBar的titl颜色
        setSupportActionBar(mToolbar);

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
