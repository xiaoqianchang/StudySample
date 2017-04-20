package com.changxiao.bottomnavigationviewdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.changxiao.bottomnavigationviewdemo.fragment.FortuneGroupListFragment;
import com.changxiao.bottomnavigationviewdemo.fragment.FortuneManagerFragment;
import com.changxiao.bottomnavigationviewdemo.fragment.MainFragment;
import com.changxiao.bottomnavigationviewdemo.fragment.MineFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = MainFragment.newInstance();
                    return true;
                case R.id.navigation_dashboard:
                    fragment = FortuneManagerFragment.newInstance();
                    return true;
                case R.id.navigation_notifications:
                    fragment = FortuneGroupListFragment.newInstance();
                    return true;
                case R.id.navigation_mine:
                    fragment = MineFragment.newInstance();
                    return true;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, fragment)
                    .commitAllowingStateLoss();
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
