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
import com.waiapp.Model.CleaningOrderAmountValues;
import com.waiapp.Model.Order;
import com.waiapp.Model.OrderAmount;
import com.waiapp.R;
import com.waiapp.Utility.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.waiapp.Utility.Utilities.getUid;

public class CleaningOrderDetailFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_ORDERKEY = "param1";

    private String mParamOrderKey, mStatus;

    private DatabaseReference mDatabase;
    private Order mOrder;
    private OrderAmount mOrderAmount;
    private CleaningOrderAmountValues mCleaningOrderAmountValues;
    private Address mAddress;
    private TextView mTextViewOrderId, mTextViewOrderStatus, mTextViewOrderDate, mTextViewRoomsAmount, mTextViewWashroomsAmount, mTextViewUtensilBucketAmount, mTextViewServiceTaxAmount, mTextViewTotalAmount, mTextViewPaymentMode;
    private TextView mTextViewAddressName, mTextViewHouseNo, mTextViewAreaName, mTextViewLandMark, mTextViewCity, mTextViewState,
            mTextViewPincode;
    private Button mButtonCancel;
    HashMap<String, Object> orderBookingTime;

    public CleaningOrderDetailFragment() {
        // Required empty public constructor
    }

    public static CleaningOrderDetailFragment newInstance(String param1) {
        CleaningOrderDetailFragment fragment = new CleaningOrderDetailFragment();
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
        final View view = inflater.inflate(R.layout.fragment_cleaning_order_detail, container, false);
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
                            mCleaningOrderAmountValues = ds.getValue(CleaningOrderAmountValues.class);
//                                    mOrderAmount = ds.getValue(OrderAmount.class);
                            break;
                        case "Address":
                            mAddress = ds.getValue(Address.class);
                            break;
                    }
                }
                initializeUI(view);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void initializeUI(View view) {
        Log.v("wai", "initializeUI");
        mTextViewOrderId = (TextView) view.findViewById(R.id.cleanorderdetail_orderid);
        mTextViewOrderStatus = (TextView) view.findViewById(R.id.cleanorderdetail_orderstatus);
        mTextViewOrderDate = (TextView) view.findViewById(R.id.cleanorderdetail_orderdate);
        mTextViewRoomsAmount = (TextView) view.findViewById(R.id.cleanorderdetail_roomsamount);
        mTextViewWashroomsAmount = (TextView) view.findViewById(R.id.cleanorderdetail_washroomamount);
        mTextViewUtensilBucketAmount = (TextView) view.findViewById(R.id.cleanorderdetail_utensilbucket);
        mTextViewServiceTaxAmount = (TextView) view.findViewById(R.id.cleanorderdetail_servicetaxamount);
        mTextViewTotalAmount = (TextView) view.findViewById(R.id.cleanorderdetail_finalamount);
        mTextViewPaymentMode = (TextView) view.findViewById(R.id.cleanorderdetail_paymentmode);

        mTextViewAddressName = (TextView) view.findViewById(R.id.cleanorderdetail_addressname);
        mTextViewHouseNo = (TextView) view.findViewById(R.id.cleanorderdetail_houseno);
        mTextViewAreaName = (TextView) view.findViewById(R.id.cleanorderdetail_areaname);
        mTextViewLandMark = (TextView) view.findViewById(R.id.cleanorderdetail_landmark);
        mTextViewCity = (TextView) view.findViewById(R.id.cleanorderdetail_cityname);
        mTextViewState = (TextView) view.findViewById(R.id.cleanorderdetail_statename);
        mTextViewPincode = (TextView) view.findViewById(R.id.cleanorderdetail_pincode);

        mTextViewOrderId.setText(mOrder.getOrderId());
        mTextViewOrderStatus.setText(mStatus);
        orderBookingTime = mOrder.getOrderbookingTime();
        Long timestamp = (Long) orderBookingTime.get(Constants.FIREBASE_PROPERTY_TIMESTAMP);
        Date date = new Date(timestamp);
        Log.v("wai", String.valueOf(timestamp));
        Log.v("wai", String.valueOf(date));
        SimpleDateFormat sfd = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);
        mTextViewOrderDate.setText(sfd.format(date));

        mTextViewPaymentMode.setText(mOrder.getPaymentMode());
        mTextViewRoomsAmount.setText(String.valueOf(mCleaningOrderAmountValues.getRoomsAmount()));
        mTextViewWashroomsAmount.setText(String.valueOf(mCleaningOrderAmountValues.getWashroomsAmount()));
        mTextViewUtensilBucketAmount.setText(String.valueOf(mCleaningOrderAmountValues.getBaseAmount()));
        mTextViewServiceTaxAmount.setText(String.valueOf(mCleaningOrderAmountValues.getServiceTaxAmount()));
        mTextViewTotalAmount.setText(String.valueOf(mCleaningOrderAmountValues.getTotalAmount()));

        mTextViewAddressName.setText(String.valueOf(mAddress.getAddressName()));
        mTextViewHouseNo.setText(String.valueOf(mAddress.getHouseNo()));
        mTextViewAreaName.setText(String.valueOf(mAddress.getAreaName()));
        mTextViewLandMark.setText(String.valueOf(mAddress.getLandmark()));
        mTextViewCity.setText(String.valueOf(mAddress.getCity()));
        mTextViewState.setText(String.valueOf(mAddress.getState()));
        mTextViewPincode.setText(String.valueOf(mAddress.getPincode()));

        mButtonCancel = (Button) view.findViewById(R.id.cleanorderdetail_bt_cancel);
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
            case R.id.cleanorderdetail_bt_cancel:
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
