package com.waiapp.confirmation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;
import android.widget.Toast;

import com.waiapp.BaseActivity;
import com.waiapp.Model.Resource;
import com.waiapp.R;
import com.waiapp.Utility.Constants;

public class BookingConfirmationActivity extends BaseActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Resource resource;
    String callingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);
        resource = (Resource) getIntent().getSerializableExtra("resource");
        callingFragment = getIntent().getStringExtra("fragment_name");

        TextView textView = (TextView) findViewById(R.id.book_confirm_tv_name);
        textView.setText(resource.getFirstName().concat(" ").concat(resource.getLastName()));

        Fragment fragment = null;
        switch(callingFragment){
            case(Constants.CHILD_COOK):
                fragment = new CookBookingConfirmationFragment();
                break;
            case(Constants.CHILD_CLEANING):
                Toast.makeText(BookingConfirmationActivity.this, "Clean is the Calling Fragment ", Toast.LENGTH_SHORT).show();
                break;
            case(Constants.CHILD_WASHING):
                Toast.makeText(BookingConfirmationActivity.this, "Wash is the Calling Fragment ", Toast.LENGTH_SHORT).show();
                break;
        }
        if (!(fragment == null)) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.booking_confirmation_placeholder, fragment);
            fragmentTransaction.commit();
        }
    }
}
