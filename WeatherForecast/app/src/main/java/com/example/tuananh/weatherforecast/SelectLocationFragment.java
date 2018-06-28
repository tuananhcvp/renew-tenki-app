package com.example.tuananh.weatherforecast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.tuananh.weatherforecast.databinding.FragmentSelectLocationBinding;
import com.example.tuananh.weatherforecast.utils.Utils;

import es.dmoral.toasty.Toasty;

/**
 * Created by anh on 2018/05/18.
 */

public class SelectLocationFragment extends Fragment {
    private ArrayAdapter<String> adapterCity = null;
    private ArrayAdapter<String> adapterJapanCity = null;

    FragmentSelectLocationBinding binding;

    /**
     * SelectLocationFragment initialize
     *
     * @return SelectLocationFragment
     */
    public static SelectLocationFragment newInstance() {
        Bundle args = new Bundle();
        SelectLocationFragment fragment = new SelectLocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarName(getResources().getString(R.string.title_select_location));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_location, container, false);

        binding = FragmentSelectLocationBinding.bind(view);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        adapterCity = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, SplashScreenActivity.cityArr);
        binding.autoComplexTextView.setAdapter(adapterCity);

        binding.autoComplexTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                adapterCity.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.autoComplexTextView.setOnItemClickListener((adapterView, view, i, l) -> Utils.hideSoftKeyboard(getContext(), view));

        adapterJapanCity = new ArrayAdapter<String>(getContext(), R.layout.simple_list_white_text, SplashScreenActivity.japanCityList);
        binding.lvJapanCity.setAdapter(adapterJapanCity);

        binding.lvJapanCity.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getActivity(), SelectedLocationWeatherActivity.class);
            intent.putExtra("SelectedAddress", SplashScreenActivity.japanCityList.get(position));
            startActivity(intent);
        });

        binding.btnSeeWeather.setOnClickListener(view -> {
            if (binding.autoComplexTextView.getText().toString().equals("")) {
                Toasty.info(getActivity(), getResources().getString(R.string.input_address), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), SelectedLocationWeatherActivity.class);
                intent.putExtra("SelectedAddress", binding.autoComplexTextView.getText().toString());
                startActivity(intent);
            }
        });

    }

}

