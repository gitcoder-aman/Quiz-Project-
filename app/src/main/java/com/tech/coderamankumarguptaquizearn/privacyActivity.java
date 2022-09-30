package com.tech.coderamankumarguptaquizearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.coderamankumarguptaquizearn.R;

public class privacyActivity extends AppCompatActivity {

    WebView webView;
    public String fileName = "privacy.html";
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        webView = (WebView) findViewById(R.id.privacyId);
        toolbar = findViewById(R.id.toolbar);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/"+fileName);
        setSupportActionBar(toolbar);
    }

}