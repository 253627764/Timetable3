package com.eleven.app.activities;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.eleven.app.R;
import com.eleven.app.adapters.NavDrawerListAdapter;
import java.util.ArrayList;

import com.eleven.app.events.ConfirmEvent;
import com.eleven.app.events.DeleteAllCourseEvent;
import com.eleven.app.events.NewCourseEvent;
import com.eleven.app.framgents.*;
import com.eleven.app.models.CourseManager;
import com.eleven.app.models.NavDrawerItem;
import com.eleven.app.util.App;
import com.squareup.otto.Subscribe;


public class MainActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    private long mCurrentFragmentId;
    public static final long INFO_FRAGMENT = 1L;
    public static final long DAY_FRAGMENT = 1L << 1;
    public static final long WEEK_FRAGMENT = 1L << 2;
    public static final long NEW_COURSE_FRAGMENT = 1L << 3;
    public static final long COURSE_FRAGMENT = 1L << 4;
    public static final long LOGIN_FRAGMENT = 1L << 5;


    // Now position
    private static int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("timetable3", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt("drawerItemPos", 1);
        } else {
            mPosition = 1;
            Log.v("timetable3", "savedInstanceState is null");
        }

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Login
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
        // Info Center
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
        // Day View
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
        // Week View
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3]));

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        displayView(mPosition);
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mCurrentFragmentId != NEW_COURSE_FRAGMENT) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.action_del_all:
                deleteAllCourse();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    public void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new LoginFragment();
                break;
            case 1:
                fragment = new InfoCenterFragment();
                break;
            case 2:
                fragment = new DayViewFragment();
                break;
            case 3:
                fragment = new WeekViewFragment();
                break;
            default:
                break;
        }
        if (fragment != null) {
            displayFragment(fragment);
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }

    }

    public void displayFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment,"TAG").commit();
    }

    public void pushFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment, "TAG");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v("timetable3", "onSaveInstanceState");
        outState.putInt("drawerItemPos", mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v("timetable3", "onRestoreInstanceState");
        mPosition = savedInstanceState.getInt("drawerItemPos", 0);
        displayView(mPosition);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.getBus().unregister(this);
    }


    public void setCurrentFragmentId(long fragmentId) {
        mCurrentFragmentId = fragmentId;
    }

    public DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }

    /**
     * 清空课表
     */
    public void deleteAllCourse() {
        ConfirmDialog dialog = new ConfirmDialog("确认删除全部课程？", ConfirmEvent.DELETE_ALL);
        dialog.show(getSupportFragmentManager(), "delete all course");
    }

    @Subscribe
    public void onConfirmEvent(ConfirmEvent event) {
        if (event.getMsg() != ConfirmEvent.DELETE_ALL) {
            return ;
        }
        CourseManager.deleteAll();
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("TAG");
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }
}

