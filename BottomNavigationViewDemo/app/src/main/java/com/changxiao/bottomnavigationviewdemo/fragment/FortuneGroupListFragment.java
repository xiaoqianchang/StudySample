package com.changxiao.bottomnavigationviewdemo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.changxiao.bottomnavigationviewdemo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FortuneGroupListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FortuneGroupListFragment extends Fragment {

    public FortuneGroupListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FortuneGroupListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FortuneGroupListFragment newInstance() {
        FortuneGroupListFragment fragment = new FortuneGroupListFragment();
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
        return inflater.inflate(R.layout.fragment_fortune_group_list, container, false);
    }

}
