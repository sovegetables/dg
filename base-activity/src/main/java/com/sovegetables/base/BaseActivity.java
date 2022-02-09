package com.sovegetables.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sovegetables.IContentView;
import com.sovegetables.SystemBarConfig;
import com.sovegetables.topnavbar.ITopBarAction;
import com.sovegetables.topnavbar.TopBar;
import com.sovegetables.topnavbar.TopBarItem;
import com.sovegetables.topnavbar.TopBarUpdater;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class BaseActivity extends com.sovegetables.BaseActivity {

    private ViewGroup activityRootViewGroup;

    static {
        com.sovegetables.BaseActivity.Companion.setLeftTopIcon(R.drawable.ic_arrow_left_black);
    }

    private ProgressDialog dialog;
    private Disposable progressDisposable;

    public void showProgressDialog(Disposable disposable, String text) {
        progressDisposable = disposable;
        showProgressDialog(text);
    }

    public void showProgressDialog(String text) {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setMessage(text);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (progressDisposable != null) {
                        progressDisposable.dispose();
                    }
                }
            });
        } else {
            dialog.setMessage(text);
        }
        dialog.show();
    }

    public void dismissProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
        for (Disposable disposable : disposableList) {
            disposable.dispose();
        }
        hideLoadingDialog();
    }

    private final List<Disposable> disposableList = new ArrayList<>();

    public void addDisposable(Disposable disposable) {
        disposableList.add(disposable);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        activityRootViewGroup = findViewById(R.id.rl_activity_container);
    }

    @Override
    public void setContentView(@Nullable View view) {
        super.setContentView(view);
        activityRootViewGroup = findViewById(R.id.rl_activity_container);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    public ViewGroup getActivityRootViewGroup() {
        return activityRootViewGroup;
    }

    public TopBarUpdater getActionBarView() {
        return topBarAction.getTopBarUpdater();
    }

    public ITopBarAction topBarAction(){
        return topBarAction;
    }

    @Override
    public SystemBarConfig createSystemBarConfig() {
        return new SystemBarConfig.Builder()
                .setStatusBarColor(Color.WHITE)
                .setNavigationBarStyle(SystemBarConfig.SystemBarFontStyle.DARK)
                .setStatusBarFontStyle(SystemBarConfig.SystemBarFontStyle.DARK)
                .build();
    }

    public void showEmpty(String title){
        super.showEmpty(title, 0, null);
    }

    public void hideEmpty(){
        super.hideEmpty();
    }

    public void showSpinner(){
        showLoading();
    }

    public void hideSpinner(){
        hideLoading();
    }

    @Override
    public IContentView getContentViewDelegate() {
        return new AppContentView();
    }

    protected final TopBar leftBackTopBar(CharSequence title) {
        TopBarItem leftItem = new TopBarItem
                .Builder()
                .listener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .icon(R.drawable.ic_arrow_left_black)
                .build(this, 0);
        return new TopBar.Builder()
                .left(leftItem)
                .title(title)
                .build(this);
    }

    protected final TopBar.Builder leftBackTopBarBuilder(CharSequence title){
        TopBarItem leftItem = new TopBarItem
                .Builder()
                .listener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                })
                .icon(R.drawable.ic_arrow_left_black)
                .build(this, 0);
        return new TopBar.Builder()
                .left(leftItem)
                .title(title);
    }
}
