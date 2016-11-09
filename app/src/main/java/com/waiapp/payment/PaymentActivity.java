package com.waiapp.payment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.waiapp.MainActivity;
import com.waiapp.Model.Address;
import com.waiapp.Model.CleaningOrderAmountValues;
import com.waiapp.Model.CookingOrderAmountValues;
import com.waiapp.Model.Order;
import com.waiapp.Model.OrderKey;
import com.waiapp.Model.WashingOrderAmountValues;
import com.waiapp.Order.OrderConfirmActivity;
import com.waiapp.R;
import com.waiapp.Realm.RealmController;
import com.waiapp.Utility.Constants;
import com.waiapp.Utility.Utilities;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, ExitAlertDialogFragment.ExitOrderListener {

    Map<String, Object> OrderUpdates = new HashMap<>();
    public static final String DIALOG_ALERT = "My Alert";
    String mOrderKey, mresourceKey, UID, mOrderType, mOrderId;
    Order mOrder = new Order();
//    OrderAmount mOrderAmount = new OrderAmount();
    Address mAddress = new Address();

    private Realm realm;
    private DatabaseReference mDatabase;
    private Toolbar mtoolbar;
    RadioGroup mRadioGroupPayment;
    RadioButton mRadioButtonCOD, mRadioButtonPaytm, mRadioButtonPayu;
    Button mButtonSubmit;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        UID = Utilities.getUid();
        mOrder = (Order) getIntent().getSerializableExtra("order");
        Log.v("wai", "Order id: " + mOrder.getOrderId());
        Log.v("wai", "Order type: " + mOrder.getOrderType());
        mAddress = (Address) getIntent().getSerializableExtra("Address");
        mOrderType = mOrder.getOrderType();
        mOrderId = mOrder.getOrderId();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mresourceKey = mOrder.getResourceId();
        this.realm = RealmController.with(this).getRealm();
        mtoolbar = (Toolbar) findViewById(R.id.payment_toolbar);
        mtoolbar.setTitleTextColor(getResources().getColor(R.color.white));
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
        switch (id) {
            case R.id.payment_bt_submit:
                showProgressDialog();
                saveOrder();
        }
    }

    private void saveOrder() {
        int id = mRadioGroupPayment.getCheckedRadioButtonId();
        String _paymentMode = null;
        switch (id) {
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
        mOrder.setPaymentMode(_paymentMode);
        mOrder.setOrderStatus(Constants.ORDER_STATUS_ORDERED);
        mOrder.setOrderProgressStatus(Constants.ORDER_PROGRESS_STATUS_RESOURCE_CONFIRMATION_AWAITED);
        HashMap<String, Object> orderbookingTime = new HashMap<>();
        orderbookingTime.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        mOrder.setOrderbookingTime(orderbookingTime);

        mOrderKey = mDatabase.child(Constants.FIREBASE_CHILD_USER_ORDERS).push().getKey();

        mDatabase.child(Constants.FIREBASE_CHILD_USER_ORDERS).child(UID).child(mOrderKey).setValue(mOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(PaymentActivity.this, "Order saving failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaymentActivity.this, "Order Saved successfully", Toast.LENGTH_SHORT).show();
                    updateUserOrderHistory();
                    updateResourceOrderHistory();
                    final OrderKey orderKey = new OrderKey();
                    orderKey.setId(UID);
                    orderKey.setOrderkey(mOrderKey);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(orderKey);
                        }
                    });
                    startActivity(new Intent(PaymentActivity.this, OrderConfirmActivity.class).putExtra("orderKey", mOrderKey).putExtra("orderId",mOrder.getOrderId()));
                    closeProgressDialog();
                }
            }
        });

    }

    private void updateResourceOrderHistory() {
        mDatabase.child(Constants.FIREBASE_CHILD_RESOURCE_ORDERS).child(mresourceKey).child(mOrderKey).setValue(mOrder);
    }

    private void updateUserOrderHistory() {
        mDatabase.child(Constants.FIREBASE_CHILD_ORDER_DETAILS).child(mOrderKey).child(Constants.FIREBASE_CHILD_ORDER).setValue(mOrder);
//        mDatabase.child(Constants.FIREBASE_CHILD_ORDER_DETAILS).child(mOrderKey).child(Constants.FIREBASE_CHILD_ORDER_AMOUNT).setValue(mOrderAmount);
        mDatabase.child(Constants.FIREBASE_CHILD_ORDER_DETAILS).child(mOrderKey).child(Constants.FIREBASE_CHILD_ADDRESS).setValue(mAddress);
        switch (mOrderType){
            case Constants.ORDER_TYPE_CLEANING:
                CleaningOrderAmountValues cleaningOrderAmountValues = realm.where(CleaningOrderAmountValues.class).equalTo("OrderId",mOrderId).findFirst();
                mDatabase.child(Constants.FIREBASE_CHILD_ORDER_DETAILS).child(mOrderKey).child(Constants.FIREBASE_CHILD_ORDER_AMOUNT).setValue(cleaningOrderAmountValues);
                break;
            case  Constants.ORDER_TYPE_COOKING:
                CookingOrderAmountValues cookingOrderAmountValues = realm.where(CookingOrderAmountValues.class).equalTo("OrderId",mOrderId).findFirst();
                mDatabase.child(Constants.FIREBASE_CHILD_ORDER_DETAILS).child(mOrderKey).child(Constants.FIREBASE_CHILD_ORDER_AMOUNT).setValue(cookingOrderAmountValues);
                break;
            case Constants.ORDER_TYPE_WASHING:
                WashingOrderAmountValues washingOrderAmountValues = realm.where(WashingOrderAmountValues.class).equalTo("OrderId",mOrderId).findFirst();
                mDatabase.child(Constants.FIREBASE_CHILD_ORDER_DETAILS).child(mOrderKey).child(Constants.FIREBASE_CHILD_ORDER_AMOUNT).setValue(washingOrderAmountValues);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        ExitAlertDialogFragment exitAlertDialogFragment = new ExitAlertDialogFragment();
        exitAlertDialogFragment.show(getSupportFragmentManager(), DIALOG_ALERT);
    }

    @Override
    public void ExitOrder(Boolean exit) {
        if (exit) {
            startActivity(new Intent(PaymentActivity.this, MainActivity.class));
        }
    }

    void showProgressDialog(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Loading...");
        mProgressDialog.show();
    }

    void closeProgressDialog(){
        if(mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }
}