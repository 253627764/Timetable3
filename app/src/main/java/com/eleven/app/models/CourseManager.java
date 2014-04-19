package com.eleven.app.models;

import android.content.Context;
import android.util.Log;

import com.eleven.app.db.DBHelper;
import com.eleven.app.util.App;
import com.eleven.app.util.ModelPrinter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by skyhacker on 14-2-16.
 */
public class CourseManager {

    public static List<Course> courses;

    public static void init(Context context) {
        DBHelper dbHelper = App.getDBHelper();
        courses = dbHelper.getAllCourse();
        if (courses == null) {
            courses = new ArrayList<Course>();
        }
    }

    public static List<Course> getAllCourses() {
        return null;
    }

    public static List<Course> getCourses(int week) {
        ArrayList<Course> list = new ArrayList<Course>();
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getWeek() == week) {
                list.add(courses.get(i));
            }
        }
        Collections.sort(list, new Comparator<Course>() {
            @Override
            public int compare(Course c1, Course c2) {
                if (c1.getCourseNumber() < c2.getCourseNumber())
                    return -1;
                else if(c1.getCourseNumber() == c2.getCourseNumber())
                    return 0;
                else
                    return 1;
            }
        });
        return list;
    }

    public static void addCourse(Course course) {
        long rowId = App.getDBHelper().addCourse(course);
        course.setId((int)rowId);
        courses.add(course);
    }

    public Course getCourse(int id) {
        return null;
    }

    public List<Course> getAllCourse() {
        return null;
    }

    public static void updateCourse(Course course) {
        int count = App.getDBHelper().updateCourse(course);
        courses = null;
        courses = App.getDBHelper().getAllCourse();
    }

    public static int deleteCourse(Course course) {
        courses.remove(course);
        DBHelper dbHelper = App.getDBHelper();
        return dbHelper.deleteCourse(course.getId());
    }

    public static void deleteCourse(List<Course> courses) {
        for (Course course : courses) {
            deleteCourse(course);
        }
    }

    public static void deleteAll() {
        courses.clear();
        App.getDBHelper().deleteAll();
    }

    /**
     * 获取星期几第几节课
     * @param courseNumber
     * @param week
     * @return
     */
    public static String getCourseName(int courseNumber, int week) {
        String name = "";
        for (Course course : courses) {
            if (course.getWeek() == week && course.getCourseNumber() == courseNumber) {
                name += course.getCourseName() + "\n";
            }
        }
        return name;
    }
}