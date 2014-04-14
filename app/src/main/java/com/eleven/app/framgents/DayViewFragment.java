package com.eleven.app.framgents;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.eleven.app.R;
import com.eleven.app.activities.MainActivity;
import com.eleven.app.adapters.MyCardArrayMultiChoiceAdapter;
import com.eleven.app.events.ConfirmEvent;
import com.eleven.app.events.CourseActionModeEvent;
import com.eleven.app.events.DeleteCourseEvent;
import com.eleven.app.events.NewCourseEvent;
import com.eleven.app.events.UpdateCourseEvent;
import com.eleven.app.models.Course;
import com.eleven.app.models.CourseManager;
import com.eleven.app.util.App;
import com.eleven.app.view.CourseCard;
import com.eleven.app.view.CourseViewPager;
import com.squareup.otto.Subscribe;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardListView;

public class DayViewFragment extends Fragment {
	
	static final int NUM_ITEMS = 7;
	
	private CourseViewPager mViewPager;
	
	private View[] mPages;

    private CardListView[] mCardListViews;

    private MyCardArrayMultiChoiceAdapter[] mCardArrayAdapter;

    private List<List> mCardList;

	public DayViewFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        Log.v("DayViewFragment", "onCreateView");
        setHasOptionsMenu(true);
		View rootView;
        rootView = inflater.inflate(R.layout.fragment_day_view, container, false);
        mViewPager = (CourseViewPager) rootView.findViewById(R.id.viewPager);
		mPages = new View[7];
        mCardListViews = new CardListView[7];
        mCardArrayAdapter = new MyCardArrayMultiChoiceAdapter[7];
        mCardList = new ArrayList<List>();
		for (int i = 0; i < 7; i++) {
			mPages[i] = inflater.inflate(R.layout.card_view, null);
            ArrayList<Card> cards = getCards(i);
            mCardList.add(cards);
            mCardListViews[i] = (CardListView) mPages[i].findViewById(R.id.cardList);
            mCardArrayAdapter[i] = new MyCardArrayMultiChoiceAdapter(getActivity(), this, cards);
            mCardListViews[i].setAdapter(mCardArrayAdapter[i]);
            mCardListViews[i].setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        }
		mViewPager.setAdapter(new MyPageAdapter());
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
		return rootView;
	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.day, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_new) {
            Fragment newCourseFragment = new NewCourseFragment(mViewPager.getCurrentItem());
            replaceFragment(newCourseFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public class MyPageAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 7;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			mViewPager.addView(mPages[position]);
			return mPages[position];
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View)object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "星期" + position;
		}
	}

    public class MyCardClickListenr implements Card.OnCardClickListener {
        @Override
        public void onClick(Card card, View view) {
            Course course = ((CourseCard) card).getCourse();
            CourseFragment fragment = new CourseFragment(course);
            replaceFragment(fragment);
        }
    }

    private ArrayList<Card> getCards(final int week) {
        ArrayList<Card> cards = new ArrayList<Card>();
        List<Course> courses = CourseManager.getCourses(week);
        for (int i = 0; i < courses.size(); i++) {
            /*
            Card card = new  CourseCard(getActivity(),courses.get(i));
            card.setOnClickListener(new MyCardClickListenr());
            card.setOnLongClickListener(new Card.OnLongCardClickListener() {
                @Override
                public boolean onLongClick(Card card, View view) {
                    return mCardArrayAdapter[week].startActionMode(getActivity());
                }
            });
            */
            Card card = createCard(courses.get(i));
            cards.add(card);
        }
        return cards;
    }

    private CourseCard createCard(final Course course) {
        CourseCard card = new CourseCard(getActivity(), course);
        card.setOnClickListener(new MyCardClickListenr());
        card.setOnLongClickListener(new Card.OnLongCardClickListener() {
            @Override
            public boolean onLongClick(Card card, View view) {
                return mCardArrayAdapter[course.getWeek()].startActionMode(getActivity());
            }
        });
        return card;
    }



    @Override
    public void onResume() {
        super.onResume();
        App.getBus().register(this);
        ((MainActivity)getActivity()).setCurrentFragmentId(MainActivity.DAY_FRAGMENT);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("DayViewFragment", "onPause");

    }

    @Override
    public void onDestroy() {
        super.onDestroyView();
        App.getBus().unregister(this);
        Log.v("DayViewFragment", "onDestroy");
    }

    @Subscribe
    public void onNewCourse(NewCourseEvent event) {
        Course course = event.getCourse();
        Log.v("onNewCourse", course.getCourseName());
        Toast.makeText(getActivity(), "添加1门课程", Toast.LENGTH_SHORT).show();
        mCardArrayAdapter[course.getWeek()].notifyDataSetChanged();
    }

    @Subscribe
    public void onDeleteCourse(DeleteCourseEvent event) {
        Course course = event.getCourse();
        mCardArrayAdapter[course.getWeek()].notifyDataSetChanged();
    }

    @Subscribe
    public void onUpateCourse(UpdateCourseEvent event) {
        Course course = event.getCourse();
        mCardArrayAdapter[course.getWeek()].notifyDataSetChanged();
    }

}
