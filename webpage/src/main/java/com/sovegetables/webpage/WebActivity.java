package com.sovegetables.webpage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.TypedValue;
import com.sovegetables.SystemBarConfig;
import com.sovegetables.base.BaseActivity;
import com.sovegetables.topnavbar.TopBar;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import org.jetbrains.annotations.Nullable;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class WebActivity extends BaseActivity {

    String OldPageTitle = "";
    private WebView mWebView;
    private static final String UTF_8 = "UTF-8";
    private static final String KEY_URL = "key.url.WebActivity";
    private static final String KEY_TITLE = "key.title.WebActivity";

    public static void open(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_URL, url);
        context.startActivity(intent);
    }

    public static void open(Context context, String url, String title) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_URL, url);
        intent.putExtra(KEY_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    public SystemBarConfig createSystemBarConfig() {
        TypedValue colorPrimaryDark = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark,
                colorPrimaryDark, true);
        int color = colorPrimaryDark.data;
        return new SystemBarConfig(color, SystemBarConfig.SystemBarFontStyle.LIGHT);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if(outState != null){
            outState.putString(KEY_URL, mUrl);
            outState.putString(KEY_TITLE, mTitle);
        }
    }

    private String mUrl = "";
    private String mTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        if(savedInstanceState == null){
            mTitle = getIntent().getStringExtra(KEY_TITLE);
            mUrl = getIntent().getStringExtra(KEY_URL);
        }else {
            mTitle = savedInstanceState.getString(KEY_TITLE);
            mUrl = savedInstanceState.getString(KEY_URL);
        }
        String title = mTitle;
        String url = mUrl;
        final boolean enableTitle = TextUtils.isEmpty(title);
        if(!enableTitle){
            topBarAction.getTopBarUpdater()
                    .title(title)
                    .update();
        }
        mWebView = findViewById(R.id.web_view);

        final WebSettings webSettings = mWebView.getSettings();
        webSettings.setGeolocationEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDefaultTextEncodingName(UTF_8);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setDomStorageEnabled(true);
        String appCachePath = getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua + " dougong");
        webSettings.setSupportMultipleWindows(false);
        webSettings.setLoadsImagesAutomatically(true);
        mWebView.loadUrl(url);
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (!OldPageTitle.equals(view.getTitle()) && enableTitle) {//getTitle has the newer Title
                    // get the Title
                    OldPageTitle = view.getTitle();
                    topBarAction.getTopBarUpdater()
                            .title(OldPageTitle)
                            .update();
                }
            }
        });
    }

    @Nullable
    @Override
    public TopBar getTopBar() {
        return leftBackTopBar("");
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
