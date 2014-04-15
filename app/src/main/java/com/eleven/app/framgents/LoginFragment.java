package com.eleven.app.framgents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.eleven.app.R;
import com.eleven.app.activities.MainActivity;

/**
 * Created by eleven on 14-4-16.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    protected EditText      mLoginnameText;
    protected EditText      mPassText;
    protected Button        mLoginBtn;
    protected CheckBox      mShowPassCheck;

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView       = inflater.inflate(R.layout.fragment_login, container, false);
        mLoginnameText      = (EditText) rootView.findViewById(R.id.loginnameText);
        mPassText           = (EditText) rootView.findViewById(R.id.passText);
        mLoginBtn           = (Button) rootView.findViewById(R.id.loginCheck);
        mShowPassCheck      = (CheckBox) rootView.findViewById(R.id.showPassBtn);
        mLoginBtn.setOnClickListener(this);
        mShowPassCheck.setOnCheckedChangeListener(this);
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
        ((MainActivity)getActivity()).setCurrentFragmentId(MainActivity.LOGIN_FRAGMENT);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            mPassText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            mPassText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}
