package com.example.coderamankumarguptaquizearn;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coderamankumarguptaquizearn.databinding.FragmentWalletBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WalletFragment extends Fragment {


     RewardedAd mRewardedAd;
    private boolean isLoaded = false;

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentWalletBinding binding;
    FirebaseFirestore database;
    UserDatabase userdatabase;

    String[] paymentMethod = {"None","PhonePe","GooglePay","Paytm"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        loadAd(); //hide for just want to minimum ads show in beginning
       binding = FragmentWalletBinding.inflate(inflater,container,false);
       database = FirebaseFirestore.getInstance();

       //Current coins update in firestore and app
       database.collection("users")
               .document(FirebaseAuth.getInstance().getUid())
               .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                   @Override
                   public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userdatabase = documentSnapshot.toObject(UserDatabase.class);
                       binding.currentCoins.setText(String.valueOf(userdatabase.getCoins()));
                       binding.progressBarId.setVisibility(View.GONE);

                       //binding.currentCoins.setText(user.getCoins() + " "); you can also write this.
                   }
               });


        paymentOption();

       // String payType = binding.paymentTypeBox.getText().toString().trim();
       //send button request process

        binding.sendRequestBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (isConnected()) {

                    String paymentType = binding.paymentTypeBox.getText().toString().trim();
                    String Coins = binding.numberOfCoins.getText().toString().trim();
                    String number = binding.number.getText().toString().trim();

                    if (paymentType.equals("None")) {
                        binding.paymentTypeBox.setError("*");
                        return;
                    } else {
                        binding.paymentTypeBox.setError(null);
                        binding.paymentTypeBox.clearFocus();
                    }

                    if (TextUtils.isEmpty(number)) {
                        binding.number.setError("*");
                        return;
                    } else {
                        binding.number.setError(null);
                        binding.number.clearFocus();
                    }
                    if (TextUtils.isEmpty(Coins)) {
                        binding.numberOfCoins.setError("*");
                        return;
                    } else {
                            if(Long.parseLong(Coins) % 1000 == 0){
                                binding.numberOfCoins.setError(null);
                                binding.numberOfCoins.clearFocus();
                            }
                            else{
                                binding.numberOfCoins.setError("Enter multiple of 1000");
                                return;
                            }
                        }
                    long numberOfCoins = Long.parseLong(Coins);
                    if (numberOfCoins >= 10000 && numberOfCoins <= userdatabase.getCoins()) {
                        binding.sendRequestBtn.setVisibility(View.INVISIBLE);
                        String uid = FirebaseAuth.getInstance().getUid();
                        long rupees = numberOfCoins/1000;

                        String generateId = generateId(20);
                        adminNotification notification = new adminNotification(generateId,number, userdatabase.getName(), numberOfCoins,"Pending",rupees,uid,paymentType);
                        binding.number.setText("");
                        binding.numberOfCoins.setText("");
                        database
                                .collection("adminNotification")
                                .document(generateId)
                                .set(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Request sent successfully.", Toast.LENGTH_SHORT).show();

                                        Toast toast = Toast.makeText(getContext(), "Another request some time after", Toast.LENGTH_LONG);
                                        View toastView = toast.getView(); // This'll return the default View of the Toast.

                                        /* And now you can get the TextView of the default View of the Toast. */
                                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                        toastMessage.setTextSize(25);
                                        toastMessage.setTextColor(Color.RED);
                                        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.paytm, 0, 0, 0);
                                        toastMessage.setGravity(Gravity.CENTER);
                                        toastMessage.setCompoundDrawablePadding(16);
                                        toastView.setBackgroundColor(Color.CYAN);
                                        toast.show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Request sent Fail.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        WithdrawRequest request = new WithdrawRequest(generateId,number, userdatabase.getName(), numberOfCoins,"Pending",rupees,paymentType);

                        database
                                .collection("withdraw")
                                .document(FirebaseAuth.getInstance().getUid())
                                .collection("History")
                                .document(generateId)
                                .set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        //Wallet update
                                        long coinsMinus = userdatabase.getCoins() - numberOfCoins;
                                        database = FirebaseFirestore.getInstance();
                                        database.collection("users")
                                                .document(FirebaseAuth.getInstance().getUid()) //unique id get of user
                                                .update("coins", coinsMinus);
                                        binding.progressBarId.setVisibility(View.GONE);
                                    }
                                });

                    } else {
                        Toast.makeText(getContext(), "You need more coins to get withdraw.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "INTERNET NOT AVAILABLE", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }

    private void loadAd() {

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(getActivity(), "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        super.onAdFailedToLoad(loadAdError);
                        Log.e("Error", loadAdError.toString());
                        mRewardedAd = null;
                        Toast.makeText(getActivity(), "Eneter", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        super.onAdLoaded(rewardedAd);
                        mRewardedAd = rewardedAd;
                        isLoaded = true;
                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                mRewardedAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                mRewardedAd = null;
                            }
                        });

                    }
                });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isLoaded){
                    ProgressDialog dialog = ProgressDialog.show(getActivity(),"Ads Break","Please wait while an ad is being set up");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();

                            mRewardedAd.show(getActivity(), new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    Toast.makeText(getActivity(),
                                            "Ad close", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    },2000);
                }
                else{
                    Toast.makeText(getActivity(), "Ad is not ready yet!", Toast.LENGTH_SHORT).show();
                }
            }
        },5000);
    }

    private void paymentOption() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, paymentMethod);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.payOptionBtn.setAdapter(adapter);

        binding.payOptionBtn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String OptionValue = parent.getItemAtPosition(position).toString();
                binding.paymentTypeBox.setText(OptionValue);
                String paymentType = binding.paymentTypeBox.getText().toString().trim();

                if(paymentType.equals("GooglePay")){
                    binding.number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.googlepay,0,0,0);
                }else if(paymentType.equals("Paytm")){
                    binding.number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.paytm,0,0,0);
                }else if(paymentType.equals("PhonePe")){
                    binding.number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.phonepe,0,0,0);
                }else{
                    binding.number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.questionmark,0,0,0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String generateId(int n) {

            // chose a Character random from this String
            String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "0123456789"
                    + "abcdefghijklmnopqrstuvxyz";

            // create StringBuffer size of AlphaNumericString
            StringBuilder sb = new StringBuilder(n);

            for (int i = 0; i < n; i++) {

                // generate a random number between
                // 0 to AlphaNumericString variable length
                int index
                        = (int)(AlphaNumericString.length()
                        * Math.random());

                // add Character one by one in end of sb
                sb.append(AlphaNumericString
                        .charAt(index));
            }
            return sb.toString();
    }

    private boolean isConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null){
            if(networkInfo.isConnected()){
                return true;
            }else return false;
        }else{
            return false;
        }
    }
}