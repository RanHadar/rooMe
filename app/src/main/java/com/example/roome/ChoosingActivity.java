package com.example.roome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roome.user_classes.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChoosingActivity extends AppCompatActivity {

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabaseReference;
    final ArrayList<String>[] allApartmentSearcherIds = new ArrayList[1];
    final ArrayList<String>[] allRoommateSearcherIds = new ArrayList[1];
    final AtomicBoolean done = new AtomicBoolean(false);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing);

        updateUserName();
        // Initialize Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabaseReference = mFirebaseDatabase.getReference();

        mFirebaseDatabaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allApartmentSearcherIds[0] = FirebaseMediate.getAllApartmentSearcherIds(dataSnapshot);
                allRoommateSearcherIds[0] = FirebaseMediate.getAllRoommateSearcherIds(dataSnapshot);
                done.set(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * The function displays the user's name (from which it got from the login) in this
     * activity
     */
    private void updateUserName() {//todo
        String userName = "";
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ChoosingActivity.this);
        if (mFirebaseUser != null) {
            userName = mFirebaseUser.getDisplayName();
        } else if (acct != null) {
            userName = acct.getDisplayName();
        }
        TextView textView = findViewById(R.id.tv_hello_name);
        textView.setText(String.format("Hi %s!", userName));
    }

    //add a toast to show when successfully signed in

    /**
     * customizable toast
     *
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void roommateSearcherOnclick(View view) throws InterruptedException {
        User userObj = createNewUser();
        mFirebaseDatabaseReference.child("users").child("RoommateSearcherUser").child(mFirebaseUser.getUid()).setValue(userObj);
        toastMessage("Adding " + mFirebaseUser.getDisplayName() + " to database..."); //todo remove
        while (!done.get());
        mFirebaseDatabaseReference.child("preferences").child("RoommateSearcherUser").child(mFirebaseUser.getUid()).child("0").setValue(allApartmentSearcherIds[0]);
        Intent i = new Intent(ChoosingActivity.this, MainActivityRoommateSearcher.class);
        startActivity(i);
        finish();

    }

    public void apartmentSearcherOnclick(View view) throws InterruptedException {
        User userObj = createNewUser();
        mFirebaseDatabaseReference.child("users").child("ApartmentSearcherUser").child(mFirebaseUser.getUid()).setValue(userObj);
        while (!done.get());
        mFirebaseDatabaseReference.child("preferences").child("ApartmentSearcherUser").child(mFirebaseUser.getUid()).child("0").setValue(allRoommateSearcherIds);
        toastMessage("Adding " + mFirebaseUser.getDisplayName() + " to database..."); //todo remove
        Intent i = new Intent(ChoosingActivity.this, MainActivityApartmentSearcher.class);
        startActivity(i);
        finish();
    }

    private User createNewUser() {
        GoogleSignInAccount userAccount = GoogleSignIn.getLastSignedInAccount(ChoosingActivity.this);
        String firstName = userAccount.getGivenName();
        String lastName = userAccount.getFamilyName();
        return new User(firstName, lastName);
    }
}
