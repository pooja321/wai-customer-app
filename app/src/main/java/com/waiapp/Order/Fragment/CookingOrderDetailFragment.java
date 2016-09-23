package com.waiapp.Order.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import static com.waiapp.Utility.Utilities.getUid;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CookingOrderDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CookingOrderDetailFragment extends Fragment {

    private static final String ARG_ORDERKEY = "OrderKey";

    private String mParamOrderKey;

    private DatabaseReference mDatabase;
    private Order mOrder;
    private OrderAmount mOrderAmount;
    private Address mAddress;
    private TextView mTextViewOrderType, mTextViewOrderId;

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
        mDatabase.child(Constants.FIREBASE_CHILD_USER_ORDER_DETAIL).child(getUid()).child(mParamOrderKey)
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

        mTextViewOrderType = (TextView) view.findViewById(R.id.cookorderdetail_ordertype);
        mTextViewOrderId = (TextView) view.findViewById(R.id.cookorderdetail_orderid);

        mTextViewOrderType.setText(mOrder.getOrderType());
        mTextViewOrderId.setText(mOrder.getOrderId());
    }

}
