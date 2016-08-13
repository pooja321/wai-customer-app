package com.waiapp.Booking.cook;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.waiapp.Booking.ListViewFragment;
import com.waiapp.Utility.Constants;

/**
 * Created by keviv on 19/07/2016.
 */
public class CookListChildFragment extends ListViewFragment {
    private DatabaseReference mDatabase;

    @Override
    public Query setQuery() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        return mDatabase.child(Constants.CHILD_RESOURCES).child(Constants.CHILD_COOK).orderByChild("status").equalTo("Online");
    }

    @Override
    public String getCallingFragmentName() {
        return Constants.CHILD_COOK;
    }
}
