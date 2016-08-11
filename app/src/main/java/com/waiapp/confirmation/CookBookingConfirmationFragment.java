package com.waiapp.confirmation;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.waiapp.Address.AddressActivity;
import com.waiapp.Model.Order;
import com.waiapp.Model.Resource;
import com.waiapp.R;
import com.waiapp.Utility.Constants;
import com.waiapp.Utility.Utilities;
import com.waiapp.WaiApplication;

public class CookBookingConfirmationFragment extends Fragment implements View.OnClickListener {

    TextView mTextViewMembersCount,mTextViewMainCourseCount,mTextViewMembersAmount, mTextViewMainCourseAmount,
            mTextViewBaseAmount, mTextViewServiceTaxAmount, mTextViewTotalAmount, mTextViewResourceName;
    Button mButtonIncrementMembers,mButtonDecrementMembers, mButtonIncrementMainCourse,mButtonDecrementMainCourse,
            mButtonConfirm;
    CheckBox mCheckBoxTerms;
    int baseAmount = 50;
    int membersCount, mainCourseCount;
    int membersAmount,mainCourseAmount;
    double totalAmount;

    private OnUserSignUpRequired listener;
    private DatabaseReference mDatabase;
    WaiApplication app;

    private static final String ARG_KEY = "key";
    private static final String ARG_RESOURCE = "resource";

    private String mParamKey;
    private Resource mParamResource;

    // callback interface to implement on item list click listener
    public interface OnUserSignUpRequired{
        void UserSignUpRequired();
    }

    public CookBookingConfirmationFragment() {
        // Required empty public constructor
    }

    public static CookBookingConfirmationFragment newInstance(String key, Resource resource) {
        CookBookingConfirmationFragment fragment = new CookBookingConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putSerializable(ARG_RESOURCE, resource);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamKey = getArguments().getString(ARG_KEY);
            mParamResource = (Resource) getArguments().getSerializable(ARG_RESOURCE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v("wai","oncreateView");
        View view =  inflater.inflate(R.layout.fragment_cook_booking_confirmation, container, false);
        listener = (OnUserSignUpRequired) getActivity();
        app = (WaiApplication) getActivity().getApplication();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(savedInstanceState != null){
            Log.v("wai","if");
            membersCount = savedInstanceState.getInt("membercount");
            mainCourseCount = savedInstanceState.getInt("maincoursecount");
            membersAmount = savedInstanceState.getInt("memberamount");
            mainCourseAmount = savedInstanceState.getInt("maincourseamount");
        }else{
            Log.v("wai","else");
            membersCount = 2;
            mainCourseCount = 2;
            membersAmount = 100;
            mainCourseAmount = 50;
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v("wai","onViewCreated");
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

        String _firstName = "First Name";
        if(!(mParamResource == null)){
            _firstName = mParamResource.getFirstName();
        }
        String _lastName = "Last Name";
        if(!(mParamResource== null)){
            _lastName = mParamResource.getLastName();
        }

        mTextViewResourceName.setText(String.format("%s %s", _firstName, _lastName));
        mTextViewMembersCount.setText(String.valueOf(membersCount));
        mTextViewMainCourseCount.setText(String.valueOf(mainCourseCount));
        mTextViewMembersAmount.setText(String.valueOf(membersAmount));
        mTextViewMainCourseAmount.setText(String.valueOf(mainCourseAmount));
        mTextViewBaseAmount.setText(String.valueOf(baseAmount));
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
                        createOrder();
                    } else {
                        // User is signed out
                        Toast.makeText(getActivity(), "Please Login First", Toast.LENGTH_SHORT).show();
                        app.setOrderPending(true);
                        listener.UserSignUpRequired();
                    }
                } else{
                    Toast.makeText(getActivity(), "Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
                }

                break;
            case(R.id.cook_booking_bt_maincourse_count_decrement):
                if(mainCourseCount > 2) {
                    mainCourseCount = mainCourseCount - 1;
                    if(!(mainCourseCount == 2)){
                        mainCourseAmount = mainCourseCount * 50;
                    }
                }
                if(mainCourseCount <= 2){
                    mainCourseAmount = 50;
                }
                mTextViewMainCourseCount.setText(String.valueOf(mainCourseCount));
                mTextViewMainCourseAmount.setText(String.valueOf(mainCourseAmount));
                calculateAmount();
                break;
            case(R.id.cook_booking_bt_maincourse_count_increment):
                mainCourseCount = mainCourseCount + 1;
                mTextViewMainCourseCount.setText(String.valueOf(mainCourseCount));
                if(mainCourseCount > 2){
                    mainCourseAmount = 50 + (mainCourseCount -2) * 50;
                }
                mTextViewMainCourseAmount.setText(String.valueOf(mainCourseAmount));
                calculateAmount();
                break;
            case(R.id.cook_booking_bt_members_count_decrement):
                if(membersCount > 2){
                    membersCount = membersCount - 1;
                    membersAmount = membersCount * 50;
                }
                mTextViewMembersCount.setText(String.valueOf(membersCount));
                mTextViewMembersAmount.setText(String.valueOf(membersAmount));
                calculateAmount();
                break;
            case(R.id.cook_booking_bt_members_count_increment):
                membersCount = membersCount + 1;
                mTextViewMembersCount.setText(String.valueOf(membersCount));
                if(membersCount >= 2){
                    membersAmount = membersCount * 50;
                }
                mTextViewMembersAmount.setText(String.valueOf(membersAmount));
                calculateAmount();
                break;
        }
    }

    private void createOrder() {
        final String key = mDatabase.child(Constants.CHILD_ORDER).push().getKey();
        Order order = new Order("11011", "Cooking", Utilities.getUid(),mParamKey,null,Constants.ORDER_STATUS_ADDRESS_NOT_SET,null,
                String.valueOf(totalAmount),null,null,null,null,null);
        mDatabase.child(Constants.CHILD_ORDER).child(key).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getActivity(), "Order saving failed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Order saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), AddressActivity.class);
                    intent.putExtra("Order_key",key);
                    startActivity(intent);
                }
            }
        });
    }

    private void calculateAmount() {
        int tempAmount = baseAmount + membersAmount + mainCourseAmount;
        double serviceTaxAmount = tempAmount*.125;
        mTextViewServiceTaxAmount.setText(String.valueOf(serviceTaxAmount));
        totalAmount = (serviceTaxAmount+tempAmount);
        mTextViewTotalAmount.setText(String.valueOf(totalAmount));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v("wai","data saved");
        outState.putInt("membercount", membersCount);
        outState.putInt("memberamount", membersAmount);
        outState.putInt("maincoursecount", mainCourseCount);
        outState.putInt("maincourseamount", mainCourseAmount);
        super.onSaveInstanceState(outState);
    }
}