package com.example.roome.user_classes;

import android.media.Image;

import java.util.ArrayList;

public class RoommateSearcherUser extends User {

    //--------------------profile additionalInfo---------------------
    private ArrayList<Image> roommatesImages; //todo: maybe delete
    private String additionalInfo; //such as additionalInfo
    private Apartment apartment;

    //--------------------filters---------------------
    private String genderPreference;


    public RoommateSearcherUser(String firstName, String lastName, int age) {
        super(firstName, lastName);
    }

    public RoommateSearcherUser() {
        super();
    }

    //------------------------------------------Getters---------------------------------------------


    public ArrayList<Image> getRoommatesImages() {
        return roommatesImages;
    }


    public String getAdditionalInfo() {
        return additionalInfo;
    }


    public String getGenderPreference() {
        return genderPreference;
    }


    public Apartment getApartment() {
        return apartment;
    }


    //------------------------------------------Setters---------------------------------------------


    public void setRoommatesImages(ArrayList<Image> roommatesImages) {
        this.roommatesImages = roommatesImages;
    }


    public void setAdditionalInfo(String info) {
        this.additionalInfo = info;
    }


    public void setGenderPreference(String genderPreference) {
        this.genderPreference = genderPreference;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }
}
