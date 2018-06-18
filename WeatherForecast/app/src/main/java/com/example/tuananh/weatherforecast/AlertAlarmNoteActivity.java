package com.example.tuananh.weatherforecast;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.tuananh.weatherforecast.model.AlarmNote;
import com.example.tuananh.weatherforecast.model.Note;
import com.example.tuananh.weatherforecast.utils.MyDatabaseHelper;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AlertAlarmNoteActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Note note = (Note) getIntent().getSerializableExtra("isSetNote");
        if (note == null) {
            String noteJson = SharedPreference.getInstance(AlertAlarmNoteActivity.this).getString("isSetNote", "");
            note = new Gson().fromJson(noteJson, new TypeToken<Note>() {
            }.getType());
            SharedPreference.getInstance(AlertAlarmNoteActivity.this).putString("isSetNote", "");
        }

        MyDatabaseHelper db = new MyDatabaseHelper(AlertAlarmNoteActivity.this);
        AlarmNote alarmNote = db.getAlarmNoteWithPendingId(note.getId());

        AlarmNoteDialogFragment alert = new AlarmNoteDialogFragment(alarmNote.getAlarmContent());
        alert.show(getSupportFragmentManager(), "AlertAlarmNote");

    }
}
