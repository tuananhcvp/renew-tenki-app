package com.example.tuananh.weatherforecast;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.tuananh.weatherforecast.databinding.FragmentCurrentLocationBinding;
import com.example.tuananh.weatherforecast.model.current.OpenWeatherJSon;
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

        selectedLocation = getIntent().getStringExtra("SelectedAddress");
        for (String city: SplashScreenActivity.japanCityList) {
            if (city.equalsIgnoreCase(selectedLocation) && !selectedLocation.equalsIgnoreCase("Osaka")
                    && !selectedLocation.equalsIgnoreCase("Tokyo") && !selectedLocation.equalsIgnoreCase("Kyoto")) {
                selectedLocation += "-ken";
            }
        }

        loadCurrentWeatherByCityName(selectedLocation);

        binding.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isNetworkConnected(SelectedLocationWeatherActivity.this)) {
//                    showToastCheckInternet();
                } else {
                    if (!selectedLocation.equalsIgnoreCase("")) {
                        Intent detailIntent = new Intent(SelectedLocationWeatherActivity.this, ForecastDetailActivity.class);
                        detailIntent.putExtra("SelectedAddress", selectedLocation);
                        startActivity(detailIntent);
                    } else {
                        Utils.showToastNotify(SelectedLocationWeatherActivity.this, getString(R.string.check_data_not_found));
                    }

                }
            }
        });

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

    private void loadCurrentWeatherByCityName(String city) {
        String appId = getResources().getString(R.string.appid_weather);
        WeatherCurrentUseCase.RequestParameter parameter = new WeatherCurrentUseCase.RequestParameter();
        parameter.type = WeatherCurrentUseCase.RequestParameter.TYPE_NAME;
        parameter.appId = appId;
        parameter.cityName = city;

        useCase.execute(parameter, new WeatherCurrentUseCase.UseCaseCallback() {
            @Override
            public void onSuccess(OpenWeatherJSon entity) {
                viewModel.setModel(entity);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
