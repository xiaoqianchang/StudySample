package com.changxiao.greendaodemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.changxiao.greendaodemo.db.DBHelper;
import com.changxiao.greendaodemo.db.dao.DaoSession;
import com.changxiao.greendaodemo.db.entity.Person;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_add)
    TextView mAdd;

    @BindView(R.id.tv_delete)
    TextView mDelete;

    @BindView(R.id.tv_update)
    TextView mUpdate;

    @BindView(R.id.tv_query)
    TextView mQuery;

    private Unbinder unbinder;
    private DaoSession mDaoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        mDaoSession = DBHelper.getInstance(this).getDaoSession();
    }

    @OnClick(R.id.tv_add)
    public void onAdd() {
        Person person = new Person(null, "name ", "sex");
        // 通过 insert 方法向数据库中添加数据，因为设置了 id 为主键，所以这里 id 填 null
        mDaoSession.getPersonDao().insert(person);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
