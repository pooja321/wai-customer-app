package com.waiapp.Address;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.waiapp.Model.Address;
import com.waiapp.R;
import com.waiapp.Utility.Constants;
import com.waiapp.Utility.common;
import com.waiapp.payment.PaymentActivity;

import java.util.HashMap;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    Query resourceQuery;
    String UID;
    private FirebaseRecyclerAdapter<Address, AddressViewHolder> mAdapter;
    private Toolbar mtoolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager mManager;
    String orderKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        orderKey = getIntent().getStringExtra("Order_key");
        Log.v("wai", "address oncreate");
        mtoolbar = (Toolbar) findViewById(R.id.address_toolbar);
        mtoolbar.setTitleTextColor(getResources().getColor( R.color.white));
        setSupportActionBar(mtoolbar);
        setTitle("Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.address_listview);
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mManager);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UID = common.getUid();
        }

        resourceQuery = mDatabase.child(Constants.CHILD_ADDRESS).child(UID);
        initFirebaseUI(resourceQuery);
    }

    private void initFirebaseUI(Query resourceQuery) {
        mAdapter = new FirebaseRecyclerAdapter<Address, AddressViewHolder>(Address.class,R.layout.list_address_item,
                AddressViewHolder.class,resourceQuery) {
            @Override
            protected void populateViewHolder(AddressViewHolder viewHolder, final Address model, final int position) {
                final DatabaseReference resourceRef = getRef(position);
//                final OnResourceSelectedInterface listener = (OnResourceSelectedInterface) getActivity();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        listener.onListResourceSelected(model,callingFragment);
                        updateAddressInorder(resourceRef.getKey());
                    }
                });
                viewHolder.bindView(model);
            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    private void updateAddressInorder(String key) {
        Map<String, Object> OrderUpdates = new HashMap<>();
        OrderUpdates.put("customerAddressId",key);
        OrderUpdates.put("orderStatus",Constants.ORDER_STATUS_PAYMENT_PENDING);
        mDatabase.child(Constants.CHILD_ORDER).child(orderKey).updateChildren(OrderUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(!(databaseError == null)){
                    Toast.makeText(AddressActivity.this, "Address updated failed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddressActivity.this, "Address updated successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddressActivity.this, PaymentActivity.class).putExtra("orderKey",orderKey));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_address) {
            startActivity(new Intent(this, AddAddressActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
