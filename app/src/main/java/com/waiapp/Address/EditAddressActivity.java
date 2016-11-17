package com.waiapp.Address;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText mEditTextAddressName, mEditTextHouseNo, mEditTextAreaName, mEditTextLandMark, mEditTextCity, mEditTextPincode, mEditTextState;
    private Button mButtonSubmit;
    Address mAddress = new Address();
    String addressName, addressid, addressType, houseNo, areaName, landmark, city, state, country, pincode, UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEditTextAddressName = (EditText) findViewById(R.id.addaddress_et_address_name);
        mEditTextHouseNo = (EditText) findViewById(R.id.addaddress_et_houseno);
        mEditTextAreaName = (EditText) findViewById(R.id.addaddress_et_areaname);
        mEditTextLandMark = (EditText) findViewById(R.id.addaddress_et_landmark);
        mEditTextCity = (EditText) findViewById(R.id.addaddress_et_city);
        mEditTextPincode = (EditText) findViewById(R.id.addaddress_et_pincode);
        mEditTextState = (EditText) findViewById(R.id.addaddress_et_state);
        mButtonSubmit = (Button) findViewById(R.id.address_button_submit);

        mAddress = (Address) getIntent().getSerializableExtra("Address");

        mEditTextAddressName.setText(mAddress.getAddressName());
        mEditTextHouseNo.setText(mAddress.getHouseNo());
        mEditTextAreaName.setText(mAddress.getAreaName());
        mEditTextLandMark.setText(mAddress.getLandmark());
        mEditTextCity.setText(mAddress.getCity());
        mEditTextPincode.setText(mAddress.getPincode());
        mEditTextState.setText(mAddress.getState());


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressName = mEditTextAddressName.getText().toString();
                houseNo = mEditTextHouseNo.getText().toString();
                areaName = mEditTextAreaName.getText().toString();
                landmark = mEditTextLandMark.getText().toString();
                city = mEditTextCity.getText().toString();
                pincode = mEditTextPincode.getText().toString();
                state = mEditTextState.getText().toString();
                country = "India";
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
        });


    }
}
