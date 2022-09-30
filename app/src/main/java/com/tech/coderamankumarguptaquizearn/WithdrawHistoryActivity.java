package com.tech.coderamankumarguptaquizearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coderamankumarguptaquizearn.R;
import com.example.coderamankumarguptaquizearn.databinding.ActivityWithdrawHistoryBinding;
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
                            Toast toast = Toast.makeText(WithdrawHistoryActivity.this, "No Withdraw Request", Toast.LENGTH_LONG);
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
                        }else{
                            adapter.notifyDataSetChanged();
                        }
                        
                        binding.progressId.setVisibility(View.GONE);
                    }
                });
    }
}