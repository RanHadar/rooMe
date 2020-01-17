package com.example.roome;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * this activity is presenting the app logo for a few seconds and then disappears.
 * this activity will be shown on every app visit.
 */
public class MainActivity extends AppCompatActivity {
    private static final String FROM = "called from";
    private static final String MAIN_SRC = "MAIN";
    protected static final int MIN_SUPPORTED_API_LEVEL = 20;
    //Time passed till next activity is launched
    private static final int TIME_OUT = 3000;

    private String userName;
    private String mPhotoUrl;

    // Firebase instance variables
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        FirebaseMediate.setDataSnapshot();
        Data.initD();
        //todo delete 4 rows
//        final SharedPreferences reader = getApplicationContext().getSharedPreferences(MyPreferences.MY_PREFERENCES, Context.MODE_PRIVATE);
//        final SharedPreferences.Editor editor = reader.edit();
//        editor.putBoolean(MyPreferences.IS_FIRST_TIME, true);
//        editor.apply();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (firebaseUser == null) { //todo check if signed up differently?
//                    // Not signed in, launch the Sign In activity
//                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
//                    finish();
//                    return;
//                } else {
//                    userName = firebaseUser.getDisplayName();
//                    if (firebaseUser.getPhotoUrl() != null) {
//                        mPhotoUrl = firebaseUser.getPhotoUrl().toString();
//                    }
//                }  //todo uncomment this
                startActivityWithAnimation();//todo maybe go to intermediate
                //activity(whose main purpose is to update the data snapshot)

                finish();
            }
        }, TIME_OUT);
    }


    //-------------------only sign in with google-------------------------------------------
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        final SharedPreferences reader = getApplicationContext().getSharedPreferences(MyPreferences.MY_PREFERENCES, Context.MODE_PRIVATE);
//        final SharedPreferences.Editor editor = reader.edit();
//        editor.putBoolean(MyPreferences.IS_FIRST_TIME, true);
//        editor.apply();
//        //Time passed till next activity is launched
//        int TIME_OUT = 3000;
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Initialize Firebase Auth
//                mFirebaseAuth = FirebaseAuth.getInstance();
//                mFirebaseUser = mFirebaseAuth.getCurrentUser();
//                if (mFirebaseUser == null) {
//                    // Not signed in, launch the Sign In activity
//                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
//                    finish();
//                    return;
//                } else {
//                    mUsername = mFirebaseUser.getDisplayName();
//                    if (mFirebaseUser.getPhotoUrl() != null) {
//                        mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
//                    }
//                }
//                Intent i;
//                boolean isFirstTime = MyPreferences.isFirstTime(MainActivity.this);
//                if (isFirstTime) {
//                    //show start activity
//                    i = new Intent(MainActivity.this, ChoosingActivity.class);
//                } else {
//                    boolean isRoommateSearcher = MyPreferences.isRoommateSearcher(MainActivity.this); //todo change activity
//                    if (isRoommateSearcher) {
//                        i = new Intent(MainActivity.this, ChoosingActivity.class);//todo change activity
//                    } else {
//                        i = new Intent(MainActivity.this, ChoosingActivity.class);
//
//                    }
//                    i.putExtra(MainActivity.FROM, MAIN_SRC);
//                }
//                startActivity(i);
//                finish();
//            }
//        }, TIME_OUT);
//    }

    /**
     * This method starts a new activity and adding the transition animation for relevant versions
     * of android
     */
    public void startActivityWithAnimation() {
        Intent i;
        boolean isFirstTime = MyPreferences.isFirstTime(MainActivity.this);
        if (isFirstTime) {
            //show home activity
            i = new Intent(MainActivity.this, ChoosingActivity.class);
        } else {
            boolean isRoommateSearcher = MyPreferences.isRoommateSearcher(MainActivity.this);
            if (isRoommateSearcher) {
                i = new Intent(MainActivity.this, MainActivityRoommateSearcher.class);
            } else {
                i = new Intent(MainActivity.this, MainActivityApartmentSearcher.class);

            }
            i.putExtra(MainActivity.FROM, MAIN_SRC);
        }
        if (Build.VERSION.SDK_INT > MIN_SUPPORTED_API_LEVEL) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(i, options.toBundle());
        } else {
            startActivity(i);
        }
    }
}
