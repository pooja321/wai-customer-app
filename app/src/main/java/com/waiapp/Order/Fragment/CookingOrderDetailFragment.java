package com.waiapp.Order.Fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.waiapp.Model.Address;
import com.waiapp.Model.CookingOrderAmountValues;
import com.waiapp.Model.Order;
import com.waiapp.Model.OrderAmount;
import com.waiapp.R;
import com.waiapp.Utility.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.waiapp.Utility.Utilities.getUid;

public class CookingOrderDetailFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_ORDERKEY = "OrderKey";
    private String mParamOrderKey, mStatus;

    private DatabaseReference mDatabase;
    private Order mOrder;
    private OrderAmount mOrderAmount;
    private CookingOrderAmountValues mCookingOrderAmountValues;
    private Address mAddress;
    private TextView mTextViewOrderId, mTextViewOrderStatus, mTextViewOrderDate, mTextViewMembersAmount, mTextViewMainCourseAmount,mTextViewBaseAmount, mTextViewServiceTaxAmount, mTextViewTotalAmount, mTextViewPaymentMode;
    private TextView mTextViewAddressName, mTextViewHouseNo, mTextViewAreaName, mTextViewLandMark, mTextViewCity, mTextViewState,
            mTextViewPincode;
    private Button mButtonCancel;

    public CookingOrderDetailFragment() {
        // Required empty public constructor
    }

    public static CookingOrderDetailFragment newInstance(String param1) {
        CookingOrderDetailFragment fragment = new CookingOrderDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ORDERKEY, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamOrderKey = getArguments().getString(ARG_ORDERKEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_cooking_order_detail, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(Constants.FIREBASE_CHILD_ORDER_DETAILS).child(mParamOrderKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.v("wai", dataSnapshot.getValue().toString());

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            switch (ds.getKey()) {
                                case "Order":
                                    mOrder = ds.getValue(Order.class);
                                    mStatus = mOrder.getOrderStatus();
                                    break;
                                case "OrderAmount":
                                    mCookingOrderAmountValues = ds.getValue(CookingOrderAmountValues.class);
//                                    mOrderAmount = ds.getValue(OrderAmount.class);
                                    break;
                                case "Address":
                                    mAddress = ds.getValue(Address.class);
                                    break;
                            }
                        }
                        initializeUi(view);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return view;
    }

    private void initializeUi(View view) {
        Log.v("wai", "initializeUI");
        mTextViewOrderId = (TextView) view.findViewById(R.id.cookorderdetail_orderid);
        mTextViewOrderStatus = (TextView) view.findViewById(R.id.cookorderdetail_orderstatus);
        mTextViewOrderDate = (TextView) view.findViewById(R.id.cookorderdetail_orderdate);
        mTextViewMembersAmount = (TextView) view.findViewById(R.id.cookorderdetail_membersamount);
        mTextViewMainCourseAmount = (TextView) view.findViewById(R.id.cookorderdetail_maincourse);
        mTextViewBaseAmount = (TextView) view.findViewById(R.id.cookorderdetail_tv_baseamount);
        mTextViewServiceTaxAmount = (TextView) view.findViewById(R.id.cookorderdetail_servicetaxamount);
        mTextViewTotalAmount = (TextView) view.findViewById(R.id.cookorderdetail_totalamount);
        mTextViewPaymentMode = (TextView) view.findViewById(R.id.cookorderdetail_paymentmode);

        mTextViewAddressName = (TextView) view.findViewById(R.id.cookorderdetail_addressname);
        mTextViewHouseNo = (TextView) view.findViewById(R.id.cookorderdetail_houseno);
        mTextViewAreaName = (TextView) view.findViewById(R.id.cookorderdetail_areaname);
        mTextViewLandMark = (TextView) view.findViewById(R.id.cookorderdetail_landmark);
        mTextViewCity = (TextView) view.findViewById(R.id.cookorderdetail_cityname);
        mTextViewState = (TextView) view.findViewById(R.id.cookorderdetail_statename);
        mTextViewPincode = (TextView) view.findViewById(R.id.cookorderdetail_pincode);

        mTextViewOrderId.setText(mOrder.getOrderId());
        mTextViewOrderStatus.setText(mStatus);
        mTextViewPaymentMode.setText(mOrder.getPaymentMode());
        mTextViewMembersAmount.setText(String.valueOf(mCookingOrderAmountValues.getMembersAmount()));
        mTextViewMainCourseAmount.setText(String.valueOf(mCookingOrderAmountValues.getMainCourseAmount()));
        mTextViewBaseAmount.setText(String.valueOf(mCookingOrderAmountValues.getBaseAmount()));
        mTextViewServiceTaxAmount.setText(String.valueOf(mCookingOrderAmountValues.getServiceTaxAmount()));
        mTextViewTotalAmount.setText(String.valueOf(mCookingOrderAmountValues.getTotalAmount()));

        mTextViewAddressName.setText(String.valueOf(mAddress.getAddressName()));
        mTextViewHouseNo.setText(String.valueOf(mAddress.getHouseNo()));
        mTextViewAreaName.setText(String.valueOf(mAddress.getAreaName()));
        mTextViewLandMark.setText(String.valueOf(mAddress.getLandmark()));
        mTextViewCity.setText(String.valueOf(mAddress.getCity()));
        mTextViewState.setText(String.valueOf(mAddress.getState()));
        mTextViewPincode.setText(String.valueOf(mAddress.getPincode()));

        mButtonCancel = (Button) view.findViewById(R.id.cookorderdetail_bt_cancel);
        mButtonCancel.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Objects.equals(mStatus, Constants.ORDER_STATUS_INPROGRESS) || Objects.equals(mStatus, Constants.ORDER_STATUS_ORDERED)) {
                mButtonCancel.setVisibility(View.VISIBLE);
            } else {
                mButtonCancel.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cookorderdetail_bt_cancel:
                cancelOrder();
        }
    }

    private void cancelOrder() {

        Map<String, Object> OrderUpdates = new HashMap<>();
        OrderUpdates.put("orderStatus", Constants.ORDER_STATUS_CANCELLED);

        mDatabase.child(Constants.FIREBASE_CHILD_ORDER_DETAILS).child(mParamOrderKey)
                .child(Constants.FIREBASE_CHILD_ORDER).updateChildren(OrderUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (!(databaseError == null)) {
                    Toast.makeText(getActivity(), "user order history status update failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "user order history status update successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDatabase.child(Constants.FIREBASE_CHILD_RESOURCE_ORDERS).child(mOrder.getResourceId()).child(mParamOrderKey).updateChildren(OrderUpdates);
        mDatabase.child(Constants.FIREBASE_CHILD_USER_ORDERS).child(getUid()).child(mParamOrderKey).updateChildren(OrderUpdates);
    }
}