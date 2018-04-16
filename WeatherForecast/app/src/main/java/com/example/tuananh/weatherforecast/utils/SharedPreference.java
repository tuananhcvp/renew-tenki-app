package com.example.tuananh.weatherforecast.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by anh on 2018/04/16.
 */

public class SharedPreference {
    private final String SETTING_NAME = "ienter.co.jp.tenki";
    private static SharedPreference _Instance;// = new SharedPreference();
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private boolean mAppEdit = false;

    /**
     * Init SharedPreference instance
     */
    public synchronized static SharedPreference getInstance(Context context) {
        if (_Instance == null)
            _Instance = new SharedPreference(context);
        return _Instance;
    }

    private SharedPreference(Context context) {
        mPref = context.getSharedPreferences(SETTING_NAME, Context.MODE_PRIVATE);
    }

    public void edit() {
        mAppEdit = true;
        mEditor = mPref.edit();
    }

    /**
     *
     */
    public void commit() {
        mAppEdit = false;
        mEditor.commit();
        mEditor = null;
    }

    /**
     *
     */
    public void clear() {
        doEdit();
        mEditor.clear();
        doCommit();
    }

    /**
     *
     */
    private void doEdit() {
        if (!mAppEdit && mEditor == null) {
            mEditor = mPref.edit();
        }
    }

    /**
     *
     */
    private void doCommit() {
        if (!mAppEdit && mEditor != null) {
            mEditor.commit();
            mEditor = null;
        }
    }

    /**
     *  Put boolean value
     */
    public void putBoolean(String key, boolean value) {
        doEdit();
        mEditor.putBoolean(key, value);
        doCommit();
    }

    /**
     *  Get boolean value
     */
    public Boolean getBoolean(String key, boolean defaultValue) {
        if (mPref != null) {
            return mPref.getBoolean(key, defaultValue);
        } else {
            return defaultValue;
        }
    }

    /**
     *  Put int value
     */
    public void putInt(String key, int value) {
        doEdit();
        mEditor.putInt(key, value);
        doCommit();
    }

    /**
     *  Get int value
     */
    public int getInt(String key, int defaultValue) {
        if (mPref != null) {
            return mPref.getInt(key, defaultValue);
        } else {
            return defaultValue;
        }
    }

    /**
     *  Put String value
     */
    public void putString(String key, String value) {
        doEdit();
        mEditor.putString(key, value);
        doCommit();
    }

    /**
     *  Get String value
     */
    public String getString(String key, String defaultValue) {
        if (mPref != null) {
            return mPref.getString(key, defaultValue);
        } else {
            return defaultValue;
        }
    }

    /**
     *  Put float value
     */
    public void putFloat(String key, float value) {
        doEdit();
        mEditor.putFloat(key, value);
        doCommit();
    }

    /**
     *  Get float value
     */
    public float getFloat(String key, float defaultValue) {
        if (mPref != null) {
            return mPref.getFloat(key, defaultValue);
        } else {
            return defaultValue;
        }
    }

    /**
     *  Put double value
     */
    public void putDouble(String key, double value) {
        doEdit();
        mEditor.putLong(key, java.lang.Double.doubleToRawLongBits(value));
        doCommit();
    }

    /**
     *  Get double value
     */
    public double getDouble(String key, double defaultValue) {
        if (mPref != null) {
            return java.lang.Double.longBitsToDouble(mPref.getLong(key, java.lang.Double.doubleToRawLongBits(defaultValue)));
        } else {
            return defaultValue;
        }
    }
}
