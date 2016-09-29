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
    private Address mAddress;
    private TextView mTextViewOrderType, mTextViewOrderId;
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
        final View view = inflater.inflate(R.layout.fragment_cooking_order, container, false);
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
                                    break;
                                case "OrderAmount":
                                    mOrderAmount = ds.getValue(OrderAmount.class);
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

        mStatus = mOrder.getOrderStatus();
        mTextViewOrderType = (TextView) view.findViewById(R.id.cookorderdetail_ordertype);
        mTextViewOrderId = (TextView) view.findViewById(R.id.cookorderdetail_orderid);
        mButtonCancel = (Button) view.findViewById(R.id.cookorderdetail_bt_cancel);
        mButtonCancel.setOnClickListener(this);
        mTextViewOrderType.setText(mOrder.getOrderType());
        mTextViewOrderId.setText(mOrder.getOrderId());

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
