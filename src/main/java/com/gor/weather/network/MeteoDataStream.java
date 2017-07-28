package com.gor.weather.network;

import com.gor.weather.events.DataEvent;
import com.gor.weather.events.WeatherDataEvent;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.AbstractHttpContentHolder;
import org.jsoup.Jsoup;
import rx.Observable;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by guser on 6/7/17.
 */
public class MeteoDataStream extends DataStream {
    private static final String mUrl = "http://www.meteo.waw.pl/";
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DataStream.class);

    /**
     * Fetches data from Meteo.waw.pl
     *
     * @return observable
     */
    @Override
    public Observable<? extends DataEvent> makeRequest() {
        return RxNetty.createHttpGet(mUrl)
                .flatMap(AbstractHttpContentHolder::getContent)
                .map(content -> content.toString(Charset.defaultCharset()))
                .map(Jsoup::parse)
                .map(document -> {
                    Date now = new Date();
                    WeatherDataEvent weatherDataEvent = new WeatherDataEvent();

                    String temp = document.select("#PARAM_TA").get(0).text();
                    weatherDataEvent.setTemp(temp.substring(0, temp.indexOf(',')));
                    weatherDataEvent.setPressure(document.select("#PARAM_PR").get(0).text());
                    weatherDataEvent.setHumidity(document.select("#PARAM_RH").get(0).text());
                    weatherDataEvent.setCurWeatherState("-");
                    weatherDataEvent.setVisibility("-");
                    weatherDataEvent.setWindSpeed(document.select("#PARAM_0_WV").get(0).text());
                    weatherDataEvent.setWindDir(document.select("#PARAM_WD").get(0).text().replace(',', '.'));

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    weatherDataEvent.setLastUpdate(sdf.format(now));
                    log.info("Request to Meteo.pl fetched and parsed");
                    return weatherDataEvent;
                });
    }
}
