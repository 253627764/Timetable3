package com.eleven.app.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.eleven.app.db.DBHelper;
import com.eleven.app.models.CourseManager;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.Calendar;
import com.eleven.app.R;

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

        // 计算周数
        int startDay = mPreferences.getInt("startDay", 0);
        Log.v("App", "startDay=" + startDay);
        if (startDay != 0) {
            int today = (int)(System.currentTimeMillis() / 1000 / 60 / 60 / 24);
            int week = (today - startDay) / 7 + 1;
            Log.v("App", "today=" + today);
            Log.v("App", "startDay=" + startDay);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(getString(R.string.pref_key_week), week);
            editor.commit();
        }
    }


}
