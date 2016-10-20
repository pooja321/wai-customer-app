package com.waiapp.Order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.waiapp.Order.Fragment.CleaningOrderDetailFragment;
import com.waiapp.Order.Fragment.CookingOrderDetailFragment;
import com.waiapp.Order.Fragment.WashingOrderDetailFragment;
import com.waiapp.R;
import com.waiapp.Utility.Constants;

import static com.waiapp.confirmation.BookingConfirmationActivity.SAVED_FRAGMENT;

public class OrderDetailActivity extends AppCompatActivity {

    public static final String ORDER_KEY = "order_key";
    public static final String ORDER_TYPE = "order_type";
    String mOrderKey, mOrderType;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        mOrderKey = getIntent().getStringExtra(ORDER_KEY);
        mOrderType = getIntent().getStringExtra(ORDER_TYPE);

        mToolbar = (Toolbar) findViewById(R.id.orderdetail_toolbar);
        mToolbar.setTitleTextColor(getResources().getColor( R.color.white));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment fragment = null;

        switch(mOrderType){
            case(Constants.FIREBASE_CHILD_COOKING):
                fragment = CookingOrderDetailFragment.newInstance(mOrderKey);
                break;
            case(Constants.FIREBASE_CHILD_CLEANING):
                fragment = CleaningOrderDetailFragment.newInstance(mOrderKey);
                break;
            case(Constants.FIREBASE_CHILD_WASHING):
                fragment = WashingOrderDetailFragment.newInstance(mOrderKey);
                break;
        }

        if (!(fragment == null)) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.orderdetail_placeholder, fragment, SAVED_FRAGMENT);
            fragmentTransaction.commit();
        }
    }
}
