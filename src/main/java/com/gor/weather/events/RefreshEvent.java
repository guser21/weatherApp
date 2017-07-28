package com.gor.weather.events;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by guser on 6/9/17.
 */
public class RefreshEvent extends NetworkEvent {
    /**
     * Refresh request event
     * */
    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:MM:SS");
        return "RefreshEvent "+simpleDateFormat.format(new Date());
    }
}
