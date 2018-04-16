package com.example.tuananh.weatherforecast.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuananh.weatherforecast.R;

import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * Created by anh on 2018/04/16.
 */

public class Utils {
    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Set actionbar title
     */
    public static void setActionbarTitle(String title, Activity context, ActionBar actionBar) {
        if (context == null || actionBar == null) return;
        TextView txtTitle = new TextView(context);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtTitle.setLayoutParams(layoutparams);
        txtTitle.setText(title);
        txtTitle.setTextColor(Color.WHITE);
        txtTitle.setGravity(Gravity.CENTER_VERTICAL);
        txtTitle.setTextSize(22);

        Typeface fontType = Typeface.createFromAsset(context.getAssets(), "fonts/Pacifico.ttf");
        txtTitle.setTypeface(fontType);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(txtTitle);
    }

    /**
     * Initialize progressdialog
     */
    public static void initProgressDialog(Activity context, ProgressDialog dialog) {
        dialog.setProgressStyle(android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setMessage(context.getString(R.string.dialog_data_loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
    }

    /**
     * Set language
     */
    public static void setLocaleLanguage(Context context, String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    /**
     * Hide soft keyboard
     */
    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     *
     */
    public static void showToastNotify(Context context, String content) {
        Toasty.info(context, content, Toast.LENGTH_SHORT, true).show();
    }

    /**
     * @param view TextView
     * @param maxLines 最大行数
     * @param where 省略する箇所
     */
    public static void setMultilineEllipsize(TextView view, int maxLines, TextUtils.TruncateAt where) {
        if (maxLines >= view.getLineCount()) {
            // ellipsizeする必要無し
            return;
        }
        float avail = 0.0f;
        for (int i = 0; i < maxLines; i++) {
            avail += view.getLayout().getLineMax(i);
        }
        CharSequence ellipsizedText = TextUtils.ellipsize(
                view.getText(), view.getPaint(), avail, where);
        view.setText(ellipsizedText);
    }
}
