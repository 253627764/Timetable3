package com.eleven.app.util;

import android.app.Application;

import com.eleven.app.db.DBHelper;
import com.eleven.app.models.CourseManager;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by skyhacker on 14-2-25.
 */
public class App extends Application{
    private static Bus mBus;

    private static DBHelper mDBHelper;

    public static Bus getBus() {
        return mBus;
    }
    public static DBHelper getDBHelper() {
        return mDBHelper;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBus = new Bus(ThreadEnforcer.ANY);
        mDBHelper = new DBHelper(this);
        CourseManager.init(this);
    }
}
