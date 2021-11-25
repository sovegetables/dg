package com.dougong.widget.view.smsButton;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * @author guanzhong
 */
public class SmsButtonView extends Button implements ISmsButtonView {
    private int mCountDownTime = 60;
    private CountDownTimer mCountDownTimer;
    private String mNormalText;

    public SmsButtonView(Context context) {
        super(context);
    }

    public SmsButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmsButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean isCountdown = false;

    public boolean isCountDown() {
        return isCountdown;
    }

    @Override
    public void startCountTime() {
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(mCountDownTime * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    isCountdown = true;
                    SmsButtonView.this.millisUntilFinished = millisUntilFinished;
                    setText(millisUntilFinished / 1000 + " s");
                    setClickable(false);
                    setEnabled(false);
                }

                @Override
                public void onFinish() {
                    isCountdown = false;
                    SmsButtonView.this.millisUntilFinished = 0;
                    setClickable(true);
                    setEnabled(true);
                    setText(mNormalText);
                }
            };
        }
        mNormalText = getText().toString();
        mCountDownTimer.start();
    }

    private long millisUntilFinished = 0;

    public long getMillisUntilFinished() {
        return millisUntilFinished;
    }

    public void startCountTime(long time) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isCountdown = true;
                SmsButtonView.this.millisUntilFinished = millisUntilFinished;
                setText(millisUntilFinished / 1000 + " s");
                setClickable(false);
                setEnabled(false);
            }

            @Override
            public void onFinish() {
                isCountdown = false;
                SmsButtonView.this.millisUntilFinished = 0;
                setClickable(true);
                setEnabled(true);
                setText(mNormalText);
            }
        };
        mNormalText = getText().toString();
        mCountDownTimer.start();
    }


    @Override
    public void stopCountTime() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer.onFinish();
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer.onFinish();
            mCountDownTimer = null;
        }
    }

    @Override
    public void setCountTime(int second) {
        mCountDownTime = second;
    }
}
