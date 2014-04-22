package com.eleven.app.framgents;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eleven.app.R;
import com.eleven.app.activities.MainActivity;
import com.eleven.app.view.TimeCard;

import java.util.Calendar;
import java.util.logging.LogRecord;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

public class InfoCenterFragment extends Fragment {

    private Handler clockHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Calendar calendar = Calendar.getInstance();
        }
    };
	
	public InfoCenterFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_info_center, container, false);
        Card card = new Card(getActivity());
        CardHeader header = new CardHeader(getActivity());
        header.setTitle("软件工程");
        card.addCardHeader(header);
        card.setTitle("主楼");
        CardView cardView = (CardView)rootView.findViewById(R.id.nextCourse);
        cardView.setCard(card);

        TimeCard timeCard = new TimeCard(getActivity());
        CardView timeCardView = (CardView)rootView.findViewById(R.id.time);
        timeCardView.setCard(timeCard);
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
