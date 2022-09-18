package com.example.coderamankumarguptaquizearn;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.coderamankumarguptaquizearn.databinding.FragmentWalletBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.net.ssl.SSLEngineResult;

public class WalletFragment extends Fragment {


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
//    String status = "Done";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
       //send button request process


        binding.sendRequestBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (isConnected()) {
                    String Coins = binding.numberOfCoins.getText().toString();
                    String paytm = binding.paytmEmailBox.getText().toString();
                    if (TextUtils.isEmpty(paytm)) {
                        binding.paytmEmailBox.setError("*");
                        return;
                    } else {
                        binding.paytmEmailBox.setError(null);
                        binding.paytmEmailBox.clearFocus();
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
                        String uid = FirebaseAuth.getInstance().getUid();
                        WithdrawRequest request = new WithdrawRequest(uid, paytm, userdatabase.getName(), numberOfCoins,"Pending");

                        String uniqueId = generateId(10);
                        database
                                .collection("withdraw")
                                .document(FirebaseAuth.getInstance().getUid())
                                .collection("History")
                                .document(uniqueId)
                                .set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Request sent successfully", Toast.LENGTH_SHORT).show();
                                        binding.sendRequestBtn.setVisibility(View.INVISIBLE);

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