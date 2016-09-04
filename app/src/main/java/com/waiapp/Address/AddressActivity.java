package com.waiapp.Address;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.waiapp.Model.Address;
import com.waiapp.Model.Order;
import com.waiapp.Model.OrderAmount;
import com.waiapp.R;
import com.waiapp.Utility.Constants;
import com.waiapp.Utility.Utilities;
import com.waiapp.payment.PaymentActivity;

import java.util.HashMap;

public class AddressActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    Query resourceQuery;
    String UID;
    private FirebaseRecyclerAdapter<Address, AddressViewHolder> mAdapter;
    private Toolbar mtoolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager mManager;
    String mOrderType, mResourceKey;
    double mTotalAmount;
    OrderAmount mOrderAmount = new OrderAmount();
    Address mAddress = new Address();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        mTotalAmount = getIntent().getDoubleExtra("totalAmount", 0);
        mOrderType = getIntent().getStringExtra("orderType");
        mResourceKey = getIntent().getStringExtra("resourceKey");
        mOrderAmount = (OrderAmount) getIntent().getSerializableExtra("OrderAmount");
        mtoolbar = (Toolbar) findViewById(R.id.address_toolbar);
        mtoolbar.setTitleTextColor(getResources().getColor( R.color.white));

        setSupportActionBar(mtoolbar);
        setTitle("Select Service Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.address_listview);
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mManager);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UID = Utilities.getUid();
        }

        resourceQuery = mDatabase.child(Constants.FIREBASE_CHILD_ADDRESS).child(UID);
        initFirebaseUI(resourceQuery);
    }

    private void initFirebaseUI(Query resourceQuery) {
        mAdapter = new FirebaseRecyclerAdapter<Address, AddressViewHolder>(Address.class,R.layout.list_address_item,
                AddressViewHolder.class,resourceQuery) {
            @Override
            protected void populateViewHolder(AddressViewHolder viewHolder, final Address model, final int position) {
                final DatabaseReference addressRef = getRef(position);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createOrder(addressRef.getKey());
                        mAddress = model;
                    }
                });
                viewHolder.bindView(model);
            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    private void createOrder(String addressKey) {
        String _UID = Utilities.getUid();
        HashMap<String, Object> orderCreationTime = new HashMap<>();
        orderCreationTime.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        final String _orderKey = mDatabase.child(Constants.FIREBASE_CHILD_ORDER).child(_UID).push().getKey();
        final Order order = new Order("11011", mOrderType, _UID, mResourceKey,addressKey,Constants.ORDER_STATUS_INCOMPLETE,
                Constants.ORDER_PROGRESS_STATUS_PAYMENT_PENDING,null,mTotalAmount,orderCreationTime,null,null,null,false);
        mDatabase.child(Constants.FIREBASE_CHILD_ORDER).child(_UID).child(_orderKey).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(AddressActivity.this, "Order saving failed", Toast.LENGTH_SHORT).show();
                }else{
                    addOrderAmount(_orderKey);
                    Toast.makeText(AddressActivity.this, "Order Saved successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddressActivity.this, PaymentActivity.class)
                            .putExtra("orderKey",_orderKey).putExtra("order",order)
                            .putExtra("OrderAmount",mOrderAmount).putExtra("Address",mAddress));
                }
            }
        });
    }

    private void addOrderAmount(String _orderKey) {
        mOrderAmount.setOrderId(_orderKey);
        mDatabase.child(Constants.FIREBASE_CHILD_ORDER_AMOUNT).child(_orderKey).setValue(mOrderAmount);
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
        if (id== android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
