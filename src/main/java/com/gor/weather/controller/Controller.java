package com.gor.weather.controller;

import com.gor.weather.Main;
import com.gor.weather.events.*;
import com.gor.weather.network.DataStream;
import com.gor.weather.network.GIOSDataStream;
import com.gor.weather.network.MeteoDataStream;
import com.gor.weather.network.OWeatherDataStream;
import io.netty.handler.codec.string.LineSeparator;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.log4j.Logger;
import rx.Observable;
import rx.Subscription;
import rx.observables.JavaFxObservable;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Controller in mvc
 */
public class Controller {
    private static final Logger log = Logger.getLogger(Controller.class);

    @FXML
    private Label windSpeed;
    @FXML
    private Label lastUpdate;
    @FXML
    private Label dLevel10;
    @FXML
    private Label dLevel2;
    @FXML
    private Label visibility;
    @FXML
    private RadioButton radioOpenWeather;
    @FXML
    private RadioButton radioMeteo;
    @FXML
    private Label weatherState;
    @FXML
    private Label temp;
    @FXML
    private Label pressure;
    @FXML
    private Label humidity;
    @FXML
    private Label windDir;
    @FXML
    private Button refreshButton;

    private ToggleGroup radioToggleGroup;
    private Observable<DataEvent> mainDataStream;
    private Observable<DataEvent> polDataStream;
    private LinkedHashMap<Observable, Subscription> logSubscriptions = new LinkedHashMap<>();
    private LinkedHashMap<Observable, Subscription> uiUpdateSubscriptions = new LinkedHashMap<>();


    @FXML
    private void initialize() throws IOException {
        this.viewInitializer();

        polDataStream = new GIOSDataStream().startStream();
        mainDataStream = new OWeatherDataStream().startStream();

        uiUpdateSubscriptions.put(mainDataStream, mainDataStreamSetup());
        uiUpdateSubscriptions.put(polDataStream, polDataStreamSetup());
        setLogSubscriptions(polDataStream);
        setLogSubscriptions(mainDataStream);


    }

    /**
     * setting up logger events to be handled
     */
    private void setLogSubscriptions(Observable observable) {

        Subscription subscription = observable.
                ofType(NetworkEvent.class)
                .subscribe(dataEvent -> log.info(dataEvent.toString()));
        logSubscriptions.put(observable, subscription);
    }


    /**
     * Initializes view with "-"s
     */
    private void viewInitializer() throws IOException {
        ToggleGroup radioToggleGroup;
        ObservableList<Label> labelCollection;

        labelCollection = FXCollections.observableArrayList(weatherState, temp, pressure, humidity,
                windSpeed, lastUpdate, dLevel10, dLevel2, visibility, windDir);
        labelCollection.forEach(label -> label.setText("-"));

        radioToggleGroup = new ToggleGroup();
        radioMeteo.setToggleGroup(radioToggleGroup);
        radioOpenWeather.setToggleGroup(radioToggleGroup);
        radioOpenWeather.setSelected(true);


        radioToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            uiUpdateSubscriptions.get(mainDataStream).unsubscribe();
            logSubscriptions.get(mainDataStream).unsubscribe();
            if (radioToggleGroup.getSelectedToggle() == radioMeteo) {
                mainDataStream = new MeteoDataStream().startStream();
            } else {
                mainDataStream = new OWeatherDataStream().startStream();
            }
            uiUpdateSubscriptions.put(mainDataStream, mainDataStreamSetup());
            setLogSubscriptions(mainDataStream);
        });


        EventMerger.addEventStream(JavaFxObservable.actionEventsOf(refreshButton).map(e->new RefreshEvent()));
    }

    /**
     * subscribes to events from gios.gov.pl
     */
    private Subscription polDataStreamSetup() {
        return polDataStream.ofType(GIOSDataEvent.class)
                .subscribe(giosDataEvent -> Platform.runLater(() -> updatePolUI(giosDataEvent)));
    }

    /**
     * subscribes on main data streams from openWeather/meteo
     */
    private Subscription mainDataStreamSetup() {
        return mainDataStream.ofType(WeatherDataEvent.class)
                .subscribe(weatherDataEvent -> Platform.runLater(() -> updateMainUI(weatherDataEvent)));
    }

    /**
     * deallocatin resources before closing application
     */
    public void onStop() {
        uiUpdateSubscriptions.forEach((dataStream, subscription) -> subscription.unsubscribe());
        logSubscriptions.forEach((dataStream, subscription) -> subscription.unsubscribe());

    }

    /**
     * Updates the main components of UI
     */
    private void updateMainUI(WeatherDataEvent weatherDataEvent) {
        windSpeed.setText(weatherDataEvent.getWindSpeed() + " km/h");
        lastUpdate.setText(weatherDataEvent.getLastUpdate());
        visibility.setText(weatherDataEvent.getVisibility() + " km");
        temp.setText(weatherDataEvent.getTemp() + "°C");
        humidity.setText(weatherDataEvent.getHumidity() + " %");
        weatherState.setText(weatherDataEvent.getCurWeatherState());
        pressure.setText(weatherDataEvent.getPressure() + " mb");
        windDir.setText(weatherDataEvent.getWindDir() + "°");
    }

    private void updatePolUI(GIOSDataEvent giosDataEvent) {
        dLevel2.setText(giosDataEvent.getPol2PM() + " µg/m³");
        dLevel10.setText(giosDataEvent.getPol10PM() + " µg/m³");
    }

}
