package com.example.tuananh.weatherforecast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.tuananh.weatherforecast.databinding.ActivityAlarmBinding;
import com.example.tuananh.weatherforecast.model.AlarmNote;
import com.example.tuananh.weatherforecast.model.Note;
import com.example.tuananh.weatherforecast.utils.MyDatabaseHelper;
import com.example.tuananh.weatherforecast.utils.SharedPreference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class AlarmActivity extends AppCompatActivity {

    private ActivityAlarmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm);

        final Note mNote = (Note) getIntent().getSerializableExtra("SetAlarmNote");

        binding.setAlarm.setOnClickListener(view -> {
            int year = binding.date.getYear();
            int month = binding.date.getMonth();
            int day = binding.date.getDayOfMonth();
            int hour = binding.time.getCurrentHour();
            int minute = binding.time.getCurrentMinute();

            MyDatabaseHelper db = new MyDatabaseHelper(AlarmActivity.this);
            Intent intent = new Intent("com.example.tuananh.weatherforecast.alertalarmnoteActivity");
            intent.putExtra("isSetNote", mNote);
            SharedPreference.getInstance(AlarmActivity.this).putString("isSetNote", new Gson().toJson(mNote));

            PendingIntent operation = PendingIntent.getActivity(getApplicationContext(), mNote.getId(), intent, Intent.FLAG_ACTIVITY_NEW_TASK);
            AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hour, minute);
            long alarmTime = calendar.getTimeInMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
            String isSetTime = sdf.format(calendar.getTime());

            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation);

            AlarmNote note = new AlarmNote(mNote.getContent(), isSetTime, mNote.getId());
            db.addAlarmNote(note);

            Toasty.info(getApplicationContext(), getResources().getString(R.string.alarm_success), Toast.LENGTH_SHORT).show();
            finish();
        });

        binding.cancelAlarm.setOnClickListener(view -> finish());

    }
}
