package com.maidit;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by keviv on 18/06/2016.
 */
public class MaidItApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

}
