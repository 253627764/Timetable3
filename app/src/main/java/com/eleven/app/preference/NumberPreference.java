package com.eleven.app.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by eleven on 14-4-22.
 */
public class NumberPreference extends DialogPreference {

    private NumberPicker mPicker;

    private int mValue = 90;

    public NumberPreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);

        setPositiveButtonText("设置");
        setNegativeButtonText("取消");
    }

    @Override
    protected View onCreateDialogView() {
        mPicker = new NumberPicker(getContext());
        return mPicker;

    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mPicker.setMaxValue(45);
        mPicker.setMinValue(1);
        mPicker.setValue(mValue);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        mValue = mPicker.getValue();

        if (positiveResult) {
            if (callChangeListener(mValue)) {
                persistInt(mValue);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {

        int value;
        if (restorePersistedValue) {
            if (defaultValue == null) {
                value = getPersistedInt(90);
            } else {
                value = getPersistedInt(Integer.parseInt(defaultValue.toString()));
            }
        } else {
            value = Integer.parseInt(defaultValue.toString());
        }

        mValue = value;
    }
}
