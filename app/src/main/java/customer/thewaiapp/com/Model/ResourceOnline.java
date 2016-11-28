package customer.thewaiapp.com.Model;

import java.util.HashMap;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class ResourceOnline extends RealmObject {

    @PrimaryKey
    private String resourceId;
    private String Name,Gender,picture;
    private int rating;
    private double Lat, Long;
    @Ignore
    private HashMap<String, Object> timestampLogin;
    @Ignore
    private HashMap<String, Object> timestampLogout;

    public ResourceOnline(){

    }

    public ResourceOnline(String resourceId, String name, String gender, int rating, String picture, double lat, double Long, HashMap<String, Object> timestampLogin, HashMap<String, Object> timestampLogout) {
        Gender = gender;
        Lat = lat;
        Name = name;
        this.resourceId = resourceId;
        this.Long = Long;
        this.picture = picture;
        this.rating = rating;
        this.timestampLogin = timestampLogin;
        this.timestampLogout = timestampLogout;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
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