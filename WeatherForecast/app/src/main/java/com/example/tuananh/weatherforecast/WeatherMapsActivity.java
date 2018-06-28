package com.example.tuananh.weatherforecast;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tuananh.weatherforecast.adapter.GoogleMapWeatherInfoAdapter;
import com.example.tuananh.weatherforecast.model.current.OpenWeatherJSon;
import com.example.tuananh.weatherforecast.utils.LocationService;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.example.tuananh.weatherforecast.utils.Utils;
import com.example.tuananh.weatherforecast.utils.application.BaseActivity;
import com.example.tuananh.weatherforecast.utils.usecase.BaseWeatherUseCase;
import com.example.tuananh.weatherforecast.utils.usecase.WeatherCurrentUseCase;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;

public class WeatherMapsActivity extends BaseActivity implements OnMapReadyCallback {
    @Inject
    WeatherCurrentUseCase useCase;

    private GoogleMap mMap;

    private Button btnReload;
    private RelativeLayout layoutWarning;
    private boolean hasGPS;
    private ProgressDialog dialog;

    protected static final int REQUEST_FIND_PLACE = 120;

    public ArrayList<Marker> listMarker = new ArrayList<>();
    public HashMap<Marker, OpenWeatherJSon> markerInfo = new HashMap<>();
    public HashMap<Marker, Bitmap> markerBitmap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_maps);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getComponent().inject(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        layoutWarning = (RelativeLayout) findViewById(R.id.layoutWarning);
        btnReload = (Button) findViewById(R.id.btnReload);
        dialog = new ProgressDialog(this);
        Utils.initProgressDialog(WeatherMapsActivity.this, dialog);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        btnReload.setOnClickListener(v -> {
            if (!Utils.isNetworkConnected(WeatherMapsActivity.this)) {
                layoutWarning.setVisibility(RelativeLayout.VISIBLE);
            } else {
                layoutWarning.setVisibility(RelativeLayout.GONE);
                init();
            }
        });

        if (!Utils.isNetworkConnected(this)) {
            Toasty.info(getApplicationContext(), getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        } else {
            layoutWarning.setVisibility(RelativeLayout.GONE);
            init();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.REQUEST_CHECK_SETTINGS) {
            SharedPreference.getInstance(this).putBoolean("isPermisionLocation",true);
            if (resultCode == RESULT_OK) {
                dialog.show();
                if (LocationService.mGoogleApiClient.isConnecting() || LocationService.mGoogleApiClient.isConnected()) {
                    LocationService.mGoogleApiClient.disconnect();
                }
                setUpMap();
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    Utils.initService(WeatherMapsActivity.this);
                    handler.postDelayed(() -> initCurrentWeather(), 1000); // After init service 1s, load weather
                }, 3000);  // Init service after 3000ms
            }
        } else if (requestCode == REQUEST_FIND_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude,place.getLatLng().longitude), 13));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Search Button onClick event
     */
    public void findPlace(View view) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
            startActivityForResult(intent, REQUEST_FIND_PLACE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
        if (!Utils.isNetworkConnected(this)) {
            layoutWarning.setVisibility(RelativeLayout.VISIBLE);
        } else {
            layoutWarning.setVisibility(RelativeLayout.GONE);
        }
    }

    private void init() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        hasGPS = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!hasGPS && SharedPreference.getInstance(this).getBoolean("isPermision",false)) {
            if (LocationService.mGoogleApiClient != null) {
                if (!LocationService.mGoogleApiClient.isConnecting() && !LocationService.mGoogleApiClient.isConnected()) {
                    LocationService.mGoogleApiClient.connect();
                }
            }
            Utils.settingRequest(this);
        } else {
            setUpMap();
            initCurrentWeather();
        }

    }

    /**
     * Setting map
     */
    private void setUpMap() {
        if (mMap == null) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMyLocationEnabled(true);
            mMap.setTrafficEnabled(true);
            mMap.setIndoorEnabled(true);
            mMap.setBuildingsEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            mMap.setOnMapClickListener(latLng -> {
                if (!Utils.isNetworkConnected(WeatherMapsActivity.this)) {
                    layoutWarning.setVisibility(RelativeLayout.VISIBLE);
                } else {
                    if (listMarker.size() > 4) {
                        listMarker.get(0).setVisible(false);
                        markerInfo.remove(listMarker.get(0));
                        markerBitmap.remove(listMarker.get(0));
                        listMarker.remove(0);
                    }

                    layoutWarning.setVisibility(RelativeLayout.GONE);
                    if (latLng != null) {
                        float zoom = mMap.getCameraPosition().zoom;
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(latLng.latitude + 0.008, latLng.longitude))      // Sets the center of the map to location user
                                .zoom(zoom)                                                         // Sets the zoom
                                .build();                                                           // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        loadCurrentWeatherByLocation(latLng.latitude, latLng.longitude);
                    }
                }
            });

            mMap.setOnInfoWindowClickListener(Marker::hideInfoWindow);

            mMap.setOnMarkerClickListener(marker -> {
                for (Marker mk : listMarker) {
                    if (mk.getId().equalsIgnoreCase(marker.getId())) {
                        GoogleMapWeatherInfoAdapter infoAdapter
                                = new GoogleMapWeatherInfoAdapter(markerInfo.get(mk), WeatherMapsActivity.this, markerBitmap.get(mk));
                        mMap.setInfoWindowAdapter(infoAdapter);
                        marker.showInfoWindow();
                    }
                }
                return true;
            });
        }
    }

    /**
     * Set first target is current location on Map
     */
    private void initCurrentWeather() {
        double lat = SplashScreenActivity.latitude;
        double lon = SplashScreenActivity.longitude;

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat + 0.008, lon))   // Sets the center of the map to location user
                .zoom(15)                               // Sets the zoom
                .build();                               // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        loadCurrentWeatherByLocation(lat, lon);
    }

    /**
     * Load current weather information by coordinate
     */
    private void loadCurrentWeatherByLocation(final double lat, final double lon) {
        WeatherCurrentUseCase.RequestParameter parameter = new WeatherCurrentUseCase.RequestParameter();
        parameter.type = WeatherCurrentUseCase.RequestParameter.TYPE_LOCATION;
        parameter.lat = lat;
        parameter.lon = lon;
        parameter.appId = getString(R.string.appid_weather);

        useCase.execute(parameter, new BaseWeatherUseCase.UseCaseCallback<OpenWeatherJSon>() {
            @Override
            public void onSuccess(OpenWeatherJSon response) {
                dialog.dismiss();

                String urlIcon = getString(R.string.base_icon_url) + response.weather.get(0).icon + ".png";
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
                imageLoader.loadImage(urlIcon, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        // Create InfoWindowAdapter object for map
                        GoogleMapWeatherInfoAdapter infoAdapter = new GoogleMapWeatherInfoAdapter(response, WeatherMapsActivity.this, loadedImage);
                        mMap.setInfoWindowAdapter(infoAdapter);
                        MarkerOptions option = new MarkerOptions();
                        option.position(new LatLng(lat, lon));
                        option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        Marker marker = mMap.addMarker(option);
                        listMarker.add(marker);
                        markerInfo.put(marker, response);
                        markerBitmap.put(marker, loadedImage);
                        marker.showInfoWindow();
                    }
                }) ;
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
