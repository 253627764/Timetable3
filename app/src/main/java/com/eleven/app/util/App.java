package com.eleven.app.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

import com.eleven.app.db.DBHelper;
import com.eleven.app.models.CourseManager;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by skyhacker on 14-2-25.
 */
public class App extends Application{

    public static String PREFS_NAME = "timetable3";

    private static SharedPreferences mPreferences;

    private static Bus mBus;

    private static DBHelper mDBHelper;

    private static InputMethodManager mImm;

    public static Bus getBus() {
        return mBus;
    }
    public static DBHelper getDBHelper() {
        return mDBHelper;
    }

    public static SharedPreferences getPreferences()
    {
        return mPreferences;
    }

    public static InputMethodManager getImm() {
        return mImm;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mBus = new Bus(ThreadEnforcer.ANY);
        mDBHelper = new DBHelper(this);
        CourseManager.init(this);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mImm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}
