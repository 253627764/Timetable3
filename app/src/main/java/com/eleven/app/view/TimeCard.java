package com.eleven.app.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eleven.app.R;

import org.w3c.dom.Text;

import java.util.Calendar;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by eleven on 14-4-23.
 */
public class TimeCard extends Card {

    private Context mContext;
    private TextView mClock;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Calendar calendar = Calendar.getInstance();
            int hh = calendar.get(Calendar.HOUR_OF_DAY);
            int mm = calendar.get(Calendar.MINUTE);
            int ss = calendar.get(Calendar.SECOND);
            mClock.setText(String.format("%02d:%02d:%02d", hh, mm, ss));
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
        TextView week   = (TextView) parent.findViewById(R.id.week);
        TextView zhou   = (TextView) parent.findViewById(R.id.zhou);
        TextView date  = (TextView) parent.findViewById(R.id.date);
        mClock          =  (TextView) parent.findViewById(R.id.clock);
        Calendar calendar = Calendar.getInstance();
        int w = calendar.get(Calendar.WEEK_OF_MONTH);
        String[] weekArr = mContext.getResources().getStringArray(R.array.week_array);
        week.setText(weekArr[w-1]);
        zhou.setText("第一周");

        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
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
