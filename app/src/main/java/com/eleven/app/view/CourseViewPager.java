package com.eleven.app.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.eleven.app.events.CourseActionModeEvent;
import com.eleven.app.util.App;
import com.squareup.otto.Subscribe;

/**
 * Created by skyhacker on 14-2-27.
 */
public class CourseViewPager extends ViewPager{

    private boolean isPagingEnabled;

    public CourseViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        App.getBus().register(this);
        this.isPagingEnabled = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.isPagingEnabled) {
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.isPagingEnabled) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Subscribe public void onCourseActionMode(CourseActionModeEvent event) {
        if (event.isActionMode()) {
            this.isPagingEnabled = false;
        } else {
            this.isPagingEnabled = true;
        }
    }

}
