package com.eleven.app.framgents;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eleven.app.R;
import com.eleven.app.activities.MainActivity;

public class WeekViewFragment extends Fragment {
	
	public WeekViewFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_week_view, container, false);
		
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
