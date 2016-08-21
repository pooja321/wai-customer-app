package com.waiapp.Booking.wash;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.waiapp.Booking.ListViewFragment;
import com.waiapp.Utility.Constants;

/**
 * Created by keviv on 19/07/2016.
 */
public class washListChildFragment extends ListViewFragment {
    private DatabaseReference mDatabase;

    @Override
    public Query setQuery() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        return mDatabase.child(Constants.CHILD_ONLINE_RESOURCE).child(Constants.CHILD_WASHING);
    }

    @Override
    public String getCallingFragmentName() {
        return Constants.CHILD_WASHING;
    }
}
