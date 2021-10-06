package com.example.testcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * 웹뷰 3개는 로드보이 코드에서 그대로 가져온 것.
 * 체크박스 수를 2개로 수정했기때문에 추후 수정이 필요.
 */

public class WebViewActivity1 extends AppCompatActivity {

    MainActivity m;


    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view1);

        mWebView = (WebView) findViewById(R.id.webView);



//        mWebView.setWebViewClient(new WebViewClient());
//        mWebSettings = mWebView.getSettings();
//        mWebSettings.setJavaScriptEnabled(true);
//        mWebSettings.setLoadWithOverviewMode(true);
//        mWebSettings.setUseWideViewPort(true);
//        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        mWebSettings.setDomStorageEnabled(true);



        mWebView.loadUrl("https://static.roadvoy.net/terms/service.html");

    }

}