package com.example.tuananh.weatherforecast.utils.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by anh on 2018/04/16.
 */

public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
        init(null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
//            TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
//            String fontName = arr.getString(R.styleable.CustomTextView_font);
//
//            try {
//                if (fontName != null) {
//                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontName);
//                    setTypeface(myTypeface);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            arr.recycle();
        }
    }
}
