package com.gor.weather.events;

/**
 * Created by guser on 6/6/17.
 */
/**
 * Emitted after request fetched and parsed
 * */
public class RequestFinishedEvent extends NetworkEvent{
    @Override
    public String toString() {
        return "Network request finished";
    }
}
