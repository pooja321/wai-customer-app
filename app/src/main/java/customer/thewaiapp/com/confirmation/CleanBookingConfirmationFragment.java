package customer.thewaiapp.com.confirmation;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import customer.thewaiapp.com.Model.CleaningOrderAmountValues;
import customer.thewaiapp.com.Utility.Constants;
import customer.thewaiapp.com.Utility.Utilities;
import customer.thewaiapp.com.Address.AddressActivity;

import customer.thewaiapp.com.R;
import customer.thewaiapp.com.Realm.RealmController;

import customer.thewaiapp.com.WeightChartUtensilsActivity;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class CleanBookingConfirmationFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_KEY = "ResourceKey";
    private static final String ARG_RESOURCE = "ResourceName";
    int mBaseAmount = 50;
    int mRoomsCount, mWashroomsCount, mUtensilBucketCount, mRoomsAmount, mWashroomsAmount, mUtensilBucketAmount;
    double mTotalAmount, mServiceTaxAmount = 0;
    private String mParamResourceKey, mParamResourceName, mOrderId;

    TextView mTextViewRoomsCount, mTextViewWashroomsCount, mTextViewUtensilBucketCount, mTextViewRoomsAmount,
            mTextViewWashroomsAmount, mTextViewUtensilBucketAmount,
            mTextViewBaseAmount, mTextViewServiceTaxAmount, mTextViewTotalAmount, mTextViewResourceName;
    Button mButtonIncrementRooms, mButtonDecrementRooms, mButtonIncrementWashrooms, mButtonDecrementWashrooms,
            mButtonIncrementUtensilBucket, mButtonDecrementUtensilBucket, mButtonConfirm;
    CheckBox mCheckBoxTerms;
    Realm realm;
    private OnUserSignUpRequired mListener;

    Button mbtnweightchart;

    // callback interface to implement on item list click mListener
    public interface OnUserSignUpRequired {
        void UserSignUpRequired();
    }

    public CleanBookingConfirmationFragment() {
        // Required empty public constructor
    }

    public static CleanBookingConfirmationFragment newInstance(String key, String resourceName) {
        CleanBookingConfirmationFragment fragment = new CleanBookingConfirmationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
//        args.putSerializable(ARG_RESOURCE, resourceName);
        args.putString(ARG_RESOURCE, resourceName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("wai", "onCreate");
        if (getArguments() != null) {
            mParamResourceKey = getArguments().getString(ARG_KEY);
//            mParamResource = (ResourceOnline) getArguments().getSerializable(ARG_RESOURCE);
            mParamResourceName = getArguments().getString(ARG_RESOURCE);
        }
        if(savedInstanceState == null){
            mRoomsCount = 1;
            mWashroomsCount = 0;
            mUtensilBucketCount = 0;
            mRoomsAmount = 75;
            mWashroomsAmount = 0;
            mUtensilBucketAmount = 0;
        }
        this.realm = RealmController.with(this).getRealm();
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

        mButtonIncrementRooms = (Button) view.findViewById(R.id.clean_booking_bt_rooms_count_increment);
        mButtonDecrementRooms = (Button) view.findViewById(R.id.clean_booking_bt_rooms_count_decrement);
        mButtonIncrementWashrooms = (Button) view.findViewById(R.id.clean_booking_bt_washroom_count_increment);
        mButtonDecrementWashrooms = (Button) view.findViewById(R.id.clean_booking_bt_washroom_count_decrement);
        mButtonIncrementUtensilBucket = (Button) view.findViewById(R.id.clean_booking_bt_utensilbucket_count_increment);
        mButtonDecrementUtensilBucket = (Button) view.findViewById(R.id.clean_booking_bt_utensilbucket_count_decrement);
        mButtonConfirm = (Button) view.findViewById(R.id.clean_booking_bt_confirm);

        mButtonIncrementWashrooms.setOnClickListener(this);
        mButtonIncrementRooms.setOnClickListener(this);
        mButtonDecrementWashrooms.setOnClickListener(this);
        mButtonDecrementRooms.setOnClickListener(this);
        mButtonIncrementUtensilBucket.setOnClickListener(this);
        mButtonDecrementUtensilBucket.setOnClickListener(this);
        mButtonConfirm.setOnClickListener(this);
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
        mServiceTaxAmount = tempAmount * .125;
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
                    Log.v("wai","checkbox selected");
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
                        startActivity(intent);
                    } else {
                        // User is signed out
                        Toast.makeText(getActivity(), "Please Login First", Toast.LENGTH_SHORT).show();
                        mListener.UserSignUpRequired();
                    }
                }else {
                    Log.v("wai","checkbox not selected");
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
            case(R.id.cook_booking_btn_weightchart):
                startActivity(new Intent(getActivity(), WeightChartUtensilsActivity.class));
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
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v("wai","onSaveInstanceState data saved");
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
        Log.v("wai", "onViewStateRestored");
        if(savedInstanceState != null){
            Log.v("wai","if");
            mRoomsCount = savedInstanceState.getInt("roomscount");
            mWashroomsCount = savedInstanceState.getInt("washroomcount");
            mUtensilBucketCount = savedInstanceState.getInt("utensilcount");
            mRoomsAmount = savedInstanceState.getInt("roomsamount");
            mWashroomsAmount = savedInstanceState.getInt("Washroomamount");
            mUtensilBucketAmount = savedInstanceState.getInt("utensilamount");
        }else{
            Log.v("wai","else");
            mRoomsCount = 1;
            mWashroomsCount = 0;
            mUtensilBucketCount = 0;
            mRoomsAmount = 75;
            mWashroomsAmount = 0;
            mUtensilBucketAmount = 0;
        }
    }

}
