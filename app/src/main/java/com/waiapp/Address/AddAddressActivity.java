package com.waiapp.Address;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.waiapp.Model.Address;
import com.waiapp.R;
import com.waiapp.Utility.Constants;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DatabaseReference mDatabase;

    private EditText mEditTextAddressName, mEditTextHouseNo, mEditTextAreaName, mEditTextLandMark, mEditTextCity, mEditTextPincode, mEditTextState;
    private Spinner mSpinnerAddressType;
    private Button mButtonSubmit;
    private Toolbar mtoolbar;
    private ProgressDialog mSaveProgressDialog;

    String addressName, addressid, addressType, houseNo, areaName, landmark, city, state, country, pincode, UID;
    public static final String selectAddressTypeLabel = "Select Address Type*";
    private String[] addressTypeList = new String[]{selectAddressTypeLabel, "Flat", "House"};

    boolean failFlag = false;

    TextInputLayout mTIL_Address_title, mTIL_Housenum, mTIL_Area_name, mTIL_Landmark, mTIL_City, mTIL_State, mTIL_Pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        mtoolbar = (Toolbar) findViewById(R.id.addaddress_toolbar);
        mtoolbar.setTitle("Add Address");
        mtoolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEditTextAddressName = (EditText) findViewById(R.id.addaddress_et_address_name);
        mEditTextHouseNo = (EditText) findViewById(R.id.addaddress_et_houseno);
        mEditTextAreaName = (EditText) findViewById(R.id.addaddress_et_areaname);
        mEditTextLandMark = (EditText) findViewById(R.id.addaddress_et_landmark);
        mEditTextCity = (EditText) findViewById(R.id.addaddress_et_city);
        mEditTextPincode = (EditText) findViewById(R.id.addaddress_et_pincode);
        mEditTextState = (EditText) findViewById(R.id.addaddress_et_state);


        mTIL_Address_title = (TextInputLayout) findViewById(R.id.til_address_title);
        mTIL_Housenum = (TextInputLayout) findViewById(R.id.til_house_no);
        mTIL_Area_name = (TextInputLayout) findViewById(R.id.til_area_name);
        mTIL_Landmark = (TextInputLayout) findViewById(R.id.til_landmark);
        mTIL_City = (TextInputLayout) findViewById(R.id.til_city);
        mTIL_State = (TextInputLayout) findViewById(R.id.til_State);
        mTIL_Pincode = (TextInputLayout) findViewById(R.id.til_pincode);

        mSpinnerAddressType = (Spinner) findViewById(R.id.addaddress_spinner_address_type);
        if (mSpinnerAddressType != null) {
            mSpinnerAddressType.setOnItemSelectedListener(this);
        }

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(AddAddressActivity.this, android.R.layout.simple_spinner_dropdown_item, addressTypeList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerAddressType.setAdapter(genderAdapter);

        mButtonSubmit = (Button) findViewById(R.id.addaddress_button_submit);
        mButtonSubmit.setOnClickListener(this);

        mSaveProgressDialog = new ProgressDialog(this);
        mSaveProgressDialog.setMessage("Saving Your Address");
        mSaveProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case (R.id.addaddress_button_submit):
                addAddress(v);
                break;
        }
    }

    private void addAddress(View v) {
        addressName = mEditTextAddressName.getText().toString();
        houseNo = mEditTextHouseNo.getText().toString();
        areaName = mEditTextAreaName.getText().toString();
        landmark = mEditTextLandMark.getText().toString();
        city = mEditTextCity.getText().toString();
        pincode = mEditTextPincode.getText().toString();
        state = mEditTextState.getText().toString();
        country = "India";

        String EmptyString = getResources().getString(R.string.provide_necessary_details);
        String Provide_10_characters = getResources().getString(R.string.less_than_10_character);
        String Provide_6_characters = getResources().getString(R.string.less_than_6_character);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        addressid = mDatabase.child(Constants.FIREBASE_CHILD_ADDRESS).push().getKey();

        if (addressName.equals("")) {
            failFlag = true;
            mTIL_Address_title.setError(EmptyString);
        } else {
            mTIL_Address_title.setErrorEnabled(false);
        }

        if (houseNo.equals("")) {
            failFlag = true;
            mTIL_Housenum.setError(EmptyString);
        } else {
            mTIL_Housenum.setErrorEnabled(false);
        }
        if (areaName.equals("")) {
            failFlag = true;
            mTIL_Area_name.setError(EmptyString);
        } else {
            mTIL_Area_name.setErrorEnabled(false);
        }
        if (landmark.equals("")) {
            failFlag = true;
            mTIL_Landmark.setError(EmptyString);
        } else {
            mTIL_Landmark.setErrorEnabled(false);
        }
        if (city.equals("")) {
            failFlag = true;
            mTIL_City.setError(EmptyString);
        } else {
            mTIL_City.setErrorEnabled(false);
        }
        if (state.equals("")) {
            failFlag = true;
            mTIL_State.setError(EmptyString);
        } else {
            mTIL_State.setErrorEnabled(false);
        }
        if (pincode.equals("")) {
            failFlag = true;
            mTIL_Pincode.setError(EmptyString);
        } else {
            mTIL_Pincode.setErrorEnabled(false);
        }
        if (houseNo.length() < 10 && houseNo.length() > 0) {
            failFlag = true;
            mTIL_Housenum.setError(Provide_10_characters);
        } else {
            mTIL_Housenum.setErrorEnabled(false);
        }
        if (pincode.length() < 6 && houseNo.length() > 0) {
            failFlag = true;
            mTIL_Pincode.setError(Provide_6_characters);
        } else {
            mTIL_Pincode.setErrorEnabled(false);
        }
        if (addressType==selectAddressTypeLabel)
        {
            Toast.makeText(this, "Please select valid Address Type", Toast.LENGTH_SHORT).show();
        }

        if (failFlag == false) {
            mSaveProgressDialog.show();

            Address address = new Address(addressid, addressName, addressType, houseNo, areaName, landmark, city, state, country, pincode);

            mDatabase.child(Constants.FIREBASE_CHILD_ADDRESS).child(UID).child(addressid).setValue(address).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(AddAddressActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddAddressActivity.this, "Addess Added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddAddressActivity.this, AddressActivity.class));
                    }
                    mSaveProgressDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner addressTypeSpinner = (Spinner) parent;
        if (addressTypeSpinner.getId() == R.id.addaddress_spinner_address_type) {
            addressType = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
