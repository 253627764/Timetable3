package com.eleven.app.framgents;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eleven.app.R;
import com.eleven.app.activities.MainActivity;
import com.eleven.app.models.CourseManager;

public class WeekViewFragment extends Fragment {
	
	public WeekViewFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        String[] weekArr = getResources().getStringArray(R.array.week_array);

		View rootView = inflater.inflate(R.layout.fragment_week_view, container, false);

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.container);

        for (int i = 1; i <= 5; i++) {
            View weekBody = inflater.inflate(R.layout.week_body, null);
            TextView num = (TextView) weekBody.findViewById(R.id.num);
            num.setText("" + i);

            TextView item1 = (TextView) weekBody.findViewById(R.id.item1);
            item1.setText(CourseManager.getCourseName(i - 1, 1));

            TextView item2 = (TextView) weekBody.findViewById(R.id.item2);
            item2.setText(CourseManager.getCourseName(i-1, 2));

            TextView item3 = (TextView) weekBody.findViewById(R.id.item3);
            item3.setText(CourseManager.getCourseName(i-1, 3));

            TextView item4 = (TextView) weekBody.findViewById(R.id.item4);
            item4.setText(CourseManager.getCourseName(i-1, 5));

            TextView item5 = (TextView) weekBody.findViewById(R.id.item5);
            item5.setText(CourseManager.getCourseName(i-1, 1));

            layout.addView(weekBody);

            //View line = inflater.inflate(R.layout.line, null);
            //layout.addView(line);
        }

		return rootView;
	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setCurrentFragmentId(MainActivity.WEEK_FRAGMENT);
    }
}
