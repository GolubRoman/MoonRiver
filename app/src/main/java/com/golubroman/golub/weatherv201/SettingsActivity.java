package com.golubroman.golub.weatherv201;

/**
 * Created by User on 14.12.2016.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements  UnitsDialog.UnitsDialogListener{
    static String unitsNameText = "Metric";
    ListView listView;
    SettingsListAdapter settingsListAdapter;
    ArrayList<ElementSettingsListView> array = new ArrayList<>();
    Drawable upArrow;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.standartWhite), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
            ElementSettingsListView elementSettingsListView2 = new ElementSettingsListView("Units", "Metric");
            listView = (ListView) findViewById(R.id.settings_listview);
            array.add(elementSettingsListView2);
            settingsListAdapter = new SettingsListAdapter(SettingsActivity.this, array);
            listView.setAdapter(settingsListAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        UnitsDialog dialog = new UnitsDialog();
                        dialog.show(getFragmentManager(), "units");
                    }
                }
            });
    }

    @Override
    public void onUnitsDialogItemChecked(DialogInterface dialogInterface, int i) {
            ListView listView = ((AlertDialog)dialogInterface).getListView();
            unitsNameText = (String)listView.getItemAtPosition(i);
            settingsListAdapter.setSubtitle(0, unitsNameText);
            settingsListAdapter.notifyDataSetChanged();
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setItemChecked(i, true);
    }

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

