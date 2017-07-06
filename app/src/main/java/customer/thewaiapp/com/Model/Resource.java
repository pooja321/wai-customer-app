package customer.thewaiapp.com.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;

@IgnoreExtraProperties
public class Resource extends RealmObject implements Serializable {

    private String firstName, lastName, Email, gender,status,adhar,policeverification,maritalstatus,description,placeofbirth,placeofwork;
    private long mobileNumber,adharnumber;
    private int rating,age,experience,children;
    private static final long serialVersionUID = 1L;

    public Resource()
    {

    }
    public Resource(String firstName, String lastName, String email, String gender, String status, String adhar, String policeverification, String maritalstatus, String description, String placeofbirth, String placeofwork, long mobileNumber, long adharnumber, int rating, int age, int experience, int children) {
        this.firstName = firstName;
        this.lastName = lastName;
        Email = email;
        this.gender = gender;
        this.status = status;
        this.adhar = adhar;
        this.policeverification = policeverification;
        this.maritalstatus = maritalstatus;
        this.description = description;
        this.placeofbirth = placeofbirth;
        this.placeofwork = placeofwork;
        this.mobileNumber = mobileNumber;
        this.adharnumber = adharnumber;
        this.rating = rating;
        this.age = age;
        this.experience = experience;
        this.children = children;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("fname", getFirstName());
        result.put("lname", getLastName());
        result.put("email", getEmail());
        result.put("gender", getGender());
        result.put("adhar", getAdhar());
        result.put("policeverification", getPoliceverification());
        result.put("maritalstatus", getMaritalstatus());
        result.put("description", getDescription());
        result.put("placeofbirth", getPlaceofbirth());
        result.put("placeofwork", getPlaceofwork());
        result.put("mobileNumber", getMobileNumber());
        result.put("adharnumber", getAdharnumber());
        result.put("rating", getRating());
        result.put("age", getAge());
        result.put("experience", getExperience());
        result.put("children", getChildren());


        return result;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdhar() {
        return adhar;
    }

    public void setAdhar(String adhar) {
        this.adhar = adhar;
    }

    public String getPoliceverification() {
        return policeverification;
    }

    public void setPoliceverification(String policeverification) {
        this.policeverification = policeverification;
    }

    public String getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(String maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceofbirth() {
        return placeofbirth;
    }

    public void setPlaceofbirth(String placeofbirth) {
        this.placeofbirth = placeofbirth;
    }

    public String getPlaceofwork() {
        return placeofwork;
    }

    public void setPlaceofwork(String placeofwork) {
        this.placeofwork = placeofwork;
    }

    public long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public long getAdharnumber() {
        return adharnumber;
    }

    public void setAdharnumber(long adharnumber) {
        this.adharnumber = adharnumber;
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

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
