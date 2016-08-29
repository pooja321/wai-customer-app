package com.waiapp.Booking.wash;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.waiapp.Booking.MapViewFragment;
import com.waiapp.Utility.Constants;

/**
 * Created by keviv on 19/07/2016.
 */
public class washMapChildFragment extends MapViewFragment {

    private DatabaseReference mDatabase;
    @Override
    public DatabaseReference getDatabaseReference() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        return mDatabase.child(Constants.FIREBASE_CHILD_ONLINE_RESOURCE).child(Constants.FIREBASE_CHILD_WASHING);
    }
}
