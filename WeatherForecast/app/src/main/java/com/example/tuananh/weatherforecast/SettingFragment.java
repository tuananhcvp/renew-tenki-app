package com.example.tuananh.weatherforecast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.tuananh.weatherforecast.databinding.FragmentSettingBinding;
import com.example.tuananh.weatherforecast.utils.MyNotifyReceiver;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.example.tuananh.weatherforecast.utils.Utils;

import java.util.Calendar;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;

    /**
     * SettingFragment initialize
     *
     * @return SettingFragment
     */
    public static SettingFragment newInstance() {
        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarName(getResources().getString(R.string.title_setting));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_setting, container, false);
        binding = FragmentSettingBinding.bind(layout);

        init();

        return binding.getRoot();
    }

    private void init() {
        String[] arrLanguage = {getActivity().getString(R.string.txt_english), getActivity().getString(R.string.txt_japan)};
        ArrayAdapter<String> adapterLang = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, arrLanguage);
        adapterLang.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        binding.spinnerLanguage.setAdapter(adapterLang);

        binding.switchNotify.setChecked(SharedPreference.getInstance(getContext()).getBoolean("notifyState", false));

        binding.switchNotify.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SharedPreference.getInstance(getContext()).putBoolean("notifyState", true);
                setNotifyAlarmManager("On");

            } else {
                SharedPreference.getInstance(getContext()).putBoolean("notifyState", false);
                Intent receiverIntent = new Intent(getActivity(), MyNotifyReceiver.class);
                receiverIntent.putExtra("Notify State", "Off");

                Calendar calendar = Calendar.getInstance();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), MainActivity.NOTIFY_REQUEST_CODE, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            }
        });

        binding.spinnerLanguage.setOnItemSelectedListener(new MyLanguageEvent());

        int pos = SharedPreference.getInstance(getContext()).getInt("Language", 0);
        binding.spinnerLanguage.setSelection(pos);
    }

    private void setNotifyAlarmManager(String n) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Calendar now = Calendar.getInstance();
        if (now.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent receiverIntent = new Intent(getActivity(), MyNotifyReceiver.class);
        receiverIntent.putExtra("Notify State", n);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), MainActivity.NOTIFY_REQUEST_CODE, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 6, pendingIntent);
//        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    private void refreshFragment() {
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fmContent);
        if (currentFragment instanceof SettingFragment) {
            FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
            fragTransaction.detach(currentFragment);
            fragTransaction.attach(currentFragment);
            fragTransaction.commit();
        }
        ((MainActivity) getActivity()).refreshNavigationView();
        ((MainActivity) getActivity()).setActionBarName(getResources().getString(R.string.title_setting));
    }

    private class MyLanguageEvent implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                if (SharedPreference.getInstance(getContext()).getInt("Language", 0) != position) {
                    Utils.setLocaleLanguage(getContext(), "en");
                    SharedPreference.getInstance(getContext()).putInt("Language", position);
                    refreshFragment();
                }
            } else if (position == 1) {
                if (SharedPreference.getInstance(getContext()).getInt("Language", 0) != position) {
                    Utils.setLocaleLanguage(getContext(), "ja");
                    SharedPreference.getInstance(getContext()).putInt("Language", position);
                    refreshFragment();
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
