package com.example.tuananh.weatherforecast;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tuananh.weatherforecast.utils.application.BaseActivity;
import com.example.tuananh.weatherforecast.utils.LocationService;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.example.tuananh.weatherforecast.utils.Utils;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int NOTIFY_REQUEST_CODE = 1;

    private boolean hasGPS;
    private NavigationView navigationView;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialog = new ProgressDialog(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        initGPS();
        initView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_currentLocation) {
            navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.getMenu().getItem(5).getSubMenu().getItem(0).setChecked(false);

            callFragment(CurrentLocationFragment.newInstance());

        } else if (id == R.id.nav_selectLocation) {
            navigationView.getMenu().getItem(1).setChecked(true);
            navigationView.getMenu().getItem(5).getSubMenu().getItem(0).setChecked(false);

            callFragment(SelectLocationFragment.newInstance());

        } else if (id == R.id.nav_googleMap) {
            Intent mapIntent = new Intent(MainActivity.this, WeatherMapsActivity.class);
            startActivity(mapIntent);

        } else if (id == R.id.nav_note) {
            navigationView.getMenu().getItem(3).setChecked(true);
            navigationView.getMenu().getItem(5).getSubMenu().getItem(0).setChecked(false);

            callFragment(NoteFragment.newInstance());

        } else if (id == R.id.nav_setting) {
            navigationView.getMenu().getItem(4).setChecked(true);
            navigationView.getMenu().getItem(5).getSubMenu().getItem(0).setChecked(false);

            callFragment(SettingFragment.newInstance());

        } else if (id == R.id.nav_about) {
            navigationView.getMenu().getItem(5).getSubMenu().getItem(0).setChecked(true);
            navigationView.getMenu().getItem(0).setChecked(false);
            navigationView.getMenu().getItem(1).setChecked(false);
            navigationView.getMenu().getItem(2).setChecked(false);
            navigationView.getMenu().getItem(3).setChecked(false);
            navigationView.getMenu().getItem(4).setChecked(false);

//            callFragment(AboutAppFragment.newInstance());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initView() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        hasGPS = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!hasGPS && SharedPreference.getInstance(this).getBoolean("isPermision", false)) {
            if (LocationService.mGoogleApiClient != null) {
                if (!LocationService.mGoogleApiClient.isConnecting() && !LocationService.mGoogleApiClient.isConnected()) {
                    LocationService.mGoogleApiClient.connect();
                }
            }
            Utils.settingRequest(this);
        } else {
            callFragment(CurrentLocationFragment.newInstance());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.initService(this);
                    SharedPreference.getInstance(this).putBoolean("isPermision", true);
                } else {
                    SharedPreference.getInstance(this).putBoolean("isPermision", false);
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.REQUEST_CHECK_SETTINGS) {
            SharedPreference.getInstance(this).putBoolean("isPermisionLocation", true);
            if (resultCode == RESULT_OK) {
                Utils.initProgressDialog(MainActivity.this, dialog);
                dialog.show();
                if (LocationService.mGoogleApiClient.isConnecting() || LocationService.mGoogleApiClient.isConnected()) {
                    LocationService.mGoogleApiClient.disconnect();
                }
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    Utils.initService(MainActivity.this);
                    handler.postDelayed(() -> {
                        dialog.dismiss();
                        callFragment(CurrentLocationFragment.newInstance());
                    }, 1000);
                }, 3000);  //Init service after 3000ms
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void initGPS() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                Utils.initService(this);
            }
            // Do something for lollipop and above versions
        } else {
            SharedPreference.getInstance(this).putBoolean("isPermision", true);
            // do something for phones running an SDK before lollipop
            Utils.initService(this);
        }

//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Bundle extras = intent.getExtras();
//                final LatLng temp = new LatLng(extras.getDouble("Latitude"),extras.getDouble("Longitude"));
//                Log.d("inReceiver","Lat:" + temp.latitude + "--Lon:" + temp.longitude);
//            }
//        };
    }

    private void callFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fmContent, fragment);
        transaction.commit();
    }

    /**
     * Refresh navigation view when language change
     */
    public void refreshNavigationView() {
        this.setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setActionBarName(String title) {
        Utils.setActionbarTitle(title, this, getSupportActionBar());
    }

}
