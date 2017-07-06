package customer.thewaiapp.com.confirmation;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import customer.thewaiapp.com.Address.AddressActivity;
import customer.thewaiapp.com.Model.Coupon;
import customer.thewaiapp.com.Model.WashingOrderAmountValues;
import customer.thewaiapp.com.R;
import customer.thewaiapp.com.Realm.RealmController;
import customer.thewaiapp.com.Utility.Constants;
import customer.thewaiapp.com.Utility.Utilities;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class WashBookingConfirmationFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_KEY = "ResourceKey";
    private static final String ARG_RESOURCE = "ResourceName";
    private static final String ARG_RESOURCE_MOBILE = "ResourceMobile";
    int mBucketCount, mBucketAmount, mBaseAmount = 50;
    private String mParamResourceName, mParamResourceKey, mOrderId;
    double mTotalAmount, mServiceTaxAmount;

    TextView mTextViewResourceName, mTextViewBucketCount, mTextViewBucketAmount, mTextViewBaseAmount, mTextViewServiceTaxAmount, mTextViewTotalAmount;
    Button mButtonIncrementBucket, mButtonDecrementBucket, mButtonConfirm, mButtonApplycoupon;
    EditText mEdittextApplyCoupon;
    CheckBox mCheckBoxTerms;
    private Long mParamResourceMobile;
    private OnUserSignUpRequired listener;
    Realm realm;
    private DatabaseReference mDatabase;
    Button mbtnweightchart;

    // callback interface to implement on item list click listener
    public interface OnUserSignUpRequired {
        void UserSignUpRequired();
    }

    public WashBookingConfirmationFragment() {
        // Required empty public constructor
    }

    public static WashBookingConfirmationFragment newInstance(String key, String resourceName, Long mResourceMobile) {
        WashBookingConfirmationFragment fragment = new WashBookingConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        args.putString(ARG_RESOURCE, resourceName);
        args.putLong(ARG_RESOURCE_MOBILE, mResourceMobile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamResourceKey = getArguments().getString(ARG_KEY);
//            mParamResource = (ResourceOnline) getArguments().getSerializable(ARG_RESOURCE);
            mParamResourceName = getArguments().getString(ARG_RESOURCE);
            mParamResourceMobile = getArguments().getLong(ARG_RESOURCE_MOBILE);
        }
        if (savedInstanceState == null) {
            mBucketCount = 1;
            mBucketAmount = 100;
        }
        this.realm = RealmController.with(this).getRealm();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wash_booking_confirmation, container, false);
        mBucketCount = 1;
        mBucketAmount = 100;
        listener = (OnUserSignUpRequired) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mbtnweightchart= (Button) view.findViewById(R.id.wash_booking_btn_weightchart);
        mCheckBoxTerms = (CheckBox) view.findViewById(R.id.wash_booking_cb_terms);
        mTextViewResourceName = (TextView) view.findViewById(R.id.wash_booking_tv_resource_name);
        mTextViewBucketCount = (TextView) view.findViewById(R.id.wash_booking_tv_bucket_count);
        mTextViewBucketAmount = (TextView) view.findViewById(R.id.wash_booking_tv_bucket_price);
        mTextViewBaseAmount = (TextView) view.findViewById(R.id.wash_booking_tv_base_price);
        mTextViewServiceTaxAmount = (TextView) view.findViewById(R.id.wash_booking_tv_service_tax_amount);
        mTextViewTotalAmount = (TextView) view.findViewById(R.id.wash_booking_tv_total_amount);
        mEdittextApplyCoupon = (EditText) view.findViewById(R.id.wash_booking_et_promocode);

        mButtonIncrementBucket = (Button) view.findViewById(R.id.wash_booking_bt_bucket_count_increment);
        mButtonDecrementBucket = (Button) view.findViewById(R.id.wash_booking_bt_bucket_count_decrement);
        mButtonConfirm = (Button) view.findViewById(R.id.wash_booking_bt_confirm);
        mButtonApplycoupon = (Button) view.findViewById(R.id.wash_booking_btn_applycode);


        mTextViewResourceName.setText(mParamResourceName);
        mTextViewBaseAmount.setText(String.valueOf(50));
        mTextViewBucketCount.setText(String.valueOf(1));
        mTextViewBucketAmount.setText(String.valueOf(100));
        mTextViewServiceTaxAmount.setText(String.valueOf(18.75));
        mTextViewTotalAmount.setText(String.valueOf(168.75));

        mButtonIncrementBucket.setOnClickListener(this);
        mButtonDecrementBucket.setOnClickListener(this);
        mButtonConfirm.setOnClickListener(this);
        mButtonApplycoupon.setOnClickListener(this);
        mbtnweightchart.setOnClickListener(this);
        calculateAmount();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case (R.id.wash_booking_bt_confirm):
                if (mCheckBoxTerms.isChecked()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        mOrderId = Utilities.generateOrderId();
                        final WashingOrderAmountValues washingOrderAmountValues = new WashingOrderAmountValues(mOrderId, mBaseAmount, mBucketAmount, mBucketCount, mServiceTaxAmount, mTotalAmount);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealm(washingOrderAmountValues);
                            }
                        });
                        Intent intent = new Intent(getActivity(), AddressActivity.class);
                        intent.putExtra("resourceKey", mParamResourceKey);
                        intent.putExtra("totalAmount", mTotalAmount);
                        intent.putExtra("orderType", Constants.ORDER_TYPE_WASHING);
                        intent.putExtra("orderId", mOrderId);
                        intent.putExtra("ResourceName", mParamResourceName);
                        intent.putExtra("ResourceType", "Washing");
                        intent.putExtra("ResourceMobile", mParamResourceMobile);
                        startActivity(intent);
                    } else {
                        // User is signed out
                        Toast.makeText(getActivity(), "Please Login First", Toast.LENGTH_SHORT).show();
                        listener.UserSignUpRequired();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please accept terms and conditions", Toast.LENGTH_SHORT).show();
                }
                break;
            case (R.id.wash_booking_bt_bucket_count_decrement):
                if (mBucketCount > 1) {
                    mBucketCount = mBucketCount - 1;
                    mBucketAmount = mBucketCount * 100;
                }
                mTextViewBucketCount.setText(String.valueOf(mBucketCount));
                mTextViewBucketAmount.setText(String.valueOf(mBucketAmount));
                calculateAmount();
                break;
            case(R.id.wash_booking_btn_weightchart):
                startActivity(new Intent(getActivity(), WeightChartWashing.class));
                break;
            case (R.id.wash_booking_bt_bucket_count_increment):
                mBucketCount = mBucketCount + 1;
                mBucketAmount = mBucketCount * 100;
                mTextViewBucketCount.setText(String.valueOf(mBucketCount));
                mTextViewBucketAmount.setText(String.valueOf(mBucketAmount));
                calculateAmount();
                break;
            case (R.id.wash_booking_btn_applycode):
                couponcode();
                break;
        }
    }

    private void couponcode() {
        final String checkcoupon = mEdittextApplyCoupon.getText().toString().toUpperCase();
        mDatabase.child(Constants.FIREBASE_CHILD_COUPONCODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isCouponexist = false;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Coupon coupon = ds.getValue(Coupon.class);

                    if (coupon.getCouponCode().equals(checkcoupon)) {

                        try {
                            verifyCoupon(coupon);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        isCouponexist = true;
                        break;
                    }


                }
                if (isCouponexist == false) {
                    Toast.makeText(getActivity(), "Not a valid coupon", Toast.LENGTH_LONG).show();

                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void verifyCoupon(Coupon coupon) throws ParseException {
        String category = coupon.getCategories();
        String status = coupon.getStatus();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date lastdatefrom = dateFormat.parse(coupon.getLastDateFrom());
        Date lastdateto = dateFormat.parse(coupon.getLastDateTo());
        String date = dateFormat.format(new Date());
        Date currentDate = dateFormat.parse(date);

        if (currentDate.after(lastdatefrom) && (currentDate.before(lastdateto))
                && (category.equals("Washing") || (category.equals("All")))
                && (status.equals("Active"))) {
            calculateDiscountedAmount(coupon);
            Toast.makeText(getActivity(), "coupon is valid", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "coupon not valid", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateDiscountedAmount(Coupon coupon) {
        float tempAmount = mBaseAmount + mBucketAmount;
        float totalDiscount = (mBaseAmount + mBucketAmount) * coupon.getDiscount() / 100;
        float discountedAmount = tempAmount - totalDiscount;

        mServiceTaxAmount = 0;
        mTextViewServiceTaxAmount.setText(String.valueOf(mServiceTaxAmount));
        mTotalAmount = (mServiceTaxAmount + discountedAmount);
        mTextViewTotalAmount.setText(String.valueOf(mTotalAmount));

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("bucketcount", mBucketCount);
        outState.putInt("bucketamount", mBucketAmount);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mBucketCount = savedInstanceState.getInt("bucketcount");
            mBucketAmount = savedInstanceState.getInt("bucketamount");
        } else {
            mBucketCount = 1;
            mBucketAmount = 100;
        }
    }

    private void calculateAmount() {
        int tempAmount = mBaseAmount + mBucketAmount;
        mServiceTaxAmount = 0;
        mTextViewServiceTaxAmount.setText(String.valueOf(mServiceTaxAmount));
        mTotalAmount = (mServiceTaxAmount + tempAmount);
        mTextViewTotalAmount.setText(String.valueOf(mTotalAmount));
    }
}
