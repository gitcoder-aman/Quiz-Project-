package com.example.coderamankumarguptaquizearn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coderamankumarguptaquizearn.databinding.ActivityLoginBinding;
import com.example.coderamankumarguptaquizearn.databinding.ActivityWithdrawHistoryBinding;
import com.example.coderamankumarguptaquizearn.databinding.FragmentLeaderboardsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WithdrawHistoryActivity extends AppCompatActivity {

    ActivityWithdrawHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        ArrayList<WithdrawRequest>requests = new ArrayList<>();


        WithdrawHistoryAdapter adapter = new WithdrawHistoryAdapter(this,requests);
        binding.recyclerView1.setAdapter(adapter);

        binding.recyclerView1.setLayoutManager(new LinearLayoutManager(this));


        database.collection("withdraw")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("History")
                .orderBy("createAt", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                            WithdrawRequest request = snapshot.toObject(WithdrawRequest.class);
                            requests.add(request);
                        }
                        if(requests.size() == 0){
                            Toast.makeText(WithdrawHistoryActivity.this, "Anyone No Withdraw ", Toast.LENGTH_SHORT).show();
                        }else{
                            adapter.notifyDataSetChanged();
                        }
                        
                        binding.progressId.setVisibility(View.GONE);
                    }
                });
    }
}