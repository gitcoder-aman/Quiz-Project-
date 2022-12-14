package com.tech.coderamankumarguptaquizearn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coderamankumarguptaquizearn.databinding.ActivityResultBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

public class ResultActivity extends AppCompatActivity implements IUnityAdsInitializationListener {

    ActivityResultBinding binding;
    UserDatabase userdatabase;
    ProgressDialog dialog;

    static int POINTS = 0;
    int points = 0;

    private final String Rewarded_Ad = "Rewarded_Android";
    final String GameID = "4994113";
    final boolean TestMode = false;

    private IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {

                    UnityAds.show(ResultActivity.this, Rewarded_Ad, new UnityAdsShowOptions(), showListener);
        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
        }
    };

    private IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message);
        }

        @Override
        public void onUnityAdsShowStart(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowStart: " + placementId);
        }

        @Override
        public void onUnityAdsShowClick(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: " + placementId);
        }

        @Override
        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
            Log.v("UnityAdsExample", "onUnityAdsShowComplete: " + placementId);
            if (state.equals(UnityAds.UnityAdsShowCompletionState.COMPLETED)) {
                // Reward the user for watching the ad to completion
            } else {
                // Do not reward the user for skipping the ad
            }
        }
    };
    @Override
    public void onInitializationComplete() {
        DisplayRewardedAd();
    }
    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String s) {
        Log.e("UnityAdsExample", "Unity Ads initialization failed with error: [" + unityAdsInitializationError + "] " + s);
    }
    private void DisplayRewardedAd() {
        UnityAds.load(Rewarded_Ad, loadListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater()); //Connect to activity_result XML
        setContentView(binding.getRoot());

        UnityAds.initialize(getApplicationContext(), GameID, TestMode, this);

        int correctAnswers = getIntent().getIntExtra("correct",0);
        int totalQuestions = getIntent().getIntExtra("total",0);

        FirebaseFirestore database;
        database = FirebaseFirestore.getInstance();
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userdatabase = documentSnapshot.toObject(UserDatabase.class);
                        POINTS = userdatabase.getQCoins();
                         points = correctAnswers * POINTS;
                        binding.earnedCoins.setText(String.valueOf(points));// direct integer not passes in Activity XML.

                        binding.score.setText(String.format("%d/%d",correctAnswers,totalQuestions));

                        FirebaseFirestore database;
                        database = FirebaseFirestore.getInstance();
                        if(isConnected()) {
                            //Wallet update
                            database = FirebaseFirestore.getInstance();
                            database.collection("users")
                                    .document(FirebaseAuth.getInstance().getUid()) //unique id get of user
                                    .update("coins", FieldValue.increment(points));
                        }else{
                            Toast.makeText(ResultActivity.this, "INTERNET NOT AVAILABLE Coins not added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ReferCode findout
                FirebaseFirestore database;
                 database = FirebaseFirestore.getInstance();
                database.collection("users")
                        .document(FirebaseAuth.getInstance().getUid())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                userdatabase = documentSnapshot.toObject(UserDatabase.class);

                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey Hey Hey!!\uD83D\uDE0D\uD83D\uDE0D\uD83D\uDE0D \n" +
                                        "I'm earning real money in this APP!!\uD83C\uDF39\uD83C\uDF39\uD83C\uDF39 \n" +
                                        "Most popular money making app in India!!!\uD83D\uDC9B\uD83E\uDD0D\uD83D\uDC9A \n" +
                                        "Download APP, everyone can get ???40!!!\uD83D\uDE3B\uD83D\uDE3B\uD83D\uDE3B \n" +
                                        "It's 100% true! \uD83D\uDE39 \n" +
                                        "Click the link???you can get ???500 a week like me! YOUR REFERRAL CODE=" + userdatabase.getReferCode());
                                sendIntent.setType("text/plain");

                                Intent shareIntent = Intent.createChooser(sendIntent, null);
                                startActivity(shareIntent);
                            }
                        });
            }
        });

        dialog = new ProgressDialog(ResultActivity.this);
        dialog.setMessage("Wait...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
            binding.restartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(ResultActivity.this,MainActivity.class));
                    dialog.show();

                   // nDialog.setTitle("Checking Network");

                }
            });

        }

    private boolean isConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null){
            if(networkInfo.isConnected()){
                return true;
            }else return false;
        }else{
            return false;
        }
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        startActivity(new Intent(ResultActivity.this,MainActivity.class));

    }

}