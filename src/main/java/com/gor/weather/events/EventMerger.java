package com.gor.weather.events;

import javafx.collections.ObservableList;
import org.omg.CORBA.DATA_CONVERSION;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.javafx.sources.CompositeObservable;
import rx.schedulers.Schedulers;

/**
 * Created by guser on 6/9/17.
 */
/**
 * Added to handle the ui generated events
 * */
public class EventMerger {
    private static EventMerger uiEvents = new EventMerger();
    private static CompositeObservable<DataEvent> composite = new CompositeObservable<>();

    public static void addEventStream(Observable<? extends DataEvent>observable) {
        final CompositeObservable<DataEvent> local = composite;
        synchronized (local) {
            local.addAll((Observable<DataEvent>) observable);
        }
    }

    public static Observable<DataEvent> getUiEvents() {
        return uiEvents.composite.toObservable().observeOn(Schedulers.io());
    }
}
