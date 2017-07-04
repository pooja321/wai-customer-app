package customer.thewaiapp.com.Model;

import java.util.HashMap;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class ResourceOnline extends RealmObject {

    @PrimaryKey
    private String resourceId;
    private String Name,Gender,picture,placeofwork,adhar,police_verification;
    private int rating,age,experience;
    private double Lat, Long;
    @Ignore
    private HashMap<String, Object> timestampLogin;
    @Ignore
    private HashMap<String, Object> timestampLogout;

    public ResourceOnline()
    {

    }
    public ResourceOnline(String resourceId, String name, String gender, String picture, String placeofwork, String adhar, String police_verification, int rating, int age, int experience, double lat, double aLong, HashMap<String, Object> timestampLogin, HashMap<String, Object> timestampLogout) {
        this.resourceId = resourceId;
        Name = name;
        Gender = gender;
        this.picture = picture;
        this.placeofwork = placeofwork;
        this.adhar = adhar;
        this.police_verification = police_verification;
        this.rating = rating;
        this.age = age;
        this.experience = experience;
        Lat = lat;
        Long = aLong;
        this.timestampLogin = timestampLogin;
        this.timestampLogout = timestampLogout;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPlaceofwork() {
        return placeofwork;
    }

    public void setPlaceofwork(String placeofwork) {
        this.placeofwork = placeofwork;
    }

    public String getAdhar() {
        return adhar;
    }

    public void setAdhar(String adhar) {
        this.adhar = adhar;
    }

    public String getPolice_verification() {
        return police_verification;
    }

    public void setPolice_verification(String police_verification) {
        this.police_verification = police_verification;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
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