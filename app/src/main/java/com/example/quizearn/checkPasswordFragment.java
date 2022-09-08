package com.example.quizearn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quizearn.databinding.FragmentCheckPasswordBinding;
import com.example.quizearn.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class checkPasswordFragment extends Fragment {

    public checkPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentCheckPasswordBinding binding;
    FirebaseFirestore database;
    UserDatabase userdatabase;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCheckPasswordBinding.inflate(inflater,container,false);
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        FragmentManager fragmentManager = getParentFragmentManager(); // fragment calling
        FragmentTransaction transaction = fragmentManager.beginTransaction(); //fragment

        binding.checkPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.content,new ProfileFragment());
                transaction.commit();

            }
        });

        //Logout Functionality

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getActivity(),LoginActivity.class));
//                ((Activity) getActivity()).overridePendingTransition(1, 1);

            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}