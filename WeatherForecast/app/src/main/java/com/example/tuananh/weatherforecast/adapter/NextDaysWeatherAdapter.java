package com.example.tuananh.weatherforecast.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tuananh.weatherforecast.R;
import com.example.tuananh.weatherforecast.databinding.ItemNextDaysWeatherBinding;
import com.example.tuananh.weatherforecast.model.NextDaysItem;
import com.example.tuananh.weatherforecast.viewmodel.NextDaysWeatherItemViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by anh on 2018/05/16.
 */

public class NextDaysWeatherAdapter extends RecyclerView.Adapter<NextDaysWeatherAdapter.ViewHolder> {

    private List<NextDaysItem> itemList = new ArrayList<>();

    private ShowDailyWeatherCallBack listener;

    @Inject
    public NextDaysWeatherAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_next_days_weather, parent, false);

        ItemNextDaysWeatherBinding binding = ItemNextDaysWeatherBinding.bind(view);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        NextDaysItem model = itemList.get(position);
        NextDaysWeatherItemViewModel viewModel = new NextDaysWeatherItemViewModel();

        viewModel.setItem(model, position);

        viewModel.setCallback(position1 -> {
            if (listener != null) {
                listener.onShowDailyWeatherInfo(position1);
            }
        });

        holder.bind(viewModel);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addAll(List<NextDaysItem> items) {
        itemList.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        itemList.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemNextDaysWeatherBinding binding;

        public ViewHolder(ItemNextDaysWeatherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(NextDaysWeatherItemViewModel viewModel) {
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
        }
    }

    public void setCallback(ShowDailyWeatherCallBack listener) {
        this.listener = listener;
    }

    public interface ShowDailyWeatherCallBack {
        void onShowDailyWeatherInfo(int position);
    }
}
