package customer.thewaiapp.com.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

@IgnoreExtraProperties
public class Resource extends RealmObject implements Serializable{
    @PrimaryKey
    private String id;
    private String type, firstName, lastName, Email, gender,adhar,policeverification,maritalstatus,description,placeofbirth,placeofwork;
    private long mobileNumber,adharnumber;
    private int rating,age,experience,children;


    @Ignore
    private HashMap<String, Object> timestampJoined;

    public Resource()
    {

    }
    public Resource(String id, String type, String firstName, String lastName, String email, String gender, String adhar, String policeverification, String maritalstatus, String description, String placeofbirth, String placeofwork, long mobileNumber, long adharnumber, int rating, int age, int experience, int children, HashMap<String, Object> timestampJoined) {
        this.id = id;
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
        Email = email;
        this.gender = gender;
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
        this.timestampJoined = timestampJoined;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }

    public void setTimestampJoined(HashMap<String, Object> timestampJoined) {
        this.timestampJoined = timestampJoined;
    }
}
