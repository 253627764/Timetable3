package com.eleven.app.events;

import com.eleven.app.models.Course;

/**
 * Created by skyhacker on 14-2-28.
 */
public class DeleteCourseEvent {

    private Course course;

    public DeleteCourseEvent(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }
}
