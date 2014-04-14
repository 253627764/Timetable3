package com.eleven.app.events;

import android.view.ActionMode;

/**
 * Created by skyhacker on 14-2-27.
 */
public class CourseActionModeEvent {

    private boolean mIsActionMode;

    public CourseActionModeEvent(boolean mIsActionMode) {
        this.mIsActionMode = mIsActionMode;
    }

    public boolean isActionMode() {
        return this.mIsActionMode;
    }
}
