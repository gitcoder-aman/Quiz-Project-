package com.tech.coderamankumarguptaquizearn;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coderamankumarguptaquizearn.R;
import com.example.coderamankumarguptaquizearn.databinding.ActivitySpinnerBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;
import com.tech.coderamankumarguptaquizearn.SpinWheel.LuckyWheelView;
import com.tech.coderamankumarguptaquizearn.SpinWheel.model.LuckyItem;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpinnerActivity extends AppCompatActivity implements IUnityAdsInitializationListener {

    private String Rewarded_Ad = "Rewarded_Android";
   private String GameID = "4994113";
    private Boolean TestMode = false;

    ActivitySpinnerBinding binding;
    UserDatabase userdatabase;
    static int getSpinCount = 0,getSpinTill = 0;

    private IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    UnityAds.show(SpinnerActivity.this, Rewarded_Ad, new UnityAdsShowOptions(), showListener);
                }
            },10000);
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
        binding  = ActivitySpinnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UnityAds.initialize(getApplicationContext(), GameID, TestMode, this); // rewarded sdk initialize

        List<LuckyItem> data = new ArrayList<>();

        LuckyItem item1 = new LuckyItem();
        item1.topText = "10";
        item1.secondaryText = "Coins";
        item1.color = Color.parseColor("#FFFFFFFF");
        item1.textColor = Color.parseColor("#FF000000");
        data.add(item1);

        LuckyItem item2 = new LuckyItem();
        item2.topText = "100";
        item2.secondaryText = "Coins";
        item2.color = Color.parseColor("#4CAF50");
        item2.textColor = Color.parseColor("#ffffff");
        data.add(item2);

        LuckyItem item3 = new LuckyItem();
        item3.topText = "50";
        item3.secondaryText = "COINS";
        item3.color = Color.parseColor("#FFFFFFFF");
        item3.textColor = Color.parseColor("#FF000000");
        data.add(item3);

        LuckyItem item4 = new LuckyItem();
        item4.topText = "75";
        item4.secondaryText = "COINS";
        item4.color = Color.parseColor("#7f00d9");
        item4.textColor = Color.parseColor("#eceff1");
        data.add(item4);

        LuckyItem item5 = new LuckyItem();
        item5.topText = "150";
        item5.secondaryText = "COINS";
        item5.color = Color.parseColor("#FFFFFFFF");
        item5.textColor = Color.parseColor("#FF000000");
        data.add(item5);

        LuckyItem item6 = new LuckyItem();
        item6.topText = "90";
        item6.secondaryText = "COINS";
        item6.color = Color.parseColor("#dc0000");
        item6.textColor = Color.parseColor("#eceff1");
        data.add(item6);

        LuckyItem item7 = new LuckyItem();
        item7.topText = "200";
        item7.secondaryText = "COINS";
        item7.color = Color.parseColor("#FFFFFFFF");
        item7.textColor = Color.parseColor("#FF000000");
        data.add(item7);

        LuckyItem item8 = new LuckyItem();
        item8.topText = "0";
        item8.secondaryText = "COINS";
        item8.color = Color.parseColor("#008bff");
        item8.textColor = Color.parseColor("#eceff1");
        data.add(item8);

        binding.wheelview.setData(data);
        binding.wheelview.setRound(7);

        //Current coins Show in Spinner Activity
        FirebaseFirestore database;
        database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userdatabase = documentSnapshot.toObject(UserDatabase.class);
                        binding.coinsShow.setText(String.valueOf(userdatabase.getCoins()));
                        getSpinCount = userdatabase.getSpinCount();
                        getSpinTill = userdatabase.getSpinCountTill();
                        binding.spinCount.setText(String.format("%d/%d",getSpinCount,getSpinTill));
                        //binding.currentCoins.setText(user.getCoins() + " "); you can also write this.
                    }
                });

        MediaPlayer mp = MediaPlayer.create(this, R.raw.spin_sound);
        binding.spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(getSpinCount < getSpinTill) {
                        Random r = new Random();
                        int randomNumber = r.nextInt(8);
                        mp.start();
                        binding.wheelview.startLuckyWheelWithTargetIndex(randomNumber);
                    }else{
                        Toast.makeText(SpinnerActivity.this, "Your total spin chance end.wait for next day", Toast.LENGTH_SHORT).show();
                    }
                UnityAds.load(Rewarded_Ad,loadListener);
            }
        });

        binding.wheelview.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {

            @Override
            public void LuckyRoundItemSelected(int index) {

                if(isConnected()){
                    spinCountUpdate();
                    updateCash(index);
                }else{
                    Toast.makeText(SpinnerActivity.this, "INTERNET NOT AVAILABLE", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void spinCountUpdate() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("spinCount", FieldValue.increment(1));

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

    void updateCash(int index){
        long cash = 0;
        switch (index){

            case 0:
                cash = 10;
                break;
            case 1:
                cash = 100;
                break;
            case 2:
                cash = 50;
                break;
            case 3:
                cash = 75;
                break;
            case 4:
                cash = 150;
                break;
            case 5:
                cash = 90;
                break;
            case 6:
                cash = 200;
                break;
            case 7:
                cash = 0;
                break;
        }

        MediaPlayer mp = MediaPlayer.create(this,R.raw.coin_sound);
        new FancyGifDialog.Builder(SpinnerActivity.this)
                .setTitle("Congratulation you won") // You can also send title like R.string.from_resources
                .setMessage("Coins: "+ cash) // or pass like R.string.description_from_resources
                .setTitleTextColor(R.color.black)
                .setDescriptionTextColor(R.color.black)
                .setNegativeBtnText("Cancel") // or pass it like android.R.string.cancel
                .setPositiveBtnBackground(R.color.Green)
                .setPositiveBtnText("OK") // or pass it like android.R.string.ok
                .setNegativeBtnBackground(R.color.orange)
                .setGifResource(R.drawable.gif_win)   //Pass your Gif here
                .isCancellable(false)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        mp.start();
                        //Current coins Show in Spinner Activity
                        FirebaseFirestore database;
                        database = FirebaseFirestore.getInstance();

                        database.collection("users")
                                .document(FirebaseAuth.getInstance().getUid())
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        userdatabase = documentSnapshot.toObject(UserDatabase.class);
                                        binding.coinsShow.setText(String.valueOf(userdatabase.getCoins()));
                                        getSpinCount = userdatabase.getSpinCount();
                                        binding.spinCount.setText(String.format("%d/%d",getSpinCount,getSpinTill));
                                        Toast.makeText(SpinnerActivity.this, "Coins Added", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(SpinnerActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(cash)).addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("coins","Added");
                    }
                });
    }

    @Override
    public void onBackPressed() {

            SpinnerActivity.super.onBackPressed();
        }

}