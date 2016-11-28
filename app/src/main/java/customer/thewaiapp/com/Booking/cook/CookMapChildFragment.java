package customer.thewaiapp.com.Booking.cook;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import customer.thewaiapp.com.Booking.MapViewFragment;
import customer.thewaiapp.com.Utility.Constants;

/**
 * Created by keviv on 19/07/2016.
 */
public class CookMapChildFragment extends MapViewFragment {

    private DatabaseReference mDatabase;
    @Override
    public DatabaseReference getGeoDatabaseReference() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        return mDatabase.child(Constants.FIREBASE_CHILD_ONLINE_COOK_GEO);
    }
    @Override
    public String getJobtype() {
        return Constants.FIREBASE_CHILD_COOKING;
    }
}
