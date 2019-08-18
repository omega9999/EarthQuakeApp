package com.example.android.earthquakeapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.android.earthquakeapp.R;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        final Intent intent = getIntent();
        final Uri uri = intent.getData();


        // TODO see https://developer.android.com/guide/webapps/webview
        final WebView webView = findViewById(R.id.web_view);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.loadUrl(uri.toString());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);



    }

    private class MyWebViewClient extends WebViewClient{
        //TODO implements
    }
    private class MyWebChromeClient extends WebChromeClient{
        //TODO implements
    }
}
