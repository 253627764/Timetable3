package com.eleven.app.events;

/**
 * Created by skyhacker on 14-2-28.
 */
public class ConfirmEvent {

    public final static long OK             = 1L;
    public final static long CANCEL         = 1 << 1L;
    public final static long DELETE_COURSE  = 1 << 2;
    public final static long DELETE_ALL     = 1 << 3;
    public final static long EMPTY          = 1<<4;

    private long msg;

    public ConfirmEvent(long msg) {
        this.msg = msg;
    }

    public long getMsg() {
        return msg;
    }
}
