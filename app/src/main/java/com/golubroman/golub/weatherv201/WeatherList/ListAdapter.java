package com.golubroman.golub.weatherv201.WeatherList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.golubroman.golub.weatherv201.R;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by User on 18.01.2017.
 */

public class ListAdapter extends BaseAdapter{
    private View baseView;
    private Context context;
    private ArrayList<ElementListView> arrayList;
    private TextView date, status, mintemp, maxtemp;
    private ImageView image;
    private String typeface = "fonts/manteka.ttf";
    private static LayoutInflater inflater = null;

    public ListAdapter(Context context, ArrayList<ElementListView> arrayList){
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
        if(baseView == null) baseView = inflater.inflate(R.layout.activity_row, null);

        date = ButterKnife.findById(baseView, R.id.date_row);
        status = ButterKnife.findById(baseView, R.id.status_row);
        mintemp = ButterKnife.findById(baseView, R.id.mintemp_row);
        maxtemp = ButterKnife.findById(baseView, R.id.maxtemp_row);

        date.setText(arrayList.get(position).getDate());
        status.setText(arrayList.get(position).getStatus());
        mintemp.setText(arrayList.get(position).getMintemp());
        maxtemp.setText(arrayList.get(position).getMaxtemp());

        chooseIcon(Integer.parseInt(arrayList.get(position).getId()));
        changeFont();
        return baseView;
    }

    void chooseIcon(int id){
        image = (ImageView)baseView.findViewById(R.id.image_row);
        if(id >= 200 && id <= 232) image.setImageResource(R.drawable.thunder);
        else if((id >= 300 && id <= 321) || (id >= 500 && id <= 532)) image.setImageResource(R.drawable.rain);
        else if(id >= 600 && id <= 622) image.setImageResource(R.drawable.snow);
        else if(id >= 701 && id <= 781) image.setImageResource(R.drawable.wind);
        else if(id >= 801 && id <= 804) image.setImageResource(R.drawable.cloud);
        else if(id == 800) image.setImageResource(R.drawable.clearsun);
    }

    void changeFont(){
        Typeface manteka = Typeface.createFromAsset(baseView.getResources().getAssets(), typeface);
        TextView maxtemp = (TextView)baseView.findViewById(R.id.maxtemp_row);
        TextView mintemp = (TextView)baseView.findViewById(R.id.mintemp_row);
        TextView date = (TextView)baseView.findViewById(R.id.date_row);
        TextView status = (TextView)baseView.findViewById(R.id.status_row);
        maxtemp.setTypeface(manteka);
        mintemp.setTypeface(manteka);
        date.setTypeface(manteka);
        status.setTypeface(manteka);
    }
}
