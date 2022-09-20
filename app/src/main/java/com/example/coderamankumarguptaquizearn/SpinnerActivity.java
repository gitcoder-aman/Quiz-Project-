package com.example.coderamankumarguptaquizearn;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coderamankumarguptaquizearn.SpinWheel.LuckyWheelView;
import com.example.coderamankumarguptaquizearn.SpinWheel.model.LuckyItem;
import com.example.coderamankumarguptaquizearn.databinding.ActivitySpinnerBinding;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpinnerActivity extends AppCompatActivity {

    ActivitySpinnerBinding binding;
    UserDatabase userdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivitySpinnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        binding.wheelview.setRound(5);

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

                        //binding.currentCoins.setText(user.getCoins() + " "); you can also write this.
                    }
                });

        MediaPlayer mp = MediaPlayer.create(this, R.raw.spin_sound);

        binding.spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random r = new Random();
                int randomNumber = r.nextInt(8);
                mp.start();
                binding.wheelview.startLuckyWheelWithTargetIndex(randomNumber);

            }
        });
        binding.wheelview.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {

            @Override
            public void LuckyRoundItemSelected(int index) {

                if(isConnected()){
                    updateCash(index);
                }else{
                    Toast.makeText(SpinnerActivity.this, "INTERNET NOT AVAILABLE", Toast.LENGTH_SHORT).show();
                }

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

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(cash)).addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void unused) {

                        Toast toast = Toast.makeText(SpinnerActivity.this, "Coins added in account", Toast.LENGTH_LONG);
                        View toastView = toast.getView(); // This'll return the default View of the Toast.

                        /* And now you can get the TextView of the default View of the Toast. */
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(20);
                        toastMessage.setTextColor(Color.RED);
                        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.coins, 0, 0, 0);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastMessage.setCompoundDrawablePadding(16);
                        toastView.setBackgroundColor(Color.CYAN);
                        toast.show();
                    }
                });

        //Current coins Show in Spinner Activity
        database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userdatabase = documentSnapshot.toObject(UserDatabase.class);
                        binding.coinsShow.setText(String.valueOf(userdatabase.getCoins()));

                        //binding.currentCoins.setText(user.getCoins() + " "); you can also write this.
                    }
                });

    }
}