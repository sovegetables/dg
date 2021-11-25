package com.dougong.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MarqueeTextView2 extends View {

    private static final String TAG = "MarqueeTextView";
    public static final int DEFAULT_SPEED = 15;

    private int currentScrollPos = 0;
    private int speed = DEFAULT_SPEED;
    private int textWidth = -1;
    private int textHeight = -1;
    private volatile boolean isMeasured = false;
    private volatile boolean isStop = true;
    private String text;
    private final Paint paint = new Paint();
    private final Rect rect = new Rect();

    private int dx = 0;
    private int textSize = 24;
    private int strokeWidth = 3;
    private float drawTextDy = -3;
    private int textColor = Color.parseColor("#FF00C8FF");

    final Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if (textWidth == -1) {
                isStop = true;
                postInvalidate();
                return;
            }
//            Log.d(TAG, "run - 0000: " + dx + " textWidth:" + textWidth);
            if (isStop || Math.abs(dx) >= textWidth || (getWidth() > 0 && textWidth < getHeight())) {
                isStop = true;
                return;
            }
//            Log.d(TAG, "run: " + dx);
            dx -= 1;
            isStop = false;
            postInvalidate();
            postDelayed(mRunnable, speed);
        }
    };

    public MarqueeTextView2(Context context) {
        super(context);
    }

    public MarqueeTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startScroll();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopScroll();
    }

    public void startScroll() {
        reset();
        if(isStop){
            isStop = false;
            postDelayed(mRunnable, speed);
        }
    }

    public void stopScroll() {
        isStop = true;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(visibility == VISIBLE){
            startScroll();
        }else {
            stopScroll();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float density = getResources().getDisplayMetrics().density;
        int width = getWidth();
        int height = getHeight();
//        Log.d(TAG, "onDraw: w - " + width + " y - " + height);
        canvas.rotate(90);
        canvas.translate(dx, -0);
        paint.setColor(textColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth * density);
        paint.setTextSize(textSize * density);
        String text = getText();
        if (!isMeasured) {
            getTextWidth();
            drawTextDy = - ( width - textHeight ) / 2.0f;
        }
        if(text != null){
            canvas.drawText(text, 0, drawTextDy - 4 * density, paint);
        }
//        Log.d(TAG, "onDraw --- : " + isMeasured + " text:" + getText() + " textWidth:" + textWidth + " textHeight:" + textHeight);
        if(!isMeasured && !TextUtils.isEmpty(getText()) && textWidth > 0 && getWidth() > 0 && textWidth > getHeight()){
            isMeasured = true;
            startScroll();
        }
    }

    public String getText(){
        return text;
    }

    private void getTextWidth() {
        Paint paint = this.paint;
        String str = this.getText();
        if (TextUtils.isEmpty(str)) {
            textWidth = 0;
            return;
        }
        rect.setEmpty();
        paint.getTextBounds(str, 0, str.length(), rect);
        textWidth = rect.width();
        textHeight = rect.height();
//        Log.d(TAG, "getTextWidth: w - " + textWidth + " y - " + textHeight);
    }

    private void reset() {
        dx = 0;
    }

    public void setText(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        this.text = sb.toString();
        isMeasured = false;
        postInvalidate();
    }
}
