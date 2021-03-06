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
import customer.thewaiapp.com.Model.CleaningOrderAmountValues;
import customer.thewaiapp.com.Model.Coupon;
import customer.thewaiapp.com.R;
import customer.thewaiapp.com.Realm.RealmController;
import customer.thewaiapp.com.Utility.Constants;
import customer.thewaiapp.com.Utility.Utilities;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class CleanBookingConfirmationFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_KEY = "ResourceKey";
    private static final String ARG_RESOURCE = "ResourceName";
    private static final String ARG_RESOURCE_MOBILE = "ResourceMobile";
    int mBaseAmount = 50;
    int mRoomsCount, mWashroomsCount, mUtensilBucketCount, mRoomsAmount, mWashroomsAmount, mUtensilBucketAmount;
    double mTotalAmount, mServiceTaxAmount = 0;
    private String mParamResourceKey, mParamResourceName, mOrderId;

    TextView mTextViewRoomsCount, mTextViewWashroomsCount, mTextViewUtensilBucketCount, mTextViewRoomsAmount,
            mTextViewWashroomsAmount, mTextViewUtensilBucketAmount,
            mTextViewBaseAmount, mTextViewServiceTaxAmount, mTextViewTotalAmount, mTextViewResourceName;
    Button mButtonIncrementRooms, mButtonDecrementRooms, mButtonIncrementWashrooms, mButtonDecrementWashrooms,
            mButtonIncrementUtensilBucket, mButtonDecrementUtensilBucket, mButtonConfirm, mButtonApplycoupon;
    CheckBox mCheckBoxTerms;
    EditText mEdittextApplyCoupon;
    Realm realm;
    private Long mParamResourceMobile;
    private OnUserSignUpRequired mListener;
    private DatabaseReference mDatabase;


    Button mbtnweightchart;

    // callback interface to implement on item list click mListener
    public interface OnUserSignUpRequired {
        void UserSignUpRequired();
    }

    public CleanBookingConfirmationFragment() {
        // Required empty public constructor
    }

    public static CleanBookingConfirmationFragment newInstance(String key, String resourceName, Long mResourceMobile) {
        CleanBookingConfirmationFragment fragment = new CleanBookingConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
//        args.putSerializable(ARG_RESOURCE, resourceName);
        args.putString(ARG_RESOURCE, resourceName);
        args.putLong(ARG_RESOURCE_MOBILE, mResourceMobile);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamResourceKey = getArguments().getString(ARG_KEY);
//            mParamResource = (ResourceOnline) getArguments().getSerializable(ARG_RESOURCE);
            mParamResourceName = getArguments().getString(ARG_RESOURCE);
            mParamResourceMobile = getArguments().getLong(ARG_RESOURCE_MOBILE);
        }
        if (savedInstanceState == null) {
            mRoomsCount = 1;
            mWashroomsCount = 0;
            mUtensilBucketCount = 0;
            mRoomsAmount = 75;
            mWashroomsAmount = 0;
            mUtensilBucketAmount = 0;
        }
        this.realm = RealmController.with(this).getRealm();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clean_booking_confirmation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mbtnweightchart= (Button) view.findViewById(R.id.cook_booking_btn_weightchart);
        mCheckBoxTerms = (CheckBox) view.findViewById(R.id.clean_booking_cb_terms);
        mTextViewResourceName = (TextView) view.findViewById(R.id.clean_booking_tv_resource_name);
        mTextViewRoomsCount = (TextView) view.findViewById(R.id.clean_booking_tv_rooms_count);
        mTextViewWashroomsCount = (TextView) view.findViewById(R.id.clean_booking_tv_washroom_count);
        mTextViewUtensilBucketCount = (TextView) view.findViewById(R.id.clean_booking_tv_utensilbucket_count);
        mTextViewRoomsAmount = (TextView) view.findViewById(R.id.clean_booking_tv_rooms_price);
        mTextViewWashroomsAmount = (TextView) view.findViewById(R.id.clean_booking_tv_washroom_price);
        mTextViewUtensilBucketAmount = (TextView) view.findViewById(R.id.clean_booking_tv_utensil_price);
        mTextViewBaseAmount = (TextView) view.findViewById(R.id.clean_booking_tv_base_price);
        mTextViewServiceTaxAmount = (TextView) view.findViewById(R.id.clean_booking_tv_service_tax_amount);
        mTextViewTotalAmount = (TextView) view.findViewById(R.id.clean_booking_tv_total_amount);
        mEdittextApplyCoupon = (EditText) view.findViewById(R.id.clean_booking_et_promocode);


        mButtonIncrementRooms = (Button) view.findViewById(R.id.clean_booking_bt_rooms_count_increment);
        mButtonDecrementRooms = (Button) view.findViewById(R.id.clean_booking_bt_rooms_count_decrement);
        mButtonIncrementWashrooms = (Button) view.findViewById(R.id.clean_booking_bt_washroom_count_increment);
        mButtonDecrementWashrooms = (Button) view.findViewById(R.id.clean_booking_bt_washroom_count_decrement);
        mButtonIncrementUtensilBucket = (Button) view.findViewById(R.id.clean_booking_bt_utensilbucket_count_increment);
        mButtonDecrementUtensilBucket = (Button) view.findViewById(R.id.clean_booking_bt_utensilbucket_count_decrement);
        mButtonConfirm = (Button) view.findViewById(R.id.clean_booking_bt_confirm);
        mButtonApplycoupon = (Button) view.findViewById(R.id.clean_booking_btn_applycode);

        mButtonIncrementWashrooms.setOnClickListener(this);
        mButtonIncrementRooms.setOnClickListener(this);
        mButtonDecrementWashrooms.setOnClickListener(this);
        mButtonDecrementRooms.setOnClickListener(this);
        mButtonIncrementUtensilBucket.setOnClickListener(this);
        mButtonDecrementUtensilBucket.setOnClickListener(this);
        mButtonConfirm.setOnClickListener(this);
        mButtonApplycoupon.setOnClickListener(this);
        mbtnweightchart.setOnClickListener(this);

        mTextViewResourceName.setText(mParamResourceName);
        mTextViewRoomsCount.setText(String.valueOf(mRoomsCount));
        mTextViewWashroomsCount.setText(String.valueOf(mWashroomsCount));
        mTextViewUtensilBucketCount.setText(String.valueOf(mUtensilBucketCount));
        mTextViewRoomsAmount.setText(String.valueOf(mRoomsAmount));
        mTextViewWashroomsAmount.setText(String.valueOf(mWashroomsAmount));
        mTextViewUtensilBucketAmount.setText(String.valueOf(mUtensilBucketAmount));
        mTextViewBaseAmount.setText(String.valueOf(mBaseAmount));
        calculateAmount();
    }

    private void calculateAmount() {
        int tempAmount = mBaseAmount + mRoomsAmount + mWashroomsAmount + mUtensilBucketAmount;
        mServiceTaxAmount = 0;
        mTextViewServiceTaxAmount.setText(String.valueOf(mServiceTaxAmount));
        mTotalAmount = (mServiceTaxAmount + tempAmount);
        mTextViewTotalAmount.setText(String.valueOf(mTotalAmount));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case (R.id.clean_booking_bt_confirm):
                if (mCheckBoxTerms.isChecked()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        mOrderId = Utilities.generateOrderId();
                        final CleaningOrderAmountValues cleaningOrderAmountValues = new CleaningOrderAmountValues(mOrderId, mBaseAmount
                                , mRoomsAmount, mRoomsCount, mServiceTaxAmount, mTotalAmount, mUtensilBucketAmount, mUtensilBucketCount, mWashroomsAmount, mWashroomsCount);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealm(cleaningOrderAmountValues);
                            }
                        });
                        Intent intent = new Intent(getActivity(), AddressActivity.class);
                        intent.putExtra("resourceKey", mParamResourceKey);
                        intent.putExtra("totalAmount", mTotalAmount);
                        intent.putExtra("orderType", Constants.ORDER_TYPE_CLEANING);
                        intent.putExtra("orderId", mOrderId);
                        intent.putExtra("ResourceName", mParamResourceName);
                        intent.putExtra("ResourceType", "Cleaning");
                        intent.putExtra("ResourceMobile", mParamResourceMobile);
                        startActivity(intent);
                    } else {
                        // User is signed out
                        Toast.makeText(getActivity(), "Please Login First", Toast.LENGTH_SHORT).show();
                        mListener.UserSignUpRequired();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please accept terms and conditions", Toast.LENGTH_SHORT).show();
                }

                break;
            case (R.id.clean_booking_bt_washroom_count_decrement):
                if (mWashroomsCount >= 1) {
                    mWashroomsCount = mWashroomsCount - 1;
                    mWashroomsAmount = mWashroomsCount * 25;
                }
                mTextViewWashroomsCount.setText(String.valueOf(mWashroomsCount));
                mTextViewWashroomsAmount.setText(String.valueOf(mWashroomsAmount));
                calculateAmount();
                break;
            case (R.id.clean_booking_bt_washroom_count_increment):
                mWashroomsCount = mWashroomsCount + 1;
                mWashroomsAmount = 25 * mWashroomsCount;

                mTextViewWashroomsCount.setText(String.valueOf(mWashroomsCount));
                mTextViewWashroomsAmount.setText(String.valueOf(mWashroomsAmount));
                calculateAmount();
                break;
            case (R.id.clean_booking_bt_rooms_count_decrement):
                if (mRoomsCount > 1) {
                    mRoomsCount = mRoomsCount - 1;
                    mRoomsAmount = mRoomsCount * 75;
                }
                mTextViewRoomsCount.setText(String.valueOf(mRoomsCount));
                mTextViewRoomsAmount.setText(String.valueOf(mRoomsAmount));
                calculateAmount();
                break;
            case(R.id.cook_booking_btn_weightchart):
                startActivity(new Intent(getActivity(), WeightChartUtensilsActivity.class));
                break;
            case (R.id.clean_booking_bt_rooms_count_increment):
                mRoomsCount = mRoomsCount + 1;
                mRoomsAmount = mRoomsCount * 75;
//                if (mRoomsCount >= 2) {
//                    mRoomsAmount = mRoomsCount * 75;
//                }
                mTextViewRoomsCount.setText(String.valueOf(mRoomsCount));
                mTextViewRoomsAmount.setText(String.valueOf(mRoomsAmount));
                calculateAmount();
                break;
            case (R.id.clean_booking_bt_utensilbucket_count_decrement):
                if (mUtensilBucketCount >= 1) {
                    mUtensilBucketCount = mUtensilBucketCount - 1;
                    mUtensilBucketAmount = mUtensilBucketCount * 50;
                }

                mTextViewUtensilBucketCount.setText(String.valueOf(mUtensilBucketCount));
                mTextViewUtensilBucketAmount.setText(String.valueOf(mUtensilBucketAmount));
                calculateAmount();
                break;
            case (R.id.clean_booking_bt_utensilbucket_count_increment):
                mUtensilBucketCount = mUtensilBucketCount + 1;
                mUtensilBucketAmount = mUtensilBucketCount * 50;

//                if (mUtensilBucketCount >= 2) {
//                    mUtensilBucketAmount = mUtensilBucketCount * 50;
//                }
                mTextViewUtensilBucketCount.setText(String.valueOf(mUtensilBucketCount));
                mTextViewUtensilBucketAmount.setText(String.valueOf(mUtensilBucketAmount));
                calculateAmount();
                break;
            case (R.id.clean_booking_btn_applycode):
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
                && (category.equals("Cleaning") || (category.equals("All")))
                && (status.equals("Active"))) {
            calculateDiscountedAmount(coupon);
            Toast.makeText(getActivity(), "coupon is valid", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "coupon not valid", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateDiscountedAmount(Coupon coupon) {
        float tempAmount = mBaseAmount + mRoomsAmount + mWashroomsAmount + mUtensilBucketAmount;
        float totalDiscount = (mBaseAmount + mRoomsAmount + mWashroomsAmount + mUtensilBucketAmount) * coupon.getDiscount() / 100;
        float discountedAmount = tempAmount - totalDiscount;

        mServiceTaxAmount = 0;
        mTextViewServiceTaxAmount.setText(String.valueOf(mServiceTaxAmount));
        mTotalAmount = (mServiceTaxAmount + discountedAmount);
        mTextViewTotalAmount.setText(String.valueOf(mTotalAmount));

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("roomscount", mRoomsCount);
        outState.putInt("roomsamount", mRoomsAmount);
        outState.putInt("washroomcount", mWashroomsCount);
        outState.putInt("Washroomamount", mWashroomsAmount);
        outState.putInt("utensilbucketcount", mUtensilBucketCount);
        outState.putInt("utensilbucketamount", mUtensilBucketAmount);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mRoomsCount = savedInstanceState.getInt("roomscount");
            mWashroomsCount = savedInstanceState.getInt("washroomcount");
            mUtensilBucketCount = savedInstanceState.getInt("utensilcount");
            mRoomsAmount = savedInstanceState.getInt("roomsamount");
            mWashroomsAmount = savedInstanceState.getInt("Washroomamount");
            mUtensilBucketAmount = savedInstanceState.getInt("utensilamount");
        } else {
            mRoomsCount = 1;
            mWashroomsCount = 0;
            mUtensilBucketCount = 0;
            mRoomsAmount = 75;
            mWashroomsAmount = 0;
            mUtensilBucketAmount = 0;
        }
    }


}
