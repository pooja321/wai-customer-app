package com.waiapp.Booking.clean;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.waiapp.Booking.MapViewFragment;
import com.waiapp.Utility.Constants;

/**
 * Created by keviv on 19/07/2016.
 */
public class cleanMapChildFragment extends MapViewFragment {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    public DatabaseReference getGeoDatabaseReference() {
        return mDatabase.child(Constants.FIREBASE_CHILD_ONLINE_CLEAN_GEO);
    }

    @Override
    public String getJobtype() {
        return Constants.FIREBASE_CHILD_CLEANING;
    }
}
