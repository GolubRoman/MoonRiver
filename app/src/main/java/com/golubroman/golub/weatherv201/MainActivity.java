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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationFragment.LocationFragmentListener{
    ImageView status_icon, search_button;
    EditText search_field;
    TextView settings, status_day, status_humidity, status_pressure,
            status_wind, status_humidity_text, status_pressure_text,
            status_wind_text, status_hightemp, status_lowtemp, status_status;
    FrameLayout settings_layout;
    ArrayList<ElementListView> array;
    String current_city;
    ProgressBar progressBar;
    ListView listView;
    Toolbar toolbar;
    android.app.FragmentManager fragmentManager;
    android.app.FragmentTransaction fragmentTransaction;
    LocationFragment locationFragment;
    boolean isVisited = false;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        if(!isVisited) {
            fragmentTransaction.add(R.id.activity_main, locationFragment);
            fragmentTransaction.commit();
            isVisited = true;
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        settings_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(search_field.getText().toString()) || search_field.getText().toString().trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Incorrect city`s name! Try again.", Toast.LENGTH_SHORT).show();
                } else {
                    current_city = search_field.getText().toString();
                    new WeatherTask().execute();
                }
            }
        });
        changeFont();
    }

    @Override
    public void getCity(String city) {
        this.current_city = city;
        new WeatherTask().execute();
    }

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
            listView = (ListView) findViewById(R.id.listview);
            array = parser(s, 16);
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
            return NetworkUtils.getResponseFromHttp(NetworkUtils.buildURL(current_city, SettingsActivity.unitsNameText));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    ArrayList<ElementListView> parser(String s, int cnt) {
        JSONObject forecast = null;
        JSONArray list = null;
        ArrayList<ElementListView> elementListViewArrayList = new ArrayList<>();
        String id, date, humidity, pressure, wind, mintemp, maxtemp, status;
        try {
            forecast = new JSONObject(s);
            list = forecast.getJSONArray("list");
            for (int i = 0; i < cnt; i++) {
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
                if(SettingsActivity.unitsNameText == "Metric"){
                    mintemp += " \u00B0"+"C";
                    maxtemp += " \u00B0"+"C";
                    wind += " km/h SW";
                }
                else{
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
    void initialization(){
        settings = (TextView) findViewById(R.id.settings);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        search_field = (EditText) findViewById(R.id.search_field);
        search_button = (ImageView) findViewById(R.id.search_button);
        status_icon = (ImageView) findViewById(R.id.status_icon);
        status_day = (TextView) findViewById(R.id.status_day);
        status_humidity = (TextView) findViewById(R.id.status_humidity);
        status_pressure = (TextView) findViewById(R.id.status_pressure);
        status_wind = (TextView) findViewById(R.id.status_wind);
        status_humidity_text = (TextView) findViewById(R.id.status_humidity_text);
        status_pressure_text = (TextView) findViewById(R.id.status_pressure_text);
        status_wind_text = (TextView) findViewById(R.id.status_wind_text);
        status_hightemp = (TextView) findViewById(R.id.status_hightemp);
        status_lowtemp = (TextView) findViewById(R.id.status_lowtemp);
        status_status = (TextView) findViewById(R.id.status_status);
        settings_layout = (FrameLayout) findViewById(R.id.settings_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        locationFragment = new LocationFragment();
        current_city = "odessa";
    }
    String roundString(String s) {
        double d = Double.parseDouble(s);
        long l = Math.round(d);
        return Integer.toString((int)l);
    }

    void changeFont(){
        Typeface manteka = Typeface.createFromAsset(this.getResources().getAssets(), "fonts/manteka.ttf");
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