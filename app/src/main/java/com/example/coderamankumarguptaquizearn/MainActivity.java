package com.example.coderamankumarguptaquizearn;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.coderamankumarguptaquizearn.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //Log.d("Aman","Here error found");

        binding = ActivityMainBinding.inflate(getLayoutInflater()); // No need to findViewId because viewbinding is true in gradle app
        setContentView(binding.getRoot());

        //before set in Manifest android:theme = "Theme.AppCompat.Light.NoActionBar"
        setSupportActionBar(binding.toolbar); // set title name "App name " in toolbar

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction(); //already click on Home button
        transaction.replace(R.id.content,new HomeFragment());  //FragmentXML are replaced when clicked bottom Buttons
        transaction.commit();

        binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();  //already click on Home button
                switch(i){
                    case 0:
                        transaction.replace(R.id.content,new HomeFragment());
                        transaction.commit();
                        break;
                    case 1:
                        transaction.replace(R.id.content,new LeaderboardsFragment());
                        transaction.commit();
                        break;
                    case 2:
                        transaction.replace(R.id.content,new WalletFragment());
                        transaction.commit();
                        break;
                    case 3:
                        transaction.replace(R.id.content,new ProfileFragment());
                        transaction.commit();
                        break;
                }
                return false;
            }
        });
    }

    //for wallet image show in home page
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

  //  for when you clicked on wallet image then show toast
    FirebaseAuth auth;
    FirebaseFirestore database;
    UserDatabase userdatabase;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.shareApp) {

            database = FirebaseFirestore.getInstance();

            //ReferCode findout
            database.collection("users")
                    .document(FirebaseAuth.getInstance().getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userdatabase = documentSnapshot.toObject(UserDatabase.class);
                            //binding.currentCoins.setText(user.getCoins() + " "); you can also write this.

                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey Hey Hey!!\uD83D\uDE0D\uD83D\uDE0D\uD83D\uDE0D \n" +
                                    "I'm earning real money in this APP!!\uD83C\uDF39\uD83C\uDF39\uD83C\uDF39 \n" +
                                    "Most popular money making app in India!!!\uD83D\uDC9B\uD83E\uDD0D\uD83D\uDC9A \n" +
                                    "Download APP, everyone can get ₹40!!!\uD83D\uDE3B\uD83D\uDE3B\uD83D\uDE3B \n" +
                                    "It's 100% true! \uD83D\uDE39 \n" +
                                    "Click the link，you can get ₹500 a week like me! YOUR REFERRAL CODE=" +userdatabase.getReferCode());
                            sendIntent.setType("text/plain");

                            Intent shareIntent = Intent.createChooser(sendIntent, null);
                            startActivity(shareIntent);
                        }
                    });
        }
        auth = FirebaseAuth.getInstance();
        if(item.getItemId() == R.id.logout) {

            auth.signOut();
            Toast.makeText(MainActivity.this, "Logged Out Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,splashActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    int counter = 1;
    @Override
    public void onBackPressed() {

        if(counter == 2){
            super.onBackPressed();
            this.finishAffinity();
        }
        else{
            counter++;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        }

    }
  
}