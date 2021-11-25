package com.dougong.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {

    private boolean isPagingEnabled = true;

    private static final String TAG = "CustomViewPager";

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isPagingEnabled){
            return false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        if(!isPagingEnabled){
            return false;
        }
        return super.canScrollHorizontally(direction);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if(!isPagingEnabled){
            return false;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(!isPagingEnabled){
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

}
