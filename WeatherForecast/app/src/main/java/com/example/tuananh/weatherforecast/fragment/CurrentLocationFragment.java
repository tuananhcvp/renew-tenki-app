package com.example.tuananh.weatherforecast.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tuananh.weatherforecast.MainActivity;
import com.example.tuananh.weatherforecast.R;
import com.example.tuananh.weatherforecast.SplashScreenActivity;
import com.example.tuananh.weatherforecast.databinding.FragmentCurrentLocationBinding;
import com.example.tuananh.weatherforecast.model.current.OpenWeatherJSon;
import com.example.tuananh.weatherforecast.utils.LocationService;
import com.example.tuananh.weatherforecast.utils.Utils;
import com.example.tuananh.weatherforecast.utils.application.BaseActivity;
import com.example.tuananh.weatherforecast.utils.usecase.WeatherCurrentUseCase;
import com.example.tuananh.weatherforecast.viewmodel.CurrentForecastViewModel;

import javax.inject.Inject;

/**
 * Created by anh on 2018/04/17.
 */

public class CurrentLocationFragment extends Fragment {
    @Inject
    CurrentForecastViewModel viewModel;
    @Inject
    WeatherCurrentUseCase useCase;

    private String curAddress = "";
    private FragmentCurrentLocationBinding binding;
    private String appId;

    /**
     * CurrentLocationFragment initialize
     *
     * @return CurrentLocationFragment
     */
    public static CurrentLocationFragment newInstance() {
        CurrentLocationFragment fragment = new CurrentLocationFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (SplashScreenActivity.latitude == 0 && SplashScreenActivity.longitude == 0) {
            if (LocationService.mGoogleApiClient.isConnecting() || LocationService.mGoogleApiClient.isConnected()) {
                Log.e("mGoogleApiClient","==> Disconnect");
                LocationService.mGoogleApiClient.disconnect();
            }
            initService();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarName(getString(R.string.title_current_location));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_current_location, container, false);
        ((BaseActivity) getActivity()).getComponent().inject(this);
        binding = FragmentCurrentLocationBinding.bind(layout);
        binding.setCurrentModel(viewModel);

        appId = getActivity().getResources().getString(R.string.appid_weather);
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (SplashScreenActivity.latitude == 0 && SplashScreenActivity.longitude == 0) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadWeather(SplashScreenActivity.latitude, SplashScreenActivity.longitude);
                }
            }, 1000);
        } else {
            loadWeather(SplashScreenActivity.latitude, SplashScreenActivity.longitude);
        }

        binding.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isNetworkConnected(getActivity())) {
                    showToastCheckInternet();
                } else {
                    if (!curAddress.equalsIgnoreCase("")) {
//                        Intent detailIntent = new Intent(getActivity(), ForecastDetailActivity.class);
//                        detailIntent.putExtra("CurrentLatitude", SplashScreenActivity.latitude);
//                        detailIntent.putExtra("CurrentLongitude", SplashScreenActivity.longitude);
//                        detailIntent.putExtra("CurrentAddressName", curLocation);
//                        startActivity(detailIntent);
                    } else {
                        Utils.showToastNotify(getContext(), getString(R.string.check_data_not_found));
                    }

                }
            }
        });

    }

    private void loadWeather(double lat, double lon) {
        WeatherCurrentUseCase.RequestParameter parameter = new WeatherCurrentUseCase.RequestParameter();
        parameter.type = WeatherCurrentUseCase.RequestParameter.TYPE_LOCATION;
        parameter.lat = lat;
        parameter.lon = lon;
        parameter.appId = appId;

        useCase.excute(parameter, new WeatherCurrentUseCase.UseCaseCallback() {
            @Override
            public void onSuccess(OpenWeatherJSon entity) {
                viewModel.setModel(entity);
                curAddress = entity.name;
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }
        });

    }


    public void initService() {
        Intent intent = new Intent(getActivity(), LocationService.class);
        getActivity().startService(intent);
    }

    private void showToastCheckInternet() {
        Utils.showToastNotify(getContext(), getString(R.string.check_internet));
    }
}
