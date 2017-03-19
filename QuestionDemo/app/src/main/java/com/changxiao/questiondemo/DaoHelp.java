package com.changxiao.questiondemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * $desc$
 * <p>
 * Created by Chang.Xiao on 2016/10/25.
 *
 * @version 1.0
 */

public class DaoHelp {
    private SQLiteDatabase db;
    //删除记录答案表的 sql语句
    private static final String DROP_TABLE = "drop table if exists forrecord";
    //记录答案的表名
    private static final String RECORD_TABLE ="forrecord";

    private List<questions> data = new ArrayList<questions>();
    private List<RecordForAnswer> answers = new ArrayList<RecordForAnswer>();
    private String TAG;

    public DaoHelp() {

        openDataBase();
    }

    private void openDataBase() {
        db = SQLiteDatabase.openOrCreateDatabase(MainActivity.DB_PATH
                + MainActivity.DA_NAME, null);

    }

    /**
     * 得到问题库的内容
     * @return
     */
    public List<questions> getQuestionData() {
        data.clear();
        // db.beginTransaction()
        Cursor cursor = db.rawQuery("select * from questions", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                questions q = new questions();
                q.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                q.setQuestion(cursor.getString(cursor.getColumnIndex("question")));
                q.setBingo(cursor.getInt(cursor.getColumnIndex("answer_bingo")));
                q.setAnswer_a(cursor.getString(cursor
                        .getColumnIndex("answer_a")));
                q.setAnswer_b(cursor.getString(cursor
                        .getColumnIndex("answer_b")));
                q.setAnswer_c(cursor.getString(cursor
                        .getColumnIndex("answer_c")));
                q.setAnswer_d(cursor.getString(cursor
                        .getColumnIndex("answer_d")));
                q.setExplanation(cursor.getString(cursor
                        .getColumnIndex("explanation")));
                data.add(q);
                cursor.moveToNext();
            }
            cursor.close();

        }

        return data;
    }

    /**
     * 得到保存答案的数据
     * @return
     */
    public List<RecordForAnswer> getRecordForAnswer() {
        answers.clear();
        // db.beginTransaction()
        Cursor cursor = db.rawQuery("select * from forrecord", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                RecordForAnswer q = new RecordForAnswer();
                q.setID(cursor.getInt(cursor.getColumnIndex("_id")));
                q.setAnswer(cursor.getInt(cursor.getColumnIndex("answer")));
                q.setPage(cursor.getInt(cursor.getColumnIndex("page")));
                cursor.moveToNext();
                answers.add(q);

            }
            cursor.close();

        }

        return answers;
    }

    /**
     * 删除该表内容
     */
    public void clearTRecordTable() {
        //      db.execSQL(DROP_TABLE);  不删除该标 会效率低
        db.delete("forrecord", "_id>=?", new String[]{1+""});

    }
    /**
     *
     * 保存答案
     * @param bingo
     */
    public void save(int bingo,int page){
        //如果已经保存过了 就调用修改方法
        if(query(page)){
            motify(page, bingo);
            return;
        }

        String sql="insert into forrecord(answer,page)values(?,?)";
        //      ContentValues c= new ContentValues();
        //      c.put("answer", bingo);

        db.execSQL(sql, new Object[]{bingo,page});


    }
    public int getOneOfAnswer(int page){
        int answerID=0;
        Cursor cursor= db.rawQuery("select * from forrecord where page=? ",new String[]{page+""});
        if(cursor.moveToFirst()){
            answerID=cursor.getInt(cursor.getColumnIndex("answer"));

        }

        cursor.close();
        return  answerID;



    }

    /**
     *
     * 查询该ID所在列  是否存在
     * @return
     */
    public boolean query(int page){
        boolean flag=false;
        Cursor cursor=db.rawQuery("select * from forrecord where page=? ",new String[]{page+""});

        //      Log.e(TAG, cursor.getInt(cursor.getColumnIndex("page"))+"");
        if(cursor.moveToFirst()){
            flag=true;
        }

        return flag;

    }
    /**
     * 修改答案
     * @param bingo
     */
    public void motify(int page ,int bingo ){
        db.execSQL("update forrecord set answer=? where page=? ", new Object[]{bingo,page});

    }
    //查询所有的 问题答案
    private ArrayList<Integer> QAnswers=new ArrayList<Integer>();
    public ArrayList<Integer> getQuestionReslut(){
        QAnswers.clear();
        Cursor cursor = db.rawQuery("select answer_bingo from questions ", null);
        if(cursor.moveToFirst()){
            for(int i=0; i<cursor.getCount();i++){

                QAnswers.add(cursor.getInt(cursor.getColumnIndex("answer_bingo")));
                Log.e(TAG, "answer_bingo"+cursor.getInt(cursor.getColumnIndex("answer_bingo")));
                cursor.moveToNext();
            }


        }
        return QAnswers;

    }

    //查选 所有的答题保存的答案

    private ArrayList<Integer> RAnswers=new ArrayList<Integer>();
    public ArrayList<Integer> getRecordReslut(){
        RAnswers.clear();
        Cursor cursor = db.rawQuery("select answer from forrecord ", null);
        if(cursor.moveToFirst()){
            for(int i=0; i<cursor.getCount();i++){

                RAnswers.add(cursor.getInt(cursor.getColumnIndex("answer")));

            }


        }
        return RAnswers;

    }
}
