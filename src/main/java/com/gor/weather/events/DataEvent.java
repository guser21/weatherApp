package com.gor.weather.events;

/**
 * Created by guser on 6/6/17.
 */
/**
 * Parent object for all events
 * Needed for the emitters to work
 * */
public class DataEvent {
    @Override
    public String toString() {
        return "DataEvent Fired";
    }
}
