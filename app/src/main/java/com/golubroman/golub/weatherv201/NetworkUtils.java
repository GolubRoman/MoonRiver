package com.golubroman.golub.weatherv201;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by User on 08.12.2016.
 */

public class NetworkUtils {
    public static final String base = "http://api.openweathermap.org/data/2.5/forecast/daily";
    public static final String idParam = "q";
    public static final String unitsParam = "units";
    public static final String cntParam = "cnt";
    public static final String cntDays = "16";
    public static final String appIdParam = "APPID";
    public static final String appId = "28e909fe816297eafabdac9118bfa097";

    /* Building URL with parameters, such as:
        cityName, units, cntDays, appId
                */

    public static URL buildURL(String cityName, String units) {
        Uri uri = Uri.parse(base).buildUpon().
                appendQueryParameter(idParam, cityName). //?id=cityName
                appendQueryParameter(unitsParam, units). //&units=units
                appendQueryParameter(cntParam, cntDays). //&cnt=16
                appendQueryParameter(appIdParam, appId). //&appid=20cfc892f26ead5608f881f1cadd2784
                build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    /* Getting response from the server in a String format
                */


    public static String getResponseFromHttp(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = httpURLConnection.getInputStream();
            Scanner sc = new Scanner(in).useDelimiter("\\A");
            if (sc.hasNext()) {
                return sc.next();
            }
        } finally {
            httpURLConnection.disconnect();
        }
        return null;
    }


}
