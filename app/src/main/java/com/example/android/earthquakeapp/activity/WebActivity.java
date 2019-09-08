package com.example.android.earthquakeapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.android.earthquakeapp.R;

// TODO see https://developer.android.com/guide/webapps/webview
public class WebActivity extends AppCompatActivity {

    public static final String TITLE = "TITLE";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mWebView = findViewById(R.id.web_view);

        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle(getApplicationInfo().labelRes);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        if (null != getIntent()) {
            final Intent intent = getIntent();
            final Uri uri = intent.getData();
            if (uri != null) {
                mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.getSettings().setSupportZoom(true);
                mWebView.getSettings().setBuiltInZoomControls(true);
                mWebView.getSettings().setDisplayZoomControls(false);
                mWebView.getSettings().setAllowFileAccess(true);
                mWebView.getSettings().setAllowContentAccess(true);
                mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
                mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
                mWebView.getSettings().setLoadsImagesAutomatically(true);
                mWebView.loadUrl(uri.toString());
                mWebView.getSettings().setDomStorageEnabled(true);
                mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                mWebView.setScrollbarFadingEnabled(true);
                mWebView.setWebViewClient(new MyWebViewClient());
                mWebView.setWebChromeClient(new MyWebChromeClient());
            }

            final String title = intent.getStringExtra(TITLE);
            if (!TextUtils.isEmpty(title)){
                setTitle(title);
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(@NonNull final WebView view, @NonNull final SslErrorHandler handler, @NonNull final SslError error) {
            Log.w(TAG, "Ignoring SSL Error: " + error);
            mWebView.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light, null));
            handler.proceed();
        }

        @Override
        public void onReceivedError(@NonNull final WebView view, WebResourceRequest request, @NonNull final WebResourceError error) {
            Log.w(TAG, "onReceivedError: " + error.getDescription());
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedHttpError(@NonNull final WebView view, @NonNull final WebResourceRequest request, @NonNull final WebResourceResponse errorResponse) {
            Log.w(TAG, "onReceivedHttpError: code " + errorResponse.getStatusCode() + ", " + errorResponse.getReasonPhrase());
            super.onReceivedHttpError(view, request, errorResponse);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(@NonNull final WebView view, final int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    public void onBackPressed() {
        if (this.mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private WebView mWebView;
    private Toolbar toolbar;
    private static final String TAG = WebActivity.class.getSimpleName();
}
