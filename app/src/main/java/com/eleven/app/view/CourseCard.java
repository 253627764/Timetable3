package com.eleven.app.view;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eleven.app.R;
import com.eleven.app.models.Course;
import com.eleven.app.util.App;

import java.util.Calendar;
import java.util.Date;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by skyhacker on 14-2-16.
 */
public class CourseCard extends Card {

    private TextView courseNumberView;
    private TextView courseTimeView;
    private TextView courseNameView;
    private TextView courseClassroomView;
    private TextView teacherView;
    private TextView rangeView;
    private Course mCourse;
    private Context mContext;

    public CourseCard(Context context, Course course) {
        this(context, R.layout.card_item, course);
    }

    public CourseCard(Context context, int innerLayout, Course course) {
        super(context, innerLayout);
        this.mCourse = course;
        mContext = context;
        init();
    }

    private void init() {

    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        courseTimeView = (TextView) parent.findViewById(R.id.courseTime);
        courseClassroomView = (TextView) parent.findViewById(R.id.courseClassroom);
        courseNameView = (TextView) parent.findViewById(R.id.courseName);
        courseNumberView = (TextView) parent.findViewById(R.id.courseNumber);
        teacherView = (TextView) parent.findViewById(R.id.teacher);
        rangeView = (TextView) parent.findViewById(R.id.range);
        if (mCourse != null) {
            courseNameView.setText(mCourse.getCourseName());
            courseClassroomView.setText(mCourse.getClassroom());
            //courseTimeView.setText("8:00 - 9:00");
            courseTimeView.setText(getCourseTimeRange(mCourse.getCourseNumber() + 1));
            String courseNumber = getContext().getResources().getString(R.string.courseNumber);
            courseNumber = String.format(courseNumber, mCourse.getCourseNumber() + 1);
            courseNumberView.setText(courseNumber);
            teacherView.setText(mCourse.getTeacher());
            rangeView.setText(mCourse.getRange());
        } else {
            courseNameView.setText("暂无课程");
        }
    }



    public Course getCourse() {
        return mCourse;
    }

    private String getCourseTimeRange(int num) {
        int eachTime = App.getPreferences().getInt(mContext.getString(R.string.pref_key_course_time), 45);
        int breakTime = App.getPreferences().getInt(mContext.getString(R.string.pref_key_course_break), 15);
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

        mm += eachTime * 2 + breakTime;
        hh += mm / 60;
        mm = mm % 60;
        String timeStr = String.format("%s - %02d:%02d", startTimeStr,hh,mm);

        return timeStr;

    }
}
