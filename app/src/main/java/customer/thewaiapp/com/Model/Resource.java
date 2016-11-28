package customer.thewaiapp.com.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Resource implements Serializable {

    private String firstName, lastName, Email, gender,status;
    private long mobileNumber;
    private int rating;
    private static final long serialVersionUID = 1L;

    public Resource() {
    }

    public Resource(String firstName, String lastName, String email, String gender, long mobileNumber, int rating, String status) {
        Email = email;
        this.firstName = firstName;
        this.gender = gender;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.rating = rating;
        this.status = status;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("fname", getFirstName());
        result.put("lname", getLastName());
        result.put("email", getEmail());
        result.put("gender", getGender());
        result.put("mobileNumber", getMobileNumber());
        result.put("rating", getRating());

        return result;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
