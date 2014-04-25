package com.eleven.app.receiver;

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

        AlarmReceiver alarm = new AlarmReceiver(context);
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            List<Course> remindList = CourseManager.getRemindCourse();
            for (Course c : remindList) {
                alarm.addAlarm(c);
            }
        }

    }
}
