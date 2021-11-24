package com.sovegetables.floatingwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.tencent.mmkv.MMKV;

/**
 * Created by albert on 2018/5/15.
 */

public class FloatingWindow {

    private static final String TAG = "FloatingWindow";

    private View mView;
    private Activity mActivity;

    private Boolean mIsShown = false;
    private TextView mTvMoa;
    private View.OnClickListener mListener;
    private boolean mIsInTouch;
    private int mTouchSlop;
    private boolean mIsBeingDragged;
    private int mLastMotionY;
    private View mTouchView;

    private static final String KEY_FLOATING_Y = "floating.y";

    /**
     * 显示弹出框
     *
     * @param context
     */
    public void showPopupWindow(final Activity context) {
        if (mIsShown) {
            return;
        }
        this.mActivity = context;

        mView = setUpView(context);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mTouchSlop = ViewConfiguration.get(mActivity).getScaledTouchSlop();
        View decorView = context.getWindow().getDecorView();
        ((ViewGroup) decorView).addView(mView, params);

        final AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) mTouchView.getLayoutParams();
        layoutParams.x = decorView.getResources().getDisplayMetrics().widthPixels - decorView.getResources().getDimensionPixelSize(R.dimen.d_floating_w);
        final int halfY = decorView.getResources().getDisplayMetrics().heightPixels / 5 * 4;
        MMKV.defaultMMKV().putInt(KEY_FLOATING_Y, 0);
        layoutParams.y = halfY + MMKV.defaultMMKV().getInt(KEY_FLOATING_Y, 0);

        mIsShown = true;
        final int statusBarHeight = QMUIDisplayHelper.getStatusBarHeight(mActivity);
        final int navMenuHeight = QMUIDisplayHelper.getNavMenuHeight(mActivity);
        final int screenHeight = QMUIDisplayHelper.getScreenHeight(mActivity);
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean comsumed = false;
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mIsBeingDragged = false;
                        mLastMotionY = (int) event.getY();
                        final Rect rect = new Rect();
                        mTouchView.getGlobalVisibleRect(rect);
                        if(rect.contains((int)event.getRawX(), (int)event.getRawY())){
                            mIsInTouch = true;
                            comsumed = true;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        final int y = (int) event.getY();
                        int offsetDiff = y - mLastMotionY;
                        final int yDiff = Math.abs(offsetDiff);

                        if(mIsInTouch && yDiff > mTouchSlop && y >= statusBarHeight && y <= (screenHeight - navMenuHeight) ){
                            Log.d(TAG, "onTouch: " + y);
                            MMKV.defaultMMKV().putInt(KEY_FLOATING_Y, y - halfY);
                            layoutParams.y = y;
                            mTouchView.setLayoutParams(layoutParams);
                            mLastMotionY = y;
                            mIsBeingDragged = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(mIsInTouch && !mIsBeingDragged){
                            if(mListener != null){
                                mListener.onClick(mView);
                                comsumed = true;
                            }
                        }
                        mIsInTouch = false;
                        mIsBeingDragged = false;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mIsInTouch = false;
                        mIsBeingDragged = false;
                        break;
                }
                return comsumed;
            }
        });
    }

    public void setTextView(@StringRes int textRes){
        if(mTvMoa != null){
            mTvMoa.setText(textRes);
        }
    }

    public void setOnTouchViewClickListener(View.OnClickListener listener){
        this.mListener = listener;
    }

    private View setUpView(Context context) {
        final View view = LayoutInflater.from(context).inflate(R.layout.window_view, null);
        mTouchView = view.findViewById(R.id.view);
        mTvMoa = view.findViewById(R.id.tv_floating_moa);
        return view;
    }

    /**
     * 隐藏弹出框
     */
    public void hidePopupWindow() {
        if (mIsShown && null != mView) {
            View decorView = mActivity.getWindow().getDecorView();
            ((ViewGroup) decorView).removeView(mView);
            mIsShown = false;
        }
    }
}
