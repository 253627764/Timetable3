package com.eleven.app.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eleven.app.R;
import com.eleven.app.util.App;

import org.w3c.dom.Text;

import java.util.Calendar;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by eleven on 14-4-23.
 */
public class TimeCard extends Card {

    private Context mContext;
    private TextView mClock;
    private TextView mWeek;
    private TextView mZhou;

    private static String[] mZhouArr  = {
        "",
        "第一周", "第二周", "第三周", "第四周","第五周","第六周","第七周",
        "第八周", "第九周", "第十周", "第十一周", "第十二周", "第十三周", "第十四周",
        "第十五周","第十六周","第十七周","第十八周","第十九周","第二十周"
    };

    private static String[] mWeekArr;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Calendar calendar = Calendar.getInstance();
            int hh = calendar.get(Calendar.HOUR_OF_DAY);
            int mm = calendar.get(Calendar.MINUTE);
            int ss = calendar.get(Calendar.SECOND);
            mClock.setText(String.format("%02d:%02d:%02d", hh, mm, ss));

            int w = calendar.get(Calendar.WEEK_OF_MONTH);
            mWeekArr = mContext.getResources().getStringArray(R.array.week_array);
            mWeek.setText(mWeekArr[w-1]);

            SharedPreferences pref = App.getPreferences();
            int curZhou = pref.getInt(mContext.getString(R.string.pref_key_week), 1);
            mZhou.setText(mZhouArr[curZhou]);

        }
    };

    public TimeCard(Context context) {
        this(context, R.layout.card_time);
    }

    public TimeCard(Context context, int innerLayout) {
        super(context, innerLayout);
        mContext = context;

    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        mWeek   = (TextView) parent.findViewById(R.id.week);
        mZhou   = (TextView) parent.findViewById(R.id.zhou);
        TextView date  = (TextView) parent.findViewById(R.id.date);
        mClock          =  (TextView) parent.findViewById(R.id.clock);
        Calendar calendar = Calendar.getInstance();

        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH) + 1;
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        date.setText(String.format("%d.%02d.%02d", yy, mm, dd));

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    mHandler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
