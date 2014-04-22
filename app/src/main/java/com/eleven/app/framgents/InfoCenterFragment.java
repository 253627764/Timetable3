package com.eleven.app.framgents;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eleven.app.R;
import com.eleven.app.activities.MainActivity;

public class InfoCenterFragment extends Fragment {
	
	public InfoCenterFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_info_center, container, false);
        TextView textView = (TextView)rootView.findViewById(R.id.font);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
        textView.setTypeface(font);
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
        ((MainActivity)getActivity()).setCurrentFragmentId(MainActivity.INFO_FRAGMENT);
    }
}
