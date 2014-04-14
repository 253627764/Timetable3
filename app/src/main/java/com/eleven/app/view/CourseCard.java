package com.eleven.app.view;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eleven.app.R;
import com.eleven.app.models.Course;

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

    public CourseCard(Context context, Course course) {
        this(context, R.layout.card_item, course);
    }

    public CourseCard(Context context, int innerLayout, Course course) {
        super(context, innerLayout);
        this.mCourse = course;
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
        courseNameView.setText(mCourse.getCourseName());
        courseClassroomView.setText(mCourse.getClassroom());
        courseTimeView.setText("8:00 - 9:00");
        String courseNumber = getContext().getResources().getString(R.string.courseNumber);
        courseNumber = String.format(courseNumber, mCourse.getCourseNumber()+1);
        courseNumberView.setText(courseNumber);
        teacherView.setText(mCourse.getTeacher());
        rangeView.setText(mCourse.getRange());
    }



    public Course getCourse() {
        return mCourse;
    }
}
