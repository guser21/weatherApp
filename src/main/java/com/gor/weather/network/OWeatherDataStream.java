package com.gor.weather.network;

import com.google.gson.Gson;
import com.gor.weather.events.DataEvent;
import com.gor.weather.events.WeatherDataEvent;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.AbstractHttpContentHolder;
import rx.Observable;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by guser on 6/6/17.
 */
public class OWeatherDataStream extends DataStream {

    private static final String oWurl = "http://api.openweathermap.org/data/2.5/weather?q=Warsaw&APPID=f4ada4d64d51b08caaa2954025482d6f";
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DataStream.class);

    /**
     * Fetches parses data from openWeatherApI
     * @return WeatherData event
     * */
    @Override
    public Observable<? extends DataEvent> makeRequest() {
        return RxNetty.createHttpGet(oWurl)
                .flatMap(AbstractHttpContentHolder::getContent)//if changed to map doesn't work. why ??
                .map(s -> s.toString(Charset.defaultCharset()))
                .map(s -> new Gson().fromJson(s, OWeatherPojo.class))
                .map(oWeatherPojo -> {
                    WeatherDataEvent weatherDataEvent = new WeatherDataEvent();
                    int temp=Double.valueOf(oWeatherPojo.getCurTemp()).intValue()-273;
                    int visibility=Double.valueOf(oWeatherPojo.getVisibility()).intValue()/1000;

                    //couldn't find a better way to populate. if any better way exits let me know
                    weatherDataEvent.setPressure(oWeatherPojo.getCurPressure());
                    weatherDataEvent.setHumidity(oWeatherPojo.getCurHumidity());
                    weatherDataEvent.setCurWeatherState(oWeatherPojo.getCurWeatherState());
                    weatherDataEvent.setTemp(String.valueOf(temp));
                    weatherDataEvent.setVisibility(String.valueOf(visibility));
                    weatherDataEvent.setWindSpeed(oWeatherPojo.getCurWindSpeed());
                    weatherDataEvent.setWindDir(String.valueOf(oWeatherPojo.getWind().getDeg()));

                    Date now = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

                    weatherDataEvent.setLastUpdate(sdf.format(now));
                    log.info("Request to Open Weather fetched and Parsed");
                    return weatherDataEvent;
                });
    }
}
