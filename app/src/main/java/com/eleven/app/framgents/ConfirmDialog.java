package com.eleven.app.framgents;



import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eleven.app.events.ConfirmEvent;
import com.eleven.app.util.App;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class ConfirmDialog extends DialogFragment {

    private String msg;
    private long callbackMsg;

    public ConfirmDialog(String msg, long callbackMsg) {
        this.msg = msg;
        this.callbackMsg = callbackMsg;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg)
               .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       App.getBus().post(new ConfirmEvent(callbackMsg));
                       dialog.dismiss();
                   }
               });
        if (callbackMsg != ConfirmEvent.EMPTY) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        return builder.create();
    }

    @Override
    public void onResume() {
        Log.v("ConfirmDialog", "onResume");
        super.onResume();
        App.getBus().register(this);
    }

    @Override
    public void onPause() {
        Log.v("ConfirmDialog", "onPause");
        super.onPause();
        App.getBus().unregister(this);
    }
}
