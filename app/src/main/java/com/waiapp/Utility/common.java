package com.waiapp.Utility;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by keviv on 08/08/2016.
 */
public class common {
    public static String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
