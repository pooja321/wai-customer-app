package customer.thewaiapp.com.confirmation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import customer.thewaiapp.com.Utility.Constants;
import customer.thewaiapp.com.Login.LoginFragment;
import customer.thewaiapp.com.Login.SignUpFragment;
import customer.thewaiapp.com.R;

public class BookingConfirmationActivity extends AppCompatActivity implements WashBookingConfirmationFragment.OnUserSignUpRequired,
        CookBookingConfirmationFragment.OnUserSignUpRequired,SignUpFragment.OnSignInButtonClickedInterface,
        LoginFragment.OnSignUpButtonClickedInterface {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private Toolbar mtoolbar;
    String callingFragment, mResourceKey, mResourceName;
    public static final String LOGIN_FRAGMENT = "login_fragment";
    public static final String SIGNUP_FRAGMENT = "SignUp_fragment";
    public static final String SAVED_FRAGMENT = "saved_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);
        Log.v("wai", "confirmation oncreate");
        mtoolbar = (Toolbar) findViewById(R.id.booking_toolbar);
        mtoolbar.setTitleTextColor(getResources().getColor( R.color.white));
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mResourceName = getIntent().getStringExtra("resourceName");
        callingFragment = getIntent().getStringExtra("fragment_name");
        mResourceKey = getIntent().getStringExtra("resourceKey");
        Fragment fragment = null;
        switch(callingFragment){
            case(Constants.FIREBASE_CHILD_COOKING):
                Toast.makeText(BookingConfirmationActivity.this, "cook fragment called", Toast.LENGTH_SHORT).show();
                fragment = CookBookingConfirmationFragment.newInstance(mResourceKey,mResourceName);
                break;
            case(Constants.FIREBASE_CHILD_CLEANING):
                Toast.makeText(BookingConfirmationActivity.this, "Clean is the Calling Fragment ", Toast.LENGTH_SHORT).show();
                fragment = CleanBookingConfirmationFragment.newInstance(mResourceKey, mResourceName);
                break;
            case(Constants.FIREBASE_CHILD_WASHING):
                fragment = WashBookingConfirmationFragment.newInstance(mResourceKey, mResourceName);
                break;
        }

        if (!(fragment == null)) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.booking_confirmation_placeholder, fragment, SAVED_FRAGMENT);
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



//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        Log.v("wai","savedinstancestate: " + callingFragment);
//        Toast.makeText(BookingConfirmationActivity.this, callingFragment , Toast.LENGTH_SHORT).show();
//        outState.putString(KEY_CALLING_FRAGMENT,callingFragment);
//        super.onSaveInstanceState(outState);
//    }

}
