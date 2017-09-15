package com.changxiao.greendaodemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.changxiao.greendaodemo.db.DBHelper;
import com.changxiao.greendaodemo.db.dao.DaoSession;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_add)
    TextView mAdd;

    @BindView(R.id.tv_delete)
    TextView mDelete;

    @BindView(R.id.tv_update)
    TextView mUpdate;

    @BindView(R.id.tv_query)
    TextView mQuery;

    private DaoSession mDaoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDaoSession = DBHelper.getInstance(this).getDaoSession();
    }

    @OnClick(R.id.tv_add)
    public void onAdd() {
        mDaoSession.getPersonDao();
    }
}
