package com.dougong.widget.view.banner;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

public interface BannerImageLoader {
    void loadImage(Context context, String url, ImageView view, @DrawableRes int defaultImage);
}