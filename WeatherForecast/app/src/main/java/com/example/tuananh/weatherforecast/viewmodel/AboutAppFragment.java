package com.example.tuananh.weatherforecast.viewmodel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tuananh.weatherforecast.MainActivity;
import com.example.tuananh.weatherforecast.R;
import com.example.tuananh.weatherforecast.databinding.FragmentAboutAppBinding;

public class AboutAppFragment extends Fragment {

    private FragmentAboutAppBinding binding;

    /**
     * AboutAppFragment initialize
     *
     * @return AboutAppFragment
     */
    public static AboutAppFragment newInstance() {
        Bundle args = new Bundle();
        AboutAppFragment fragment = new AboutAppFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarName(getResources().getString(R.string.title_about));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_about_app, container, false);

        binding = FragmentAboutAppBinding.bind(layout);

        binding.appName.setText(getResources().getString(R.string.txt_app_name)
                + ": " + getResources().getString(R.string.app_name));
        binding.version.setText(getResources().getString(R.string.txt_version));

        return binding.getRoot();
    }
}
