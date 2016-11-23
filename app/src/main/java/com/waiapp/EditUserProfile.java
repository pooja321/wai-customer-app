package com.waiapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.waiapp.EditProfile.ChangePassword;
import com.waiapp.EditProfile.EditUserDetail;

import static com.waiapp.ProfileActivity.UserKey;
import static com.waiapp.confirmation.BookingConfirmationActivity.SAVED_FRAGMENT;

public class EditUserProfile extends AppCompatActivity {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        mToolbar= (Toolbar) findViewById(R.id.edit_userprofile_toolbar);
        setSupportActionBar(mToolbar);
        int key = getIntent().getIntExtra(UserKey, 1);
        Fragment fragment = null;

        switch (key) {
            case (1):
                fragment = EditUserDetail.newInstance();
                getSupportActionBar().setTitle("Edit Profile");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case (2):
                fragment = ChangePassword.newInstance();
                getSupportActionBar().setTitle("Change Password");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;

        }

        if (!(fragment == null)) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.activity_layoutedituser, fragment, SAVED_FRAGMENT);
            fragmentTransaction.commit();
        }
    }
}

