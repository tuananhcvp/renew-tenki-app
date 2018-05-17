package com.example.tuananh.weatherforecast.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tuananh.weatherforecast.R;

/**
 * Created by anh on 2018/05/11.
 */

public class HorizontalListViewDailyAdapter extends BaseAdapter {

    private Activity context;
    private String[] time;  //日中の３時間ごと　例えば：3AM, 6AM
    private String[] temp;  //上の時間に応じて温度
    private String[] icon;  //温度を表すアイコン

    public HorizontalListViewDailyAdapter(Activity context, String[] time, String[] temp, String[] icon) {
        this.context = context;
        this.time = time;
        this.temp = temp;
        this.icon = icon;
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_horizontal_daily_weather, null);
        }
        TextView txtDailyTime = (TextView)convertView.findViewById(R.id.txtDailyTime);
        TextView txtDailyTemp = (TextView)convertView.findViewById(R.id.txtDailyTemp);
        ImageView imgDailySky = (ImageView)convertView.findViewById(R.id.imgDailySky);

        txtDailyTime.setText(time[position]);
        txtDailyTemp.setText(temp[position]);
        Glide.with(context).load(context.getString(R.string.base_icon_url)+icon[position]+".png").into(imgDailySky);

        return convertView;
    }
}
