package com.eleven.app.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.eleven.app.R;
import com.eleven.app.activities.MainActivity;
import com.eleven.app.models.Course;
import com.eleven.app.util.App;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AlarmReceiver extends BroadcastReceiver {

    private static String TAG = AlarmReceiver.class.getSimpleName();

    private AlarmManager mAlarmMgr;

    private Map<Course, PendingIntent> mPendIntentMap;

    private Context mContext;

    public AlarmReceiver() {}

    public AlarmReceiver(Context context) {
        mPendIntentMap = new HashMap<Course, PendingIntent>();
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("你有一门课将要上课");
        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

        Log.v(TAG, "notify");

    }

    public void addAlarm( Course course) {

        mAlarmMgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        mPendIntentMap.put(course, alarmIntent);
        Calendar calendar = getAlarmTime(course);
        mAlarmMgr.setInexactRepeating(AlarmManager.RTC,
                calendar.getTimeInMillis(), 7 * 24 * 60 * 60 * 1000, alarmIntent);

    }

    public void cancelAlarm(Course course) {
        mAlarmMgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        mAlarmMgr.cancel(mPendIntentMap.get(course));
    }

    private Calendar getAlarmTime(Course course) {
        int week = course.getWeek();
        int num = course.getCourseNumber();
        SharedPreferences sharedPreferences = App.getPreferences();
        String startTimeStr = "00:00";
        switch (num) {
            case 1:
                startTimeStr = App.getPreferences().getString(mContext.getString(R.string.pref_key_time_1), "08:15");
                break;
            case 2:
                startTimeStr = App.getPreferences().getString(mContext.getString(R.string.pref_key_time_2), "10:15");
                break;
            case 3:
                startTimeStr = App.getPreferences().getString(mContext.getString(R.string.pref_key_time_3), "14:45");
                break;
            case 4:
                startTimeStr = App.getPreferences().getString(mContext.getString(R.string.pref_key_time_4), "16:30");
                break;
            case 5:
                startTimeStr = App.getPreferences().getString(mContext.getString(R.string.pref_key_time_5), "19:30");
                break;
        }
        int hh = Integer.parseInt(startTimeStr.split(":")[0]);
        int mm = Integer.parseInt(startTimeStr.split(":")[1]);
        mm += 60 - course.getRemindAhead();
        hh += mm / 60 - 1;
        mm = mm % 60;

        Log.v(TAG, "hh = " + hh);
        Log.v(TAG, "mm = " + mm);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, week+1);
        calendar.set(Calendar.HOUR_OF_DAY, hh);
        calendar.set(Calendar.MINUTE, mm);
        return calendar;

    }

}
