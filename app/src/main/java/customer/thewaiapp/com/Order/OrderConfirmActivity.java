package customer.thewaiapp.com.Order;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import customer.thewaiapp.com.MainActivity;
import customer.thewaiapp.com.Model.Address;

import customer.thewaiapp.com.R;

public class OrderConfirmActivity extends AppCompatActivity {

    TextView mTextViewOrderKey;
    private Toolbar mtoolbar;
    private Address address;
    String mOrderKey, mOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        mOrderKey = getIntent().getStringExtra("orderKey");
        mOrderId = getIntent().getStringExtra("orderId");

        address = (Address) getIntent().getSerializableExtra("address");
        mtoolbar = (Toolbar) findViewById(R.id.order_confirm_toolbar);
        mtoolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mtoolbar);
        setTitle("Your Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView mTextViewTimer = (TextView) findViewById(R.id.order_confirm_tv_timer);
        new CountDownTimer(2700000, 1000) {

            public void onTick(long millisUntilFinished) {
                int durationSeconds = (int) (millisUntilFinished / 1000);
                String time = String.format("%02d:%02d:%02d", durationSeconds / 3600,
                        (durationSeconds % 3600) / 60, (durationSeconds % 60));
                mTextViewTimer.setText(time);
            }

            public void onFinish() {
                mTextViewTimer.setText("done!");
            }
        }.start();
        mTextViewOrderKey = (TextView) findViewById(R.id.order_confirm_orderkey);
        mTextViewOrderKey.setText(mOrderId);
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
