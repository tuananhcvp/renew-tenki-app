package com.example.tuananh.weatherforecast;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tuananh.weatherforecast.databinding.FragmentCurrentLocationBinding;
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

    private String currentAddress = "";
    private FragmentCurrentLocationBinding binding;

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

        // If coordinator have no value, initialize location service again
        if (SplashScreenActivity.latitude == 0 && SplashScreenActivity.longitude == 0) {
            if (LocationService.mGoogleApiClient.isConnecting() || LocationService.mGoogleApiClient.isConnected()) {
                LocationService.mGoogleApiClient.disconnect();
            }
            Utils.initService(getContext());
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

        viewModel.init(viewModel.TYPE_LOCATION, "");

        return binding.getRoot();
    }
}
