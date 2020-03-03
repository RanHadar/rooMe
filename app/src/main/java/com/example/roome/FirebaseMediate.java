package com.example.roome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.roome.user_classes.ApartmentSearcherUser;
import com.example.roome.user_classes.RoommateSearcherUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is responsible for mediating with the firebase data base.
 */
public class FirebaseMediate {

    /* Firebase instance variables */
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference firebaseDatabaseReference;
    private static FirebaseStorage storage;
    private static StorageReference storageReference;

    private static DataSnapshot dataSs;
    private final static AtomicBoolean fmDone = new AtomicBoolean(false);

    /**
     * This method sets the data snapshot to the current data snapshot.
     * @param dataSnapshot a DataSnapshot object
     */
    public static void setDataSnapshot(@NonNull DataSnapshot dataSnapshot) {
        dataSs = dataSnapshot;
    }

    /**
     * This method sets the data snapshot of current state in firebase data base.
     */
    public static void setDataSnapshot() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        firebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSs = dataSnapshot;
                fmDone.set(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        firebaseDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSs = dataSnapshot;
                fmDone.set(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * This method uploads an image to firebase storage.
     *
     * @param selectedImage - the uri of the image to upload.
     * @param activity      - the activity calling this method.
     * @param context       - the activity context.
     * @param userType      - the users type (RoommateSearcherUser/ApartmentSearcherUser).
     * @param photoType     - the photo type (profilePic/apartmentPic).
     */
    public static void uploadPhotoToStorage(Uri selectedImage, final Activity activity, Context context, String userType, String photoType) {
        if (selectedImage != null) {
            final ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("Images").child(userType).
                    child(MyPreferences.getUserUid(context)).child(photoType);
            ref.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(activity, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(activity, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    /**
     * @return all ApartmentSearcher users ids (from database).
     */
    public static ArrayList<String> getAllApartmentSearcherIds() {
        DataSnapshot dataSnapshotRootUsers = dataSs.child("users");
        ArrayList<String> allAptSearcherUsersIds = new ArrayList<>();
        DataSnapshot refDSS = dataSnapshotRootUsers.child("ApartmentSearcherUser");

        for (DataSnapshot aptS : refDSS.getChildren()) {
            String userAKey = aptS.getKey();
            allAptSearcherUsersIds.add(userAKey);
        }
        return allAptSearcherUsersIds;
    }

    /**
     * This method returns all ApartmentSearcher users.
     * @return all ApartmentSearcher users.
     */
    public static ArrayList<ApartmentSearcherUser> getAllApartmentSearcher() {
        DataSnapshot dataSnapshotRootUsers = dataSs.child("users");
        ArrayList<ApartmentSearcherUser> allAptSearcherUsers = new ArrayList<>();
        DataSnapshot refDSS = dataSnapshotRootUsers.child("ApartmentSearcherUser");

        for (DataSnapshot aptS : refDSS.getChildren()) {
            ApartmentSearcherUser userA = aptS.getValue(ApartmentSearcherUser.class);
            allAptSearcherUsers.add(userA);
        }
        return allAptSearcherUsers;
    }

    /**
     * This method returns all RoommateSearcher users ids (from database).
     *
     * @return all RoommateSearcher users ids (from database).
     */
    public static ArrayList<String> getAllRoommateSearcherIds() {
        DataSnapshot dataSnapshotRootUsers = dataSs.child("users");
        ArrayList<String> allRoommateSearcherUsersIds = new ArrayList<>();
        DataSnapshot refDSS = dataSnapshotRootUsers.child("RoommateSearcherUser");

        for (DataSnapshot roomS : refDSS.getChildren()) {
            String userRKey = roomS.getKey();
            allRoommateSearcherUsersIds.add(userRKey);
        }
        return allRoommateSearcherUsersIds;
    }

    /**
     * This method returns all RoommateSearcher users from database.
     *
     * @return all RoommateSearcher users.
     */
    public static ArrayList<RoommateSearcherUser> getAllRoommateSearcher() {
        DataSnapshot dataSnapshotRootUsers = dataSs.child("users");
        ArrayList<RoommateSearcherUser> allRoommateSearcherUsers = new ArrayList<>();
        DataSnapshot refDSS = dataSnapshotRootUsers.child("RoommateSearcherUser");

        for (DataSnapshot roomS : refDSS.getChildren()) {
            RoommateSearcherUser userR = roomS.getValue(RoommateSearcherUser.class);
            allRoommateSearcherUsers.add(userR);
        }
        return allRoommateSearcherUsers;
    }

    /**
     * This method returns the ApartmentSearcherUser object with data from firebase database.
     *
     * @param aptUid - uid of ApartmentSearcherUser
     * @return the ApartmentSearcherUser object with data from firebase database.
     */
    public static ApartmentSearcherUser getApartmentSearcherUserByUid(String aptUid) {
        DataSnapshot temp = dataSs.child("users").child("ApartmentSearcherUser").child(aptUid);
        return temp.getValue(ApartmentSearcherUser.class);
    }

    /**
     * This method returns the RoommateSearcherUser object with data from firebase database.
     *
     * @param roommateUid - uid of RoommateSearcherUser
     * @return the RoommateSearcherUser object with data from firebase database.
     */
    public static RoommateSearcherUser getRoommateSearcherUserByUid(String roommateUid) {
        DataSnapshot temp = dataSs.child("users").child("RoommateSearcherUser").child(roommateUid);
        return temp.getValue(RoommateSearcherUser.class);
    }

    /**
     * This method returns all RoommateSearcherUsers ids from the relevant list in database.
     *
     * @param list   - the list name to retrieve the data from in data base (not_seen/yes_to_house/maybe_to_house).
     * @param aptUid - uid of ApartmentSearcherUser
     * @return all RoommateSearcherUsers ids from the relevant list in database.
     */
    public static ArrayList<String> getAptPrefList(String list, String aptUid) {
        GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
        };

        ArrayList<String> allRoommateSearcherUsersIds;
        DataSnapshot refDSS =
                dataSs.child("preferences").child("ApartmentSearcherUser").child(aptUid).child(list);
        allRoommateSearcherUsersIds = refDSS.getValue(t);
        if (allRoommateSearcherUsersIds == null) {
            return new ArrayList<>();

        }
        return allRoommateSearcherUsersIds;
    }

    /**
     * This method returns all ApartmentSearcherUsers ids from the relevant list in database.
     *
     * @param list        - the list name to retrieve the data from in data base (not_seen/yes_roommate).
     * @param roommateUid - the roommate user firebase id.
     * @return all ApartmentSearcherUsers ids from the relevant list in database.
     */
    public static ArrayList<String> getRoommatePrefList(String list, String roommateUid) {
        GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
        };

        ArrayList<String> allAptSearcherUsersIds;
        DataSnapshot refDSS =
                dataSs.child("preferences").child("RoommateSearcherUser").child(roommateUid).child(list);
        allAptSearcherUsersIds = refDSS.getValue(t);
        if (allAptSearcherUsersIds == null) {
            return new ArrayList<>();
        }
        return allAptSearcherUsersIds;
    }

    /**
     * This method returns all roommates user in the specified list of the user.
     * not in use for now because we didn't implement the roommate searcher side.
     * @param list        - specified list of the user.
     * @param roommateUid - the roommate user firebase id.
     * @return all roommates user in the specified list of the user.
     */
    public static ArrayList<RoommateSearcherUser> getRoommatesByPrefList(String list,
                                                                         String roommateUid) {
        ArrayList<String> allRoommateSearcherUsersIds = getAptPrefList(list, roommateUid);
        ArrayList<RoommateSearcherUser> allListRoommates = new ArrayList<>();
        for (String roommateId : allRoommateSearcherUsersIds) {
            allListRoommates.add(getRoommateSearcherUserByUid(roommateId));
        }
        return allListRoommates;
    }

    /**
     * This method sets a specified list of an ApartmentSearcherUser (in firebase database).
     *
     * @param list         - the list name to retrieve the data from in data base (not_seen/yes_to_house/maybe_to_house).
     * @param aptUid       - the ApartmentSearcherUser firebase id.
     * @param relevantUids - the new relevant roommates users ids to put in the list in data base.
     */
    public static void setAptPrefList(String list, String aptUid, ArrayList<String> relevantUids) {
        firebaseDatabaseReference.child("preferences").child(
                "ApartmentSearcherUser").child(aptUid).child(list).setValue(relevantUids);
    }

    /**
     * This method sets a specified list of an RoommateSearcherUser (in firebase database).
     *
     * @param list         - the list name to retrieve the data from in data base (not_seen/yes_roommate).
     * @param roommateUid  - the RoommateSearcherUser firebase id.
     * @param relevantUids - the new relevant ApartmentSearcher users ids to put in the list in data base.
     */
    public static void setRoommatePrefList(String list, String roommateUid
            , ArrayList<String> relevantUids) {
        firebaseDatabaseReference.child("preferences").child(
                "RoommateSearcherUser").child(roommateUid).child(list).setValue(relevantUids);
    }

    /**
     * This method returns the list name in wich the RoommateSearcher (roommateUid) is in for the ApartmentSearcherUser lists.
     *
     * @param aptUid      - the ApartmentSearcherUser firebase id.
     * @param roommateUid - the RoommateSearcherUser firebase id.
     * @return the list name in wich the RoommateSearcher (roommateUid) is in for the ApartmentSearcherUser lists.
     */
    public static String RoommateInApartmentSearcherPrefsList(String aptUid,
                                                              String roommateUid) {
        ArrayList<String> likedArr, maybeArr, unlikedArr, notSeenArr;
        likedArr = FirebaseMediate.getAptPrefList(ChoosingActivity.YES_TO_HOUSE,
                aptUid);
        maybeArr = FirebaseMediate.getAptPrefList(ChoosingActivity.MAYBE_TO_HOUSE,
                aptUid);
        unlikedArr = FirebaseMediate.getAptPrefList(ChoosingActivity.NO_TO_HOUSE,
                aptUid);
        notSeenArr = FirebaseMediate.getAptPrefList(ChoosingActivity.NOT_SEEN,
                aptUid);
        if (likedArr.contains(roommateUid)) {
            return ChoosingActivity.YES_TO_HOUSE;
        }
        if (maybeArr.contains(roommateUid)) {
            return ChoosingActivity.MAYBE_TO_HOUSE;
        }
        if (unlikedArr.contains(roommateUid)) {
            return ChoosingActivity.NO_TO_HOUSE;
        }
        if (notSeenArr.contains(roommateUid)) {
            return ChoosingActivity.NOT_SEEN;
        }
        return ChoosingActivity.NOT_IN_LISTS;
    }

    /**
     * This method removes a RoommateSearcherUser from the specified list in firebase database.
     *
     * @param list        - the list name to retrieve the data from in data base (not_seen/yes_to_house/maybe_to_house).
     * @param aptUid      - the ApartmentSearcherUser firebase id.
     * @param roommateUid - the RoommateSearcherUser firebase id.
     */
    public static void removeFromAptPrefList(String list, String aptUid,
                                             String roommateUid) {

        ArrayList<String> prefListRoommates = getAptPrefList(list, aptUid);
        prefListRoommates.remove(roommateUid);
        setAptPrefList(list, aptUid, prefListRoommates);
    }

    /**
     * This method removes a RoommateSearcherUser from the specified list in firebase database.
     *
     * @param list        - the list name to retrieve the data from in data base (not_seen/yes_to_roommate).
     * @param roommateUid - the RoommateSearcherUser firebase id.
     * @param aptUid      - the ApartmentSearcherUser firebase id.
     */
    public static void removeFromRoommatePrefList(String list,
                                                  String roommateUid,
                                                  String aptUid) {

        ArrayList<String> prefListApt = getRoommatePrefList(list, roommateUid);
        prefListApt.remove(aptUid);
        setRoommatePrefList(list, roommateUid, prefListApt);
    }

    /**
     * This method adds a RoommateSearcherUser to a specified list in firebase database.
     *
     * @param list        - the list name to retrieve the data from in data base (not_seen/yes_to_house/maybe_to_house).
     * @param aptUid      - the ApartmentSearcherUser firebase id (that needs to add a RoommateSearcherUser).
     * @param roommateUid - the RoommateSearcherUser firebase id to add.
     */
    public static void addToAptPrefList(String list, String aptUid,
                                        String roommateUid) {
        ArrayList<String> allRelevantRoommatesUid = getAptPrefList(list, aptUid);
        allRelevantRoommatesUid.add(roommateUid);
        setAptPrefList(list, aptUid, allRelevantRoommatesUid);
    }

    /**
     * This method adds a ApartmentSearcherUser to a specified list in firebase database.
     *
     * @param list        - the list name to retrieve the data from in data base (not_seen/yes_to_house/maybe_to_house).
     * @param roommateUid - the RoommateSearcherUser firebase id (that needs to add a ApartmentSearcherUser).
     * @param aptUid      - the ApartmentSearcherUser firebase id to add.
     */
    public static void addToRoommatePrefList(String list, String roommateUid,
                                             String aptUid) {
        ArrayList<String> allRelevantAptUid = getRoommatePrefList(list,
                roommateUid);
        allRelevantAptUid.add(aptUid);
        setRoommatePrefList(list, roommateUid, allRelevantAptUid);
    }
}
