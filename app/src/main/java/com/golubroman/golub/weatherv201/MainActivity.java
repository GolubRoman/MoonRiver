package com.golubroman.golub.weatherv201;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.golubroman.golub.weatherv201.Settings.SettingsActivity;
import com.golubroman.golub.weatherv201.WeatherList.ElementListView;
import com.golubroman.golub.weatherv201.WeatherList.ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LocationFragment.LocationFragmentListener{
    @BindView(R.id.status_icon) ImageView status_icon;
    @BindView(R.id.search_button) ImageView search_button;

    @BindView(R.id.search_field) EditText search_field;

    @BindView(R.id.settings) TextView settings;
    @BindView(R.id.status_day) TextView status_day;
    @BindView(R.id.status_humidity) TextView status_humidity;
    @BindView(R.id.status_pressure) TextView status_pressure;
    @BindView(R.id.status_wind) TextView status_wind;
    @BindView(R.id.status_humidity_text) TextView status_humidity_text;
    @BindView(R.id.status_pressure_text) TextView status_pressure_text;
    @BindView(R.id.status_wind_text) TextView status_wind_text;
    @BindView(R.id.status_lowtemp) TextView status_lowtemp;
    @BindView(R.id.status_hightemp) TextView status_hightemp;
    @BindView(R.id.status_status) TextView status_status;

    @BindView(R.id.settings_layout) FrameLayout settings_layout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progressbar) ProgressBar progressBar;
    @BindView(R.id.listview) ListView listView;

    private ArrayList<ElementListView> array;
    private String current_city  = "odessa";
    final String incorrectCity = "Incorrect city`s name! Try again.";
    final String typeface = "fonts/manteka.ttf";
    private android.app.FragmentManager fragmentManager;
    private android.app.FragmentTransaction fragmentTransaction;
    private LocationFragment locationFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /* Calling LocationFragment for getting users
            geolocation
            */
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        locationFragment = new LocationFragment();
        fragmentTransaction.add(R.id.activity_main, locationFragment);
        fragmentTransaction.commit();

        /* Setting the toolbar of the app
            (header) without title
                */

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /*  Opening Settings Activity after clicking
             the settings button
                */

        settings_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        /* Handling of clicking of search button
                */

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(search_field.getText().toString()) || search_field.getText().toString().trim().length() == 0) {
                    Toast.makeText(MainActivity.this, incorrectCity, Toast.LENGTH_SHORT).show();
                } else {
                    current_city = search_field.getText().toString();
                    new WeatherTask().execute();
                }
            }
        });

        changeFont();
    }

    /* Executing the query to the server for getting information
        with current_city parameter(LocationFragmentListener)
         */

    @Override
    public void getCity(String city) {
        this.current_city = city;
        new WeatherTask().execute();
    }

    /* AsynkTask class for getting query from the server
           */

    private class WeatherTask extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return getQuery();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            array = parser(s);
            listView.setAdapter(new ListAdapter(MainActivity.this, array));
            setStatusImage(0);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    setStatusImage(i);
                }
            });
            progressBar.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    String getQuery() {
        try {
            return NetworkUtils.getResponseFromHttp(NetworkUtils.buildURL(current_city, SettingsActivity.unitsSubtitle));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* Parsing of information, which was got from
            the server
                */

    ArrayList<ElementListView> parser(String s) {
        JSONObject forecast = null;
        JSONArray list = null;
        ArrayList<ElementListView> elementListViewArrayList = new ArrayList<>();
        String id, date, humidity, pressure, wind, mintemp, maxtemp, status;
        try {
            forecast = new JSONObject(s);
            list = forecast.getJSONArray("list");
            for (int i = 0; i < 16; i++) {
                JSONObject elementJSONArray = list.getJSONObject(i);
                date = elementJSONArray.getString("dt");
                long unixDate = Long.valueOf(date) * 1000L;
                date = new Date(Long.parseLong(String.valueOf(unixDate))).toString();
                date = date.substring(0, 10);
                humidity = elementJSONArray.getString("humidity") + " %";
                pressure = elementJSONArray.getString("pressure") + " hPa";
                wind = roundString(elementJSONArray.getString("speed"));
                JSONObject temp = elementJSONArray.getJSONObject("temp");
                mintemp = roundString(temp.getString("min"));
                maxtemp = roundString(temp.getString("max"));
                JSONArray weather = elementJSONArray.getJSONArray("weather");
                JSONObject weatherElement = weather.getJSONObject(0);
                id = weatherElement.getString("id");
                status = weatherElement.getString("main");
                if(SettingsActivity.unitsSubtitle.equals("Metric")){
                    mintemp += " \u00B0"+"C";
                    maxtemp += " \u00B0"+"C";
                    wind += " km/h SW";
                }
                else if(SettingsActivity.unitsSubtitle.equals("Imperial")){
                    mintemp += " \u00B0"+"F";
                    maxtemp += " \u00B0"+"F";
                    wind += " mph SW";
                }
                elementListViewArrayList.add(new ElementListView(id, date, humidity, pressure, wind, mintemp, maxtemp, status));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return elementListViewArrayList;
    }

    /* Setting the status image, which
        displays weather status for some day
                */

    void setStatusImage(int position) {
        ElementListView elementListView = array.get(position);
        status_day.setText(elementListView.getDate());
        status_humidity.setText(elementListView.getHumidity());
        status_pressure.setText(elementListView.getPressure());
        status_wind.setText(elementListView.getWind());
        status_hightemp.setText(elementListView.getMaxtemp());
        status_lowtemp.setText(elementListView.getMintemp());
        status_status.setText(elementListView.getStatus());
        int id = Integer.parseInt(elementListView.getId());
        if (id >= 200 && id <= 232) status_icon.setImageResource(R.drawable.thunder_1);
        else if ((id >= 300 && id <= 321) || (id >= 500 && id <= 532))
            status_icon.setImageResource(R.drawable.rain_1);
        else if (id >= 600 && id <= 622) status_icon.setImageResource(R.drawable.snow_1);
        else if (id >= 701 && id <= 781) status_icon.setImageResource(R.drawable.wind_1);
        else if (id >= 801 && id <= 804) status_icon.setImageResource(R.drawable.cloud_1);
        else if (id == 800) status_icon.setImageResource(R.drawable.clearsun_1);
    }

    String roundString(String s) {
        double d = Double.parseDouble(s);
        long l = Math.round(d);
        return Integer.toString((int)l);
    }

    /* Changing font of signs in activity
                */

    void changeFont(){
        Typeface manteka = Typeface.createFromAsset(this.getResources().getAssets(), typeface);
        settings.setTypeface(manteka);
        status_day.setTypeface(manteka);
        status_humidity.setTypeface(manteka);
        status_pressure.setTypeface(manteka);
        status_wind.setTypeface(manteka);
        status_humidity_text.setTypeface(manteka);
        status_pressure_text.setTypeface(manteka);
        status_wind_text.setTypeface(manteka);
        status_hightemp.setTypeface(manteka);
        status_lowtemp.setTypeface(manteka);
        status_status.setTypeface(manteka);
    }
}