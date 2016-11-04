package com.changxiao.swipelistviewdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changxiao.swipelistviewdemo.widget.SwipeListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SwipeListView lvListView;
    private List<String> datas;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvListView = (SwipeListView) findViewById(R.id.lv_list_view);
        datas = new ArrayList<>();
        datas = getData();
        adapter = new MyAdapter(this, datas);
        lvListView.setAdapter(adapter);
    }

    public List<String> getData() {
        for (int i = 0; i < 20; i++) {
            datas.add("iaochang" + i);
        }
        return datas;
    }

    static class MyAdapter extends BaseAdapter {

        private Context context;
        private List<String> datas;

        public MyAdapter(Context context, List<String> datas) {
            this.context = context;
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            TextView textView = new TextView(context);
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()));
//            textView.setLayoutParams(params);
//            textView.setText(datas.get(position));

            View converView = LayoutInflater.from(context).inflate(R.layout.lv_my_property_item, null);
            TextView tv_group_name = (TextView) converView.findViewById(R.id.tv_group_name);
            tv_group_name.setText(datas.get(position));
            return converView;
        }
    }
}
