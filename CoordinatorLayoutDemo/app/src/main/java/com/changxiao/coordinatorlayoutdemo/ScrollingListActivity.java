package com.changxiao.coordinatorlayoutdemo;

import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.changxiao.coordinatorlayoutdemo1.R;

/**
 * 要让listview/gridview一起滚动，需设置ViewCompat.setNestedScrollingEnabled(listView/gridview,true);
 *
 * Created by Chang.Xiao on 2017/8/24.
 *
 * @version 1.0
 */
public class ScrollingListActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = (ListView) findViewById(R.id.lv_list);
        String[] strs = new String[20];
        for (int i = 0; i < 20; i++) {
            strs[i] = "我是" + i;
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strs);
        mListView.setAdapter(arrayAdapter);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mListView.setNestedScrollingEnabled(true);
//        }
        ViewCompat.setNestedScrollingEnabled(mListView, true);
    }
}
