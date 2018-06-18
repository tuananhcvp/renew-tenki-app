package com.example.tuananh.weatherforecast;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tuananh.weatherforecast.databinding.FragmentCreateNoteBinding;
import com.example.tuananh.weatherforecast.model.AlarmNote;
import com.example.tuananh.weatherforecast.model.Note;
import com.example.tuananh.weatherforecast.utils.MyDatabaseHelper;
import com.example.tuananh.weatherforecast.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class CreateNoteFragment extends Fragment {

    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private static final int MODE_ALARM_EDIT = 3;

    private Note note;
    private AlarmNote almNote;
    private int mode;
    private boolean isInitialize = false;

    private FragmentCreateNoteBinding binding;

    /**
     * CreateNoteFragment initialize
     *
     * @return CreateNoteFragment
     */
    public static CreateNoteFragment newInstance() {
        Bundle args = new Bundle();
        CreateNoteFragment fragment = new CreateNoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            // called here
            Log.e("CreateNoteFrag", "==> visible -- isInitialize = " + isInitialize);
            if (isInitialize) {
                if (note != null || almNote != null) {
                    note = null;
                    almNote = null;
                }
                binding.content.setText("");
                String time = binding.noteTime.getText().toString();
                if (time.startsWith(getResources().getString(R.string.create_at))
                        || time.startsWith(getResources().getString(R.string.modify_at))
                        || time.startsWith(getResources().getString(R.string.alarm_at))) {
                    binding.noteTime.setText(getCurrentTime());
                }
                mode = MODE_CREATE;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isInitialize = true;

        View view = inflater.inflate(R.layout.fragment_create_note, container, false);
        binding = FragmentCreateNoteBinding.bind(view);

        init();

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isInitialize = false;
        if (note != null) {
            note = null;
        }
    }

    private void init() {
        if (note == null) {
            mode = MODE_CREATE;
            binding.noteTime.setText(getCurrentTime());
            binding.content.setText("");
        }

        binding.save.setOnClickListener(v -> {
            Utils.hideSoftKeyboard(getContext(), getActivity().getCurrentFocus());
            MyDatabaseHelper db = new MyDatabaseHelper(getContext());
            String content = binding.content.getText().toString();

            if (content.equalsIgnoreCase("")) {
                return;
            }

            if (mode == MODE_CREATE) {
                String createTime = getCurrentTime();
                Note note1 = new Note(content, createTime, "");
                db.addNote(note1);
                Toasty.info(getContext(), getResources().getString(R.string.note_saved), Toast.LENGTH_SHORT).show();

            } else if (mode == MODE_EDIT) {
                String modifyTime = getCurrentTime();
                note.setContent(content);
                note.setModifyTime(modifyTime);
                db.updateNote(note);
                Toasty.info(getContext(), getResources().getString(R.string.note_saved), Toast.LENGTH_SHORT).show();

            } else if (mode == MODE_ALARM_EDIT) {
                almNote.setAlarmContent(content);
                db.updateAlarmNote(almNote);
                Toasty.info(getContext(), getResources().getString(R.string.note_saved), Toast.LENGTH_SHORT).show();
            }

            db.close();

        });

        binding.cancel.setOnClickListener(v -> {
            Utils.hideSoftKeyboard(getContext(), getActivity().getCurrentFocus());
            mode = MODE_CREATE;
            binding.content.setText("");
            binding.noteTime.setText(getCurrentTime());
        });
    }

    /**
     * Show note's content
     */
    public void showNoteEdit(Note nodeEdit) {
        if (nodeEdit != null) {
            note = nodeEdit;
            mode = MODE_EDIT;
            if (nodeEdit.getModifyTime().equalsIgnoreCase("")) {
                binding.noteTime.setText(getResources().getString(R.string.create_at) + ": " + nodeEdit.getCreateTime());
            } else {
                binding.noteTime.setText(getResources().getString(R.string.modify_at) + ": " + nodeEdit.getModifyTime());
            }
            binding.content.setText(nodeEdit.getContent());
        }
    }

    /**
     * Show alarm note's content
     */
    public void showNoteAlarm(AlarmNote alarmNote) {
        if (alarmNote != null) {
            almNote = alarmNote;
            mode = MODE_ALARM_EDIT;
            binding.noteTime.setText(getResources().getString(R.string.alarm_at) + ": " + alarmNote.getAlarmTime());
            binding.content.setText(alarmNote.getAlarmContent());
        }
    }

    //Get time in current
    private String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        return df.format(c.getTime());
    }
}
