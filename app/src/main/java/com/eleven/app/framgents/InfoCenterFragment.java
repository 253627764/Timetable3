package com.eleven.app.framgents;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eleven.app.R;
import com.eleven.app.activities.MainActivity;
import com.eleven.app.models.Course;
import com.eleven.app.models.CourseManager;
import com.eleven.app.util.App;
import com.eleven.app.view.CourseCard;
import com.eleven.app.view.TimeCard;

import java.util.Calendar;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

public class InfoCenterFragment extends Fragment {

	private static String TAG = InfoCenterFragment.class.getSimpleName();

    public class CourseCardListener implements Card.OnCardClickListener {

        @Override
        public void onClick(Card card, View view) {
            Fragment courseFragment = new CourseFragment(((CourseCard) card).getCourse());
            ((MainActivity)getActivity()).pushFragment(courseFragment);
        }
    }

	public InfoCenterFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_info_center, container, false);

        Course course = getNextCourse();
        CourseCard nextCourseCard = new CourseCard(getActivity(), course);
        nextCourseCard.setOnClickListener(new CourseCardListener());
        CardView cardView = (CardView) rootView.findViewById(R.id.nextCourse);
        cardView.setCard(nextCourseCard);

        TimeCard timeCard = new TimeCard(getActivity());
        CardView timeCardView = (CardView)rootView.findViewById(R.id.time);
        timeCardView.setCard(timeCard);
        return rootView;
	}

    public Course getNextCourse() {
        // 现在时间
        Calendar date = Calendar.getInstance();
        int hh = date.get(Calendar.HOUR_OF_DAY);
        int mm = date.get(Calendar.MINUTE);
        int week = date.get(Calendar.WEEK_OF_MONTH) - 1; // sunday = 1
        String time = String.format("%02d:%02d", hh, mm);
        String time1 = App.getPreferences().getString(getResources().getString(R.string.pref_key_time_1), "08:15");
        String time2 = App.getPreferences().getString(getResources().getString(R.string.pref_key_time_2), "10:15");
        String time3 = App.getPreferences().getString(getResources().getString(R.string.pref_key_time_3), "14:45");
        String time4 = App.getPreferences().getString(getResources().getString(R.string.pref_key_time_4), "16:30");
        String time5 = App.getPreferences().getString(getResources().getString(R.string.pref_key_time_5), "19:30");
        Course course = null;
        if (time.compareTo(time1) < 0) {
            course = CourseManager.getCourse(1, week);
        }
        if (course != null && time.compareTo(time1) > 0) {
            course = CourseManager.getCourse(2, week);
        }
        if (course != null && time.compareTo(time2) > 0) {
            course = CourseManager.getCourse(3, week);
        }
        if (course != null && time.compareTo(time3) > 0) {
            course = CourseManager.getCourse(4, week);
        }
        if (course != null && time.compareTo(time4) > 0) {
            course = CourseManager.getCourse(5, week);
        }
        return course;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setCurrentFragmentId(MainActivity.INFO_FRAGMENT);
    }
}
