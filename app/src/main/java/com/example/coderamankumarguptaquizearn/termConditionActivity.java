package com.example.coderamankumarguptaquizearn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class termConditionActivity extends AppCompatActivity {

    WebView webView;
    public String fileName = "termCondition.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_condition);


        webView = (WebView) findViewById(R.id.term);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/"+fileName);
    }
}