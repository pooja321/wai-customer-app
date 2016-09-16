package com.waiapp.Order;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.waiapp.Model.Order;
import com.waiapp.Model.OrderKey;
import com.waiapp.R;
import com.waiapp.Realm.RealmController;
import com.waiapp.Utility.Constants;
import com.waiapp.Utility.Utilities;

import io.realm.Realm;

public class OrderHistoryActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    Query resourceQuery;
    private RecyclerView recyclerView;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<Order, OrderViewHolder> mAdapter;
    private Toolbar mtoolbar;
    String UID, currentOrderKey;
    private TextView mTextViewCurrentOrderId, mTextViewCurrentOrderType, mTextViewCurrentOrderStatus, mTextViewCurrentOrder;
    private Button mButtonCancelCurrentOrder;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("wai", "OrderHistoryActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        this.realm = RealmController.with(this).getRealm();
        OrderKey orderKey = realm.where(OrderKey.class).findFirst();
        currentOrderKey = orderKey.getOrderkey();
        Log.v("wai", "current order key: " + currentOrderKey);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UID = Utilities.getUid();
        }

        mtoolbar = (Toolbar) findViewById(R.id.order_history_toolbar);
        mtoolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mtoolbar);
        setTitle("Order History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTextViewCurrentOrder = (TextView) findViewById(R.id.order_history_Currentorder);
        mTextViewCurrentOrderId = (TextView) findViewById(R.id.order_history_currentorder_id);
        mTextViewCurrentOrderType = (TextView) findViewById(R.id.order_history_currentorder_type);
        mTextViewCurrentOrderStatus = (TextView) findViewById(R.id.order_history_currentorder_status);
        mButtonCancelCurrentOrder = (Button) findViewById(R.id.order_history_cancel_button);
        mTextViewCurrentOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.order_history_recyclerview);

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mManager);
        initCurrentOrderUI();
//        initFirebaseUI(resourceQuery);
    }

    private void initCurrentOrderUI() {
        Log.v("wai", "initCurrentOrderUI");

        mDatabase.child(Constants.FIREBASE_CHILD_ORDERS).child(currentOrderKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String orderid = dataSnapshot.getKey();
                Order order = dataSnapshot.getValue(Order.class);
                mTextViewCurrentOrderId.setText(orderid);
                mTextViewCurrentOrderType.setText(order.getOrderType());
                mTextViewCurrentOrderStatus.setText(order.getOrderStatus());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initFirebaseUI(Query resourceQuery) {

//        mAdapter = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(Order.class,R.layout.list_order_item,
//                OrderViewHolder.class,resourceQuery) {
//            @Override
//            protected void populateViewHolder(OrderViewHolder viewHolder, final Order model, final int position) {
//                final DatabaseReference resourceRef = getRef(position);
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(OrderHistoryActivity.this, OrderDetailActivity.class));
//                    }
//                });
//                viewHolder.bindView(model);
//            }
//        };
//        recyclerView.setAdapter(mAdapter);
    }
}