package com.changxiao.bottomnavigationviewdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.changxiao.bottomnavigationviewdemo.fragment.FortuneGroupListFragment;
import com.changxiao.bottomnavigationviewdemo.fragment.FortuneManagerFragment;
import com.changxiao.bottomnavigationviewdemo.fragment.MainFragment;
import com.changxiao.bottomnavigationviewdemo.fragment.MineFragment;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainFragment mainFragment = MainFragment.newInstance();
    private FortuneManagerFragment fortuneManagerFragment = FortuneManagerFragment.newInstance();
    private FortuneGroupListFragment fortuneGroupListFragment = FortuneGroupListFragment.newInstance();
    private MineFragment mineFragment = MineFragment.newInstance();
    private List<Fragment> fragmentList = Arrays.asList(mainFragment, fortuneManagerFragment, fortuneGroupListFragment, mineFragment);
    private int currSel = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    currSel = 0;
                    break;
                case R.id.navigation_dashboard:
                    currSel = 1;
                    break;
                case R.id.navigation_notifications:
                    currSel = 2;
                    break;
                case R.id.navigation_mine:
                    currSel = 3;
                    break;
                default:
                    return false;
            }
            addFragmentToStack(currSel);
            return true;
        }

    };

    /**
     * 充当Fragment适配器,控制显示与影藏,并触发相关显示与影藏的方法
     *
     * @param cur
     */
    private void addFragmentToStack(int cur) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = fragmentList.get(cur);
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.content, fragment);
            fragment.setMenuVisibility(true);
            fragment.setUserVisibleHint(true);
        }
        for (int i = 0; i < fragmentList.size(); i++) {
            Fragment f = fragmentList.get(i);
            if (i == cur && f.isAdded()) {
                fragmentTransaction.show(f);
                f.setMenuVisibility(true);
                f.setUserVisibleHint(true);
            } else if (f != null && f.isAdded() && f.isVisible()) {
                fragmentTransaction.hide(f);
                f.setMenuVisibility(false);
                f.setUserVisibleHint(false);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        addFragmentToStack(currSel);
    }

}
