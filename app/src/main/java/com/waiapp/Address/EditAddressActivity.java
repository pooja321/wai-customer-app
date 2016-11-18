package com.waiapp.Address;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class EditAddressActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private Spinner mSpinnerAddressType;

    private EditText mEditTextAddressName, mEditTextHouseNo, mEditTextAreaName, mEditTextLandMark, mEditTextCity, mEditTextPincode, mEditTextState;
    private Button mButtonSubmit;
    Address mAddress = new Address();
    String addressName, addressType, houseNo, areaName, landmark, city, state, country, pincode, UID;

    boolean failFlag = false;
    public static final String selectAddressTypeLabel = "Select Address Type*";
    private String[] addressTypeList = new String[]{selectAddressTypeLabel, "Flat", "House"};
    Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        mAddress = (Address) getIntent().getSerializableExtra("Address");

        mtoolbar = (Toolbar) findViewById(R.id.editaddress_toolbar);
        mtoolbar.setTitle("Edit Address");
        mtoolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEditTextAddressName = (EditText) findViewById(R.id.editaddress_et_address_name);
        mEditTextHouseNo = (EditText) findViewById(R.id.editaddress_et_houseno);
        mEditTextAreaName = (EditText) findViewById(R.id.editaddress_et_areaname);
        mEditTextLandMark = (EditText) findViewById(R.id.editaddress_et_landmark);
        mEditTextCity = (EditText) findViewById(R.id.editaddress_et_city);
        mEditTextPincode = (EditText) findViewById(R.id.editaddress_et_pincode);
        mEditTextState = (EditText) findViewById(R.id.editaddress_et_state);
        mButtonSubmit = (Button) findViewById(R.id.editaddress_button_submit);

        mEditTextAddressName.setText(mAddress.getAddressName());
        mEditTextHouseNo.setText(mAddress.getHouseNo());
        mEditTextAreaName.setText(mAddress.getAreaName());
        mEditTextLandMark.setText(mAddress.getLandmark());
        mEditTextCity.setText(mAddress.getCity());
        mEditTextPincode.setText(mAddress.getPincode());
        mEditTextState.setText(mAddress.getState());

        mSpinnerAddressType = (Spinner) findViewById(R.id.editaddress_spinner_address_type);
        if (mSpinnerAddressType != null) {
            mSpinnerAddressType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Spinner addressTypeSpinner = (Spinner) parent;
                    if (addressTypeSpinner.getId() == R.id.editaddress_spinner_address_type) {
                        addressType = parent.getItemAtPosition(position).toString();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(EditAddressActivity.this, android.R.layout.simple_spinner_dropdown_item, addressTypeList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerAddressType.setAdapter(genderAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                failFlag = false;
                updateAddress();
            }
        });
    }

    private void updateAddress() {
        addressName = mEditTextAddressName.getText().toString();
        houseNo = mEditTextHouseNo.getText().toString();
        areaName = mEditTextAreaName.getText().toString();
        landmark = mEditTextLandMark.getText().toString();
        city = mEditTextCity.getText().toString();
        pincode = mEditTextPincode.getText().toString();
        state = mEditTextState.getText().toString();
        country = "India";

        String EmptyString = getResources().getString(R.string.provide_necessary_details);
        String Provide_6_characters = getResources().getString(R.string.less_than_6_character);

        if (addressName.equals("")) {
            failFlag = true;
            mEditTextAddressName.setError(EmptyString);
        }
        if (houseNo.equals("")) {
            failFlag = true;
            mEditTextHouseNo.setError(EmptyString);
        }
        if (areaName.equals("")) {
            failFlag = true;
            mEditTextAreaName.setError(EmptyString);
        }
        if (landmark.equals("")) {
            failFlag = true;
            mEditTextLandMark.setError(EmptyString);
        }
        if (city.equals("")) {
            failFlag = true;
            mEditTextCity.setError(EmptyString);
        }
        if (state.equals("")) {
            failFlag = true;
            mEditTextState.setError(EmptyString);
        }
        if (pincode.equals("")) {
            failFlag = true;
            mEditTextPincode.setError(EmptyString);
        }
        if (pincode.length() < 6 && houseNo.length() > 0) {
            failFlag = true;
            mEditTextPincode.setError(Provide_6_characters);
        }
        if (addressType.equals(selectAddressTypeLabel)) {
            failFlag = true;
            Toast.makeText(EditAddressActivity.this, "Please select valid Address Type", Toast.LENGTH_SHORT).show();
        }
        if (!failFlag) {
            Address address = new Address(mAddress.getAddressId(), addressName, addressType, houseNo, areaName, landmark, city, state, country, pincode);

            mDatabase.child(Constants.FIREBASE_CHILD_ADDRESS).child(UID).child(address.getAddressId()).setValue(address).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(EditAddressActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditAddressActivity.this, "Addess Added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditAddressActivity.this, AddressActivity.class));
                    }
                }
            });
        }
    }
}

