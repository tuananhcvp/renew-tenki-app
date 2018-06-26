package com.example.tuananh.weatherforecast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tuananh.weatherforecast.adapter.ListAlarmNoteAdapter;
import com.example.tuananh.weatherforecast.databinding.FragmentListAlarmNoteBinding;
import com.example.tuananh.weatherforecast.model.AlarmNote;
import com.example.tuananh.weatherforecast.utils.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ListAlarmNoteFragment extends Fragment {

    private List<AlarmNote> alarmList = new ArrayList<AlarmNote>();
    private ListAlarmNoteAdapter alarmAdapter;
    private boolean isInitialize = false;
    OnListAlarmNoteListener mCallback;

    private FragmentListAlarmNoteBinding binding;

    /**
     * ListAlarmNoteFragment initialize
     *
     * @return ListAlarmNoteFragment
     */
    public static ListAlarmNoteFragment newInstance() {
        Bundle args = new Bundle();
        ListAlarmNoteFragment fragment = new ListAlarmNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            // called here
            if (isInitialize) {
                updateListNote();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isInitialize = true;

        View view = inflater.inflate(R.layout.fragment_list_alarm_note, container, false);
        binding = FragmentListAlarmNoteBinding.bind(view);

        init();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInitialize = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isInitialize = false;
    }

    private void init() {
        alarmList.clear();
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        alarmList.addAll(db.getListAlarmNote());

        alarmAdapter = new ListAlarmNoteAdapter(getActivity(), alarmList);
        binding.alarmNote.setAdapter(alarmAdapter);

        binding.alarmNote.setOnItemClickListener((parent, view, position, id) -> mCallback.sendAlarmNoteInfo(alarmList.get(position)));

        binding.alarmNote.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(getActivity())
                    .setMessage(getResources().getString(R.string.check_delete))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.txt_yes), (dialogInterface, i) -> {
                        AlarmNote note = alarmList.get(position);

                        MyDatabaseHelper db1 = new MyDatabaseHelper(getContext());
                        db1.deleteAlarmNote(note);
                        alarmList.remove(note);
                        alarmAdapter.notifyDataSetChanged();

                        Intent intent = new Intent("com.example.tuananh.weatherforecast.alertalarmnoteActivity");
                        PendingIntent operation = PendingIntent.getActivity(getActivity().getApplicationContext(), note.getPendingId(), intent, Intent.FLAG_ACTIVITY_NEW_TASK);
                        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                        alarmManager.cancel(operation);

                    })
                    .setNegativeButton(getResources().getString(R.string.txt_no), null)
                    .show();

            return true;
        });
    }

    private void updateListNote() {
        alarmList.clear();
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        alarmList.addAll(db.getListAlarmNote());
        alarmAdapter.notifyDataSetChanged();
    }

    public void setAlarmNoteCallbackListener(OnListAlarmNoteListener callback) {
        this.mCallback = callback;
    }

    public interface OnListAlarmNoteListener {
        void sendAlarmNoteInfo(AlarmNote note);
    }
}
