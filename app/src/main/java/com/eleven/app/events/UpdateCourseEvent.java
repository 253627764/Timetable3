package com.eleven.app.events;

import com.eleven.app.models.Course;

/**
 * Created by eleven on 3/5/14.
 */
public class UpdateCourseEvent {

    private Course mCourse;

    public UpdateCourseEvent(Course course) {
        mCourse = course;
    }

    public Course getCourse() {
        return mCourse;
    }
}
