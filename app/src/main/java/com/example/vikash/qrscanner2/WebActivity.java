package com.example.vikash.qrscanner2;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        WebView webView=(WebView)findViewById(R.id.webView);
        String url= getIntent().getStringExtra("url");
        webView.setWebViewClient(new MyWebClient());
        webView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }

    private class MyWebClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
