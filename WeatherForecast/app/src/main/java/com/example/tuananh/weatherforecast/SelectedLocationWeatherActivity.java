package com.example.tuananh.weatherforecast;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.tuananh.weatherforecast.databinding.FragmentCurrentLocationBinding;
import com.example.tuananh.weatherforecast.utils.application.BaseActivity;
import com.example.tuananh.weatherforecast.viewmodel.CurrentForecastViewModel;

import javax.inject.Inject;

/**
 * Created by anh on 2018/05/18.
 */

public class SelectedLocationWeatherActivity extends BaseActivity {

    @Inject
    CurrentForecastViewModel viewModel;

    private FragmentCurrentLocationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getComponent().inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.fragment_current_location);
        binding.setCurrentModel(viewModel);
    }
}
