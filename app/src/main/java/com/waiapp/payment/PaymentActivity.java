package com.waiapp.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.waiapp.MainActivity;
import com.waiapp.Order.OrderConfirmActivity;
import com.waiapp.R;
import com.waiapp.Utility.Constants;
import com.waiapp.Utility.Utilities;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, ExitAlertDialogFragment.ExitOrderListener {

    private DatabaseReference mDatabase;

    private Toolbar mtoolbar;
    RadioGroup mRadioGroupPayment;
    RadioButton mRadioButtonCOD, mRadioButtonPaytm, mRadioButtonPayu;
    Button mButtonSubmit;
    Map<String, Object> OrderUpdates = new HashMap<>();
    public static final String DIALOG_ALERT = "My Alert";
    String orderKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        orderKey = getIntent().getStringExtra("orderKey");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mtoolbar = (Toolbar) findViewById(R.id.payment_toolbar);
        mtoolbar.setTitleTextColor(getResources().getColor( R.color.white));
        setSupportActionBar(mtoolbar);
        setTitle("Select payment Method");

        mRadioGroupPayment = (RadioGroup) findViewById(R.id.payment_radiogroup);
        mRadioButtonCOD = (RadioButton) findViewById(R.id.payment_rb_mode_COD);
        mRadioButtonPaytm = (RadioButton) findViewById(R.id.payment_rb_mode_paytm);
        mRadioButtonPayu = (RadioButton) findViewById(R.id.payment_rb_mode_payu);
        mButtonSubmit = (Button) findViewById(R.id.payment_bt_submit);
        mButtonSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.payment_bt_submit:
                savePaymentDetails();
        }
    }

    private void savePaymentDetails() {
        int id = mRadioGroupPayment.getCheckedRadioButtonId();
        String _paymentMode = null;
        switch (id){
            case R.id.payment_rb_mode_COD:
                    Toast.makeText(PaymentActivity.this, "You selected Cash on delivery", Toast.LENGTH_SHORT).show();
                    _paymentMode = "COD";
                break;
            case R.id.payment_rb_mode_paytm:
                    Toast.makeText(PaymentActivity.this, "You selected paytm", Toast.LENGTH_SHORT).show();
                    _paymentMode = "PAYTM";
                break;
            case R.id.payment_rb_mode_payu:
                    Toast.makeText(PaymentActivity.this, "You selected paytu", Toast.LENGTH_SHORT).show();
                    _paymentMode = "PAYU";
                break;
        }
        OrderUpdates.put("paymentMode",_paymentMode);
        OrderUpdates.put("orderStatus",Constants.ORDER_STATUS_ORDERED);
        OrderUpdates.put("orderProgressStatus",Constants.ORDER_STATUS_WAITING_FOR_RESOURCE);
        HashMap<String, Object> orderbookingTime = new HashMap<>();
        orderbookingTime.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        OrderUpdates.put("orderbookingTime",orderbookingTime);

        mDatabase.child(Constants.FIREBASE_CHILD_ORDER).child(Utilities.getUid()).child(orderKey)
                .updateChildren(OrderUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(!(databaseError == null)){
                    Toast.makeText(PaymentActivity.this, "payment method updated failed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(PaymentActivity.this, "payment method updated successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PaymentActivity.this, OrderConfirmActivity.class).putExtra("orderKey",orderKey));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        ExitAlertDialogFragment exitAlertDialogFragment = new ExitAlertDialogFragment();
        exitAlertDialogFragment.show(getSupportFragmentManager(),DIALOG_ALERT);
    }

    @Override
    public void ExitOrder(Boolean exit) {
        if(exit){
            mDatabase.child(Constants.FIREBASE_CHILD_ORDER).child(Utilities.getUid()).child(orderKey).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                        }
                    });
        }
    }
}
