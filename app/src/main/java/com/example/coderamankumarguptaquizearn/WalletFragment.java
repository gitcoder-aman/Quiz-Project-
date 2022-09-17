package com.example.coderamankumarguptaquizearn;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.coderamankumarguptaquizearn.databinding.FragmentWalletBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

                       //binding.currentCoins.setText(user.getCoins() + " "); you can also write this.
                   }
               });
       //send button request process

        binding.sendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String paytm = binding.paytmEmailBox.getText().toString();
                if(TextUtils.isEmpty(paytm)){
                    binding.paytmEmailBox.setError("*");
                   return;
                }
               else{
                    binding.paytmEmailBox.setError(null);
                    binding.paytmEmailBox.clearFocus();
                }
                if(userdatabase.getCoins() >= 50000){
                    String uid = FirebaseAuth.getInstance().getUid();
                    WithdrawRequest request = new WithdrawRequest(uid,paytm,userdatabase.getName());
                    database
                            .collection("withdraw")
                            .document(FirebaseAuth.getInstance().getUid())
                            .set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(), "Request sent successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    Toast.makeText(getContext(), "You need more coins to get withdraw.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }
}