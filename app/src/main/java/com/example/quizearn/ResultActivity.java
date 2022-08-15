package com.example.quizearn;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizearn.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    int POINTS = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater()); //Connect to activity_result XML
        setContentView(binding.getRoot());

        int correctAnswers = getIntent().getIntExtra("correct",0);
        int totalQuestions = getIntent().getIntExtra("total",0);

        int points = correctAnswers * POINTS;

        binding.score.setText(String.format("%d/%d",correctAnswers,totalQuestions));
        binding.earnedCoins.setText(String.valueOf(points));// direct integer not passes in Activity XML.
    }
}