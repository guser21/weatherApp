package com.gor.weather.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.gor.weather.events.GIOSDataEvent;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.AbstractHttpContentHolder;
import rx.Observable;

import java.nio.charset.Charset;
import java.util.logging.Logger;

/**
 * Created by guser on 6/6/17.
 */
public class GIOSDataStream extends DataStream {
    private final String url = "http://powietrze.gios.gov.pl/pjp/current/getAQIDetailsList?param=AQI";
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DataStream.class);

    /**
     * fetching and parsing data with JSoup
     * returns observable of type giosdata
     * */
    @Override
    public Observable<GIOSDataEvent> makeRequest() {

        return RxNetty.createHttpGet(url)
                .flatMap(AbstractHttpContentHolder::getContent)
                .map(content -> content.toString(Charset.defaultCharset()))
                .map(s -> new JsonParser().parse(s).getAsJsonArray())
                .map(jsonElements -> {
                    for (JsonElement j : jsonElements) {
                        if (j.getAsJsonObject().get("stationId").toString().equals("550")) {
                            return j;
                        }
                    }
                    return null;
                })
                .map(jsonElement -> jsonElement.getAsJsonObject().get("values"))
                .map(jsonElement -> {
                    log.info("Request to GIOS fetched and parsed");
                    String pol10pm=jsonElement.getAsJsonObject().get("PM10").toString();
                    String pol25pm=jsonElement.getAsJsonObject().get("PM2.5").toString();
                    return new GIOSDataEvent(pol10pm,pol25pm);

                });
    }
}
