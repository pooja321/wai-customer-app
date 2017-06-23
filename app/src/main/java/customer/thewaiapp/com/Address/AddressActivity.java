package customer.thewaiapp.com.Address;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import customer.thewaiapp.com.Model.Address;
import customer.thewaiapp.com.Model.Order;
import customer.thewaiapp.com.R;
import customer.thewaiapp.com.Utility.Constants;
import customer.thewaiapp.com.Utility.Utilities;
import customer.thewaiapp.com.payment.PaymentActivity;

public class AddressActivity extends AppCompatActivity {

    public static final String KEY_RESOURCE_KEY = "resourceKey";
    public static final String RESOURCE_MOBILE = "ResourceMobile";
    public static final String KEY_TOTAL_AMOUNT = "totalAmount";
    public static final String KEY_ORDER_TYPE = "orderType";
    public static final String KEY_ORDER_ID = "orderId";
    String mOrderType, mResourceKey, mOrderId, UID;
    Long ResourceMobile;
    String ResourceName,ResourceType;
    double mTotalAmount;

    private DatabaseReference mDatabase;
    Query mResourceQuery;
    private FirebaseRecyclerAdapter<Address, AddressViewHolder> mAdapter;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mManager;
    ProgressDialog mProgressDialog;
    Address mAddress = new Address();
    TextView mTextView_NoAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        mTextView_NoAddress = (TextView) findViewById(R.id.address_noaddress);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mResourceKey = getIntent().getStringExtra(KEY_RESOURCE_KEY);
        mTotalAmount = getIntent().getDoubleExtra(KEY_TOTAL_AMOUNT, 0);
        mOrderType = getIntent().getStringExtra(KEY_ORDER_TYPE);
        mOrderId = getIntent().getStringExtra(KEY_ORDER_ID);
        ResourceMobile = getIntent().getLongExtra(RESOURCE_MOBILE,0);
        ResourceName = getIntent().getStringExtra("ResourceName");
        ResourceType = getIntent().getStringExtra("ResourceType");
        mToolbar = (Toolbar) findViewById(R.id.address_toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(mToolbar);
        setTitle("Select Service Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.address_listview);
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setHasFixedSize(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UID = Utilities.getUid();
        }

        mResourceQuery = mDatabase.child(Constants.FIREBASE_CHILD_ADDRESS).child(UID);
        mResourceQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
        initFirebaseUI(mResourceQuery);
    }

    private void initFirebaseUI(Query resourceQuery) {
        mAdapter = new FirebaseRecyclerAdapter<Address, AddressViewHolder>(Address.class, R.layout.list_address_item,
                AddressViewHolder.class, resourceQuery) {
            @Override
            protected void populateViewHolder(AddressViewHolder viewHolder, final Address model, final int position) {
                mTextView_NoAddress.setVisibility(View.GONE);
                final DatabaseReference addressRef = getRef(position);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAddress = model;
                        createOrder(addressRef.getKey());
                    }
                });
                viewHolder.bindView(model);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    private void createOrder(String addressKey) {
        String _UID = Utilities.getUid();
        HashMap<String, Object> orderCreationTime = new HashMap<>();
        orderCreationTime.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        final Order order = new Order(mOrderId, mOrderType, _UID, mResourceKey, addressKey, Constants.ORDER_STATUS_INCOMPLETE,
                Constants.ORDER_PROGRESS_STATUS_PAYMENT_PENDING, null, mTotalAmount, orderCreationTime, null, null, null, false);
        startActivity(new Intent(AddressActivity.this, PaymentActivity.class)
                .putExtra("order", order)
                .putExtra("Address", mAddress)
                .putExtra("ResourceName", ResourceName)
                .putExtra("ResourceType",ResourceType)
                .putExtra("ResourceMobileNumber",ResourceMobile )
        );

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
        if (id == android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_RESOURCE_KEY, mResourceKey);
        outState.putDouble(KEY_TOTAL_AMOUNT, mTotalAmount);
        outState.putString(KEY_ORDER_TYPE, mOrderType);
        outState.putString(KEY_ORDER_ID, mOrderId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mResourceKey = savedInstanceState.getString(KEY_RESOURCE_KEY);
            mTotalAmount = savedInstanceState.getDouble(KEY_TOTAL_AMOUNT);
            mOrderType = savedInstanceState.getString(KEY_ORDER_TYPE);
            mOrderId = savedInstanceState.getString(KEY_ORDER_ID);
        }
    }
}
