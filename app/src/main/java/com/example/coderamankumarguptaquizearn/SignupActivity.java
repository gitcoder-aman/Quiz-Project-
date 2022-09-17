package com.example.coderamankumarguptaquizearn;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    
    FirebaseAuth auth;
    FirebaseFirestore database;
    ProgressDialog dialog;
    UserDatabase userdatabase;
    DatabaseReference reference;

//    final FirebaseUser fUser = auth.getCurrentUser();
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

//        String referShareCode = binding.referBox.getText().toString();
        binding.createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,pass,name,referShareCode;
                
                email = binding.emailBox.getText().toString();
                pass = binding.passwordBox.getText().toString();
                name = binding.nameBox.getText().toString();
                referShareCode = binding.referBox.getText().toString();

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

                String referCode = referCodeGenerate(6);

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
////                                 referCode = referCodeGenerate(6);
//                            }
//                            else{
//                                Toast.makeText(SignupActivity.this, "Unique referCode", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        if(flag == 1){
//
//                            Toast.makeText(SignupActivity.this, "Invalid Referral code!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

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

                                Toast.makeText(SignupActivity.this, user.getUid(), Toast.LENGTH_SHORT).show();
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

    FirebaseUser user;
    String oppositeUID;
    private void redeemQuery(String referShareCode) {

        Toast.makeText(SignupActivity.this, "Enter ", Toast.LENGTH_SHORT).show();
        com.google.firebase.database.Query query = reference.orderByChild("referCode").equalTo(referShareCode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(SignupActivity.this, "Enter Data", Toast.LENGTH_SHORT).show();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    oppositeUID = dataSnapshot.getKey();

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String opposite = snapshot.child(oppositeUID).child("coins").getValue().toString();
                            int currentCoins = Integer.parseInt(opposite);
                            int updateCoins = currentCoins + 500;

                            String my = snapshot.child(user.getUid()).child("coins").getValue().toString();
                            int myCurrentCoins = Integer.parseInt(opposite);
                            int myUpdateCoins = myCurrentCoins + 500;

                            HashMap<String,Object> map = new HashMap<>();
                            map.put("coins",updateCoins);

                            HashMap<String,Object>myMap = new HashMap<>();
                            myMap.put("coins",myUpdateCoins);

                            reference.child(oppositeUID).updateChildren(map);
                            reference.child(user.getUid()).updateChildren(myMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isComplete()){
                                        Toast.makeText(SignupActivity.this, "Congrates", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SignupActivity.this, "Error2", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignupActivity.this, "Error3", Toast.LENGTH_SHORT).show();
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