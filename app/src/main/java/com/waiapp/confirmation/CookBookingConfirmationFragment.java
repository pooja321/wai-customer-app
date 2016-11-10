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
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.waiapp.Address.AddressActivity;
import com.waiapp.Model.CookingOrderAmountValues;
import com.waiapp.Model.ResourceOnline;
import com.waiapp.R;
import com.waiapp.Realm.RealmController;
import com.waiapp.Utility.Constants;

import io.realm.Realm;

import static com.waiapp.Utility.Utilities.generateOrderId;

public class CookBookingConfirmationFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_KEY = "ResourceKey";
    private static final String ARG_RESOURCE = "ResourceName";
    int mBaseAmount = 50;
    int mMembersCount, mMainCourseCount;
    int mMembersAmount, mMainCourseAmount;
    double mTotalAmount;
    double mServiceTaxAmount = 0;
    private String mParamResourceKey, mParamResourceName, mOrderId;

    TextView mTextViewMembersCount,mTextViewMainCourseCount,mTextViewMembersAmount, mTextViewMainCourseAmount,
            mTextViewBaseAmount, mTextViewServiceTaxAmount, mTextViewTotalAmount, mTextViewResourceName;
    Button mButtonIncrementMembers,mButtonDecrementMembers, mButtonIncrementMainCourse,mButtonDecrementMainCourse,
            mButtonConfirm;
    CheckBox mCheckBoxTerms;
    private OnUserSignUpRequired mListener;
    private DatabaseReference mDatabase;
    private ResourceOnline mParamResource;
    Realm realm;

    // callback interface to implement on item list click mListener
    public interface OnUserSignUpRequired{
        void UserSignUpRequired();
    }

    public CookBookingConfirmationFragment() {
        // Required empty public constructor
    }

    public static CookBookingConfirmationFragment newInstance(String key, String resourceName) {
        CookBookingConfirmationFragment fragment = new CookBookingConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putString(ARG_RESOURCE, resourceName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("wai","CookBookingConfirmationFragment onCreate");
        if (getArguments() != null) {
            mParamResourceKey = getArguments().getString(ARG_KEY);
            mParamResourceName = getArguments().getString(ARG_RESOURCE);
        }
        if(savedInstanceState == null){
            mMembersCount = 2;
            mMainCourseCount = 2;
            mMembersAmount = 100;
            mMainCourseAmount = 50;
        }
        this.realm = RealmController.with(this).getRealm();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v("wai","CookBookingConfirmationFragment oncreateView");
        View view =  inflater.inflate(R.layout.fragment_cook_booking_confirmation, container, false);
        mListener = (OnUserSignUpRequired) getActivity();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v("wai","CookBookingConfirmationFragment onViewCreated");
        mCheckBoxTerms = (CheckBox) view.findViewById(R.id.cook_booking_cb_terms);
        mTextViewResourceName = (TextView) view.findViewById(R.id.cook_booking_tv_resource_name);
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

        mButtonIncrementMainCourse.setOnClickListener(this);
        mButtonIncrementMembers.setOnClickListener(this);
        mButtonDecrementMainCourse.setOnClickListener(this);
        mButtonDecrementMembers.setOnClickListener(this);
        mButtonConfirm.setOnClickListener(this);

        mTextViewResourceName.setText(mParamResourceName);
        mTextViewMembersCount.setText(String.valueOf(mMembersCount));
        mTextViewMainCourseCount.setText(String.valueOf(mMainCourseCount));
        mTextViewMembersAmount.setText(String.valueOf(mMembersAmount));
        mTextViewMainCourseAmount.setText(String.valueOf(mMainCourseAmount));
        mTextViewBaseAmount.setText(String.valueOf(mBaseAmount));
        calculateAmount();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case(R.id.cook_booking_bt_confirm):
                if (mCheckBoxTerms.isChecked()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        mOrderId = generateOrderId();
                        Log.v("wai", "Order Id: " + mOrderId);
                        final CookingOrderAmountValues cookingOrderAmountValues = new CookingOrderAmountValues(mOrderId, mBaseAmount, mMainCourseAmount, mMainCourseCount, mMembersAmount, mMembersCount, mServiceTaxAmount, mTotalAmount);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealm(cookingOrderAmountValues);
                            }
                        });
                        Intent intent = new Intent(getActivity(), AddressActivity.class);
                        intent.putExtra("resourceKey",mParamResourceKey);
                        intent.putExtra("totalAmount", mTotalAmount);
                        intent.putExtra("orderType", Constants.ORDER_TYPE_COOKING);
                        intent.putExtra("orderId", mOrderId);
                        startActivity(intent);
                    } else {
                        // User is signed out
                        Toast.makeText(getActivity(), "Please Login First", Toast.LENGTH_SHORT).show();
                        mListener.UserSignUpRequired();
                    }
                }
                break;
            case(R.id.cook_booking_bt_maincourse_count_decrement):
                if(mMainCourseCount > 2) {
                    mMainCourseCount = mMainCourseCount - 1;
                    if(!(mMainCourseCount == 2)){
                        mMainCourseAmount = mMainCourseCount * 50;
                    }
                }
                if(mMainCourseCount <= 2){
                    mMainCourseAmount = 50;
                }
                mTextViewMainCourseCount.setText(String.valueOf(mMainCourseCount));
                mTextViewMainCourseAmount.setText(String.valueOf(mMainCourseAmount));
                calculateAmount();
                break;
            case(R.id.cook_booking_bt_maincourse_count_increment):
                mMainCourseCount = mMainCourseCount + 1;
                mTextViewMainCourseCount.setText(String.valueOf(mMainCourseCount));
                if(mMainCourseCount > 2){
                    mMainCourseAmount = 50 + (mMainCourseCount -2) * 50;
                }
                mTextViewMainCourseAmount.setText(String.valueOf(mMainCourseAmount));
                calculateAmount();
                break;
            case(R.id.cook_booking_bt_members_count_decrement):
                if(mMembersCount > 2){
                    mMembersCount = mMembersCount - 1;
                    mMembersAmount = mMembersCount * 50;
                }
                mTextViewMembersCount.setText(String.valueOf(mMembersCount));
                mTextViewMembersAmount.setText(String.valueOf(mMembersAmount));
                calculateAmount();
                break;
            case(R.id.cook_booking_bt_members_count_increment):
                mMembersCount = mMembersCount + 1;
                mTextViewMembersCount.setText(String.valueOf(mMembersCount));
                if(mMembersCount >= 2){
                    mMembersAmount = mMembersCount * 50;
                }
                mTextViewMembersAmount.setText(String.valueOf(mMembersAmount));
                calculateAmount();
                break;
        }
    }

    private void calculateAmount() {
        int tempAmount = mBaseAmount + mMembersAmount + mMainCourseAmount;
        mServiceTaxAmount = tempAmount*.125;
        mTextViewServiceTaxAmount.setText(String.valueOf(mServiceTaxAmount));
        mTotalAmount = (mServiceTaxAmount +tempAmount);
        mTextViewTotalAmount.setText(String.valueOf(mTotalAmount));
    }

    @Override
    public void onPause() {

        super.onPause();
        Log.v("wai","onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("wai","onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("wai","onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("wai","onStop");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v("wai","onSaveInstanceState data saved");
        outState.putInt("membercount", mMembersCount);
        outState.putInt("memberamount", mMembersAmount);
        outState.putInt("maincoursecount", mMainCourseCount);
        outState.putInt("maincourseamount", mMainCourseAmount);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.v("wai","onViewStateRestored");
        if(savedInstanceState != null){
            Log.v("wai","if");
            mMembersCount = savedInstanceState.getInt("membercount");
            mMainCourseCount = savedInstanceState.getInt("maincoursecount");
            mMembersAmount = savedInstanceState.getInt("memberamount");
            mMainCourseAmount = savedInstanceState.getInt("maincourseamount");
        }else{
            Log.v("wai","else");
            mMembersCount = 2;
            mMainCourseCount = 2;
            mMembersAmount = 100;
            mMainCourseAmount = 50;
        }

    }
}