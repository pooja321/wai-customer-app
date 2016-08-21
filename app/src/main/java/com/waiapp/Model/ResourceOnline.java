package com.waiapp.Model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by keviv on 21/08/2016.
 */
public class ResourceOnline implements Serializable {
    private String Name,Gender,picture;
    private int rating;
    private double Lat, Long;
    private HashMap<String, Object> timestampLogin;
    private HashMap<String, Object> timestampLogout;
    private static final long serialVersionUID = 1L;

    public ResourceOnline(){

    }

    public ResourceOnline(String name, String gender, int rating, String picture, double lat, double Long, HashMap<String, Object> timestampLogin, HashMap<String, Object> timestampLogout) {
        Gender = gender;
        Lat = lat;
        this.Long = Long;
        Name = name;
        this.picture = picture;
        this.rating = rating;
        this.timestampLogin = timestampLogin;
        this.timestampLogout = timestampLogout;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public HashMap<String, Object> getTimestampLogin() {
        return timestampLogin;
    }

    public void setTimestampLogin(HashMap<String, Object> timestampLogin) {
        this.timestampLogin = timestampLogin;
    }

    public HashMap<String, Object> getTimestampLogout() {
        return timestampLogout;
    }

    public void setTimestampLogout(HashMap<String, Object> timestampLogout) {
        this.timestampLogout = timestampLogout;
    }
}

