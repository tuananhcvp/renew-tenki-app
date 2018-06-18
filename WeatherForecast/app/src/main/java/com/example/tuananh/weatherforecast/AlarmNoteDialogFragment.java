package com.example.tuananh.weatherforecast;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.example.tuananh.weatherforecast.databinding.CustomAlarmDialogBinding;

public class AlarmNoteDialogFragment extends DialogFragment {

    private String content;
    private MediaPlayer mp;

    private CustomAlarmDialogBinding binding;

    public AlarmNoteDialogFragment(String content) {
        this.content = content;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /** Turn Screen On and Unlock the keypad when this alert dialog is displayed */
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_alarm_dialog, null);
        binding = CustomAlarmDialogBinding.bind(layout);

        binding.alarmContent.setText(content);

        Uri notify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(getContext(), notify);
        mp.start();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("");
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
            mp.stop();
            dialogInterface.dismiss();
        });

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.stop();
        getActivity().finish();
    }
}
