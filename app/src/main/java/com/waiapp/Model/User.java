package com.waiapp.Model;

import java.util.HashMap;

/**
 * Created by keviv on 21/07/2016.
 */
public class User {
    private String userId, firstName, lastName, Email, gender;
    private long mobileNumber;
    private HashMap<String, Object> timestampChanged;
    private HashMap<String, Object> timestampJoined;

    public User() {
    }

    public User(String customerId, String email, String firstName, String gender, String lastName, long mobileNumber, HashMap<String, Object> timestampChanged, HashMap<String, Object> timestampJoined) {
        this.userId = customerId;
        Email = email;
        this.firstName = firstName;
        this.gender = gender;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.timestampChanged = timestampChanged;
        this.timestampJoined = timestampJoined;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public HashMap<String, Object> getTimestampChanged() {
        return timestampChanged;
    }

    public void setTimestampChanged(HashMap<String, Object> timestampChanged) {
        this.timestampChanged = timestampChanged;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }

    public void setTimestampJoined(HashMap<String, Object> timestampJoined) {
        this.timestampJoined = timestampJoined;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
