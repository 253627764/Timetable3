package com.eleven.app.activities;



import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.eleven.app.R;
import com.eleven.app.db.DBHelper;
import com.eleven.app.events.NewCourseEvent;
import com.eleven.app.models.Course;
import com.eleven.app.util.App;
import com.squareup.otto.Produce;

public class NewCourseActivity extends FragmentActivity {

    Spinner mWeekSpinner;
    Spinner mCourseIndexSpinner;
    Spinner mCourseTypeSpinner;

    TextView mCourseNameText;
    TextView mClassroomText;
    TextView mTeacherText;
    TextView mRangeText;

    Course mCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_course);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        this.setTitle("添加课程");

        mWeekSpinner = (Spinner) findViewById(R.id.week_spinner);
        mCourseIndexSpinner = (Spinner) findViewById(R.id.course_index);
        mCourseTypeSpinner = (Spinner) findViewById(R.id.type);
        ArrayAdapter<CharSequence> weekAdapter = ArrayAdapter.createFromResource(this, R.array.week_array, android.R.layout.simple_spinner_item);
        weekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mWeekSpinner.setAdapter(weekAdapter);

        ArrayAdapter<CharSequence> courseIndexAdapter = ArrayAdapter.createFromResource(this, R.array.course_index_array, android.R.layout.simple_spinner_item);
        courseIndexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCourseIndexSpinner.setAdapter(courseIndexAdapter);

        ArrayAdapter<CharSequence> courseTypeAdapter = ArrayAdapter.createFromResource(this, R.array.course_type_array, android.R.layout.simple_spinner_item);
        courseIndexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCourseTypeSpinner.setAdapter(courseTypeAdapter);

        mCourseNameText = (TextView) findViewById(R.id.courseName);
        mClassroomText = (TextView) findViewById(R.id.classRoom);
        mTeacherText = (TextView) findViewById(R.id.teacher);
        mRangeText = (TextView) findViewById(R.id.range);

        App.getBus().register(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_finish) {
            addCourse();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_cancel) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addCourse() {
        String courseName = mCourseNameText.getText().toString();
        String classroom = mClassroomText.getText().toString();
        String teacher = mTeacherText.getText().toString();
        String range = mRangeText.getText().toString();
        int week = mWeekSpinner.getSelectedItemPosition();
        int courseNumber = mCourseIndexSpinner.getSelectedItemPosition();
        int courseType = mCourseTypeSpinner.getSelectedItemPosition();
        Course course = new Course();
        course.setCourseName(courseName);
        course.setTeacher(teacher);
        course.setClassroom(classroom);
        course.setRange(range);
        course.setWeek(week);
        course.setCourseNumber(courseNumber);
        course.setType(courseType);
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.addCourse(course);
        mCourse = course;
        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        App.getBus().post(new NewCourseEvent(course));
        onBackPressed();
    }

}
