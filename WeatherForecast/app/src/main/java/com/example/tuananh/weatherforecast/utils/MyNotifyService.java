package com.example.tuananh.weatherforecast.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.example.tuananh.weatherforecast.MainActivity;
import com.example.tuananh.weatherforecast.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyNotifyService extends Service {
    private String notifyTitle = "";
    private String notifyContent = "";
    private NumberFormat format = new DecimalFormat("#0.0");

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        double lat = SharedPreference.getInstance(this).getDouble("latitude", 0);
        double lon = SharedPreference.getInstance(this).getDouble("longitude", 0);

        if (isNetworkConnected(this)) {
            getDailyWeather(lat, lon);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void getDailyWeather(double lat, double lon) {
//        int posLanguage = SharedPreference.getInstance(this).getInt("Language", 0);
//        WeatherInfoAPI infoAPI = ApiClient.getClient().create(WeatherInfoAPI.class);
//        Call<OpenWeatherJSon> callCurrentLocation;
//        Call<OpenWeatherNextDaysJSon> callNextDayLocation;
//
//        if (posLanguage == 1) {
//            callCurrentLocation = infoAPI.loadCurrentWeatherByLocation(lat, lon, "ja", getString(R.string.appid_weather));
//            callNextDayLocation = infoAPI.loadNextDayWeatherByLocation(lat, lon, 7, "ja", getString(R.string.appid_weather));
//        } else {
//            callCurrentLocation = infoAPI.loadCurrentWeatherByLocation(lat, lon, getString(R.string.appid_weather));
//            callNextDayLocation = infoAPI.loadNextDayWeatherByLocation(lat, lon, 7, getString(R.string.appid_weather));
//        }
//
//        callNextDayLocation.enqueue(new Callback<OpenWeatherNextDaysJSon>() {
//            @Override
//            public void onResponse(Call<OpenWeatherNextDaysJSon> call, Response<OpenWeatherNextDaysJSon> response) {
//                OpenWeatherNextDaysJSon results = response.body();
//
//                String city = results.getCity().getName();
//                String minTemp = format.format(results.getList().get(0).getTemp().getMin() - 273.15) + "°";
//                String maxTemp = format.format(results.getList().get(0).getTemp().getMax() - 273.15) + "°";
//                String mornTemp = format.format(results.getList().get(0).getTemp().getMorn() - 273.15) + "°";
//                String eveTemp = format.format(results.getList().get(0).getTemp().getEve() - 273.15) + "°";
//                String nightTemp = format.format(results.getList().get(0).getTemp().getNight() - 273.15) + "°";
//
//                notifyContent = city + " - " + maxTemp + "/" + minTemp + "\n" + getResources().getString(R.string.txt_morning) + ":" + mornTemp
//                        + " " + getResources().getString(R.string.txt_evening) + ":" + eveTemp
//                        + " " + getResources().getString(R.string.txt_night) + ":" + nightTemp;
//
//            }
//
//            @Override
//            public void onFailure(Call<OpenWeatherNextDaysJSon> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//
//        callCurrentLocation.enqueue(new Callback<OpenWeatherJSon>() {
//            @Override
//            public void onResponse(Call<OpenWeatherJSon> call, Response<OpenWeatherJSon> response) {
//                OpenWeatherJSon result = response.body();
//                String curTemp = format.format(result.getMain().getTemp() - 273.15) + "°C";
//                String curState = result.getWeather().get(0).getDescription();
//                notifyTitle = curTemp + " - " + curState;
//                Log.d("Check notify", notifyTitle + "-" + notifyContent);
//
//                String urlIcon = getString(R.string.base_icon_url) + result.getWeather().get(0).getIcon() + ".png";
//                ImageLoader imageLoader = ImageLoader.getInstance();
//                imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
//                imageLoader.loadImage(urlIcon, new SimpleImageLoadingListener() {
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                        setNotification(loadedImage);
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Call<OpenWeatherJSon> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });

    }

    private void setNotification(Bitmap myBitmap) {
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this.getApplicationContext(), MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap resized = Bitmap.createScaledBitmap(myBitmap, (int)(myBitmap.getWidth() * 2), (int)(myBitmap.getHeight() * 2), true);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.cloud32px);
        builder.setLargeIcon(resized);
        builder.setContentTitle(notifyTitle);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(notifyContent));
        builder.setContentText(notifyContent);
        builder.setSound(alarmSound);
        builder.setAutoCancel(true);
        builder.setWhen(when);
        builder.setContentIntent(pendingIntent);
        builder.build();
        notificationManager.notify(0, builder.getNotification());
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}

