package com.example.coderamankumarguptaquizearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class splashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBarId);
        //parallel threading or asynchronous // Parallel two works first hold the splash Activity and run method.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = splashActivity.this.getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn",false);

                if(hasLoggedIn){
                    progressBar.setVisibility(View.GONE);
                    splashActivity.this.startActivity(new Intent(splashActivity.this, MainActivity.class));
                    finish();
                }else{
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(splashActivity.this,LoginActivity.class));
                    finish();
                }

            }
        },2000);

    }
}