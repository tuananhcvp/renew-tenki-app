package com.example.tuananh.weatherforecast.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tuananh.weatherforecast.model.AlarmNote;
import com.example.tuananh.weatherforecast.model.Note;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Note_Manager";
    private static final String TABLE_NOTE = "Note";
    private static final String COLUMN_NOTE_ID = "Note_Id";
    private static final String COLUMN_NOTE_CONTENT = "Note_Content";
    private static final String COLUMN_NOTE_CREATETIME = "Note_CreateTime";
    private static final String COLUMN_NOTE_MODIFYTIME = "Note_ModifyTime";

    private static final String TABLE_ALARM = "Alarm";
    private static final String COLUMN_ALARM_ID = "AlarmNote_Id";
    private static final String COLUMN_ALARM_NOTECONTENT = "AlarmNote_Content";
    private static final String COLUMN_ALARM_TIME = "Alarm_Time";
    private static final String COLUMN_ALARM_PENDING_ID = "Alarm_PendingId";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableNote = "CREATE TABLE " + TABLE_NOTE + "("
                + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOTE_CONTENT + " TEXT,"
                + COLUMN_NOTE_CREATETIME + " TEXT,"
                + COLUMN_NOTE_MODIFYTIME + " TEXT" + ")";

        String createTableAlarm = "CREATE TABLE " + TABLE_ALARM + "("
                + COLUMN_ALARM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ALARM_NOTECONTENT + " TEXT,"
                + COLUMN_ALARM_TIME + " TEXT,"
                + COLUMN_ALARM_PENDING_ID + " INTEGER" + ")";
        db.execSQL(createTableNote);
        db.execSQL(createTableAlarm);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
        onCreate(db);
    }

    /**
     *
     */
    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (note.getContent() != null) {
            values.put(COLUMN_NOTE_CONTENT, note.getContent());
        }
        values.put(COLUMN_NOTE_CONTENT, note.getContent());
        values.put(COLUMN_NOTE_CREATETIME, note.getCreateTime());
        values.put(COLUMN_NOTE_MODIFYTIME, note.getModifyTime());

        db.insert(TABLE_NOTE, null, values);
        db.close();
    }

    /**
     *
     */
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NOTE_CONTENT, note.getContent());
        values.put(COLUMN_NOTE_MODIFYTIME, note.getModifyTime());

        return db.update(TABLE_NOTE, values, COLUMN_NOTE_ID + "=?", new String[]{String.valueOf(note.getId())});
    }

    /**
     *
     */
    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE, COLUMN_NOTE_ID + "=?", new String[]{String.valueOf(note.getId())});
        db.close();
    }

    /**
     *
     */
    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<Note>();
        String selectQuery = "SELECT * FROM " + TABLE_NOTE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setContent(cursor.getString(1));
                note.setCreateTime(cursor.getString(2));
                note.setModifyTime(cursor.getString(3));
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return noteList;
    }

    /**
     *
     */
    public List<Note> searchNote(String keyword) {
        List<Note> list = new ArrayList<Note>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT *"
                + " FROM " + TABLE_NOTE
                + " WHERE " + COLUMN_NOTE_CONTENT + " LIKE '%" + keyword.toLowerCase() + "%'"
                + " OR " + COLUMN_NOTE_CREATETIME + " LIKE '%" + keyword.toLowerCase() + "%'"
                + " OR " + COLUMN_NOTE_MODIFYTIME + " LIKE '%" + keyword.toLowerCase() + "%'" + " )";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Note note = new Note();
            note.setId(Integer.parseInt(c.getString(0)));
            note.setContent(c.getString(1));
            note.setCreateTime(c.getString(2));
            note.setModifyTime(c.getString(3));
            list.add(note);
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }

    /**
     *
     */
    public void addAlarmNote(AlarmNote note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ALARM_NOTECONTENT, note.getAlarmContent());
        values.put(COLUMN_ALARM_TIME, note.getAlarmTime());
        values.put(COLUMN_ALARM_PENDING_ID, note.getPendingId());

        db.insert(TABLE_ALARM, null, values);
        db.close();
    }

    /**
     *
     */
    public AlarmNote getAlarmNoteWithPendingId(int pendingId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ALARM, new String[]{COLUMN_ALARM_ID, COLUMN_ALARM_NOTECONTENT, COLUMN_ALARM_TIME, COLUMN_ALARM_PENDING_ID}, COLUMN_ALARM_PENDING_ID + "=?", new String[]{String.valueOf(pendingId)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        AlarmNote note = new AlarmNote(cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)));
        cursor.close();
        db.close();
        return note;
    }

    /**
     *
     */
    public List<AlarmNote> getListAlarmNote() {
        List<AlarmNote> alarmList = new ArrayList<AlarmNote>();
        String query = "SELECT * FROM " + TABLE_ALARM;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                AlarmNote note = new AlarmNote();
                note.setAlarmId(Integer.parseInt(cursor.getString(0)));
                note.setAlarmContent(cursor.getString(1));
                note.setAlarmTime(cursor.getString(2));
                note.setPendingId(Integer.parseInt(cursor.getString(3)));
                alarmList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return alarmList;
    }

    /**
     *
     */
    public int updateAlarmNote(AlarmNote note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ALARM_NOTECONTENT, note.getAlarmContent());

        return db.update(TABLE_ALARM, values, COLUMN_ALARM_ID + "=?", new String[]{String.valueOf(note.getAlarmId())});
    }


    /**
     *
     */
    public void deleteAlarmNote(AlarmNote note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARM, COLUMN_ALARM_ID + "=?", new String[]{String.valueOf(note.getAlarmId())});
        db.close();
    }

}

