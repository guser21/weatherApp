package com.gor.weather.events;

/**
 * Created by guser on 6/6/17.
 */


/**
 * Data model for main data components
 * */
public class WeatherDataEvent extends DataEvent {
    String temp;
    String pressure;
    String windSpeed;
    String visibility;
    String humidity;
    String curWeatherState;
    String lastUpdate;
    String windDir;

    @Override
    public String toString() {
        return "WeatherDataEvent{" +
                "temp='" + temp + '\'' +
                ", pressure='" + pressure + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                ", visibility='" + visibility + '\'' +
                ", humidity='" + humidity + '\'' +
                ", curWeatherState='" + curWeatherState + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                '}';
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getWindSpeed() {return windSpeed;}

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getVisibility() {return visibility;}

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getHumidity() {return humidity;}

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getCurWeatherState() {
        return curWeatherState;
    }

    public void setCurWeatherState(String curWeatherState) {
        this.curWeatherState = curWeatherState;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
