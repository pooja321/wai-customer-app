package com.waiapp.confirmation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.waiapp.Login.LoginFragment;
import com.waiapp.Login.SignUpFragment;
import com.waiapp.Model.Resource;
import com.waiapp.R;
import com.waiapp.Utility.Constants;

public class BookingConfirmationActivity extends AppCompatActivity implements WashBookingConfirmationFragment.OnUserSignUpRequired,
        CookBookingConfirmationFragment.OnUserSignUpRequired,SignUpFragment.OnSignInButtonClickedInterface,
        LoginFragment.OnSignUpButtonClickedInterface {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private Toolbar mtoolbar;
    Resource resource;
    String callingFragment;
    public static final String LOGIN_FRAGMENT = "login_fragment";
    public static final String SIGNUP_FRAGMENT = "SignUp_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);

        mtoolbar = (Toolbar) findViewById(R.id.booking_toolbar);
        mtoolbar.setTitleTextColor(getResources().getColor( R.color.white));
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resource = (Resource) getIntent().getSerializableExtra("resource");
        callingFragment = getIntent().getStringExtra("fragment_name");

        Fragment fragment = null;
        switch(callingFragment){
            case(Constants.CHILD_COOK):
                fragment = new CookBookingConfirmationFragment();
                break;
            case(Constants.CHILD_CLEANING):
                Toast.makeText(BookingConfirmationActivity.this, "Clean is the Calling Fragment ", Toast.LENGTH_SHORT).show();
                break;
            case(Constants.CHILD_WASHING):
                fragment = new WashBookingConfirmationFragment();
                break;
        }
        if (!(fragment == null)) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.booking_confirmation_placeholder, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void UserSignUpRequired() {
        SignUpFragment fragment = new SignUpFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.booking_confirmation_placeholder, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSignInFragmentSelected(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.booking_confirmation_placeholder, fragment, LOGIN_FRAGMENT);
        fragmentTransaction.commit();
    }

    @Override
    public void onSignUpFragmentSelected(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.booking_confirmation_placeholder, fragment, SIGNUP_FRAGMENT);
        fragmentTransaction.commit();
    }
}
