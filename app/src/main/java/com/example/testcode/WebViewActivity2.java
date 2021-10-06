package com.example.testcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity2 extends AppCompatActivity {

    private WebView mWebView2; // 웹뷰 선언
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view2);

        mWebView2 = (WebView) findViewById(R.id.webView2);

        mWebView2.loadUrl("https://static.roadvoy.net/terms/location.html");

    }
}