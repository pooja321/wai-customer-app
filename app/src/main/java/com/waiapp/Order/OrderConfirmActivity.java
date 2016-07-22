package com.waiapp.Order;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.waiapp.R;

public class OrderConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        final TextView mTextViewTimer = (TextView) findViewById(R.id.order_confirm_tv_timer);
        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextViewTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextViewTimer.setText("done!");
            }
        }.start();
    }
}
