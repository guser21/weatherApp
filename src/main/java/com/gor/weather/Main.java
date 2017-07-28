package com.gor.weather;

import com.google.common.io.Resources;
import com.google.gson.*;
import com.gor.weather.controller.Controller;
import com.gor.weather.events.WeatherDataEvent;
import com.gor.weather.network.*;
import com.jfoenix.controls.JFXDecorator;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.AbstractHttpContentHolder;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.JavaFxObservable;
import rx.schedulers.Schedulers;

import javax.annotation.Resource;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    private static final Logger log = Logger.getLogger(Main.class);
    Controller controller;

    public static void main(String[] args) {

        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        controller = new Controller();
        URL mainFxmlURL = Resources.getResource("fxml/main.fxml");
        FXMLLoader loader = new FXMLLoader(mainFxmlURL);
        loader.setController(controller);
        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);

        ///adding material design to the uiA
        scene.getStylesheets().addAll(Resources.getResource("css/material.css").toExternalForm());

        primaryStage.setScene(scene);

        primaryStage.setResizable(false);

        primaryStage.show();

        log.info("Application Started");
    }


    @Override
    public void stop() throws Exception {
        super.stop();
        controller.onStop();
    }
}
