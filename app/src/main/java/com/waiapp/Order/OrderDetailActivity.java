package com.waiapp.Order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.waiapp.Model.Address;
import com.waiapp.Model.Order;
import com.waiapp.Model.OrderAmount;
import com.waiapp.Order.Fragment.CookingOrderDetailFragment;
import com.waiapp.R;
import com.waiapp.Utility.Constants;

import static com.waiapp.confirmation.BookingConfirmationActivity.SAVED_FRAGMENT;

public class OrderDetailActivity extends AppCompatActivity {

    public static final String ORDER_KEY = "order_key";
    public static final String ORDER_TYPE = "order_type";
    String mOrderKey, mOrderType;
    private Order mOrder;
    private OrderAmount mOrderAmount;
    private Address mAddress;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        mOrderKey = getIntent().getStringExtra(ORDER_KEY);
        mOrderType = getIntent().getStringExtra(ORDER_TYPE);

        Toolbar mtoolbar = (Toolbar) findViewById(R.id.orderdetail_toolbar);
        mtoolbar.setTitleTextColor(getResources().getColor( R.color.white));
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment fragment = null;

        switch(mOrderType){
            case(Constants.FIREBASE_CHILD_COOKING):
                fragment = CookingOrderDetailFragment.newInstance(mOrderKey);
                break;
            case(Constants.FIREBASE_CHILD_CLEANING):
                break;
            case(Constants.FIREBASE_CHILD_WASHING):
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
