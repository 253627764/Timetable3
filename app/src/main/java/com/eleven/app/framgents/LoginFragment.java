package com.eleven.app.framgents;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import android.widget.Toast;

import com.eleven.app.R;
import com.eleven.app.activities.MainActivity;
import com.eleven.app.events.ConfirmEvent;
import com.eleven.app.models.Course;
import com.eleven.app.models.CourseManager;
import com.eleven.app.util.App;
import com.eleven.app.util.ClassInfo;
import com.eleven.app.util.ModelPrinter;
import com.eleven.app.util.WYUApi;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.List;
import java.util.logging.LogRecord;

/**
 * Created by eleven on 14-4-16.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private  static String TAG = LoginFragment.class.getSimpleName();

    protected EditText      mLoginnameText;
    protected EditText      mPassText;
    protected Button        mLoginBtn;
    protected CheckBox      mShowPassCheck;

    protected ProgressDialog    mProgressDialog;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mProgressDialog.dismiss();
            Toast.makeText(getActivity(),"同步课表成功", Toast.LENGTH_LONG).show();
            ((MainActivity)getActivity()).displayView(1);
        }
    };

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        String storeName    = App.getPreferences().getString("loginname", "");

        View rootView       = inflater.inflate(R.layout.fragment_login, container, false);
        mLoginnameText      = (EditText)    rootView.findViewById(R.id.loginnameText);
        mPassText           = (EditText)    rootView.findViewById(R.id.passText);
        mLoginBtn           = (Button)      rootView.findViewById(R.id.loginCheck);
        mShowPassCheck      = (CheckBox)    rootView.findViewById(R.id.showPassBtn);
        mLoginBtn.setOnClickListener(this);
        mShowPassCheck.setOnCheckedChangeListener(this);
        mLoginnameText.setText(storeName);
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
        App.getBus().register(this);
        ((MainActivity)getActivity()).setCurrentFragmentId(MainActivity.LOGIN_FRAGMENT);
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getBus().unregister(this);
    }

    @Override
    public void onClick(View view) {
        App.getImm().hideSoftInputFromWindow(mLoginnameText.getWindowToken(),0);
        new ConfirmDialog("重新登陆会清除以前的数据，是否继续？", ConfirmEvent.OK).show(getFragmentManager(), "confirm");
    }



    public void downloadTimetable(final String loginname, final String pass) {
        // 登录
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("正在同步课表...");
        mProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                WYUApi api = new WYUApi(loginname, pass);
                try {
                    api.login(getActivity());
                    List<ClassInfo> courses = api.getTimetable();
                    if (courses != null) {
                        CourseManager.deleteAll();
                        for (ClassInfo c : courses) {
                            if (c.classname.equals("")) continue;
                            //Log.v(TAG, "classname=" + c.classtime);
                            Course course = new Course(c);
                            CourseManager.addCourse(course);
                        }
                        mHandler.sendEmptyMessage(0);
                    } else {
                        new ConfirmDialog("账号或密码错误", ConfirmEvent.EMPTY).show(getFragmentManager(), "error");
                        mProgressDialog.dismiss();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            mPassText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            mPassText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    @Subscribe
    public void confirmLogin(ConfirmEvent event) {
        if (event.getMsg() != ConfirmEvent.OK) {
            return ;
        }
        Log.v(TAG , "confirm login");
        final String loginname    = mLoginnameText.getText().toString();
        final String pass         = mPassText.getText().toString();
        if (loginname.equals("")) {
            new ConfirmDialog("请输入账号", ConfirmEvent.EMPTY).show(getFragmentManager(), "input loginname");
            return;
        } else if (pass.equals("")) {
            new ConfirmDialog("请输入密码", ConfirmEvent.EMPTY).show(getFragmentManager(), "input pass");
            return;
        }
        SharedPreferences settings = App.getPreferences();
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("loginname", loginname);
        editor.commit();
        downloadTimetable(loginname, pass);

    }


}
