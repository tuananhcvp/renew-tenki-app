package com.example.tuananh.weatherforecast;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.tuananh.weatherforecast.databinding.FragmentCurrentLocationBinding;
import com.example.tuananh.weatherforecast.utils.Utils;
import com.example.tuananh.weatherforecast.utils.application.BaseActivity;
import com.example.tuananh.weatherforecast.utils.usecase.WeatherCurrentUseCase;
import com.example.tuananh.weatherforecast.viewmodel.CurrentForecastViewModel;

import javax.inject.Inject;

/**
 * Created by anh on 2018/05/18.
 */

public class SelectedLocationWeatherActivity extends BaseActivity {

    @Inject
    CurrentForecastViewModel viewModel;

    @Inject
    WeatherCurrentUseCase useCase;

    private FragmentCurrentLocationBinding binding;

    private String selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setActionbarTitle(getResources().getString(R.string.title_selected_city), this, getSupportActionBar());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getComponent().inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.fragment_current_location);
        binding.setCurrentModel(viewModel);

        changeSpecialCityName();
        viewModel.init(viewModel.TYPE_NAME, selectedLocation);
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

    // Change special city name value
    private void changeSpecialCityName() {
        selectedLocation = getIntent().getStringExtra("SelectedAddress");
        for (String city: SplashScreenActivity.japanCityList) {
            if (city.equalsIgnoreCase(selectedLocation) && !selectedLocation.equalsIgnoreCase("Osaka")
                    && !selectedLocation.equalsIgnoreCase("Tokyo") && !selectedLocation.equalsIgnoreCase("Kyoto")) {
                selectedLocation += "-ken";
            }
        }
    }
}
