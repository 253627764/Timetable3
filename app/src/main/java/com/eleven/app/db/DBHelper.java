package com.eleven.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Shader;
import android.util.Log;

import com.eleven.app.models.Course;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by skyhacker on 14-2-23.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "timetable.db";
    private static final int DB_VERSION = 3;
    private static final String TABLE_TIMETABLE = "timetable";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_TIMETABLE +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, courseName TEXT, courseNumber INTEGER, " +
            "classroom TEXT, teacher TEXT, " +
            "type INTEGER, week INTEGER, range TEXT)";

    private static final String KEY_COURSE_NAME = "courseName";
    private static final String KEY_COURSE_NUMBER = "courseNumber";
    private static final String KEY_CLASSROOM = "classroom";
    private static final String KEY_TEACHER = "teacher";
    private static final String KEY_TYPE = "type";
    private static final String KEY_WEEK = "week";
    private static final String KEY_RANGE = "range";


    private Context mContext;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMETABLE);
        onCreate(db);
    }

    public long addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_NAME, course.getCourseName());
        values.put(KEY_COURSE_NUMBER, course.getCourseNumber());
        values.put(KEY_CLASSROOM, course.getClassroom());
        values.put(KEY_RANGE, course.getRange());
        values.put(KEY_TEACHER, course.getTeacher());
        values.put(KEY_TYPE, course.getType());
        values.put(KEY_WEEK, course.getWeek());

        long rowId = db.insert(TABLE_TIMETABLE, null, values);
        //Log.v(DBHelper.class.getName(), "rowId=" + rowId);
        db.close();
        return rowId;
    }

    public Course getCourse(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TIMETABLE, null, "_id=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return getCourseFromCursor(cursor);
        }
        return null;
    }

    public List<Course> getAllCourse() {
        List<Course> courses = new ArrayList<Course>();
        String queryStr = "SELECT * FROM " + TABLE_TIMETABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryStr, null);
        if (cursor.moveToFirst()) {
            do {
                courses.add(getCourseFromCursor(cursor));
            } while(cursor.moveToNext());
            return courses;
        }
        return null;
    }

    public int updateCourse(Course course) {
        Log.v("DBHepler", "_id=" + course.getId());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_NAME, course.getCourseName());
        values.put(KEY_COURSE_NUMBER, course.getCourseNumber());
        values.put(KEY_CLASSROOM, course.getClassroom());
        values.put(KEY_RANGE, course.getRange());
        values.put(KEY_TEACHER, course.getTeacher());
        values.put(KEY_TYPE, course.getType());
        values.put(KEY_WEEK, course.getWeek());
        try {
            return db.update(TABLE_TIMETABLE, values, "_id=?", new String[]{String.valueOf(course.getId())});
        } finally {
            db.close();
        }
    }

    public int deleteCourse(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = db.delete(TABLE_TIMETABLE, "_id=?", new String[]{String.valueOf(id)});
        db.close();
        return count;
    }

    private Course getCourseFromCursor(Cursor cursor) {
        Course course = new Course();
        course.setId(cursor.getInt(0));
        course.setCourseName(cursor.getString(1));
        course.setCourseNumber(cursor.getInt(2));
        course.setClassroom(cursor.getString(3));
        course.setTeacher(cursor.getString(4));
        course.setType(cursor.getInt(5));
        course.setWeek(cursor.getInt(6));
        course.setRange(cursor.getString(7));
        return course;
    }

    /**
     * 清空表
     */

    public void deleteAll() {
        String delSql = "DELETE FROM " + TABLE_TIMETABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(delSql);
        // 重设_id
        String resetIdSql = "UPDATE sqlite_sequence SET seq = 0 WHERE name = '" + TABLE_TIMETABLE + "'";
        db.execSQL(resetIdSql);
        db.close();;
    }
}
