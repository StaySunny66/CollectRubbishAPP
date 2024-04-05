package com.shilight.rubbish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class web extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = (WebView) findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setDomStorageEnabled(true);
        Intent go = getIntent();
        String st = go.getStringExtra("data");
        String tittle = go.getStringExtra("tittle");
        Log.i("标题", st);


        webView.setWebViewClient(new WebViewClient(){

                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                         return super.shouldOverrideUrlLoading(view, request);
                                     }
                                 });
        webView.loadUrl(st);
        getSupportActionBar().setTitle(tittle);

    }


}