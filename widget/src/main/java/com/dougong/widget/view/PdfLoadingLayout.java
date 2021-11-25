package com.dougong.widget.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.dougong.widget.R;


public class PdfLoadingLayout extends LinearLayout {

    private TextView tvLoading;
    private ProgressBar pb;
    private TextView tvLoadingError;
    private TextView tvRetry;

    public PdfLoadingLayout(Context context) {
        super(context);
        init(context, null);
    }

    public PdfLoadingLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PdfLoadingLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PdfLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.pdf_loading, this);
        tvLoading = findViewById(R.id.tv_loading);
        pb = findViewById(R.id.pb);
        tvLoadingError = findViewById(R.id.tv_loading_error);
        tvRetry = findViewById(R.id.tv_retry);
    }

    public void hide(){
        setVisibility(View.GONE);
    }

    public void setProgress(int progress){
        setVisibility(View.VISIBLE);
        tvLoading.setVisibility(View.VISIBLE);
        pb.setVisibility(View.VISIBLE);
        pb.setProgress(progress);
        tvRetry.setVisibility(View.GONE);
        tvLoadingError.setVisibility(View.GONE);
    }

    public void showError(OnClickListener listener){
        setVisibility(View.VISIBLE);
        tvRetry.setVisibility(View.VISIBLE);
        tvLoadingError.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
        tvLoading.setVisibility(View.GONE);
        tvRetry.setOnClickListener(listener);
    }
}
