package com.eleven.app.receiver;

import android.app.AlarmManager;
import android.app.Notification;
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
import com.eleven.app.models.CourseManager;
import com.eleven.app.util.App;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlarmReceiver extends BroadcastReceiver {

    private static String TAG = AlarmReceiver.class.getSimpleName();

    private AlarmManager mAlarmMgr;

    private Context mContext;

    public AlarmReceiver() {}

    public AlarmReceiver(Context context) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        /*
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
        */
        mContext = context;
        // 获取今天所有课程
        Calendar calendar = Calendar.getInstance();
        List<Course> courses = CourseManager.getCourses(calendar.get(Calendar.DAY_OF_WEEK)-1);
        //Log.d(TAG, "" +calendar.get(Calendar.DAY_OF_WEEK));
        // 遍历课程判断是否提醒
        for (Course course : courses) {
            if (isAlarm(course)) {
                notifyCourse(context, course);
                break;
            }
        }
        //Log.v(TAG, "收到广播");

    }

    public void notifyCourse(Context context, Course course) {
        Log.v(TAG, "提示上课");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("你有一门课将要上课");
        builder.setContentText(course.getCourseName());
        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }

    public void cancelAlarm(Course course) {
        mAlarmMgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
    }

    // 是否提醒
    public boolean isAlarm(Course course) {
        String alarmTimeStr = getAlarmTime(course);
        //Log.v(TAG, "alarmTimeStr = " + alarmTimeStr);
        Calendar calendar = Calendar.getInstance();
        // 目前小时和分
        int curHH = calendar.get(Calendar.HOUR_OF_DAY);
        int curMM = calendar.get(Calendar.MINUTE);
        int hh = Integer.parseInt(alarmTimeStr.split(":")[0]);
        int mm = Integer.parseInt(alarmTimeStr.split(":")[1]);

        if (curHH == hh && curMM == mm) {
            return true;
        } else {
            return false;
        }
    }

    private String getAlarmTime(Course course) {
        int week = course.getWeek();
        int num = course.getCourseNumber() + 1;
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

        return String.format("%02d:%02d", hh, mm);

    }

}
