package com.waiapp.confirmation;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.waiapp.OrderConfirmActivity;
import com.waiapp.R;

public class CookBookingConfirmationFragment extends Fragment implements View.OnClickListener {

    TextView mTextViewMembersCount,mTextViewMainCourseCount,mTextViewMembersAmount, mTextViewMainCourseAmount,
            mTextViewBaseAmount, mTextViewServiceTaxAmount, mTextViewTotalAmount;
    Button mButtonIncrementMembers,mButtonDecrementMembers, mButtonIncrementMainCourse,mButtonDecrementMainCourse,
            mButtonConfirm;
    int baseAmount = 50;
    int _membersCount, _mainCourseCount;
    int membersAmount,mainCourseAmount;
    double totalAmount;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public CookBookingConfirmationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cook_booking_confirmation, container, false);
        _membersCount = 2;
        _mainCourseCount = 2;
        membersAmount = 100;
        mainCourseAmount = 50;
        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextViewMembersCount = (TextView) view.findViewById(R.id.cook_booking_tv_members_count);
        mTextViewMainCourseCount = (TextView) view.findViewById(R.id.cook_booking_tv_maincourse_count);
        mTextViewMembersAmount = (TextView) view.findViewById(R.id.cook_booking_tv_members_price);
        mTextViewMainCourseAmount = (TextView) view.findViewById(R.id.cook_booking_tv_maincourse_price);
        mTextViewBaseAmount = (TextView) view.findViewById(R.id.cook_booking_tv_base_price);
        mTextViewServiceTaxAmount = (TextView) view.findViewById(R.id.cook_booking_tv_service_tax_amount);
        mTextViewTotalAmount = (TextView) view.findViewById(R.id.cook_booking_tv_total_amount);

        mButtonIncrementMembers = (Button) view.findViewById(R.id.cook_booking_bt_members_count_increment);
        mButtonDecrementMembers = (Button) view.findViewById(R.id.cook_booking_bt_members_count_decrement);
        mButtonIncrementMainCourse = (Button) view.findViewById(R.id.cook_booking_bt_maincourse_count_increment);
        mButtonDecrementMainCourse = (Button) view.findViewById(R.id.cook_booking_bt_maincourse_count_decrement);
        mButtonConfirm = (Button) view.findViewById(R.id.cook_booking_bt_confirm);

        mTextViewMembersCount.setText(String.valueOf(2));
        mTextViewMainCourseCount.setText(String.valueOf(2));
        mTextViewMembersAmount.setText(String.valueOf(100));
        mTextViewMainCourseAmount.setText(String.valueOf(50));
        mTextViewBaseAmount.setText(String.valueOf(50));
        mTextViewServiceTaxAmount.setText(String.valueOf(18.75));
        mTextViewTotalAmount.setText(String.valueOf(218.75));

        mButtonIncrementMainCourse.setOnClickListener(this);
        mButtonIncrementMembers.setOnClickListener(this);
        mButtonDecrementMainCourse.setOnClickListener(this);
        mButtonDecrementMembers.setOnClickListener(this);
        mButtonConfirm.setOnClickListener(this);
        calculateAmount();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case(R.id.cook_booking_bt_confirm):
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.v("wai", "onAuthStateChanged:signed_in");
                    Intent intent = new Intent(getActivity(), OrderConfirmActivity.class);
                    startActivity(intent);
                } else {
                    // User is signed out
                    Toast.makeText(getActivity(), "Please Login First", Toast.LENGTH_SHORT).show();
                    Log.v("wai", "onAuthStateChanged:signed_out");
                }
                break;
            case(R.id.cook_booking_bt_maincourse_count_decrement):
                if(_mainCourseCount > 2) {
                    _mainCourseCount = _mainCourseCount - 1;
                    if(!(_mainCourseCount == 2)){
                        mainCourseAmount =_mainCourseCount * 50;
                    }
                }
                if(_mainCourseCount <= 2){
                    mainCourseAmount = 50;
                }
                mTextViewMainCourseCount.setText(String.valueOf(_mainCourseCount));
                mTextViewMainCourseAmount.setText(String.valueOf(mainCourseAmount));
                calculateAmount();
                break;
            case(R.id.cook_booking_bt_maincourse_count_increment):
                _mainCourseCount = _mainCourseCount + 1;
                mTextViewMainCourseCount.setText(String.valueOf(_mainCourseCount));
                if(_mainCourseCount > 2){
                    mainCourseAmount = 50 + (_mainCourseCount-2) * 50;
                }
                mTextViewMainCourseAmount.setText(String.valueOf(mainCourseAmount));
                calculateAmount();
                break;
            case(R.id.cook_booking_bt_members_count_decrement):
                if(_membersCount > 2){
                    _membersCount = _membersCount - 1;
                    membersAmount =_membersCount * 50;
//                    if(!(_membersCount == 2)){
//
//                    }
                }
                mTextViewMembersCount.setText(String.valueOf(_membersCount));
                mTextViewMembersAmount.setText(String.valueOf(membersAmount));
                calculateAmount();
                break;
            case(R.id.cook_booking_bt_members_count_increment):
                _membersCount = _membersCount + 1;
                mTextViewMembersCount.setText(String.valueOf(_membersCount));
                if(_membersCount >= 2){
                    membersAmount =_membersCount * 50;
                }
                mTextViewMembersAmount.setText(String.valueOf(membersAmount));
                calculateAmount();
                break;
        }
    }

    private void calculateAmount() {
        int tempAmount = baseAmount + membersAmount + mainCourseAmount;
        double serviceTaxAmount = tempAmount*.125;
        mTextViewServiceTaxAmount.setText(String.valueOf(serviceTaxAmount));
        totalAmount = (serviceTaxAmount+tempAmount);
        mTextViewTotalAmount.setText(String.valueOf(totalAmount));
    }
}