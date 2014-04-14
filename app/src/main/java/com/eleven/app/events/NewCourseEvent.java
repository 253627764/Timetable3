package com.eleven.app.events;

import com.eleven.app.models.Course;

/**
 * Created by skyhacker on 14-2-25.
 */
public class NewCourseEvent {

    private Course course;

    public NewCourseEvent(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }
}
