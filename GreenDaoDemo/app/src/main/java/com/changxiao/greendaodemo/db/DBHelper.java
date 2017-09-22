package com.changxiao.greendaodemo.db;

import android.content.Context;

import com.changxiao.greendaodemo.db.dao.DaoMaster;
import com.changxiao.greendaodemo.db.dao.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * DevOpenHelper helper = new DevOpenHelper(this, "notes-db");
 * Database db = helper.getWritableDb();
 * daoSession = new DaoMaster(db).newSession();
 *
 * 参考：
 * http://greenrobot.org/greendao/documentation/how-to-get-started/#Extending_and_adding_entities
 * <p>
 * Created by Chang.Xiao on 2017/9/15.
 *
 * @version 1.0
 */
public class DBHelper extends DaoMaster.OpenHelper {

    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = false;

    private static DBHelper mInstance;

    private Database mDb;
    private DaoSession mDaoSession;

    private DBHelper(Context context, String name) {
        super(context, name);
        mDb = ENCRYPTED ? getEncryptedReadableDb("super-secret") : getWritableDb();
        mDaoSession = new DaoMaster(mDb).newSession();
    }

    public static DBHelper getInstance(Context context) {
        if (null == mInstance) {
            synchronized (DBHelper.class) {
                if (null == mInstance) {
                    mInstance = new DBHelper(context.getApplicationContext(), getDbName());
                }
            }
        }
        return mInstance;
    }

    public static String getDbName() {
        return ENCRYPTED ? "notes-db-encrypted" : "notes-db";
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
