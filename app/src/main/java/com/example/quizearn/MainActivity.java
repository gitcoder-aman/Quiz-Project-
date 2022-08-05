package com.example.quizearn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.quizearn.databinding.ActivityMainBinding;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //Log.d("Aman","Here error found");

        binding = ActivityMainBinding.inflate(getLayoutInflater()); // No need to findViewId because viewbinding is true in gradle app
        setContentView(binding.getRoot());

        //setSupportActionBar(binding.toolbar); // set title name "App name " in toolbar

        database = FirebaseFirestore.getInstance();
        ArrayList<CategoryModel>categories = new ArrayList<>();

//        categories.add(new CategoryModel("", "Mathematics", "https://img.freepik.com/premium-vector/math-icon-with-formular-tools_1639-26099.jpg"));
//        categories.add(new CategoryModel("", "Science", "https://img.freepik.com/premium-vector/math-icon-with-formular-tools_1639-26099.jpg"));
//        categories.add(new CategoryModel("", "History", "https://img.freepik.com/premium-vector/math-icon-with-formular-tools_1639-26099.jpg"));
//        categories.add(new CategoryModel("", "Language", "https://img.freepik.com/premium-vector/math-icon-with-formular-tools_1639-26099.jpg"));

        CategoryAdapter adapter = new CategoryAdapter(this,categories);

        database.collection("Categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {  //Automatic update data in UI
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    }
                });
        binding.categoryList.setLayoutManager(new GridLayoutManager(this,2));
        binding.categoryList.setAdapter(adapter);
    }

    //for wallet image show in home page
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //for when you clicked on wallet image then show toast
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.wallet)
            Toast.makeText(this, "Wallet is Clicked", Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }
}