package com.example.tuananh.weatherforecast.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tuananh.weatherforecast.MainActivity;
import com.example.tuananh.weatherforecast.R;
import com.example.tuananh.weatherforecast.model.nextday.ListItem;
import com.example.tuananh.weatherforecast.model.nextday.OpenWeatherNextDaysJSon;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * Created by anh on 2018/04/16.
 */

public class Utils {
    public static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * Check network
     */
    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Set actionbar title
     */
    public static void setActionbarTitle(String title, Activity context, ActionBar actionBar) {
        if (context == null || actionBar == null) return;
        TextView txtTitle = new TextView(context);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtTitle.setLayoutParams(layoutparams);
        txtTitle.setText(title);
        txtTitle.setTextColor(Color.WHITE);
        txtTitle.setGravity(Gravity.CENTER_VERTICAL);
        txtTitle.setTextSize(22);

        Typeface fontType = Typeface.createFromAsset(context.getAssets(), "fonts/Pacifico.ttf");
        txtTitle.setTypeface(fontType);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(txtTitle);
    }

    /**
     * Initialize progressdialog
     */
    public static void initProgressDialog(Activity context, ProgressDialog dialog) {
        dialog.setProgressStyle(android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setMessage(context.getString(R.string.dialog_data_loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
    }

    /**
     * Set language
     */
    public static void setLocaleLanguage(Context context, String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    /**
     * Hide soft keyboard
     */
    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     *
     */
    public static void showToastNotify(Context context, String content) {
        Toasty.info(context, content, Toast.LENGTH_SHORT, true).show();
    }

    /**
     * @param view TextView
     * @param maxLines 最大行数
     * @param where 省略する箇所
     */
    public static void setMultilineEllipsize(TextView view, int maxLines, TextUtils.TruncateAt where) {
        if (maxLines >= view.getLineCount()) {
            // ellipsizeする必要無し
            return;
        }
        float avail = 0.0f;
        for (int i = 0; i < maxLines; i++) {
            avail += view.getLayout().getLineMax(i);
        }
        CharSequence ellipsizedText = TextUtils.ellipsize(
                view.getText(), view.getPaint(), avail, where);
        view.setText(ellipsizedText);
    }

    /**
     * Init location service
     */
    public static void initService(Context context) {
        Intent intent = new Intent(context, LocationService.class);
        context.startService(intent);
    }

    /**
     * Setting location request
     */
    public static void settingRequest(Activity context) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(LocationService.mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(context, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    /**
     *
     */
    public static void showDailyWeatherDialog(Activity context, OpenWeatherNextDaysJSon nextDaysJSon, String date, int i) {
        View v = context.getLayoutInflater().inflate(R.layout.next_days_weather_info, null);
        NumberFormat format = new DecimalFormat("#0.0");

        TextView tvDate = v.findViewById(R.id.tvDate);
        ImageView imgIconState = v.findViewById(R.id.imgIconState);
        TextView tvTemp = v.findViewById(R.id.tvTemp);
        TextView tvState = v.findViewById(R.id.tvState);
        TextView tvMaxMinTemp = v.findViewById(R.id.tvMaxMinTemp);
        TextView tvMorTemp = v.findViewById(R.id.tvMorTemp);
        TextView tvEveTemp = v.findViewById(R.id.tvEveTemp);
        TextView tvNightTemp = v.findViewById(R.id.tvNightTemp);
        TextView tvWind = v.findViewById(R.id.tvWind);
        TextView tvHum = v.findViewById(R.id.tvHum);
        TextView tvPress = v.findViewById(R.id.tvPress);

        ListItem item = nextDaysJSon.list.get(i + 1);
        String tempDay = format.format(item.temp.day - 273.15) + "°C";
        String state = item.weather.get(0).description;
        String tempMax = format.format(item.temp.max - 273.15) + "°C";
        String tempMin = format.format(item.temp.min - 273.15) + "°C";
        String tempMorn = format.format(item.temp.morning - 273.15) + "°C";
        String tempEve = format.format(item.temp.evening - 273.15) + "°C";
        String tempNight = format.format(item.temp.night - 273.15) + "°C";
        String wind = item.speed + "m/s";
        String press = item.pressure + "hpa";
        String hum = item.humidity + "%";
        String urlIcon = item.weather.get(0).icon;

        tvDate.setText(date);
        Glide.with(context).load(context.getString(R.string.base_icon_url) + urlIcon + ".png").into(imgIconState);
        tvTemp.setText(tempDay);
        tvState.setText(state);
        tvMaxMinTemp.setText(tempMax + "/" + tempMin);
        tvMorTemp.setText(context.getResources().getString(R.string.txt_morning) + ": " + tempMorn);
        tvEveTemp.setText(context.getResources().getString(R.string.txt_evening) + ": " + tempEve);
        tvNightTemp.setText(context.getResources().getString(R.string.txt_night) + ": " + tempNight);
        tvWind.setText(context.getResources().getString(R.string.txt_wind) + ": " + wind);
        tvHum.setText(context.getResources().getString(R.string.txt_humidity) + ": " + hum);
        tvPress.setText(context.getResources().getString(R.string.txt_pressure) + ": " + press);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("");
        builder.setView(v);
        builder.setCancelable(true);

        final AlertDialog dialog = builder.create();
        v.setOnClickListener(v1 -> dialog.dismiss());

//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        float dp = 320f;
//        float fpixels = metrics.density * dp;
//        int pixels = (int) (fpixels + 0.5f);

        dialog.show();
//        dialog.getWindow().setLayout(pixels, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}
