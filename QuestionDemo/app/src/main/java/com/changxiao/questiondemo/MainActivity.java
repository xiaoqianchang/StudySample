package com.changxiao.questiondemo;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Page.ViewPageScrollerListener, MyDialog.NotifySaveOrDeleteListener, View.OnClickListener {

    private String TAG;
    //上一页按钮
    private Button prebtn;
    //下一页按钮
    private Button nextbtn;
    //解释
    private TextView qustion_v;
    //
    private ViewPager viewpg;
    //展示数据的View
    private ArrayList<Page> Pages = new ArrayList<Page>();
    //页数
    private int mLimit;
    //数据库帮助类
    DaoHelp db;
    // 做完选择 回调的页数
    private int mMaxPage;
    // 还没选择 点击上下翻页
    private int mPNSelect;
    private Button btn1;
    private Button btn2;

    //保存路径
    public static final String DB_PATH = "/data/data/com.changxiao.questiondemo/databases/";
    //表名
    public static final String DA_NAME = "answer.db";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // File file1 = new File(DB_PATH + DA_NAME);
        // file1.delete();
        //从assets文件下的 数据库文件 保存到 应用里的databases目录下
        initDataBase();
        //初始化页面显示的View
        intiPageView();

        viewpg = (ViewPager) this.findViewById(R.id.viewpager);

        viewpg.setAdapter(new MyAdapter<View>(MainActivity.this, Pages, mLimit,
                MainActivity.this, db));

        btn1 = (Button) this.findViewById(R.id.btn1);
        btn2 = (Button) this.findViewById(R.id.btn2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        //设置viewpager的监听
        viewpg.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                mPNSelect = arg0;

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                Log.e(TAG, "onPageScrollStateChanged" + arg0);

            }
        });
    }

    /**
     * 创建并显示 对话框
     */
    private void dialogCreate() {
        FragmentManager fm = getSupportFragmentManager();
        MyDialog dialog = new MyDialog();
        // MyDialog1 dialog=new MyDialog1();

        dialog.show(fm, "tip");

    }

    private void intiPageView() {
        db = new DaoHelp();
        mLimit = db.getQuestionData().size();
        // 初始化前三题
        for (int i = 0; i < 4; i++) {

            Pages.add(new Page(MainActivity.this, db));
        }

    }

    private void initDataBase() {
        InputStream in = null;
        OutputStream out = null;

        try {

            File file1 = new File(DB_PATH + DA_NAME);
            Log.e(TAG, file1.exists() + "sdsadada");
            if (file1.exists())
                return; // 如果数据库表存在 则不创建

            File file = new File(DB_PATH);
            file.mkdir();

            Log.e(TAG, file.exists() + "sdsadada");
            in = getBaseContext().getAssets().open(DA_NAME);
            out = new FileOutputStream(DB_PATH + DA_NAME);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {

                out.write(buffer, 0, len);

            }
            out.flush();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            try {
                if (in != null)
                    in.close();

                if (out != null)
                    out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            dialogCreate();
        }

        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }





//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.submit) {
//            //判断 并得到分数
//            int result=getGrade();
//            showDialogForGrade(result);
//
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    private void showDialogForGrade(int a) {
//        AlertDialog.Builder builder=new AlertDialog.Builder(this);
//        builder.setTitle("Score").setMessage("Your grade is"+a+"分");
//        builder.show();
//
//    }



    /**
     * 得到分数
     * @return
     */
    private int getGrade() {
        int result=0;
        if(db!=null){
            //所有答案集合
            ArrayList<Integer> qa=db.getQuestionReslut();
            ArrayList<RecordForAnswer>  r=(ArrayList<RecordForAnswer>) db.getRecordForAnswer();
            ArrayList<questions> q=(ArrayList<questions>) db.getQuestionData();
            //页面数 有可能中间有漏掉没填的   停在第3页 却只做了 1 3
            for(int i=0;i<r.size();i++){



                for(questions question:q){
                    //答完题   的页数 和问题ID对比 相同则说明答题页数 有填答案
                    if(r.get(i).getPage()==question.getId()){
                        if(question.getBingo()==r.get(i).getAnswer()){
                            result+=20;}}}}

            return result;
        }


        return 0;
    }

    /*
     * 回调自动下一页滚动
     * @see com.example.answer.Page.ViewPageScrollerListener#scrollNext(int)
     */
    @Override
    public void scrollNext(int page) {
        // viewpg.setCurrentItem(page);
        // 保存最大的页面
        if (page > mMaxPage) {
            //当页面最大的  点击才自动滚动
            viewpg.setCurrentItem(page, true);
            mMaxPage = page;
        }

    }

    @Override
    public void scrollPre(int page) {
        // TODO Auto-generated method stub

    }

    /*
     * 对话框的回调方法
     * @see com.example.answer.MyDialog.NotifySaveOrDeleteListener#onNotifySave()
     */
    @Override
    public void onNotifySave() {
        if (db != null) {
            // 选择的时候已经入库了 这里默认保存
            MainActivity.this.finish();
        }

        Toast.makeText(MainActivity.this, "保存", 1000).show();

    }

    @Override
    public void onNotifyDelete() {
        if (db != null) {
            db.clearTRecordTable();
            MainActivity.this.finish();
        }
        Toast.makeText(MainActivity.this, "删除", 1000).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                //如果当前页是0则无效
                if (mPNSelect == 0) {
                    mPNSelect = 0;
                    return;
                }
                int pre = --mPNSelect;
                viewpg.setCurrentItem(pre);

                break;
            case R.id.btn2:
                //如果当前页是最后页则无效
                if (mPNSelect == mLimit - 1) {
                    mPNSelect = mLimit - 1;
                    return;
                }
                int next = ++mPNSelect;
                viewpg.setCurrentItem(next);

                break;

        }

    }
}
