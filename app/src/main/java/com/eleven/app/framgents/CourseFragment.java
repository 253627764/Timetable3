package com.eleven.app.framgents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eleven.app.R;
import com.eleven.app.activities.MainActivity;
import com.eleven.app.events.ConfirmEvent;
import com.eleven.app.events.DeleteCourseEvent;
import com.eleven.app.events.NewCourseEvent;
import com.eleven.app.events.UpdateCourseEvent;
import com.eleven.app.models.Course;
import com.eleven.app.models.CourseManager;
import com.eleven.app.util.App;
import com.squareup.otto.Subscribe;


public class CourseFragment extends Fragment {

    private Course mCourse;

    public CourseFragment(Course course) {
        this.mCourse = course;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit, menu);
        inflater.inflate(R.menu.delete, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            ConfirmDialog dialog = new ConfirmDialog("确认删除本课程？", ConfirmEvent.DELETE_COURSE);
            dialog.show(getFragmentManager(), "delete course");
            return true;
        } else if (item.getItemId() == R.id.action_edit) {
            NewCourseFragment fragment = new NewCourseFragment(mCourse);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frame_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
            return true;
        }
        return false;
    }

    @Subscribe public void onConfirmEvent(ConfirmEvent event) {
        if (event.getMsg() == ConfirmEvent.DELETE_COURSE) {
            CourseManager.deleteCourse(mCourse);
            Toast.makeText(getActivity(), "删除1门课程", Toast.LENGTH_SHORT).show();
            App.getBus().post(new DeleteCourseEvent(mCourse));
            getActivity().onBackPressed();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("CourseFragment", "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_course, container, false);
        String courseNumber = getActivity().getResources().getString(R.string.courseNumber);
        courseNumber = String.format(courseNumber, mCourse.getCourseNumber()+1);
        ((TextView) rootView.findViewById(R.id.courseName)).setText(mCourse.getCourseName());
        ((TextView) rootView.findViewById(R.id.courseNumber)).setText(courseNumber);
        ((TextView) rootView.findViewById(R.id.teacher)).setText(mCourse.getTeacher());
        ((TextView) rootView.findViewById(R.id.classroom)).setText(mCourse.getClassroom());
        ((TextView) rootView.findViewById(R.id.range)).setText(mCourse.getRange());
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getBus().register(this);
        ((MainActivity)getActivity()).setCurrentFragmentId(MainActivity.COURSE_FRAGMENT);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getBus().unregister(this);
    }

    @Subscribe
    public void onUpdateCourseEvent(UpdateCourseEvent event) {
        Log.v("onUpdateCourseEvent", event.getCourse().getCourseName());
        Toast.makeText(getActivity(), "更新1门课程", Toast.LENGTH_SHORT).show();
        this.mCourse = event.getCourse();
    }


}
