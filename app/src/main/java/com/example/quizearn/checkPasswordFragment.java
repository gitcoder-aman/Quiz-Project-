package com.example.quizearn;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCheckPasswordBinding.inflate(inflater,container,false);
        database = FirebaseFirestore.getInstance();

        binding.checkPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String EnteredPassword = binding.checkPasswordBox.getText().toString();

                database.collection("users")
                        .document(FirebaseAuth.getInstance().getUid())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                userdatabase = documentSnapshot.toObject(UserDatabase.class);
                                String  originalPassword = userdatabase.getPass();

                                //Check entered Password from original password
                                if(originalPassword.equals(EnteredPassword)){
                                    FragmentManager fragmentManager = getParentFragmentManager();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.replace(R.id.content,new ProfileFragment());
                                    transaction.commit();

                                }else if(EnteredPassword.equals("")){
                                    Toast.makeText(getContext(), "Enter Your Password", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getActivity(), "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}