package com.changxiao.coordinatorlayoutdemo.panel;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.changxiao.coordinatorlayoutdemo.R;
import com.changxiao.coordinatorlayoutdemo.SimpleFragmentPagerAdapter;
import com.changxiao.coordinatorlayoutdemo.fragment.FragmentOne;
import com.changxiao.coordinatorlayoutdemo.fragment.FragmentThree;
import com.changxiao.coordinatorlayoutdemo.fragment.FragmentTwo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuickControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuickControlFragment extends Fragment {

    @BindView(R.id.viewpager_song)
    ViewPager mViewPagerSong;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    public QuickControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QuickControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuickControlFragment newInstance() {
        QuickControlFragment fragment = new QuickControlFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quick_control, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 切歌
        View inflate = View.inflate(getActivity(), R.layout.view_song_item, null);
        SimpleViewPagerAdapter viewPagerAdapter = new SimpleViewPagerAdapter();
        viewPagerAdapter.addV(inflate);//添加Fragment
        viewPagerAdapter.addV(inflate);
        viewPagerAdapter.addV(inflate);
        mViewPagerSong.setAdapter(viewPagerAdapter);//设置适配器


        SimpleFragmentPagerAdapter fragmentPagerAdapter = new SimpleFragmentPagerAdapter(getChildFragmentManager());
        fragmentPagerAdapter.addFragment(FragmentOne.newInstance(), "TabOne");//添加Fragment
        fragmentPagerAdapter.addFragment(FragmentTwo.newInstance(), "TabTwo");
        fragmentPagerAdapter.addFragment(FragmentThree.newInstance(), "TabThree");
        mViewPager.setAdapter(fragmentPagerAdapter);//设置适配器
    }
}
