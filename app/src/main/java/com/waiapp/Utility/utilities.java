package com.waiapp.Utility;

import com.google.firebase.auth.FirebaseAuth;
import com.waiapp.R;

/**
 * Created by keviv on 08/08/2016.
 */
public class Utilities {
    public static String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    public static int[] tabIcons = {
            R.drawable.ic_map_white_24dp,
            R.drawable.ic_view_list_white_24dp
    };
}
