package com.example.coderamankumarguptaquizearn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coderamankumarguptaquizearn.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    
    FirebaseAuth auth;
    FirebaseFirestore database;
    ProgressDialog dialog;
    UserDatabase userdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("We're creating new account...");

//        String referShareCode = binding.referBox.getText().toString();
        binding.createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,pass,name,referShareCode;
                
                email = binding.emailBox.getText().toString();
                pass = binding.passwordBox.getText().toString();
                name = binding.nameBox.getText().toString();
                referShareCode = binding.referBox.getText().toString();

                if(name.equals("")) {
                    Toast.makeText(SignupActivity.this, "Please Enter a Name", Toast.LENGTH_SHORT).show();
                    return;
                }else if(email.equals("")){
                    Toast.makeText(SignupActivity.this, "Please Enter an Email", Toast.LENGTH_SHORT).show();
                    return;
                }else if(pass.equals("")){
                    Toast.makeText(SignupActivity.this, "Please Enter a Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                String referCode = referCodeGenerate(6);
//                uniqueReferCoderCheck(referCode);
//                newUserEarn(referShareCode);
                

                final UserDatabase user = new UserDatabase(name,email,pass,referCode);

                    dialog.show();
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = task.getResult().getUser().getUid();
                                database
                                        .collection("users")
                                        .document(uid)
                                        .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                dialog.dismiss();
                                                if (task.isSuccessful()) {
                                                Toast.makeText(SignupActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                    newUserEarn(referShareCode);
                                                    finish();
                                                } else {
                                                    Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                dialog.dismiss();
                                Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
            }
        });


        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });

    }
    
//    private void uniqueReferCoderCheck(String referCode) {
//
//        database.collection("users")
//                .orderBy("coins", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        Toast.makeText(SignupActivity.this, "enter onSuccess", Toast.LENGTH_SHORT).show();
//                        int flag = 0;
//                        for(DocumentSnapshot snapshot : queryDocumentSnapshots){
//
//                            UserDatabase user = snapshot.toObject(UserDatabase.class);
//                            if(user.getReferCode().equals(referCode)){
//                                flag = 1;
//                                Toast.makeText(SignupActivity.this, "not unique", Toast.LENGTH_SHORT).show();
//                                String referCode = referCodeGenerate(6);
//                            }
//                            else{
//                                Toast.makeText(SignupActivity.this, "Unique referCode", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        if(flag == 0){
//                            Toast.makeText(SignupActivity.this, "Invalid Referral code!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

    //find the existing referral code
    private void newUserEarn(String referShareCode) {

        database.collection("users")
//                .document("referCode",Query.Direction.valueOf(referShareCode)).get()
                .orderBy("coins", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        int flag = 0;
                        for(DocumentSnapshot snapshot : queryDocumentSnapshots){

                            UserDatabase user = snapshot.toObject(UserDatabase.class);

                            if(user.getReferCode().equals(referShareCode)){
                                flag = 1;
                                database
                                        .collection("users")
                                        .document(FirebaseAuth.getInstance().getUid()) //find userId current user
                                        .update("coins", FieldValue.increment(500)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                Toast.makeText(SignupActivity.this, "Referral 500 Coins added in account.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                        if(flag == 0 && !referShareCode.equals("")){
                            Toast.makeText(SignupActivity.this, "Invalid Referral code!", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

    String referCodeGenerate(int n){

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
}