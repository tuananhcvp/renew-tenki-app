package com.example.tuananh.weatherforecast.utils;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.tuananh.weatherforecast.SplashScreenActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by anh on 2018/04/16.
 */

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;

    Intent intent;
    public static final String BROADCAST_ACTION = "asia.ienter.matching.services";

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("LocationService.class", "onConnected");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            try {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    Log.d("LocationService.class", "Longitude: " + mLastLocation.getLongitude());
                    Log.d("LocationService.class", "Latitude: " + mLastLocation.getLatitude());
                    SplashScreenActivity.latitude = mLastLocation.getLatitude();
                    SplashScreenActivity.longitude = mLastLocation.getLongitude();
                    SharedPreference.getInstance(this).putDouble("latitude", SplashScreenActivity.latitude);
                    SharedPreference.getInstance(this).putDouble("longitude", SplashScreenActivity.longitude);
                    intent.putExtra("Latitude", mLastLocation.getLatitude());
                    intent.putExtra("Longitude", mLastLocation.getLongitude());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendBroadcast(intent);
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            stopSelf();
        }
        Log.d("LocationService.class", "connect GoogleApiClient Service ok");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("LocationService.class", "connect GoogleApiClient Service suspend");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("LocationService.class", "connect GoogleApiClient Service false");
    }

    // Handler that receives messages from the thread

    @Override
    public void onCreate() {

        if (intent == null) intent = new Intent(BROADCAST_ACTION);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
            Log.e("mGoogleApiClient", "==> Connect");
        }
        Log.d("LocationService.class", "onCreate Service");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d("LocationService.class", "service starting");
        if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
            Log.e("mGoogleApiClient", "==> Connect");
            mGoogleApiClient.connect();
        }

        // If we get killed, after return
        // ng from here, restart
        return START_NOT_STICKY;
    }


//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
//    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        Log.d("LocationService.class", "service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e("mGoogleApiClient", "==> Disconnect");
        mGoogleApiClient.disconnect();
        Log.d("LocationService.class", "service onDestroy");
    }

    @Override
    public void onLocationChanged(Location location) {

        if (mLastLocation == null) {
            mLastLocation = location;
            intent.putExtra("Latitude", mLastLocation.getLatitude());
            intent.putExtra("Longitude", mLastLocation.getLongitude());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sendBroadcast(intent);
        }
        if ((mLastLocation.getLatitude() != location.getLatitude() && (mLastLocation.getLongitude() != location.getLongitude()))) {
            mLastLocation = location;
            SplashScreenActivity.latitude = mLastLocation.getLatitude();
            SplashScreenActivity.longitude = mLastLocation.getLongitude();
        }

    }
}