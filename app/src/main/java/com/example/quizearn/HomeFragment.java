package com.example.quizearn;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizearn.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentHomeBinding binding;
    FirebaseFirestore database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater,container,false);
        final ArrayList<CategoryModel> categories = new ArrayList<>();

//        categories.add(new CategoryModel("", "Mathematics", "https://img.freepik.com/premium-vector/math-icon-with-formular-tools_1639-26099.jpg"));
//        categories.add(new CategoryModel("", "Science", "https://img.freepik.com/premium-vector/math-icon-with-formular-tools_1639-26099.jpg"));
//        categories.add(new CategoryModel("", "History", "https://img.freepik.com/premium-vector/math-icon-with-formular-tools_1639-26099.jpg"));
//        categories.add(new CategoryModel("", "Language", "https://img.freepik.com/premium-vector/math-icon-with-formular-tools_1639-26099.jpg"));

        final CategoryAdapter adapter = new CategoryAdapter(getContext(),categories);

        database = FirebaseFirestore.getInstance();
        database.collection("Categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {  //Automatic update data in UI
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        categories.clear(); // when create category then all previous categories will be cleared.
                        for(DocumentSnapshot snapshot : value.getDocuments()){
                            CategoryModel model = snapshot.toObject(CategoryModel.class); // categoryName,categoryImage change in CategoryModel then show image and name in UI.
                            model.setCategoryId(snapshot.getId()); //Unique category id set in firestore Database
                            categories.add(model);
                        }
                        adapter.notifyDataSetChanged(); // Data changed  in firestore then update in UI.
                    }
                });
        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.categoryList.setAdapter(adapter);

        binding.spinWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("Aman","Found");
                startActivity(new Intent(getContext(),SpinnerActivity.class));
            }
        });

        //invite Friends
        binding.inviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"Play Games & Earn Money QuizEarn App-");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent,null);
                startActivity(shareIntent);
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}