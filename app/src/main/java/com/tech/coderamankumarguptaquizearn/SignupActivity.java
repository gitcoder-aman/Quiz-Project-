package com.tech.coderamankumarguptaquizearn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coderamankumarguptaquizearn.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    
    FirebaseAuth auth;
    FirebaseFirestore database;
    ProgressDialog dialog;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("We're creating new account...");

        binding.createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,pass,name,referShareCode;
                
                email = binding.emailBox.getText().toString().trim();
                pass = binding.passwordBox.getText().toString().trim();
                name = binding.nameBox.getText().toString().trim();
                referShareCode = binding.referBox.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                     binding.nameBox.setError("*");
                    return;
                }else{
                    binding.nameBox.setError(null);
                    binding.nameBox.clearFocus();
                }

                 if(TextUtils.isEmpty(email)){
                    binding.emailBox.setError("*");
                    return;
                } else{
                     binding.emailBox.setError(null);
                     binding.emailBox.clearFocus();
                 }

                if(TextUtils.isEmpty(pass)){
                    binding.passwordBox.setError("*");
                    return;
                } else{
                    binding.passwordBox.setError(null);
                    binding.passwordBox.clearFocus();
                }

                String referCode = referCodeGenerate(8);

                    dialog.show();
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                EmailSentForVerify();

                                String uid = task.getResult().getUser().getUid();
                                String UserId = auth.getUid();
                                UserDatabase user = new UserDatabase(name,email,pass,referCode,UserId);
                                database
                                        .collection("users")
                                        .document(uid)
                                        .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                dialog.dismiss();
                                                if (task.isSuccessful()) {
                                                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                        newUserEarn(referShareCode);
                                                        redeemReferralCode(referShareCode);
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

    private void EmailSentForVerify() {

        FirebaseUser fUser = auth.getCurrentUser();
        //send verification link
      fUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
              if(task.isSuccessful()){
                  Toast.makeText(SignupActivity.this, "Verification Email has been Sent", Toast.LENGTH_SHORT).show();
                  binding.emailBox.setText("");
                  binding.passwordBox.setText("");
                  binding.nameBox.setText("");
              }
              else{
                  Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
              }
          }
      });

    }

    //find the existing referral code
    private void newUserEarn(String referShareCode) {

        database.collection("users")
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
                                        .update("coins", FieldValue.increment(5000)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                Toast.makeText(SignupActivity.this, "Referral 5000 Coins added in account.", Toast.LENGTH_SHORT).show();
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

    private void redeemReferralCode(String referShareCode) {

        database
                .collection("users")
                .whereEqualTo("referCode",referShareCode).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots){

                            UserDatabase user = snapshot.toObject(UserDatabase.class);
                            if(user.getReferCode().equals(referShareCode)){
                                String uid = user.getUid();

                                database
                                        .collection("users")
                                        .document(uid)
                                        .update("coins",FieldValue.increment(5000));
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, "Referral Code Invalid !", Toast.LENGTH_SHORT).show();
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