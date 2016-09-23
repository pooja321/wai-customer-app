package com.waiapp.Order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.waiapp.Model.Order;
import com.waiapp.R;
import com.waiapp.Utility.Constants;
import com.waiapp.Utility.Utilities;

public class OrderHistoryActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    ChildEventListener childEventListener;
    Query resourceQuery;
    private RecyclerView recyclerView;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<Order, OrderViewHolder> mAdapter;
    private Toolbar mtoolbar;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("wai", "OrderHistoryActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

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
        recyclerView = (RecyclerView) findViewById(R.id.order_history_recyclerview);

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mManager);
        resourceQuery = mDatabase.child(Constants.FIREBASE_CHILD_USER_ORDERS).child(UID).orderByKey();
        initFirebaseUI(resourceQuery);
    }

    private void initFirebaseUI(Query resourceQuery) {

        mAdapter = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(Order.class,R.layout.list_order_item,
                OrderViewHolder.class,resourceQuery) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, final Order model, final int position) {
                final DatabaseReference resourceRef = getRef(position);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(OrderHistoryActivity.this, OrderDetailActivity.class).putExtra(OrderDetailActivity.ORDER_KEY,resourceRef.getKey()));
                    }
                });
                viewHolder.bindView(model);
            }
        };
        recyclerView.setAdapter(mAdapter);
    }
}