package com.tech.coderamankumarguptaquizearn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.coderamankumarguptaquizearn.R;
import com.example.coderamankumarguptaquizearn.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import me.ibrahimsn.lib.OnItemSelectedListener;
import com.google.android.play.core.tasks.OnCompleteListener;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private ReviewInfo reviewInfo;
    private ReviewManager manager;
    public static int UPDATE_CODE = 22;
    AppUpdateManager appUpdateManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // No need to findViewId because viewbinding is true in gradle app
        setContentView(binding.getRoot());
        activateReviewInfo();
        InAppUpdate();
        //Initialize  ads first

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });

//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adView.loadAd(adRequest);

//        binding.adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                binding.adView.loadAd(adRequest);
//            }
//        });

        //Log.d("Aman","Here error found");

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

    private void InAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);
        Task<AppUpdateInfo> task = appUpdateManager.getAppUpdateInfo();
        task.addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                 if(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                     try {
                         appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE,MainActivity.this,UPDATE_CODE);
                     } catch (IntentSender.SendIntentException e) {
                         e.printStackTrace();
                         Log.d("UpdateError","OnSuccess: "+ e.toString());
                     }
                 }
            }
        });
//        appUpdateManager.registerListener(listener); //install state update listner
    }
    InstallStateUpdatedListener listener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(@NonNull InstallState installState) {
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                MainActivity.this.popUp(); // show completed update
            }
        }
    };

    private void popUp() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"App Update Almost Done.",Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Task<AppUpdateInfo> task = appUpdateManager.getAppUpdateInfo();
        task.addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if(appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE,MainActivity.this,UPDATE_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                        Log.d("UpdateError","OnSuccess: "+ e.toString());
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPDATE_CODE){
            Toast.makeText(this, "Start Download", Toast.LENGTH_SHORT).show();

            if(requestCode != RESULT_OK){
                Log.d("update","Update flow failed "+resultCode);
            }
        }
    }

    void activateReviewInfo(){
        manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> managerInfoTask = manager.requestReviewFlow();
        managerInfoTask.addOnCompleteListener((task)->{
            if(task.isSuccessful()){
               reviewInfo = task.getResult();
            }else{
                Toast.makeText(this, "Review failed to start", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void startReviewFlow(){
        if(reviewInfo != null){
            Task<Void>flow =  manager.launchReviewFlow(this,reviewInfo);
            flow.addOnCompleteListener(task -> {
                Toast.makeText(this, "Rating is completed.", Toast.LENGTH_SHORT).show();
            });
        }
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
                                    "Click the link，you can get ₹500 a week like me! YOUR REFERRAL CODE=" +userdatabase.getReferCode()+"\n"+
                                    "https://play.google.com/store/apps/details?id=" + getPackageName());
                            sendIntent.setType("text/plain");

                            Intent shareIntent = Intent.createChooser(sendIntent, null);
                            startActivity(shareIntent);
                        }
                    });
        }
        auth = FirebaseAuth.getInstance();
        if(item.getItemId() == R.id.logout) {

            SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME,0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hasLoggedIn",false);
            editor.commit();

            auth.signOut();
            Toast.makeText(MainActivity.this, "Logged Out Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
        if(item.getItemId() == R.id.rateUs){
            startReviewFlow();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        //new AlertDialog.Builder(MainActivity.this)
        builder.setIcon(R.drawable.ic_baseline_warning_24);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit this App?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //end the app
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }
                });

//        builder.setPositiveButton(getResources().getDrawable(R.drawable.i))
             builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }
  
}