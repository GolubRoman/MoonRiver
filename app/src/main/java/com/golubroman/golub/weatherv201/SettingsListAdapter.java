package com.golubroman.golub.weatherv201;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by User on 27.01.2017.
 */

public class SettingsListAdapter extends BaseAdapter{
    View baseView;
    Context context;
    ArrayList<ElementSettingsListView> arrayList;
    String title, subtitle;
    TextView textTitle, textSubtitle;
    private static LayoutInflater inflater = null;
    public SettingsListAdapter(Context context, ArrayList<ElementSettingsListView> arrayList){
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
        textSubtitle = (TextView)baseView.findViewById(R.id.subtitle_row);
        textTitle = (TextView)baseView.findViewById(R.id.title_row);
        textSubtitle.setText(arrayList.get(position).getSubtitle());
        textTitle.setText(arrayList.get(position).getTitle());
        changeFont();
        return baseView;
    }
    public void setSubtitle(int position, String s){
        arrayList.get(position).setSubtitle(s);

    }
    void changeFont(){
        Typeface manteka = Typeface.createFromAsset(baseView.getResources().getAssets(), "fonts/manteka.ttf");
        TextView textTitle = (TextView)baseView.findViewById(R.id.title_row);
        TextView textSubtitle = (TextView)baseView.findViewById(R.id.subtitle_row);
        textTitle.setTypeface(manteka);
        textSubtitle.setTypeface(manteka);
    }
}
