package com.gor.weather.events;

import java.time.LocalDateTime;

/**
 * Created by guser on 6/8/17.
 */

/**
 * Exception like event to handle network errors
 * */
public class NetworkErrorEvent extends NetworkEvent {
    private final LocalDateTime timestamp;
    private final Throwable cause;

    public NetworkErrorEvent(Throwable cause) {
        this.timestamp = LocalDateTime.now();
        this.cause = cause;
    }


    @Override
    public String toString() {
        return "NetworkErrorEvent{" +
                "timestamp=" + timestamp +
                ", cause=" + cause +
                '}';
    }
}
