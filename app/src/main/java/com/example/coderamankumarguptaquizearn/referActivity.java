package com.example.coderamankumarguptaquizearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.coderamankumarguptaquizearn.databinding.ActivityReferBinding;
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

        binding.referralBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)referActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(binding.referralBox.getText().toString());
                Toast.makeText(referActivity.this, "Copied to Referral Code", Toast.LENGTH_SHORT).show();
            }
        });
        binding.invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseFirestore.getInstance();

                //ReferCode findout
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
                                        "Download APP, everyone can get ₹40!!!\uD83D\uDE3B\uD83D\uDE3B\uD83D\uDE3B \n" +
                                        "It's 100% true! \uD83D\uDE39 \n" +
                                        "Click the link，you can get ₹500 a week like me! YOUR REFERRAL CODE=" +userdatabase.getReferCode());
                                sendIntent.setType("text/plain");

                                Intent shareIntent = Intent.createChooser(sendIntent, null);
                                startActivity(shareIntent);
                            }
                        });
            }
        });
    }
}