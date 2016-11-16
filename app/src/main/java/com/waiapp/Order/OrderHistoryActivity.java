package com.waiapp.Order;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.waiapp.BaseActivity;
import com.waiapp.Model.Order;
import com.waiapp.R;
import com.waiapp.Utility.Constants;
import com.waiapp.Utility.Utilities;

public class OrderHistoryActivity extends BaseActivity {

    private DatabaseReference mDatabase;
    private ProgressDialog mProgressDialog;
    Query mResourceQuery;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<Order, OrderViewHolder> mAdapter;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("wai", "OrderHistoryActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        setTitle("Order History");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UID = Utilities.getUid();
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mRecyclerView = (RecyclerView) findViewById(R.id.order_history_recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mResourceQuery = mDatabase.child(Constants.FIREBASE_CHILD_USER_ORDERS).child(UID).orderByKey();
        mResourceQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
            }
        });
        mAdapter = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(Order.class,R.layout.list_order_item,
                OrderViewHolder.class,mResourceQuery) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, final Order model, final int position) {
                final DatabaseReference resourceRef = getRef(position);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String orderType = model.getOrderType();
                        startActivity(new Intent(OrderHistoryActivity.this, OrderDetailActivity.class)
                                .putExtra(OrderDetailActivity.ORDER_KEY,resourceRef.getKey())
                                .putExtra(OrderDetailActivity.ORDER_TYPE, orderType));
                    }
                });
                viewHolder.bindView(model);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }
}