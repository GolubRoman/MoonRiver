package com.golubroman.golub.weatherv201.Settings;

/**
 * Created by User on 14.12.2016.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.golubroman.golub.weatherv201.MainActivity;
import com.golubroman.golub.weatherv201.R;
import com.golubroman.golub.weatherv201.WeatherList.UnitsDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements UnitsDialog.UnitsDialogListener{

    public static String unitsSubtitle = "Metric";
    final String units = "units";

    private List<ElementSettingsListView> array
                = Arrays.asList(new ElementSettingsListView("Units", "Metric"));
    private SettingsListAdapter settingsListAdapter;

    @BindView(R.id.settings_listview) ListView listView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindDrawable(R.drawable.abc_ic_ab_back_material) Drawable upArrow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        upArrow.setColorFilter((Color.WHITE), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        settingsListAdapter = new SettingsListAdapter(SettingsActivity.this, array);
        listView.setAdapter(settingsListAdapter);

        /* Handling of clicking on the ListView element
                in the settings
            */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    UnitsDialog dialog = new UnitsDialog();
                    dialog.show(getFragmentManager(), units);
                }
            }
        });
    }

    /* Realization of Interface UnitsDialogListener method
        */
    @Override
    public void onUnitsDialogItemChecked(DialogInterface dialogInterface, int i) {
            ListView listView = ((AlertDialog)dialogInterface).getListView();
            unitsSubtitle = (String)listView.getItemAtPosition(i);
            settingsListAdapter.setSubtitle(0, unitsSubtitle);
            settingsListAdapter.notifyDataSetChanged();
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setItemChecked(i, true);
    }

    /* Clicking on the back arrow to return home
        */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

