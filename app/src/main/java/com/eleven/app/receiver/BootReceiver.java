package com.eleven.app.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.eleven.app.models.Course;
import com.eleven.app.models.CourseManager;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            // 启动计时器，每隔1分钟发送一次广播查询是否有课程需要提醒
            AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, AlarmReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, i, 0);
            alarmMgr.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),60 * 1000, alarmIntent);
        }

    }
}
