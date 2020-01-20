package com.example.roome.user_classes;

import java.util.ArrayList;

public class ApartmentSearcherUser extends User {

    //--------------------profile info---------------------
    private String bio; //where the user can write about himself

    //---------------------------------------------filters------------------------------------------
    private ArrayList<String> optionalNeighborhoods;
    private int minRent;
    private int maxRent;
    private String earliestEntryDate;//todo: make sure to represent it as MM/YYYY
    private String latestEntryDate;
    private int minNumDesiredRoommates;
    private int maxNumDesiredRoommates;

    private static final int MAX_RENT_VALUE = 4000;
    private static final int MAX_NUM_D_VALUE = 5;


    public ApartmentSearcherUser(String firstName, String lastName, int age) {
        super(firstName, lastName);
    }

    public ApartmentSearcherUser() {
        super();
    }

    public ApartmentSearcherUser(ApartmentSearcherUser aUser) {
        super(aUser.getFirstName(), aUser.getLastName());
        this.optionalNeighborhoods = aUser.optionalNeighborhoods;
        this.minRent = aUser.minRent;
        this.maxRent = aUser.maxRent;
        this.earliestEntryDate = aUser.earliestEntryDate;
        this.latestEntryDate = aUser.latestEntryDate;
        this.minNumDesiredRoommates = aUser.minNumDesiredRoommates;
        this.maxNumDesiredRoommates = aUser.maxNumDesiredRoommates;
        this.bio = aUser.bio;
    }

    //------------------------------------------Getters---------------------------------------------

    public String getBio() {
        return bio;
    }

    public ArrayList<String> getOptionalNeighborhoods() {
        return optionalNeighborhoods;
    }

    public int getMinRent() {
        return minRent;
    }

    public int getMaxRent() {
        if (maxRent == 0) {
            return MAX_RENT_VALUE;
        }
        return maxRent;
    }

    public String getEarliestEntryDate() {
        return earliestEntryDate;
    }

    public String getLatestEntryDate() {
        return latestEntryDate;
    }

    public int getMinNumDesiredRoommates() {
        return minNumDesiredRoommates;
    }

    public int getMaxNumDesiredRoommates() {
        if (maxNumDesiredRoommates==0){
            return MAX_NUM_D_VALUE;
        }
        return maxNumDesiredRoommates;
    }


    //------------------------------------------Seters---------------------------------------------
    private void setOptionalNeighborhoods(ArrayList<String> optionalNeighborhoods) {
        this.optionalNeighborhoods = optionalNeighborhoods;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setMinRent(int minRent) {
        this.minRent = minRent;
    }

    public void setMaxRent(int maxRent) {
        this.maxRent = maxRent;
    }

    public void setEarliestEntryDate(String earliestEntryDate) {
        this.earliestEntryDate = earliestEntryDate;
    }

    public void setLatestEntryDate(String latestEntryDate) {
        this.latestEntryDate = latestEntryDate;
    }

    public void setMinNumDesiredRoommates(int minNumDesiredRoommates) {
        this.minNumDesiredRoommates = minNumDesiredRoommates;
    }

    public void setMaxNumDesiredRoommates(int maxNumDesiredRoommates) {
        this.maxNumDesiredRoommates = maxNumDesiredRoommates;
    }

    public boolean matches(RoommateSearcherUser roommateSearcherUser) {
        //todo check if there is a match
        return true;   //todo delete
    }
}
