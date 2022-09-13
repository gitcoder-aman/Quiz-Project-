package com.example.coderamankumarguptaquizearn;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.coderamankumarguptaquizearn.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //update Profile function

    FragmentProfileBinding binding;
    FirebaseFirestore database;
    FirebaseAuth auth;
    UserDatabase userdatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater,container,false);
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        database
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userdatabase = documentSnapshot.toObject(UserDatabase.class);
                        binding.profileName.setText(String.valueOf(userdatabase.getName()));
                    }
                });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();

                Toast.makeText(getContext(), "Logged Out Successful", Toast.LENGTH_SHORT).show();
            }
        });
        binding.privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),privacy.class);
                startActivity(intent);
            }
        });

        binding.termConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),termCondition.class);
                startActivity(intent);
            }
        });
        binding.facebookId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri location = Uri.parse("https://www.facebook.com/coder.amankumargupta");
                Intent intent = new Intent(Intent.ACTION_VIEW, location);

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "Link failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] strTo = { "amankumar93578@gmail.com" };
                intent.putExtra(Intent.EXTRA_EMAIL, strTo);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "Body");
                intent.setType("message/rfc822");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);

            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}