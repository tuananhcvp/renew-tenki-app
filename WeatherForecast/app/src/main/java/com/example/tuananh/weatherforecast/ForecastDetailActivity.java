package com.example.tuananh.weatherforecast;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.tuananh.weatherforecast.databinding.ActivityForecastDetailBinding;
import com.example.tuananh.weatherforecast.utils.Utils;
import com.example.tuananh.weatherforecast.utils.application.BaseActivity;
import com.example.tuananh.weatherforecast.viewmodel.DetailForecastViewModel;

import javax.inject.Inject;

public class ForecastDetailActivity extends BaseActivity {
    private String selectedAddress;
    private String currentAddress;

    @Inject
    DetailForecastViewModel viewModel;

    ActivityForecastDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setActionbarTitle(getResources().getString(R.string.title_forecast_detail), this, getSupportActionBar());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getComponent().inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forecast_detail);
        binding.setViewModel(viewModel);

        currentAddress = getIntent().getStringExtra("CurrentAddressName");
        selectedAddress = getIntent().getStringExtra("SelectedAddress");

        if (currentAddress != null) {
            viewModel.init(this, binding, currentAddress, viewModel.TYPE_CURRENT_ADDRESS);
        } else if (selectedAddress != null) {
            viewModel.init(this, binding, selectedAddress, viewModel.TYPE_SELECTED_ADDRESS);
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
}
