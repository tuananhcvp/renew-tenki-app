package com.example.tuananh.weatherforecast.view;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * DataBindingHelper
 * カスタムバインディングを定義する
 */
public class DataBindingHelper {
//    @BindingAdapter({"imageUrl", "error"})
//    public static void loadImage(ImageView view, String url, Drawable error) {
//        if (url == null || url.isEmpty()) {
//            view.setImageDrawable(error);
//            return;
//        }
//        Glide.with(view.getContext()).load(url).error(error).into(view);
//    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        if (url == null || url.isEmpty()) {
            return;
        }
        Glide.with(view.getContext()).load(url).into(view);
    }
}
