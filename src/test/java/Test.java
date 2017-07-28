import com.google.gson.*;
import com.gor.weather.events.GIOSDataEvent;
import com.gor.weather.events.WeatherDataEvent;
import com.gor.weather.network.OWeatherDataStream;
import com.gor.weather.network.OWeatherPojo;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.AbstractHttpContentHolder;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import org.jsoup.Jsoup;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by guser on 6/5/17.
 */
public class Test {
    @org.junit.Test

    public void test() throws Exception {
        class A{

        }
        class B extends A{

        }
        B b=new B();
        A a= (A) b;

    }
}
