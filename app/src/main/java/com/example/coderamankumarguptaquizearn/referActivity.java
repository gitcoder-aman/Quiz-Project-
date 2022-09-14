package com.example.coderamankumarguptaquizearn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.coderamankumarguptaquizearn.databinding.ActivityReferBinding;
import com.example.coderamankumarguptaquizearn.databinding.ActivityResultBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class referActivity extends AppCompatActivity {

    ActivityReferBinding binding;
    FirebaseFirestore database;
    UserDatabase userdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();

        //ReferCode findout
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userdatabase = documentSnapshot.toObject(UserDatabase.class);
                        binding.referralBox.setText(String.valueOf(userdatabase.getReferCode()));

                        //binding.currentCoins.setText(user.getCoins() + " "); you can also write this.
                    }
                });
    }
}