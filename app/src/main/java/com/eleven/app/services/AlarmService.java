package com.eleven.app.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.eleven.app.R;
import com.eleven.app.activities.MainActivity;
import com.eleven.app.models.Course;
import com.eleven.app.models.CourseManager;
import com.eleven.app.util.App;

import java.util.Calendar;
import java.util.List;

public class AlarmService extends Service {

    private static String TAG = AlarmService.class.getSimpleName();


    private int sleepTimeCount = 1 * 60 * 1000;
    private int sleepTime = sleepTimeCount;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Calendar calendar = Calendar.getInstance();
            List<Course> remindList = CourseManager.getRemindCourse(calendar.get(Calendar.DAY_OF_WEEK) - 1);
            Log.v(TAG, "remindList size = " + remindList.size());
            boolean isReminded = false;
            for (Course course : remindList) {
                if (isAlarmTime(course)) {
                    showNotification();
                    isReminded = true;
                }
            }
            if (isReminded) {
                sleepTime = sleepTimeCount * 90;
            } else {
                sleepTime = sleepTimeCount;
            }
        }
    };

    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    mHandler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();;

    }

    public void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("你有一门课将要上课");
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    private boolean isAlarmTime(Course course) {
        int week = course.getWeek();
        int num = course.getCourseNumber();
        SharedPreferences sharedPreferences = App.getPreferences();
        String startTimeStr = "00:00";
        switch (num) {
            case 1:
                startTimeStr = App.getPreferences().getString(getString(R.string.pref_key_time_1), "08:15");
                break;
            case 2:
                startTimeStr = App.getPreferences().getString(getString(R.string.pref_key_time_2), "10:15");
                break;
            case 3:
                startTimeStr = App.getPreferences().getString(getString(R.string.pref_key_time_3), "14:45");
                break;
            case 4:
                startTimeStr = App.getPreferences().getString(getString(R.string.pref_key_time_4), "16:30");
                break;
            case 5:
                startTimeStr = App.getPreferences().getString(getString(R.string.pref_key_time_5), "19:30");
                break;
        }
        int hh = Integer.parseInt(startTimeStr.split(":")[0]);
        int mm = Integer.parseInt(startTimeStr.split(":")[1]);
        String courseTime = String.format("%02d:%02d", hh, mm);
        mm += 60 - course.getRemindAhead();
        hh += mm / 60 - 1;
        mm = mm % 60;

        String alarmTime = String.format("%02d:%02d", hh, mm);
        Calendar calendar = Calendar.getInstance();
        String now = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        if (now.compareTo(alarmTime) >= 0 && now.compareTo(courseTime) <= 0) {
            return true;
        }

        return false;

    }
}
