package com.waiapp.Order;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.waiapp.R;

public class OrderDetailActivity extends AppCompatActivity {

    public static final String ORDER_KEY = "order_key";
    String mOrderKey;
    private TextView mTextViewOrderKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        mOrderKey = getIntent().getStringExtra(ORDER_KEY);
        mTextViewOrderKey = (TextView) findViewById(R.id.orderdetail_orderkey);
        mTextViewOrderKey.setText(mOrderKey);
    }
}
