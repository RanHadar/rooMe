package com.example.roome.Apartment_searcher_tabs_classes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.roome.ChoosingActivity;
import com.example.roome.FirebaseMediate;
import com.example.roome.MyPreferences;
import com.example.roome.ApartmentSearcherOnBoardDialogActivity;
import com.example.roome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApartmentSearcherHome extends Fragment {

    private ArrayList<String> relevantRoommateSearchersIds;
    private ArrayList<Integer> temp_img;
    private int currentPlaceInList = -1;
    private ImageView yesButton, maybeButton, noButton;
    private ImageView mainImage;
    private TextView noMoreHousesText;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabaseReference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = mFirebaseDatabase.getReference();
        boolean isFirstTime = MyPreferences.isFirstTime(getContext());
        if (isFirstTime) {
            showWelcomeOnBoardDialog();
            MyPreferences.setIsFirstTimeToFalse(getContext());
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_apartment_searcher_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mainImage = getView().findViewById(R.id.iv_home_display);
        yesButton = getView().findViewById(R.id.btn_yes_house);
        maybeButton = getView().findViewById(R.id.btn_maybe_house);
        noButton = getView().findViewById(R.id.btn_no_house);
        noMoreHousesText = getView().findViewById(R.id.tv_no_more_houses);
        setClickListeners();
        setFirebaseListeners();
        retrieveRelevantRoommateSearchers();
        moreHouses();
        super.onActivityCreated(savedInstanceState);
    }

    private void fillTempImgArray() {

        temp_img = new ArrayList<Integer>();
        for (int i = 0; i < relevantRoommateSearchersIds.size(); i++) {
            temp_img.add(R.drawable.home_example1);
            temp_img.add(R.drawable.home_example2);
            temp_img.add(R.drawable.home_example3);
        }
        temp_img = new ArrayList<Integer>(temp_img.subList(0,
                relevantRoommateSearchersIds.size()));
    }

    private void retrieveRelevantRoommateSearchers() {
        relevantRoommateSearchersIds =
                FirebaseMediate.getAptPrefList(ChoosingActivity.NOT_SEEN,
                        MyPreferences.getUserUid(getContext()));
        ArrayList<String> allMaybeUid = FirebaseMediate.getAptPrefList(ChoosingActivity.MAYBE_TO_HOUSE,
                MyPreferences.getUserUid(getContext()));

        relevantRoommateSearchersIds.addAll(
                FirebaseMediate.getAptPrefList(ChoosingActivity.MAYBE_TO_HOUSE,
                        MyPreferences.getUserUid(getContext())));

    }

    private void setClickListeners() {
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressedYesToApartment(view);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressedNoToApartment(view);
            }
        });
        maybeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressedMaybeToApartment(view);
            }
        });

    }

    private String getUserUid() {
        return MyPreferences.getUserUid(getContext());
    }

    private boolean isMatch(String aptUid, String roommateUid) {
        //todo this function returns if theres a match between users
        return true; //todo delete
    }

    private void setFirebaseListeners() {
        mFirebaseDatabaseReference.child("users").child("RoommateSearcherUser").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                onChildChanged(dataSnapshot, s);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String roommateKey = dataSnapshot.getKey();
                String aUserKey = getUserUid();
                String inWhichList =
                        FirebaseMediate.RoommateInApartmentSearcherPrefsList(aUserKey, roommateKey);
                if (inWhichList.equals(ChoosingActivity.NOT_IN_LISTS)) {
                    if (isMatch(aUserKey, roommateKey)) {
                        //if theres a match - add to the havent seen list of
                        // AptUser
                        FirebaseMediate.addToAptPrefList(ChoosingActivity.NOT_SEEN, aUserKey, roommateKey);
                    }
                } else {
                    if (!isMatch(aUserKey, roommateKey)) {
                        //if there is !!no!! match -> remove roommatekey from apt prefs list
                        FirebaseMediate.removeFromAptPrefList(inWhichList,
                                aUserKey, roommateKey);
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String roommateKey = dataSnapshot.getKey();
                String aUserKey = getUserUid();
                String inWhichList =
                        FirebaseMediate.RoommateInApartmentSearcherPrefsList(aUserKey, roommateKey);
                if (!inWhichList.equals(ChoosingActivity.NOT_IN_LISTS)) {
                    //remove from the list
                    FirebaseMediate.removeFromAptPrefList(inWhichList,
                            aUserKey, roommateKey);
                    //todo we need to remember to update the apt lists when
                    // he is offline
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mFirebaseDatabaseReference.child("preferences").child(
                "ApartmentSearcherUser").child(getUserUid()).child(ChoosingActivity.NOT_SEEN).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
                };
                relevantRoommateSearchersIds = dataSnapshot.getValue(t);
                if (relevantRoommateSearchersIds == null) {
                    relevantRoommateSearchersIds = new ArrayList<>();
                }
                refreshList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void refreshList() {
        moreHouses();
    }

    private void updateMainImage() {
        //todo add all this
//        RoommateSearcherUser currentRoomateSearcher = relevantRoommateSearchers.get(currentPlaceInList);
//        Apartment cur_apt = currentRoomateSearcher.getApartment();
//        Image mainImg = cur_apt.getMainImage();
        //todo update the imageview (iv_display_house) to mainImg
        mainImage.setImageResource(R.drawable.home_example1); //todo delete this
    }


    private void removeFromHaveNotSeen(String roommateUid) {
        FirebaseMediate.removeFromAptPrefList(ChoosingActivity.NOT_SEEN,
                getUserUid(), roommateUid);
    }

    public void pressedYesToApartment(View view) {
        String likedRoomateId = relevantRoommateSearchersIds.get(currentPlaceInList);
        String myUid = getUserUid();
        removeFromHaveNotSeen(likedRoomateId);
        FirebaseMediate.addToAptPrefList(ChoosingActivity.YES_TO_HOUSE,
                myUid, likedRoomateId);
        FirebaseMediate.addToRoommatePrefList(ChoosingActivity.NOT_SEEN,
                likedRoomateId, myUid);
        moveToNextOption();
    }

    public void pressedNoToApartment(View view) {

        String unlikedRoommateId =
                relevantRoommateSearchersIds.get(currentPlaceInList);
        removeFromHaveNotSeen(unlikedRoommateId);
        FirebaseMediate.addToAptPrefList(ChoosingActivity.NO_TO_HOUSE,
                getUserUid(), unlikedRoommateId);
        moveToNextOption();
    }

    public void pressedMaybeToApartment(View view) {

        String maybeRoommateId =
                relevantRoommateSearchersIds.get(currentPlaceInList);
        removeFromHaveNotSeen(maybeRoommateId);
        FirebaseMediate.addToAptPrefList(ChoosingActivity.MAYBE_TO_HOUSE,
                getUserUid(), maybeRoommateId);
        moveToNextOption();
    }

    private void moveToNextOption() { //todo delete , undelete the function below
        currentPlaceInList++;
        if (currentPlaceInList < temp_img.size()) {
            Toast.makeText(getActivity(), Integer.toString(currentPlaceInList), Toast.LENGTH_SHORT).show();
            mainImage.setImageResource(temp_img.get(currentPlaceInList));
        } else {
            noMoreHouses();
        }
    }
//    private void moveToNextOption() {
//        //todo check boundries of array
//        currentPlaceInList++;
//        if (currentPlaceInList < relevantRoommateSearchers.size()) {
//            Toast.makeText(getActivity(), Integer.toString(currentPlaceInList), Toast.LENGTH_SHORT).show();
//            updateMainImage();
//        } else {
//            noMoreHouses();
//        }
//    }

    private void noMoreHouses() {
        yesButton.setVisibility(View.INVISIBLE);
        noButton.setVisibility(View.INVISIBLE);
        maybeButton.setVisibility(View.INVISIBLE);
        mainImage.setImageResource(R.drawable.no_more_houses_2);
        noMoreHousesText.setVisibility(View.VISIBLE);
    }

    private void moreHouses() {

        yesButton.setVisibility(View.VISIBLE);
        noButton.setVisibility(View.VISIBLE);
        maybeButton.setVisibility(View.VISIBLE);
        noMoreHousesText.setVisibility(View.INVISIBLE);
        currentPlaceInList = -1;
        fillTempImgArray(); //todo delete
        moveToNextOption();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            // todo if we want to refresh when page is uploaded - do it here
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * This method opens the Apartment Searcher On Board Dialog Activity.
     */
    void showWelcomeOnBoardDialog() {
        Intent intent = new Intent(ApartmentSearcherHome.this.getActivity(), ApartmentSearcherOnBoardDialogActivity.class);
        startActivity(intent);
    }
}
