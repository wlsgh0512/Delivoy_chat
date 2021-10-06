package com.example.testcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity3 extends AppCompatActivity {

    private WebView mWebView3; // 웹뷰 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view3);

        mWebView3 = (WebView) findViewById(R.id.webView3);

        mWebView3.loadUrl("https://static.roadvoy.net/terms/openbanking.html");
    }
}