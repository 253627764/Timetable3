package com.eleven.app.framgents;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eleven.app.R;
import com.eleven.app.activities.MainActivity;
import com.eleven.app.db.DBHelper;
import com.eleven.app.events.NewCourseEvent;
import com.eleven.app.events.UpdateCourseEvent;
import com.eleven.app.models.Course;
import com.eleven.app.models.CourseManager;
import com.eleven.app.util.App;
import com.squareup.otto.Produce;


public class NewCourseFragment extends Fragment {

    Spinner mWeekSpinner;
    Spinner mCourseIndexSpinner;
    Spinner mCourseTypeSpinner;

    EditText mCourseNameText;
    EditText mClassroomText;
    EditText mTeacherText;
    EditText mRangeText;

    private InputMethodManager mInputMethodManager;

    private int mWeek;

    private Course mCourse;

    private boolean isEdit = false;

    View.OnClickListener mActionbarListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            onActionbarItemSelected(v.getId());
        }
    };

    public NewCourseFragment(int week) {
        this.mWeek = week;
    }

    public NewCourseFragment(Course course) {
        this.mCourse = course;
        this.mWeek = course.getWeek();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mInputMethodManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_new_course, null);
        mWeekSpinner = (Spinner) rootView.findViewById(R.id.week_spinner);
        mCourseIndexSpinner = (Spinner) rootView.findViewById(R.id.course_index);
        mCourseTypeSpinner = (Spinner) rootView.findViewById(R.id.type);
        ArrayAdapter<CharSequence> weekAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.week_array, android.R.layout.simple_spinner_item);
        weekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mWeekSpinner.setAdapter(weekAdapter);
        mWeekSpinner.setSelection(mWeek);

        ArrayAdapter<CharSequence> courseIndexAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.course_index_array, android.R.layout.simple_spinner_item);
        courseIndexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCourseIndexSpinner.setAdapter(courseIndexAdapter);

        ArrayAdapter<CharSequence> courseTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.course_type_array, android.R.layout.simple_spinner_item);
        courseIndexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCourseTypeSpinner.setAdapter(courseTypeAdapter);

        mCourseNameText = (EditText) rootView.findViewById(R.id.courseName);
        mClassroomText = (EditText) rootView.findViewById(R.id.classRoom);
        mTeacherText = (EditText) rootView.findViewById(R.id.teacher);
        mRangeText = (EditText) rootView.findViewById(R.id.range);
        // 初始化数据
        if (mCourse != null) {
            isEdit = true;
            mCourseNameText.setText(mCourse.getCourseName());
            mClassroomText.setText(mCourse.getClassroom());
            mTeacherText.setText(mCourse.getTeacher());
            mRangeText.setText(mCourse.getRange());
            mCourseIndexSpinner.setSelection(mCourse.getCourseNumber());
            mCourseTypeSpinner.setSelection(mCourse.getType());
        }

        // 自定义ActionBar
        View actionButtons = inflater.inflate(R.layout.edit_custom_actionbar, new LinearLayout(getActivity()), false);
        View doneActionView = actionButtons.findViewById(R.id.action_done);
        View cancelActionView = actionButtons.findViewById(R.id.action_cancel);
        doneActionView.setOnClickListener(mActionbarListener);
        cancelActionView.setOnClickListener(mActionbarListener);
        getActivity().getActionBar().setCustomView(actionButtons);
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getActionBar().setCustomView(null);
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.new_course, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return onActionbarItemSelected(item.getItemId());
    }

    private Course getEditCourse() {
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
        return course;
    }

    private void addCourse() {
        Course course = getEditCourse();
        CourseManager.addCourse(course);
        //DBHelper dbHelper = new DBHelper(getActivity());
        //dbHelper.addCourse(course);

        App.getBus().post(new NewCourseEvent(course));
    }

    private void editCourse() {
        Log.v("editCourse id=", mCourse.getId()+"");
        Course course = getEditCourse();
        course.setId(mCourse.getId());
        CourseManager.updateCourse(course);
        App.getBus().post(new UpdateCourseEvent(course));
    }

    public boolean onActionbarItemSelected(int itemId) {
        View focusedView = getActivity().getCurrentFocus();
        if (focusedView != null) {
            mInputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
            focusedView.clearFocus();
        }
        switch (itemId) {
            case R.id.action_cancel:
                getActivity().onBackPressed();
                break;
            case R.id.action_done:
                if (isEdit) {
                    editCourse();
                } else {
                    addCourse();
                }
                getActivity().onBackPressed();
                break;
        }

        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setCurrentFragmentId(MainActivity.NEW_COURSE_FRAGMENT);
        ((MainActivity) getActivity()).getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        App.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity)getActivity()).getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        App.getBus().unregister(this);
    }

}
