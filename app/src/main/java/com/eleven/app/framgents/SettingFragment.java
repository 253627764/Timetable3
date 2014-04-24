package com.eleven.app.framgents;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eleven.app.R;
import com.eleven.app.activities.MainActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static String TAG = SettingFragment.class.getSimpleName();

    public static Map<String, String> defalutTime;

	public SettingFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        defalutTime = new HashMap<String, String>();
        defalutTime.put(getString(R.string.pref_key_time_1), "08:15");
        defalutTime.put(getString(R.string.pref_key_time_2), "10:15");
        defalutTime.put(getString(R.string.pref_key_time_3), "14:45");
        defalutTime.put(getString(R.string.pref_key_time_4), "16:30");
        defalutTime.put(getString(R.string.pref_key_time_5), "19:30");

        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        onSharedPreferenceChanged(sharedPreferences, getString(R.string.pref_key_time_1));
        onSharedPreferenceChanged(sharedPreferences, getString(R.string.pref_key_time_2));
        onSharedPreferenceChanged(sharedPreferences, getString(R.string.pref_key_time_3));
        onSharedPreferenceChanged(sharedPreferences, getString(R.string.pref_key_time_4));
        onSharedPreferenceChanged(sharedPreferences, getString(R.string.pref_key_time_5));
        onSharedPreferenceChanged(sharedPreferences, getString(R.string.pref_key_course_time));
        onSharedPreferenceChanged(sharedPreferences, getString(R.string.pref_key_course_break));
        onSharedPreferenceChanged(sharedPreferences, getString(R.string.pref_key_week));
    }


    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (key.equals(getString(R.string.pref_key_course_time))) {
            preference.setSummary(sharedPreferences.getInt(key, 90) + "分钟");
        } else if(key.equals(getString(R.string.pref_key_course_break))) {
            preference.setSummary(sharedPreferences.getInt(key, 15) + "分钟");
        } else if(key.equals(getString(R.string.pref_key_week))) {
            int week = sharedPreferences.getInt(key, 1);
            calStartTime(sharedPreferences, week);
            preference.setTitle("第" + week + "周");
        } else if(key.equals("startDay") ) {
            return;
        } else if (key.equals(getString(R.string.pref_key_remind))) {
            return;
        } else {
            preference.setSummary(sharedPreferences.getString(key, defalutTime.get(key)));
        }
    }

    public void calStartTime(SharedPreferences sharedPreferences, int week) {
        Calendar calendar = Calendar.getInstance();
        int day = (int)(System.currentTimeMillis() / 1000 / 60 / 60 / 24);
        Log.v(TAG, "day=" + day);
        int normalDay = day - calendar.get(Calendar.DAY_OF_WEEK) + 1;
        int startDay = normalDay - (7 * (week-1));
        Log.v(TAG, "startDay" + startDay);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("startDay", startDay);
        editor.commit();

    }
}
