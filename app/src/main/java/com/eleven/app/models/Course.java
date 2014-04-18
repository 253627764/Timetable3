package com.eleven.app.models;


import com.eleven.app.util.ClassInfo;

/**
 * Created by Eleven on 14-2-16.
 */
public class Course {

    private int id;

    private String courseName;

    private int courseNumber; // 第几节

    private String classroom;

    private String teacher;

    private int type; // 选修，必修

    private int week;

    private String range;


    public Course(int id, String courseName, int courseNumber, String classroom, String teacher, int type, int week) {
        setId(id);
        setCourseName(courseName);
        setCourseNumber(courseNumber);
        setClassroom(classroom);
        setTeacher(teacher);
        setType(type);
        setWeek(week);
    }

    public Course(ClassInfo info) {
        setCourseName(info.classname);
        setClassroom(info.location);
        setTeacher(info.teacher);
        setCourseNumber(info.classtime-1);
        setRange(info.time);
        setWeek(info.week);
    }

    public Course() {};

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String name) {
        this.courseName = name;
    }

    public int getCourseNumber() {
        return this.courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getClassroom() {
        return this.classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getTeacher() {
        return this.teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWeek() {
        return this.week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }
}
