package com.example.roome.user_classes; //todo rename?

public class User {

    public static final int NAME_MAXIMUM_LENGTH = 18;
    public static final int MAXIMUM_AGE_LENGTH = 2;
    public static final int MAXIMUM_AGE = 70;
    public static final int MINIMUM_AGE = 14;
    public static final int PHONE_NUMBER_LENGTH = 10;


    //--------------------profile info---------------------
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String phoneNumber;
    private String info;

    //--------------------filters---------------------
    private boolean kosherImportance;
    private int minAgeRequired;
    private int maxAgeRequired;


    private boolean hasProfilePic = false;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = 0;
    }

    public User() {
    }

    //------------------------------------------Getters---------------------------------------------
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getPhoneNumber() { return phoneNumber; }

    public String getGender() { return gender; }

    public boolean getKosherImportance() {
        return kosherImportance;
    }

    public int getMinAgeRequired() {
        return minAgeRequired;
    }

    public int getMaxAgeRequired() {
        return maxAgeRequired;
    }

    public boolean getHasProfilePic() {return hasProfilePic;}

    public String getInfo() { return info; }

    //------------------------------------------Setters---------------------------------------------
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {this.lastName = lastName; }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setKosherImportance(boolean kosherImportance) {
        this.kosherImportance = kosherImportance;
    }

    public void setMinAgeRequired(int minAgeRequired) {
        this.minAgeRequired = minAgeRequired;
    }


    public void setMaxAgeRequired(int maxAgeRequired) {
        this.maxAgeRequired = maxAgeRequired;
    }

    public void setHasProfilePic(boolean hasProfilePic) {this.hasProfilePic = hasProfilePic;}

    public void setInfo(String info) { this.info = info; }
}
