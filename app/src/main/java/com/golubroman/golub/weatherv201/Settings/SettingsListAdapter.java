package com.golubroman.golub.weatherv201.Settings;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.golubroman.golub.weatherv201.R;
import com.golubroman.golub.weatherv201.Settings.ElementSettingsListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by User on 27.01.2017.
 */

public class SettingsListAdapter extends BaseAdapter{
    private View baseView;
    private Context context;
    private List<ElementSettingsListView> arrayList;
    private TextView textTitle, textSubtitle;
    private static LayoutInflater inflater = null;
    final String typeface = "fonts/manteka.ttf";

    public SettingsListAdapter(Context context, List<ElementSettingsListView> arrayList){
        this.context = context;
        this.arrayList = arrayList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        baseView = view;
        if(baseView == null) baseView = inflater.inflate(R.layout.activity_settings_row, null);
        textSubtitle = ButterKnife.findById(baseView, R.id.subtitle_row);
        textTitle = ButterKnife.findById(baseView, R.id.title_row);
        textSubtitle.setText(arrayList.get(position).getSubtitle());
        textTitle.setText(arrayList.get(position).getTitle());
        changeFont();
        return baseView;
    }

    public void setSubtitle(int position, String s){
        arrayList.get(position).setSubtitle(s);
    }

    /* Method for changing font of signs
        */

    void changeFont(){
        Typeface manteka = Typeface.createFromAsset(baseView.getResources().getAssets(), typeface);
        TextView textTitle = (TextView)baseView.findViewById(R.id.title_row);
        TextView textSubtitle = (TextView)baseView.findViewById(R.id.subtitle_row);
        textTitle.setTypeface(manteka);
        textSubtitle.setTypeface(manteka);
    }
}
