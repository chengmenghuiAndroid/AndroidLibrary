package com.android.baseline.framework.db;

import android.content.Context;
import com.android.baseline.AppDroid;
import com.android.baseline.framework.log.Logger;
import com.android.baseline.util.SPDBHelper;
import net.sqlcipher.database.SQLiteDatabase;

/**
 * 数据库轻量级操作封装
 * 使用SQLCipher加密数据库，详见https://www.zetetic.net/sqlcipher/sqlcipher-for-android/
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2015-08-10]
 */
public class EncryptDBHelper
{
    private DataBaseHelper dbHelper;
    private SQLiteDatabase writableDB;
    private SQLiteDatabase readableDB;
    /** 数据库名称 */
    private static final String DATABASE_NAME = "project.db";
    /** 数据库版本 */
    private static final int DATABASE_VERSION = 1;
    /** 数据库加密密码*/
    private static final String DATABASE_PASSWORD = "Android-Baseline";
    public EncryptDBHelper()
    {
        SQLiteDatabase.loadLibs(AppDroid.getInstance().getApplicationContext());
        dbHelper = new DataBaseHelper(AppDroid.getInstance().getApplicationContext());
    }

    /**
     * 获取数据库操作对象
     * 
     * @return SQLiteDatabase
     */
    public synchronized SQLiteDatabase getWritableSQLiteDatabase()
    {
        writableDB = dbHelper.getWritableDatabase(DATABASE_PASSWORD);
        return writableDB;
    }
    
    /**
     * 获取数据库操作对象
     * 
     * @return SQLiteDatabase
     */
    public SQLiteDatabase getReadableSQLiteDatabase()
    {
        readableDB = dbHelper.getReadableDatabase(DATABASE_PASSWORD);
        return readableDB;
    }

    /**
     * 关闭数据库
     */
    public void close()
    {
        writableDB = null;
        readableDB = null;
        if (dbHelper != null)
        {
            dbHelper.close();
        }
    }

    public class DataBaseHelper extends net.sqlcipher.database.SQLiteOpenHelper
    {
        private static final String TAG = "DataBaseHelper";

        public DataBaseHelper(Context context)
        {
            super(context, DATABASE_NAME,
                    null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.beginTransaction();
            try
            {
                db.execSQL(SPDBHelper.TABLE_CREATE_SQL);
                db.setTransactionSuccessful();
            }
            catch (Exception e)
            {
                Logger.e(TAG, e);
            }
            finally
            {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL("DROP DATABASE IF EXISTS " + DATABASE_NAME);
            onCreate(db);
        }
    }
}