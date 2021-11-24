package com.sovegetables.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.qmuiteam.qmui.widget.QMUILoadingView;

public class EmptyOrErrorView extends FrameLayout {

    private QMUILoadingView mLoadingView;
    private TextView mTitleTextView;
    private TextView mDetailTextView;
    private ImageView mIvEmptyView;
    protected Button mButton;

    public EmptyOrErrorView(Context context) {
        this(context,null);
    }

    public EmptyOrErrorView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public EmptyOrErrorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.EmptyOrErrorView);
        boolean attrShowLoading = arr.getBoolean(R.styleable.EmptyOrErrorView_dougong_show_loading, false);
        String attrTitleText = arr.getString(R.styleable.EmptyOrErrorView_dougong_title_text);
        String attrDetailText = arr.getString(R.styleable.EmptyOrErrorView_dougong_detail_text);
        String attrBtnText = arr.getString(R.styleable.EmptyOrErrorView_dougong_btn_text);
        arr.recycle();
        show(attrShowLoading,attrTitleText,attrDetailText,attrBtnText,null);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.app_empty_view, this, true);
        mLoadingView = (QMUILoadingView)findViewById(R.id.empty_view_loading);
        mTitleTextView = (TextView)findViewById(R.id.empty_view_title);
        mDetailTextView = (TextView)findViewById(R.id.empty_view_detail);
        mIvEmptyView = (ImageView) findViewById(R.id.iv_empty);
        mButton = (Button)findViewById(R.id.empty_view_button);
    }

    public void show(boolean loading, Drawable drawable, String titleText, String detailText, String buttonText, OnClickListener onButtonClickListener) {
        setEmptyImage(drawable);
        setLoadingShowing(loading);
        setTitleText(titleText);
        setDetailText(detailText);
        setButton(buttonText, onButtonClickListener);
        show();
    }

    /**
     * 显示emptyView
     * @param loading 是否要显示loading
     * @param titleText 标题的文字，不需要则传null
     * @param detailText 详情文字，不需要则传null
     * @param buttonText 按钮的文字，不需要按钮则传null
     * @param onButtonClickListener 按钮的onClick监听，不需要则传null
     */
    public void show(boolean loading, String titleText, String detailText, String buttonText, OnClickListener onButtonClickListener) {
        setLoadingShowing(loading);
        setTitleText(titleText);
        setDetailText(detailText);
        setEmptyImage(null);
        setButton(buttonText, onButtonClickListener);
        show();
    }

    /**
     * 用于显示emptyView并且只显示loading的情况，此时title、detail、button都被隐藏
     * @param loading 是否显示loading
     */
    public void show(boolean loading) {
        setLoadingShowing(loading);
        setTitleText(null);
        setDetailText(null);
        setEmptyImage(null);
        setButton(null, null);
        show();
    }

    /**
     * 用于显示纯文本的简单调用方法，此时loading、button均被隐藏
     * @param titleText 标题的文字，不需要则传null
     * @param detailText 详情文字，不需要则传null
     */
    public void show(String titleText, String detailText) {
        setLoadingShowing(false);
        setTitleText(titleText);
        setDetailText(detailText);
        setEmptyImage(null);
        setButton(null, null);
        show();
    }

    /**
     * 显示emptyView，不建议直接使用，建议调用带参数的show()方法，方便控制所有子View的显示/隐藏
     */
    public void show() {
        setVisibility(VISIBLE);
    }

    /**
     * 隐藏emptyView
     */
    public void hide() {
        setVisibility(GONE);
        setLoadingShowing(false);
        setTitleText(null);
        setDetailText(null);
        setButton(null, null);
    }

    public boolean isShowing() {
        return getVisibility() == VISIBLE;
    }

    public boolean isLoading() {
        return mLoadingView.getVisibility() == VISIBLE;
    }

    public void setEmptyImage(Drawable drawable){
        mIvEmptyView.setVisibility(drawable == null? GONE: VISIBLE);
        mIvEmptyView.setImageDrawable(drawable);
    }

    public void setLoadingShowing(boolean show) {
        mLoadingView.setVisibility(show ? VISIBLE : GONE);
    }

    public void setTitleText(String text) {
        mTitleTextView.setText(text);
        mTitleTextView.setVisibility(text != null ? VISIBLE : GONE);
    }

    public void setDetailText(String text) {
        mDetailTextView.setText(text);
        mDetailTextView.setVisibility(text != null ? VISIBLE : GONE);
    }

    public void setTitleColor(int color) {
        mTitleTextView.setTextColor(color);
    }

    public void setDetailColor(int color) {
        mDetailTextView.setTextColor(color);
    }

    public void setButton(String text, OnClickListener onClickListener) {
        mButton.setText(text);
        mButton.setVisibility(text != null ? VISIBLE : GONE);
        mButton.setOnClickListener(onClickListener);
    }

}
