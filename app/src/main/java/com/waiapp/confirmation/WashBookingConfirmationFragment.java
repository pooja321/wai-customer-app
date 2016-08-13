package com.waiapp.confirmation;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.waiapp.Order.OrderConfirmActivity;
import com.waiapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WashBookingConfirmationFragment extends Fragment implements View.OnClickListener {

    TextView mTextViewBucketCount,mTextViewBucketAmount, mTextViewBaseAmount, mTextViewServiceTaxAmount, mTextViewTotalAmount;
    Button mButtonIncrementBucket,mButtonDecrementBucket, mButtonConfirm;
    int baseAmount = 50;
    int bucketCount,bucketAmount;
    double totalAmount;
    private OnUserSignUpRequired listener;

    // callback interface to implement on item list click listener
    public interface OnUserSignUpRequired{
        void UserSignUpRequired();
    }
    public WashBookingConfirmationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wash_booking_confirmation, container, false);
        bucketCount = 1;
        bucketAmount = 100;
        listener = (OnUserSignUpRequired) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextViewBucketCount = (TextView) view.findViewById(R.id.wash_booking_tv_bucket_count);
        mTextViewBucketAmount = (TextView) view.findViewById(R.id.wash_booking_tv_bucket_price);
        mTextViewBaseAmount = (TextView) view.findViewById(R.id.wash_booking_tv_base_price);
        mTextViewServiceTaxAmount = (TextView) view.findViewById(R.id.wash_booking_tv_service_tax_amount);
        mTextViewTotalAmount = (TextView) view.findViewById(R.id.wash_booking_tv_total_amount);

        mButtonIncrementBucket = (Button) view.findViewById(R.id.wash_booking_bt_bucket_count_increment);
        mButtonDecrementBucket = (Button) view.findViewById(R.id.wash_booking_bt_bucket_count_decrement);
        mButtonConfirm = (Button) view.findViewById(R.id.wash_booking_bt_confirm);

        mTextViewBaseAmount.setText(String.valueOf(50));
        mTextViewBucketCount.setText(String.valueOf(1));
        mTextViewBucketAmount.setText(String.valueOf(100));
        mTextViewServiceTaxAmount.setText(String.valueOf(18.75));
        mTextViewTotalAmount.setText(String.valueOf(168.75));

        mButtonIncrementBucket.setOnClickListener(this);
        mButtonDecrementBucket.setOnClickListener(this);
        mButtonConfirm.setOnClickListener(this);
        calculateAmount();
    }

    private void calculateAmount() {
        int tempAmount = baseAmount + bucketAmount;
        double serviceTaxAmount = tempAmount*.125;
        mTextViewServiceTaxAmount.setText(String.valueOf(serviceTaxAmount));
        totalAmount = (serviceTaxAmount+tempAmount);
        mTextViewTotalAmount.setText(String.valueOf(totalAmount));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case(R.id.wash_booking_bt_confirm):
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent intent = new Intent(getActivity(), OrderConfirmActivity.class);
                    startActivity(intent);
                } else {
                    // User is signed out
                    Toast.makeText(getActivity(), "Please Login First", Toast.LENGTH_SHORT).show();
                    listener.UserSignUpRequired();
                }
                break;
            case(R.id.wash_booking_bt_bucket_count_decrement):
                if(bucketCount > 1) {
                    bucketCount = bucketCount - 1;
                    bucketAmount = bucketCount * 100;
                }
                mTextViewBucketCount.setText(String.valueOf(bucketCount));
                mTextViewBucketAmount.setText(String.valueOf(bucketAmount));
                calculateAmount();
                break;
            case(R.id.wash_booking_bt_bucket_count_increment):
                bucketCount = bucketCount + 1;
                bucketAmount = bucketCount * 100;
                mTextViewBucketCount.setText(String.valueOf(bucketCount));
                mTextViewBucketAmount.setText(String.valueOf(bucketAmount));
                calculateAmount();
                break;
        }
    }
}
