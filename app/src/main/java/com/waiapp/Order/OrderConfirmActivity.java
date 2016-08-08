package com.waiapp.Order;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.waiapp.MainActivity;
import com.waiapp.Model.Address;
import com.waiapp.R;

public class OrderConfirmActivity extends AppCompatActivity {

    TextView mTextViewOrderKey;
    private Toolbar mtoolbar;
    private Address address;
    String orderKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        orderKey = getIntent().getStringExtra("orderKey");

        address = (Address) getIntent().getSerializableExtra("address");
        mtoolbar = (Toolbar) findViewById(R.id.order_confirm_toolbar);
        mtoolbar.setTitleTextColor(getResources().getColor( R.color.white));
        setSupportActionBar(mtoolbar);
        setTitle("Your Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView mTextViewTimer = (TextView) findViewById(R.id.order_confirm_tv_timer);
        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextViewTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextViewTimer.setText("done!");
            }
        }.start();
        mTextViewOrderKey = (TextView)  findViewById(R.id.order_confirm_orderkey);
        mTextViewOrderKey.setText(orderKey);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
