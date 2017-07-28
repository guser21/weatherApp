package com.gor.weather.events;

import javax.print.DocFlavor;

/**
 * Created by guser on 6/6/17.
 */

/**
 * Data model for the pollution data feteched from gios.gov.pl
 * */
public class GIOSDataEvent extends DataEvent {
    private String pol10PM;
    private String pol2PM;


    public GIOSDataEvent(String pol10PM, String pol2PM) {
        this.pol10PM = pol10PM;
        this.pol2PM = pol2PM;
    }

    public String getPol10PM() {
        return pol10PM;
    }

    public String getPol2PM() {
        return pol2PM;
    }
}
