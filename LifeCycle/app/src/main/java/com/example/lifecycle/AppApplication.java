package com.example.lifecycle;


import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.lifecycle.counter.WorkModel;
import com.higyon.myapplication.appcomm.greendao.DaoMaster;
import com.higyon.myapplication.appcomm.greendao.DaoSession;

public class AppApplication extends Application{

    public static AppApplication instances = null;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    private WorkModel work = new WorkModel();

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        setDatabase();
    }

    public static AppApplication getInstances(){
        if(instances == null){
            instances = new AppApplication();
        }
        return instances;
    }

    public WorkModel getWork() {
        return work;
    }

    private void setDatabase() {

        mHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = mHelper.getWritableDatabase();

        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
    public static DaoSession getDaoSession() {
        return mDaoSession;
    }
    public SQLiteDatabase getDb() {
        return db;
    }
}
