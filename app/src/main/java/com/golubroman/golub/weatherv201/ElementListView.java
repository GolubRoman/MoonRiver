package com.golubroman.golub.weatherv201;

/**
 * Created by User on 18.01.2017.
 */
public class ElementListView {
    private String id, date, humidity, pressure, wind, mintemp, maxtemp, status;

    public ElementListView(String id, String date, String humidity, String pressure, String wind, String mintemp, String maxtemp, String status) {
        this.id = id;
        this.date = date;
        this.humidity = humidity;
        this.pressure = pressure;
        this.wind = wind;
        this.mintemp = mintemp;
        this.maxtemp = maxtemp;
        this.status = status;
    }
    public String getId() { return id; }

    public String getDate() {
        return date;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public String getWind() {
        return wind;
    }

    public String getMintemp() {
        return mintemp;
    }

    public String getMaxtemp() {
        return maxtemp;
    }

    public String getStatus() {
        return status;
    }
}
