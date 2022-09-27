package com.example.coderamankumarguptaquizearn;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coderamankumarguptaquizearn.databinding.ActivityQuizBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;

    ArrayList<Question>questions;
    int index = 0;
    int countClick = 0;
    int checkClick = 0;
    Question question;
    CountDownTimer countDownTimer;
    FirebaseFirestore database;
    int correctAnswer = 0;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadAds();
        final AdRequest adRequest = new AdRequest.Builder().build();

        binding.bannerAd.loadAd(adRequest);

        binding.bannerAd.setAdListener(new AdListener() {

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                binding.bannerAd.loadAd(adRequest);
            }
        });
        questions = new ArrayList<>();
//        questions.add(new Question("What is Earth?","Planet","Sun","Human","Car","Planet"));
//        questions.add(new Question("What is Samosa?","Planet","Food","Human","Car","Food"));

        database = FirebaseFirestore.getInstance();
       final String catId = getIntent().getStringExtra("catId"); // come CategoryAdapter Unique CatId for any category

        Random random = new Random();
        final int rand = random.nextInt(15);
        database.collection("Categories")
                        .document(catId)
                            .collection("questions")
                                .whereGreaterThanOrEqualTo("index",rand)
                                        .orderBy("index")
                                                .limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.getDocuments().size() < 5){
                            database.collection("Categories")
                                    .document(catId)
                                    .collection("questions")
                                    .whereLessThanOrEqualTo("index",rand)
                                    .orderBy("index")
                                    .limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                    Question question = snapshot.toObject(Question.class);

                                                    binding.progressBarId.setVisibility(View.GONE);
                                                    questions.add(question);
                                                }

                                            countDownTimer.cancel();
                                                setNextQuestion();
                                        }
                                    });

                        }
                        else{
                            //come 5 quesitons
                            for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                                Question question = snapshot.toObject(Question.class);
                                binding.progressBarId.setVisibility(View.GONE);
                                questions.add(question);
                            }
                            setNextQuestion();
                        }
                    }
                });
        StartTimer();
        setNextQuestion();
    }

    private void loadAds() {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                mInterstitialAd = null;
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        super.onAdFailedToLoad(loadAdError);
                        Log.e("Error", loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

    private void StartTimer(){
         countDownTimer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished/1000));
            }
             @Override
             public void onFinish() {
                 if(index < questions.size()) {
                     index++;

                     if(index != questions.size()){ //when we will reached last position of question then restOption will not call.
                         resetOption();
                     }

                 //if user no check any option then handle
                        checkClick = 0;
                         countClick++;

                     setNextQuestion();
                 }

                 Toast.makeText(QuizActivity.this,"Your time has End", Toast.LENGTH_SHORT).show();
            }
        };

    }

    private void showAnswer(){
        if(question.getAnswer().equals(binding.option1.getText().toString()))
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option2.getText().toString()))
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option3.getText().toString()))
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option4.getText().toString()))
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
    }
    private void setNextQuestion(){

        if(countDownTimer!= null){
            countDownTimer.cancel();
        }

        countDownTimer.start(); //here have a error

        if(index < questions.size()){
            binding.questionCounter.setText(String.format("%d/%d",(index+1),questions.size())); //show how many question are remaining.

             question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());

        }
        else{
            countDownTimer.cancel();
        }
    }


    private void checkAnswer(TextView textView){
        String selectedAnswer = textView.getText().toString();
        if(selectedAnswer.equals(question.getAnswer())){
            correctAnswer++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
           // Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
        }else{
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
           // Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
        }
    }
    private void resetOption(){
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }
    //onClink on Option and nextBtn
    public void onClick(View view){

        switch(view.getId()){
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:

                if(countClick == index){  //if one option is selected then other option is not allowed to check.

                    if(countDownTimer != null) countDownTimer.cancel(); //timer stop

                    TextView selected1 = (TextView) view;
                    checkAnswer(selected1);
                    countClick++;
                    checkClick = 1; //just check any one option is selected.?
                }
                if(index == questions.size()-1) index++;
                break;

            case R.id.Nextbtn:
                if(index < questions.size()) {
                    index++;

                    if(index != questions.size()) // when we reach last Position of question then not resetOption will be call.
                      resetOption();

                    if(checkClick == 0) //if user no check any option then handle.
                        countClick++;
                    else {
                        checkClick = 0; // if user check any option then checkClick is 0 for next question.
                    }

                    setNextQuestion();
                }
                else{
                    Toast.makeText(this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
                    //for Going to ResultActivity Class and set correctAnswer and totalQuestions
                    ProgressDialog dialogue = ProgressDialog.show(QuizActivity.this,"Ads Break","Please wait Ad is Loading..");
                    loadAds();
                    if(mInterstitialAd != null){
                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               mInterstitialAd.show(QuizActivity.this);
                               mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                   @Override
                                   public void onAdDismissedFullScreenContent() {
                                       super.onAdDismissedFullScreenContent();
                                       mInterstitialAd = null;
                                       Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                                       intent.putExtra("correct", correctAnswer);
                                       intent.putExtra("total", questions.size());
                                       countDownTimer.cancel();
                                       startActivity(intent);
                                   }
                               });
                           }
                       },2000);
                    }
                    else{
                        Log.e("Ad Pending","Ad is not ready yet!");
                        startActivity(new Intent(QuizActivity.this,ResultActivity.class));
                    }

                }
                break;
            case R.id.quitBtn:
                ExitAlertdialog();
        }
    }

    private void ExitAlertdialog() {

        new AlertDialog.Builder(QuizActivity.this)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setTitle("Exit")
                .setMessage("Do You want to Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadAds();
                        ProgressDialog dialogue = ProgressDialog.show(QuizActivity.this,"Ads Break","Please wait Ad is Loading..");
                        if(mInterstitialAd != null){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialogue.dismiss();
                                    mInterstitialAd.show(QuizActivity.this);
                                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            mInterstitialAd = null;
                                            countDownTimer.cancel();
                                            finish();
                                        }
                                    });
                                }
                            },2000);

                        }else{
                            Log.e("Ad Pending","Ad is not ready yet!");
                            countDownTimer.cancel();
                            finish();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        ExitAlertdialog();
    }
}