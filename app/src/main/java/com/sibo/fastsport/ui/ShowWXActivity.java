package com.sibo.fastsport.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.sibo.fastsport.R;
import com.sibo.fastsport.base.BaseActivity;
import com.sibo.fastsport.utils.StatusBarUtil;

public class ShowWXActivity extends BaseActivity {

    private WebView webView;
    private ImageView back;

    @Override
    protected void findViewByIDS() {
        webView = (WebView) findViewById(R.id.showwx_webview);
        back = (ImageView) findViewById(R.id.showx_back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarDarkMode(this, StatusBarUtil.StatusBarLightMode(this));
        setContentView(R.layout.activity_show_wx);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    private void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
