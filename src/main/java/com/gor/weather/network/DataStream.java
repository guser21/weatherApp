package com.gor.weather.network;

import com.gor.weather.events.*;
import org.apache.log4j.Logger;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;


/**
 * Created by guser on 6/5/17.
 */
public abstract class DataStream {
    private static final Logger log = Logger.getLogger(DataStream.class);
    private static final int pollingInterval = 600;
    private static final int initialDelay=1;
    public abstract Observable<? extends DataEvent> makeRequest();

    /**
     * starts the stream for a given observable
     * published in order to avoid multiple evaluation
     * */
    public Observable<DataEvent> startStream() {
        return Observable.
                interval(initialDelay,pollingInterval, TimeUnit.SECONDS, Schedulers.io())
                .compose(this::chainTheRequest)
                .mergeWith(EventMerger.getUiEvents().compose(this::chainTheRequest))
                .publish().refCount();
    }

    /**
     * adds two more event before and after actual data fetching
     *
     * @return  observable stream
     * */
    public Observable<DataEvent> chainTheRequest(Observable<?> observable) {
        return observable.flatMap(i ->
                Observable.concat(
                    Observable.just(new RequestFormedEvent()),
                    makeRequest()
                            .timeout(30, TimeUnit.SECONDS)
                            .doOnError(log::error)
                            .cast(DataEvent.class)
                            .doOnError(NetworkErrorEvent::new),///better to change side effects
                    Observable.just(new RequestFinishedEvent())
        ));
    }
}
